package sagnikverse.IPAM.engine.cidr;

import org.springframework.stereotype.Component;

@Component
public class CidrCalculator {

    public long totalIps(int prefix) {
        return (long) Math.pow(2, 32 - prefix);
    }

    public String getNetworkAddress(String ip, int prefix) {

        int ipInt = ipToInt(ip);

        int mask = -1 << (32 - prefix);

        int network = ipInt & mask;

        return intToIp(network);
    }

    public String getBroadcastAddress(String ip, int prefix) {

        int ipInt = ipToInt(ip);

        int mask = -1 << (32 - prefix);

        int broadcast = ipInt | ~mask;

        return intToIp(broadcast);
    }

    private int ipToInt(String ip) {

        String[] parts = ip.split("\\.");

        return (Integer.parseInt(parts[0]) << 24)
                | (Integer.parseInt(parts[1]) << 16)
                | (Integer.parseInt(parts[2]) << 8)
                | Integer.parseInt(parts[3]);
    }

    private String intToIp(int ip) {

        return ((ip >> 24) & 255) + "."
                + ((ip >> 16) & 255) + "."
                + ((ip >> 8) & 255) + "."
                + (ip & 255);
    }

    // Converts IP string → numeric value
    public long ipToLong(String ip){

        String[] parts = ip.split("\\.");

        return (Long.parseLong(parts[0]) << 24)
                | (Long.parseLong(parts[1]) << 16)
                | (Long.parseLong(parts[2]) << 8)
                | Long.parseLong(parts[3]);
    }

    public long getNetworkStart(String networkAddress){
        return ipToLong(networkAddress);
    }

    public long getNetworkEnd(String broadcastAddress){
        return ipToLong(broadcastAddress);
    }
}