package sagnikverse.IPAM.engine.validation;

public class IntervalNode {

    public long start;
    public long end;
    public long maxEnd;

    public IntervalNode left;
    public IntervalNode right;

    public IntervalNode(long start, long end) {
        this.start = start;
        this.end = end;
        this.maxEnd = end;
    }
}
