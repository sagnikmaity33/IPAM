package sagnikverse.IPAM.engine.validation;

public class IntervalTree {

    private IntervalNode root;

    public void insert(long start, long end) {
        root = insert(root, start, end);
    }

    private IntervalNode insert(IntervalNode node, long start, long end) {

        if (node == null) {
            return new IntervalNode(start, end);
        }

        if (start < node.start) {
            node.left = insert(node.left, start, end);
        } else {
            node.right = insert(node.right, start, end);
        }

        node.maxEnd = Math.max(node.maxEnd, end);

        return node;
    }

    public boolean overlaps(long start, long end) {
        return overlaps(root, start, end);
    }

    private boolean overlaps(IntervalNode node, long start, long end) {

        if (node == null) {
            return false;
        }

        if (start <= node.end && end >= node.start) {
            return true;
        }

        if (node.left != null && node.left.maxEnd >= start) {
            return overlaps(node.left, start, end);
        }

        return overlaps(node.right, start, end);
    }
}