package sagnikverse.IPAM.network.controller;

import sagnikverse.IPAM.network.entity.Network;
import sagnikverse.IPAM.network.service.NetworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/networks")
@RequiredArgsConstructor
public class NetworkController {

    private final NetworkService networkService;

    @PostMapping
    public Network createNetwork(@RequestBody String cidr){

        return networkService.createNetwork(cidr);
    }
}