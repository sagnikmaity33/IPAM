package sagnikverse.IPAM.network.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class NetworkTreeNode {

    private Long id;
    private String cidr;

    private Long totalIps;
    private Long usedIps;

    private List<NetworkTreeNode> children = new ArrayList<>();
}