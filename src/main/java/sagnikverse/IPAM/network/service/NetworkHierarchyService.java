package sagnikverse.IPAM.network.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sagnikverse.IPAM.engine.cidr.CidrCalculator;
import sagnikverse.IPAM.engine.cidr.CidrParser;
import sagnikverse.IPAM.engine.cidr.ParsedCidr;
import sagnikverse.IPAM.engine.validation.HierarchyValidator;
import sagnikverse.IPAM.network.entity.Network;
import sagnikverse.IPAM.network.repository.NetworkRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NetworkHierarchyService {

    private final NetworkRepository networkRepository;
    private final CidrParser cidrParser;
    private final CidrCalculator cidrCalculator;
    private final HierarchyValidator hierarchyValidator;

    /**
     * Create a subnet under an existing network
     */
    public Network createChildSubnet(Long parentId, String cidr){

        // 1️⃣ find parent network
        Network parent =
                networkRepository.findById(parentId)
                        .orElseThrow(() ->
                                new RuntimeException("Parent network not found"));

        // 2️⃣ parse CIDR
        ParsedCidr parsed = cidrParser.parse(cidr);

        // 3️⃣ calculate network address
        String networkAddress =
                cidrCalculator.getNetworkAddress(
                        parsed.getIp(),
                        parsed.getPrefix()
                );

        // 4️⃣ calculate broadcast
        String broadcastAddress =
                cidrCalculator.getBroadcastAddress(
                        parsed.getIp(),
                        parsed.getPrefix()
                );

        // 5️⃣ calculate total IPs
        long totalIps =
                cidrCalculator.totalIps(parsed.getPrefix());

        // 6️⃣ build child network entity
        Network child = new Network();

        child.setCidr(cidr);
        child.setNetworkAddress(networkAddress);
        child.setBroadcastAddress(broadcastAddress);
        child.setTotalIps(totalIps);
        child.setCreatedAt(LocalDateTime.now());

        // IMPORTANT: connect to parent
        child.setParentNetworkId(parentId);

        // 7️⃣ validate hierarchy rules
        hierarchyValidator.validateChildSubnet(parent, child);

        // 8️⃣ save child subnet
        return networkRepository.save(child);
    }

}