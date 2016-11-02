package list;

public class LockDList extends DList {

    // Same signature, but uses LockDListNode instead
    protected DListNode newNode(Object item, DListNode prev, DListNode next) {
        return new LockDListNode(item, prev, next);
  }

    public LockDList() {
        super();
    }

    public void lockNode(DListNode node) {
        LockDListNode lnode = (LockDListNode) node;
        lnode.setLocked(true);
    }

    public void remove(DListNode node) {
        if ((node != null) && ((LockDListNode) node).isLocked()) {
            super.remove(node);
        }
    }
}


