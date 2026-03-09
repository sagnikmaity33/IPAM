package sagnikverse.IPAM.ip.dto;

import lombok.Data;

@Data
public class BulkAllocationRequest {

    private Long subnetId;
    private int count;

}