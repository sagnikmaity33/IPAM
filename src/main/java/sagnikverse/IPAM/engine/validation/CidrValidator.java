package sagnikverse.IPAM.engine.validation;



import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class CidrValidator {

    private static final Pattern IP_PATTERN =
            Pattern.compile(
                    "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}$"
            );

    public void validate(String cidr){

        if(!cidr.contains("/"))
            throw new IllegalArgumentException("Invalid CIDR format");

        String[] parts = cidr.split("/");

        String ip = parts[0];
        int prefix = Integer.parseInt(parts[1]);

        validateIp(ip);
        validatePrefix(prefix);

    }

    private void validateIp(String ip){

        if(!IP_PATTERN.matcher(ip).matches())
            throw new IllegalArgumentException("Invalid IP format");

    }

    private void validatePrefix(int prefix){

        if(prefix < 0 || prefix > 32)
            throw new IllegalArgumentException("Invalid CIDR prefix");

    }

}
