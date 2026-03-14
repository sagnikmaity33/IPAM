package sagnikverse.IPAM.util;

public class IpAddressConverter {

    public static long ipToLong(String ip){

        String[] parts = ip.split("\\.");

        return (Long.parseLong(parts[0]) << 24)
                | (Long.parseLong(parts[1]) << 16)
                | (Long.parseLong(parts[2]) << 8)
                | Long.parseLong(parts[3]);
    }

    public static String longToIp(long ip){

        return ((ip >> 24) & 255) + "."
                + ((ip >> 16) & 255) + "."
                + ((ip >> 8) & 255) + "."
                + (ip & 255);
    }

}