package sagnikverse.IPAM.ip.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sagnikverse.IPAM.engine.allocation.IpAllocator;
import sagnikverse.IPAM.ip.entity.IpAddress;
import sagnikverse.IPAM.ip.entity.IpStatus;
import sagnikverse.IPAM.ip.repository.IpRepository;
import sagnikverse.IPAM.locking.RedisLockService;
import sagnikverse.IPAM.network.dto.SubnetUtilization;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class IpAllocationService {

    private final IpRepository ipRepository;
    private final IpAllocator allocator;
    private final RedisLockService lockService;

    public IpAddress allocateNextIp(Long subnetId,
                                    String startIp,
                                    String endIp){

        String lockKey = "lock:subnet:" + subnetId;

        if(!lockService.acquireLock(lockKey))
            throw new RuntimeException("Allocation in progress");

        try {

            Set<String> allocatedIps =
                    ipRepository.findBySubnetId(subnetId)
                            .stream()
                            .map(IpAddress::getIpAddress)
                            .collect(Collectors.toSet());

            String nextIp =
                    allocator.findNextAvailableIp(
                            startIp,
                            endIp,
                            allocatedIps
                    );

            IpAddress ip = new IpAddress();

            ip.setSubnetId(subnetId);
            ip.setIpAddress(nextIp);
            ip.setStatus(IpStatus.ALLOCATED);
            ip.setAllocatedAt(LocalDateTime.now());

            return ipRepository.save(ip);

        } finally {

            lockService.releaseLock(lockKey);
        }

    }

    public void releaseIp(String ipAddress){

        IpAddress ip = ipRepository
                .findByIpAddress(ipAddress)
                .orElseThrow(() ->
                        new RuntimeException("IP not found"));

        ipRepository.delete(ip);

    }

    public IpAddress allocateSpecificIp(Long subnetId,
                                        String ipAddress){

        if(ipRepository.findByIpAddress(ipAddress).isPresent())
            throw new RuntimeException("IP already allocated");

        IpAddress ip = new IpAddress();

        ip.setSubnetId(subnetId);
        ip.setIpAddress(ipAddress);
        ip.setStatus(IpStatus.ALLOCATED);
        ip.setAllocatedAt(LocalDateTime.now());

        return ipRepository.save(ip);

    }

    public List<IpAddress> getIpsOfSubnet(Long subnetId){

        return ipRepository.findBySubnetId(subnetId);
    }


    public SubnetUtilization getUtilization(Long subnetId,
                                            long totalIps){

        long allocated =
                ipRepository.findBySubnetId(subnetId).size();

        long free = totalIps - allocated;

        double percent =
                ((double) allocated / totalIps) * 100;

        SubnetUtilization util =
                new SubnetUtilization();

        util.setTotalIps(totalIps);
        util.setAllocatedIps(allocated);
        util.setFreeIps(free);
        util.setUtilization(percent);

        return util;
    }



    public List<IpAddress> bulkAllocate(Long subnetId,
                                        int count,
                                        String startIp,
                                        String endIp){

        List<IpAddress> allocatedIps =
                new ArrayList<>();

        for(int i=0;i<count;i++){

            IpAddress ip =
                    allocateNextIp(
                            subnetId,
                            startIp,
                            endIp
                    );

            allocatedIps.add(ip);
        }

        return allocatedIps;

    }
}