package sagnikverse.IPAM.ip.controller;

import sagnikverse.IPAM.ip.dto.BulkAllocationRequest;
import sagnikverse.IPAM.ip.entity.IpAddress;
import sagnikverse.IPAM.ip.service.IpAllocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ip")
@RequiredArgsConstructor
public class IpController {

    private final IpAllocationService ipService;

    @PostMapping("/allocate")
    public IpAddress allocateIp(@RequestParam Long subnetId,
                                @RequestParam String startIp,
                                @RequestParam String endIp){

        return ipService.allocateNextIp(subnetId, startIp, endIp);
    }


    @DeleteMapping("/{ip}")
    public void releaseIp(@PathVariable String ip){

        ipService.releaseIp(ip);
    }

    @PostMapping("/allocate-specific")
    public IpAddress allocateSpecific(@RequestParam Long subnetId,
                                      @RequestParam String ip){

        return ipService.allocateSpecificIp(subnetId, ip);
    }

    @GetMapping("/subnet/{id}")
    public List<IpAddress> getSubnetIps(@PathVariable Long id){

        return ipService.getIpsOfSubnet(id);
    }

    @PostMapping("/bulk-allocate")
    public List<IpAddress> bulkAllocate(
            @RequestBody BulkAllocationRequest request){

        return ipService.bulkAllocate(
                request.getSubnetId(),
                request.getCount(),
                "192.168.1.1",
                "192.168.1.254"
        );

    }
}