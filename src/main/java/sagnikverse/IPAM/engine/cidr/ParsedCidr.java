package sagnikverse.IPAM.engine.cidr;

public class ParsedCidr {

    private String ip;
    private int prefix;


    public ParsedCidr(String ip, int prefix) {
        this.ip = ip;
        this.prefix = prefix;
    }

    public String getIp() {
        return ip;
    }
    public int getPrefix() {
        return prefix;
    }
}
