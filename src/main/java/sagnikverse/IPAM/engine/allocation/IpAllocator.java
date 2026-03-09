package sagnikverse.IPAM.engine.allocation;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class IpAllocator {

    public String findNextAvailableIp(String startIp,
                                     String endIp,
                                     Set<String> allocatedIps) {

        long start = ipToLong(startIp);
        long end = ipToLong(endIp);

        for (long ip = start; ip <= end; ip++) {

            String candidate = longToIp(ip);

            if (!allocatedIps.contains(candidate)) {

                return candidate;
            }
        }

        throw new RuntimeException("No IP available");
    }

    private long ipToLong(String ip) {

        String[] parts = ip.split("\\.");

        return (Long.parseLong(parts[0]) << 24)
                | (Long.parseLong(parts[1]) << 16)
                | (Long.parseLong(parts[2]) << 8)
                | Long.parseLong(parts[3]);
    }

    private String longToIp(long ip) {

        return ((ip >> 24) & 255) + "."
                + ((ip >> 16) & 255) + "."
                + ((ip >> 8) & 255) + "."
                + (ip & 255);
    }
}