package list;

public class LockDListNode extends DListNode {
    
    protected boolean locked;

    LockDListNode(Object i, DListNode p, DListNode n) {
        super(i, p, n);
        locked = false;
    }

    void setLocked(boolean b) {
        locked = b;
    } 

    boolean isLocked() {
        return locked;
    }
}

