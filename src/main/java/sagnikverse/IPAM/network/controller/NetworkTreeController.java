package sagnikverse.IPAM.network.controller;



import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sagnikverse.IPAM.network.dto.NetworkTreeNode;
import sagnikverse.IPAM.network.service.NetworkTreeService;

import java.util.List;

@RestController
@RequestMapping("/api/networks")
@RequiredArgsConstructor
public class NetworkTreeController {

    private final NetworkTreeService treeService;

    @GetMapping("/tree")
    public List<NetworkTreeNode> getTree(){
        return treeService.getNetworkTree();
    }
}