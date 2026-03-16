package sagnikverse.IPAM.network.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sagnikverse.IPAM.engine.cidr.SubnetSplitEngine;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubnetSplitService {

    private final SubnetSplitEngine splitEngine;

    public List<String> split(Long networkId, String cidr, int newPrefix){

        return splitEngine.splitSubnet(cidr, newPrefix);
    }
}