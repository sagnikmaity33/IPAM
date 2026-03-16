package sagnikverse.IPAM.network.controller;

import sagnikverse.IPAM.network.entity.Network;
import sagnikverse.IPAM.network.service.NetworkHierarchyService;
import sagnikverse.IPAM.network.service.NetworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sagnikverse.IPAM.network.service.SubnetSplitService;

import java.util.List;

@RestController
@RequestMapping("/api/networks")
@RequiredArgsConstructor
public class NetworkController {

    private final NetworkService networkService;
    private final NetworkHierarchyService hierarchyService;
    private final SubnetSplitService splitService;

    @PostMapping
    public Network createNetwork(@RequestBody String cidr){

        return networkService.createNetwork(cidr);
    }

    @PostMapping("/{id}/subnet")
    public Network createChildSubnet(
            @PathVariable Long id,
            @RequestBody String cidr){

        return hierarchyService.createChildSubnet(id, cidr);
    }

    @PostMapping("/{id}/split")
    public List<String> splitNetwork(
            @PathVariable Long id,
            @RequestParam int newPrefix,
            @RequestParam String cidr){

        return splitService.split(id, cidr, newPrefix);
    }
}