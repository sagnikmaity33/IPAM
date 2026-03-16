package sagnikverse.IPAM.engine.allocation;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import sagnikverse.IPAM.ip.entity.IpAddress;
import sagnikverse.IPAM.ip.repository.IpRepository;
import sagnikverse.IPAM.util.IpAddressConverter;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BitmapWarmupService {

    private final IpRepository ipRepository;
    private final StringRedisTemplate redisTemplate;

    @PostConstruct
    public void warmup() {

        List<IpAddress> ips = ipRepository.findAll();

        for (IpAddress ip : ips) {

            String subnetKey = "ipam:bitmap:subnet:" + ip.getSubnetId();

            long ipLong = IpAddressConverter.ipToLong(ip.getIpAddress());

            redisTemplate.opsForValue().setBit(
                    subnetKey,
                    ipLong,
                    true
            );
        }
    }
}