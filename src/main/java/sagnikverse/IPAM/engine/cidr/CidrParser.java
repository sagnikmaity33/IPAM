package sagnikverse.IPAM.engine.cidr;


import org.springframework.stereotype.Component;

@Component
public class CidrParser {

    public ParsedCidr parse(String cidr) {

        if(!cidr.contains("/"))
            throw new IllegalArgumentException("Invalid CIDR format");

        String[] parts = cidr.split("/");

        String ip = parts[0];
        int prefix = Integer.parseInt(parts[1]);

        if(prefix < 0 || prefix > 32)
            throw new IllegalArgumentException("Invalid prefix");

        return new ParsedCidr(ip, prefix);
    }

}