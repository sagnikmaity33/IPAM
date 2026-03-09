package sagnikverse.IPAM.network.dto;
import lombok.Data;

@Data
public class SubnetUtilization {

    private long totalIps;
    private long allocatedIps;
    private long freeIps;
    private double utilization;

}