package sagnikverse.IPAM.engine.cidr;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sagnikverse.IPAM.util.IpAddressConverter;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SubnetSplitEngine {

    private final CidrParser cidrParser;

    public List<String> splitSubnet(String cidr, int newPrefix){

        ParsedCidr parsed = cidrParser.parse(cidr);

        int originalPrefix = parsed.getPrefix();

        if(newPrefix <= originalPrefix){
            throw new RuntimeException("New prefix must be larger than parent prefix");
        }

        long networkLong =
                IpAddressConverter.ipToLong(parsed.getIp());

        int subnetCount =
                (int) Math.pow(2, newPrefix - originalPrefix);

        long blockSize =
                (long) Math.pow(2, 32 - newPrefix);

        List<String> result = new ArrayList<>();

        for(int i = 0; i < subnetCount; i++){

            long subnetStart = networkLong + (i * blockSize);

            String subnetIp =
                    IpAddressConverter.longToIp(subnetStart);

            String subnetCidr =
                    subnetIp + "/" + newPrefix;

            result.add(subnetCidr);
        }

        return result;
    }
}