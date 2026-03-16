package sagnikverse.IPAM.engine.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sagnikverse.IPAM.network.entity.Network;
import sagnikverse.IPAM.network.repository.NetworkRepository;
import sagnikverse.IPAM.util.IpAddressConverter;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OverlapDetector {

    private final NetworkRepository networkRepository;

    /**
     * Check if new subnet overlaps with existing ones
     */
    public boolean hasOverlap(long newStart, long newEnd) {

        List<Network> networks = networkRepository.findAll();

        for (Network n : networks) {

            long start =
                    IpAddressConverter.ipToLong(n.getNetworkAddress());

            long end =
                    IpAddressConverter.ipToLong(n.getBroadcastAddress());

            if (isOverlapping(newStart, newEnd, start, end)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Mathematical overlap check
     */
    public boolean isOverlapping(long start1,
                                 long end1,
                                 long start2,
                                 long end2) {

        return start1 <= end2 && start2 <= end1;
    }
}