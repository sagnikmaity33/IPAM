package sagnikverse.IPAM.engine.validation;

import org.springframework.stereotype.Component;

@Component
public class OverlapDetector {

    public boolean isOverlapping(long start1,
                                 long end1,
                                 long start2,
                                 long end2) {

        return start1 <= end2 && start2 <= end1;
    }
}