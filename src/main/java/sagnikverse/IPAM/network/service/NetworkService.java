package sagnikverse.IPAM.network.service;



import org.springframework.data.redis.core.StringRedisTemplate;
import sagnikverse.IPAM.engine.cidr.CidrCalculator;
import sagnikverse.IPAM.engine.cidr.CidrParser;
import sagnikverse.IPAM.engine.cidr.ParsedCidr;
import sagnikverse.IPAM.engine.validation.CidrValidator;
import sagnikverse.IPAM.engine.validation.OverlapDetector;
import sagnikverse.IPAM.network.entity.Network;
import sagnikverse.IPAM.network.repository.NetworkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static org.apache.naming.SelectorContext.prefix;

@Service
@RequiredArgsConstructor
public class NetworkService {

    private final NetworkRepository networkRepository;
    private final CidrParser cidrParser;
    private final CidrCalculator cidrCalculator;
    private final CidrValidator validator;
    private final OverlapDetector overlapDetector;
    private final StringRedisTemplate redisTemplate;

    public Network createNetwork(String cidr){

        validator.validate(cidr);

        ParsedCidr parsed = cidrParser.parse(cidr);

        String networkAddress =
                cidrCalculator.getNetworkAddress(
                        parsed.getIp(),
                        parsed.getPrefix()
                );

        String broadcastAddress =
                cidrCalculator.getBroadcastAddress(
                        parsed.getIp(),
                        parsed.getPrefix()
                );

        long totalIps =
                cidrCalculator.totalIps(parsed.getPrefix());

        Network network = new Network();

        network.setCidr(cidr);
        network.setNetworkAddress(networkAddress);
        network.setBroadcastAddress(broadcastAddress);
        network.setTotalIps(totalIps);
        network.setCreatedAt(LocalDateTime.now());

        List<Network> existingNetworks =
                networkRepository.findAll();

        long newStart =
                cidrCalculator.ipToLong(networkAddress);

        long newEnd =
                cidrCalculator.ipToLong(broadcastAddress);

        for(Network net : existingNetworks){

            long start =
                    cidrCalculator.ipToLong(net.getNetworkAddress());

            long end =
                    cidrCalculator.ipToLong(net.getBroadcastAddress());

            if(overlapDetector.isOverlapping(
                    newStart,
                    newEnd,
                    start,
                    end
            )){
                throw new RuntimeException("Subnet overlap detected");
            }
        }

        return networkRepository.save(network);
    }


}