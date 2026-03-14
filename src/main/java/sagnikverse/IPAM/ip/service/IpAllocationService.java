package sagnikverse.IPAM.ip.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sagnikverse.IPAM.ip.entity.IpAddress;
import sagnikverse.IPAM.ip.entity.IpStatus;
import sagnikverse.IPAM.ip.repository.IpRepository;
import sagnikverse.IPAM.network.entity.Network;
import sagnikverse.IPAM.network.repository.NetworkRepository;
import sagnikverse.IPAM.util.IpAddressConverter;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IpAllocationService {

    private final IpRepository ipRepository;
    private final NetworkRepository networkRepository;

    /**
     * Allocate next available IP (O(n) scan)
     */
    public IpAddress allocateNextIp(Long subnetId,
                                    String startIp,
                                    String endIp) {

        Network network = networkRepository
                .findById(subnetId)
                .orElseThrow(() -> new RuntimeException("Subnet not found"));

        long networkLong =
                IpAddressConverter.ipToLong(network.getNetworkAddress());

        long broadcastLong =
                IpAddressConverter.ipToLong(network.getBroadcastAddress());

        Set<String> allocated =
                ipRepository.findBySubnetId(subnetId)
                        .stream()
                        .map(IpAddress::getIpAddress)
                        .collect(Collectors.toSet());

        long start = IpAddressConverter.ipToLong(startIp);
        long end = IpAddressConverter.ipToLong(endIp);

        for(long ip = start; ip <= end; ip++){

            if(ip == networkLong || ip == broadcastLong){
                continue;
            }

            String ipStr = IpAddressConverter.longToIp(ip);

            if(!allocated.contains(ipStr)){

                IpAddress entity = new IpAddress();

                entity.setSubnetId(subnetId);
                entity.setIpAddress(ipStr);
                entity.setStatus(IpStatus.ALLOCATED);
                entity.setAllocatedAt(LocalDateTime.now());

                return ipRepository.save(entity);
            }
        }

        throw new RuntimeException("No free IP available");
    }

    /**
     * Allocate a specific IP
     */
    public IpAddress allocateSpecificIp(Long subnetId, String ip){

        Network network = networkRepository
                .findById(subnetId)
                .orElseThrow(() -> new RuntimeException("Subnet not found"));

        long ipLong = IpAddressConverter.ipToLong(ip);

        long networkLong =
                IpAddressConverter.ipToLong(network.getNetworkAddress());

        long broadcastLong =
                IpAddressConverter.ipToLong(network.getBroadcastAddress());

        if(ipLong == networkLong || ipLong == broadcastLong){
            throw new RuntimeException("Reserved network/broadcast IP");
        }

        if(ipRepository.existsByIpAddress(ip)){
            throw new RuntimeException("IP already allocated");
        }

        IpAddress entity = new IpAddress();

        entity.setSubnetId(subnetId);
        entity.setIpAddress(ip);
        entity.setStatus(IpStatus.ALLOCATED);
        entity.setAllocatedAt(LocalDateTime.now());

        return ipRepository.save(entity);
    }

    /**
     * Release an allocated IP
     */
    public void releaseIp(String ip){

        Optional<IpAddress> optional =
                ipRepository.findByIpAddress(ip);

        if(optional.isEmpty()){
            return;
        }

        ipRepository.delete(optional.get());
    }

    /**
     * List IPs in subnet
     */
    public List<IpAddress> getIpsOfSubnet(Long subnetId){

        return ipRepository.findBySubnetId(subnetId);
    }

    /**
     * Bulk allocate IPs
     */

    public List<IpAddress> bulkAllocate(Long subnetId,
                                        int count,
                                        String startIp,
                                        String endIp){

        Network network = networkRepository
                .findById(subnetId)
                .orElseThrow(() -> new RuntimeException("Subnet not found"));

        long networkLong =
                IpAddressConverter.ipToLong(network.getNetworkAddress());

        long broadcastLong =
                IpAddressConverter.ipToLong(network.getBroadcastAddress());

        long start = IpAddressConverter.ipToLong(startIp);
        long end = IpAddressConverter.ipToLong(endIp);

        Set<String> allocated =
                ipRepository.findBySubnetId(subnetId)
                        .stream()
                        .map(IpAddress::getIpAddress)
                        .collect(Collectors.toSet());

        List<IpAddress> result = new ArrayList<>();

        for(long ip = start; ip <= end && result.size() < count; ip++){

            if(ip == networkLong || ip == broadcastLong){
                continue;
            }

            String ipStr = IpAddressConverter.longToIp(ip);

            if(!allocated.contains(ipStr)){

                IpAddress entity = new IpAddress();

                entity.setSubnetId(subnetId);
                entity.setIpAddress(ipStr);
                entity.setStatus(IpStatus.ALLOCATED);
                entity.setAllocatedAt(LocalDateTime.now());

                result.add(entity);
                allocated.add(ipStr);
            }
        }

        return ipRepository.saveAll(result);
    }

}