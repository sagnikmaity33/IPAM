package sagnikverse.IPAM.engine.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sagnikverse.IPAM.network.entity.Network;
import sagnikverse.IPAM.network.repository.NetworkRepository;
import sagnikverse.IPAM.util.IpAddressConverter;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HierarchyValidator {

    private final NetworkRepository networkRepository;

    public void validateChildSubnet(Network parent, Network child){

        long parentStart =
                IpAddressConverter.ipToLong(parent.getNetworkAddress());

        long parentEnd =
                IpAddressConverter.ipToLong(parent.getBroadcastAddress());

        long childStart =
                IpAddressConverter.ipToLong(child.getNetworkAddress());

        long childEnd =
                IpAddressConverter.ipToLong(child.getBroadcastAddress());

        if(childStart < parentStart || childEnd > parentEnd){
            throw new RuntimeException("Child subnet outside parent range");
        }

        List<Network> siblings =
                networkRepository.findByParentNetworkId(parent.getId());

        for(Network sib : siblings){

            long start =
                    IpAddressConverter.ipToLong(sib.getNetworkAddress());

            long end =
                    IpAddressConverter.ipToLong(sib.getBroadcastAddress());

            if(!(childEnd < start || childStart > end)){
                throw new RuntimeException("Child subnet overlaps sibling");
            }
        }
    }
}