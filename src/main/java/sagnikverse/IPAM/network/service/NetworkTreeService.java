package sagnikverse.IPAM.network.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sagnikverse.IPAM.network.dto.NetworkTreeNode;
import sagnikverse.IPAM.network.entity.Network;
import sagnikverse.IPAM.network.repository.NetworkRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NetworkTreeService {

    private final NetworkRepository networkRepository;

    public List<NetworkTreeNode> getNetworkTree(){

        List<Network> roots =
                networkRepository.findAll()
                        .stream()
                        .filter(n -> n.getParentNetworkId() == null)
                        .toList();

        return roots.stream()
                .map(this::buildNode)
                .collect(Collectors.toList());
    }

    private NetworkTreeNode buildNode(Network network){

        NetworkTreeNode node = new NetworkTreeNode();

        node.setId(network.getId());
        node.setCidr(network.getCidr());
        node.setTotalIps(network.getTotalIps());

        List<Network> children =
                networkRepository.findByParentNetworkId(network.getId());

        for(Network child : children){

            node.getChildren().add(buildNode(child));
        }

        return node;
    }
}