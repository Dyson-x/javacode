                                                                AQS���
��ռʽ��
ͬ������
��ռʽ����ȡ -- Դ�������
����ȡ��ʧ��ʱ�������·�����
��ռʽ���Ļ�ȡ����ͼ���£�
��ռʽ���ͷ� --  Դ�����:
�ܽ᣺��ռ����ȡ���ͷ��ܽ�:
���ж�ʽ��ȡ��
��ʱ�ȴ���ȡ����tryAcquireNanos()������
�������ReentrantLock
ReentrantLock��Lock��ʹ��Ƶ����ߵ��ࣩ ---   ��������
�����Ե�ʵ��ԭ��
��ƽ����ǹ�ƽ�� ��ReentryLockĬ��Ϊ�ǹ�ƽ����Ч�ʿ죬�ڸ߲���������£��ǹ�ƽ����Ч�ʼ����ǹ�ƽ����100����
�������ReentrantReadWriteLock ��д��

--------------------------------------------------------------------------------
��ͬ������У�AQS������Ĳ��֣�ͬ�������ʵ������AQS�ṩ��ģ�巽����ʵ��ͬ
��������塣

AQSʵ���˶�ͬ��״̬�Ĺ����Լ��������߳̽����Ŷӡ��ȴ�֪ͨ�ȵȵײ�ʵ�֡�

AQS�������:ͬ�����С���ռ���Ļ�ȡ���ͷš��������Ļ�ȡ���ͷš����ж�������ʱ����
��һϵ�й��ܵ�ʵ��������AQS�ṩ��ģ�巽����
��ռʽ��
void acquire(int arg) : ��ռʽ��ȡͬ��״̬�������ȡʧ�ܲ���ͬ�����н��еȴ���
void acquireInteruptibly(int arg)����1�Ļ����ϣ��˷���������ͬ����������Ӧ�ж�
boolean tryAcquireNanos(int arg,long nanosTimeOut)����2�Ļ��������˳�ʱ�ȴ����ܣ�
//����Ԥ��ʱ�仹δ�����ֱ�ӷ��ء�
boolean tryAcquire(int arg)����ȡ���ɹ�����true�����򷵻�false
boolean release(int arg) : �ͷ�ͬ��״̬���÷����ỽ����ͬ�����е���һ���ڵ㡣

����ʽ��
void acquireShared(int arg) : �����ȡͬ��״̬,ͬһʱ�̶���̻߳�ȡͬ��״̬
void acquireSharedInterruptibly(int arg) : ��1�Ļ�����������Ӧ�ж�
boolean tryAcquireSharedNanos(int arg,long nanosTimeOut):��2�Ļ���������
//��ʱ�ȴ�
boolean releaseShared(int arg) : ����ʽ�ͷ�ͬ��״̬

ͬ������
��AQS�ڲ���һ����̬�ڲ���Node������ͬ��������ÿ������Ľڵ㡣
�ڵ�����������:
int waitStatus:�ڵ�״̬
Node prev:ͬ��������ǰ���ڵ�
Node next:ͬ�������к�̽ڵ�
Thread thread:��ǰ�ڵ��װ���̶߳���
Node nextWaiter:�ȴ���������һ���ڵ�

�ڵ�״ֵ̬����:

/** waitStatus value to indicate thread has cancelled */
//��ǰ�ڵ����ڳ�ʱ�����ж���ͬ��������ȡ��
static final int CANCELLED =  1; 
/** waitStatus value to indicate successor's thread needs unparking */
//��ǰ�Ľڵ��ǰ���ڵ㱻��������ǰ�ڵ���ִ��release����cancelʱ��Ҫִ��unpark
//�����Ѻ�̽ڵ�
static final int SIGNAL    = -1;
/** waitStatus value to indicate thread is waiting on condition */
//�ڵ㴦�ڵȴ������С��������̶߳�Condition����signal()�����󣬸ýڵ��
//�ӵȴ������Ƶ�ͬ��������
static final int CONDITION = -2;
/**
 * waitStatus value to indicate the next acquireShared should
 * unconditionally propagate
 */
 //����ʽͬ��״̬������������
static final int PROPAGATE = -3;


AQSͬ�����в��ô���ͷβ�ڵ��˫������
--------------------------------------------------------------------------------
��ռʽ����ȡ -- Դ�������
��ռʽ���Ļ�ȡ������ͼ��ʾΪReentrantLockԴ�룺
/**
 * Performs lock.  Try immediate barge, backing up to normal
 * acquire on failure.
 */
final void lock() {
    //ʹ��CAS�������Խ�ͬ��״̬��0��Ϊ1������ɹ���ͬ��״̬����
    //�߳���Ϊ��ǰ�̣߳��������acquire()����
    if (compareAndSetState(0, 1))  
        setExclusiveOwnerThread(Thread.currentThread());
    else
        acquire(1);
}


--------------------------------------------------------------------------------
acquire()������
//�ٴγ��Ի�ȡͬ��״̬������ɹ�����ǰ�߳���Ϊ�������̣߳������˳�
//ʧ�����������ִ��
public final void acquire(int arg) {
    if (!tryAcquire(arg) &&
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
        selfInterrupt();
}

tryAcquire()������
protected boolean tryAcquire(int arg) {
    throw new UnsupportedOperationException();
}

���Ի�ȡ����Դ���ɹ�����true��������Դ��ȡ/�ͷŷ�ʽ�����Զ���ͬ����ʵ�֡�ReentrantLock��Ĭ��Ϊ�ǹ�ƽ������ƽ�����������ۣ����ڷǹ�ƽ���ľ���ʵ�ַ�ʽ���£�
nonfairTryAcquire()����������ȥ��ȡ��������ǰ����Դû�б���ʼ������ֱ�ӽ���ǰ�߳���Ϊ�������̣߳����������߳̾��ǵ�ǰ�̣߳����룬�޸�ͬ��״̬����Ϊ���������������true�����򷵻�false
/**
 * Performs non-fair tryLock.  tryAcquire is implemented in
 * subclasses, but both need nonfair try for trylock method.
 * ִ�зǹ�ƽtryLock��tryAcquire����������ʵ�ֵģ����Ƕ�
 * ��ҪtryLock�����ķǹ�ƽ����
 */
 
final boolean nonfairTryAcquire(int acquires) {
     final Thread current = Thread.currentThread();  //��ȡ��ǰ�߳�
    int c = getState();  //ȡ�õ�ǰͬ��״̬
    //����ǰͬ��״̬Ϊ0��������û�б���ʼ���������CAS�����޸�ͬ��״̬
    //����ǰ�߳���Ϊ�������̣߳�����true
    if (c == 0) {
        if (compareAndSetState(0, acquires)) {
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    //���ͬ��״̬�Ѿ�����ʼ��������Ҫ�жϳ������߳��Ƿ�Ϊ��ǰ�߳�
    //���Ŀ������ԣ�����ǵ�ǰ�̣߳��򽫵�ǰ�������̵߳�״ֵ̬��һ
    else if (current == getExclusiveOwnerThread()) {
        int nextc = c + acquires;
        //�쳣����
        if (nextc < 0) // overflow
            throw new Error("Maximum lock count exceeded");
        //��������ͬ��״̬
        setState(nextc);
        return true;
    }
    return false;
}


--------------------------------------------------------------------------------
addWaiter()����������ǰ�̷߳�װ�ɽڵ�β����ͬ�����е���
/**
 * Creates and enqueues node for current thread and given mode.
 * ���ո���ģʽ����ռʽ��Ϊ�ýڵ��װ��ǰ�̲߳�����ͬ������
 * @param mode Node.EXCLUSIVE for exclusive, Node.SHARED for shared
 * @return the new node
 */
private Node addWaiter(Node mode) {
    //����ǰ�̷߳�װΪ�ڵ�
    Node node = new Node(Thread.currentThread(), mode);
    // Try the fast path of enq; backup to full enq on failure
    Node pred = tail;    //��ȡ��ǰ���е�β�ڵ�
    //���β�ڵ㲻Ϊ����ʹ��CAS�������Խ���ǰ�ڵ�β����ͬ������
    //����ɹ����ص�ǰ�ڵ�
    if (pred != null) {
        node.prev = pred;
        if (compareAndSetTail(pred, node)) {
            pred.next = node;
            return node;
        }
    }
    //��ǰβ�ڵ�Ϊ�գ�����CASβ��ʧ�ܾͻ�ִ�и÷���
    enq(node);
    return node;
}

enq()������
1.  �ڵ�ǰ�߳��ǵ�һ������ͬ������ʱ������compareAndSetHead(new Node())�����������ʽ���е�ͷ��� �ĳ�ʼ���� 
2. ���CASβ����ڵ�ʧ�ܺ����������г��ԣ�
/**
 * Inserts node into queue, initializing if necessary. See picture above.
 * ���ڵ���뵽ͬ�������У���Ҫʱ��ʼ��
 * @param node the node to insert
 * @return node's predecessor
 */
private Node enq(final Node node) {
    for (;;) {
        //��ǰ�ڵ�Ϊ��
        Node t = tail;
        if (t == null) { // Must initialize
            //ͷ�ڵ��ʼ��
            if (compareAndSetHead(new Node()))
                tail = head;
        } else {
            node.prev = t;
            //CASβ�壬ʧ���򲻶Ͻ�����������ֱ���ɹ�Ϊֹ
            if (compareAndSetTail(t, node)) {
                t.next = node;
                return t;
            }
        }
    }
}


--------------------------------------------------------------------------------
acquireQueued()������ ���Ŷӻ�ȡ����----  ��Ҫ  ---- 
�����ǰ�ڵ��ǰ���ڵ���ͷ��㣬�����ܹ����ͬ��״̬�Ļ�����ǰ�߳��ܹ���������÷���ִ�н����˳���
��ȡ��ʧ�ܵĻ����Ƚ��ڵ�״̬����ΪSIGNAL��Ȼ�����LookSupport.park()����ʹ�õ�ǰ�߳�������
/**
 * Acquires in exclusive uninterruptible mode for thread already in
 * queue. Used by condition wait methods as well as acquire.
 *
 * @param node the node
 * @param arg the acquire argument
 * @return {@code true} if interrupted while waiting
 */
// �����ȴ���ȡ��Դ
final boolean acquireQueued(final Node node, int arg) {
    boolean failed = true;
    try {
        boolean interrupted = false;
        for (;;) {
            //��ǰ�ڵ��ǰ���ڵ�
            final Node p = node.predecessor();
            //���ǰ���ڵ�Ϊͷ�ڵ㣬���Ի�ȡͬ��״̬
            if (p == head && tryAcquire(arg)) {
                //����ͷָ��ָ��ǰ�ڵ�
                setHead(node);
                //�ͷ�ǰ���ڵ�
                p.next = null; // help GC
                failed = false;
                return interrupted;
            }
            //��ȡͬ��״̬ʧ�ܣ��߳̽���ȴ�״̬�ȴ���ȡ��ռʽ
            //�����interruptֻ��һ�������ı�Ƕ���
            if (shouldParkAfterFailedAcquire(p, node) &&
                parkAndCheckInterrupt())
                interrupted = true;
        }
    } finally {
        //�����ȡ��Դʧ�ܣ�����ǰ�ڵ���Ϊȡ��״̬
        if (failed)
            cancelAcquire(node);
    }
}


˼��һ�������жϱ��λ���⣺
����acquireQueued��⣺���ﵱ�߳̽��뵽�������Ի�ȡ����Դʱ���ᷢ�ֵ���ȡͬ��ʧ��ʱ���߳̽��뵽shouldParkAfterFaildAcquire()��������ǰ���ڵ�״̬��ΪSIGNAL��ʾ��ǰ�ڵ㱻������Ȼ�����parlAndCheckInterrupt()��������ǰ�ڵ�����������ΪparlAndCheckInterrupt()������
private final boolean parkAndCheckInterrupt() {
    LockSupport.park(this);
    return Thread.interrupted();
}

�������ڸ÷�������л���� interrupted()�������÷����ĺ����Ϊ���̵߳�һ�α��жϵ�ʱ��᷵��true�����ҽ��ж�״̬��������߳����������ø÷�ʱ�򷵻�false��

�Ϳ������Ϊ�÷���ֻ�ǵ����ĸ��߳���������ȡͬ��״̬ʱ���ж���û�б��жϹ���ֻҪ��һ�α��ж��ˣ�������Ҫ�����Ľ����߳��жϣ�ֻ��Ҫ�ñ�־λinterrupted֪�����̱߳��жϹ������߳��ٴα��ж�û��Ҫ�ٸ�interrupted���¸�ֵ��ֱ�ӷ���false���ɡ���������⣩
/**
 * Tests whether the current thread has been interrupted.  The
 * <i>interrupted status</i> of the thread is cleared by this method.  In
 * other words, if this method were to be called twice in succession, the
 * second call would return false (unless the current thread were
 * interrupted again, after the first call had cleared its interrupted
 * status and before the second call had examined it).
 *
 * <p>A thread interruption ignored because a thread was not alive
 * at the time of the interrupt will be reflected by this method
 * returning false.
 *
 * @return  <code>true</code> if the current thread has been interrupted;
 *          <code>false</code> otherwise.
 * @see #isInterrupted()
 * @revised 6.0
 */
public static boolean interrupted() {
    return currentThread().isInterrupted(true);
}

��ʱ����˵�߳��ڻ�ȡ���Ĺ����б��ж��ˣ����ǲ�û�д����������ǻ᷵��interrupted�������������жϴ�����ǵ���selfInterrupt()
public final void acquire(int arg) {
    if (!tryAcquire(arg) &&
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
        selfInterrupt();
}

selfInterrupt()�������ڸ÷��������������Ͻ��߳��ж�
/**
 * Convenience method to interrupt current thread.
 */
static void selfInterrupt() {
    Thread.currentThread().interrupt();
}


--------------------------------------------------------------------------------
˼���������ڽڵ㴦�������˳�����
�����뵽acquireQueued�����в���������ȡͬ��״̬����ռʽ���Ļ�ȡ�Ϳ��ж����Ļ�ȡ����������
�����жϻ�ȡ��ʱ��ֻҪ�̱߳��жϾͻ�ֱ�ӱ��쳣Ȼ���˳������Ƕ�ռʽ����һ������������Ϊ
�ж϶��˳������ǻὫ�жϱ�Ǹı���ѣ�����һֱ����ֱ����ȡ������ô����ʲô����»��Ƴ��أ�
������뵽tryAcquire()������tryAcquire()Ĭ�ϵ��õ���nofiretryAcquire()������д������
final boolean nonfairTryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    int c = getState();
    if (c == 0) {
        if (compareAndSetState(0, acquires)) {
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    else if (current == getExclusiveOwnerThread()) {
        int nextc = c + acquires;
        if (nextc < 0) // overflow
            throw new Error("Maximum lock count exceeded");
        setState(nextc);
        return true;
    }
    return false;
}

���������Դ�룬���߳�������һֱ���Ի�ȡ����Դʱ�����Ҫ����ֹͣ����Ψһ�ķ����������쳣���ǻ���ʲô�쳣�أ������кܶ���
�ͱ�������Դ����ʾ��������������������쳣��������˼����˵����������ﵽ������ޣ��ֱ��統�������������Ի�ȡͬ��״̬ʱ��
��ʱ����await()���������߳�����ȴ�������ʱҲ���ܻ��׳��쳣�����оͱ������AQS��await()�����ͻ�����ߵ���һ�� fullyRelease()����
�÷����Ĵ����˼��������ýڵ�״̬�����ȡ��״̬�Ļ��ͻ���IllegalMonitorStateException()�쳣�����������״̬�������ദ��
��Ȼ���кܶ����쳣������Ͳ��������֮Ҫ������acquireQueued������Ҫô��ȡ���ɹ���Ҫô���쳣������tryfinally�顣

�����뵽tryfinally��֮��ͻᴦ���쳣
���濴һ��cancelAcquire()Դ�룺��ʵ��ߵĺ��Ĵ������node.waitStatus = Node.CANCELLED; ���ڵ�״̬��Ϊȡ��״̬
private void cancelAcquire(Node node) {
    // Ignore if node doesn't exist
    if (node == null)
        return;

    node.thread = null;

    // Skip cancelled predecessors
    Node pred = node.prev;
    while (pred.waitStatus > 0)
        node.prev = pred = pred.prev;

    // predNext is the apparent node to unsplice. CASes below will
    // fail if not, in which case, we lost race vs another cancel
    // or signal, so no further action is necessary.
    Node predNext = pred.next;

    // Can use unconditional write instead of CAS here.
    // After this atomic step, other Nodes can skip past us.
    // Before, we are free of interference from other threads.
    node.waitStatus = Node.CANCELLED;

    // If we are the tail, remove ourselves.
    if (node == tail && compareAndSetTail(node, pred)) {
        compareAndSetNext(pred, predNext, null);
    } else {
        // If successor needs signal, try to set pred's next-link
        // so it will get one. Otherwise wake it up to propagate.
        int ws;
        if (pred != head &&
            ((ws = pred.waitStatus) == Node.SIGNAL ||
             (ws <= 0 && compareAndSetWaitStatus(pred, ws, Node.SIGNAL))) &&
            pred.thread != null) {
            Node next = node.next;
            if (next != null && next.waitStatus <= 0)
                compareAndSetNext(pred, predNext, next);
        } else {
            unparkSuccessor(node);
        }

        node.next = node; // help GC
    }
}


--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
���ǰ���ڵ���ͷ���Ĳ��ҳɹ����ͬ��״̬��ʱ��if (p == head && tryAcquire(arg))������ǰ�ڵ���ָ����߳��ܹ���ȡ����ʾ��ͼ���£�

����ȡ�ɹ������г��Ӳ�����
//����ͷָ��ָ��ǰ�ڵ�
setHead(node);
//�ͷ�ǰ���ڵ�
p.next = null; // help GC
failed = false;
return interrupted;

setHead() ��������������ͷ��㣬Ȼ�󽫵�ǰ�ڵ�prevָ����Ϊnull�����ڵ��thread��Ϊnull
private void setHead(Node node) {
    head = node;
    node.thread = null;
    node.prev = null;
}

ͨ���������̣���ԭ����ͷ���next����Ϊnull����������prev��ԭ������null�����µĶ�ͷָ��
�Ѿ�ָ���˵�ǰ�ڵ㣬��ʱ���κ����õ�ԭ��ͷ��㱻GC���л��ա�ʾ��ͼ���£�


--------------------------------------------------------------------------------
����ȡ��ʧ��ʱ�������·�����
shouldParkAfterFailedAcquire()���������Խ���ǰ�ڵ��ǰ���ڵ�״̬����Ϊ����״̬
private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
    int ws = pred.waitStatus;   //��ȡǰ���ڵ�״̬
    //�����ǰ�ڵ㱻��������ֱ�ӵ㷵��
    if (ws == Node.SIGNAL)
        /*
         * This node has already set status asking a release
         * to signal it, so it can safely park.
         */
        return true;
    //����ǰ�ڵ㱻ȡ�����򲻶�����ֱ���ҵ���һ����Ϊȡ��״̬�Ľڵ�  
    if (ws > 0) {
        /*
         * Predecessor was cancelled. Skip over predecessors and
         * indicate retry.
         */
        do {
            node.prev = pred = pred.prev;
        } while (pred.waitStatus > 0);
        pred.next = node;
    //���Խ�ǰ���ڵ�����Ϊ����״̬������޸ĳɹ���ô�´ν���÷�����ֱ�ӷ���true������Ҫ������жϴ���
    } else {
        /*
         * waitStatus must be 0 or PROPAGATE.  Indicate that we
         * need a signal, but don't park yet.  Caller will need to
         * retry to make sure it cannot acquire before parking.
         */
        compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
    }
    return false;

��compareAndSetWaitStatus����ʧ����˵�� shouldParkAfterFailedAcquire��������false��Ȼ�����acquireQueued()������for (;;)��ѭ���л�������ԣ�ֱ�� compareAndSetWaitStatus���ýڵ�״̬λΪSIGNALʱshouldParkAfterFailedAcquire����trueʱ�Ż�ִ�з��� parkAndCheckInterrupt()������

parkAndCheckInterrupt()����������LockSupport.park()�������������ۣ���������ǰ�߳�
private final boolean parkAndCheckInterrupt() {
    LockSupport.park(this);
    return Thread.interrupted();
}


--------------------------------------------------------------------------------
��ռʽ���Ļ�ȡ����ͼ���£�


--------------------------------------------------------------------------------

--------------------------------------------------------------------------------

��ռʽ���ͷ� --  Դ�����:


unlock()����������AQS�е�release����
public void unlock() {
    sync.release(1);
}

release()��������ͬ��״̬�ͷųɹ�������true����ִ��if��䣬��headָ���ͷ��㲻Ϊnull�����Ҹýڵ�״̬��Ϊ0ʱ��ִ��unparkSuccessor()������
public final boolean release(int arg) {
    if (tryRelease(arg)) {  //����ȥ�ͷ���Դ
        Node h = head;      //ͷ���
        if (h != null && h.waitStatus != 0)
            unparkSuccessor(h);  //����head����һ����Ϊnull�Ľڵ�
        return true;
    }
    return false;
}

tryRelease()��������tryAcquireһ�����÷�����Ҫͨ��ͬ�����Զ���ʵ�֡�һ����˵���ͷ���Դֱ����state��ȥ��������releases���ͷź�state==0��˵���ͷųɹ���
protected final boolean tryRelease(int releases) {
    int c = getState() - releases;
    //��Ҫ�ͷŵ���Դ���ǵ�ǰ�̣߳��ͻ��׳�һ������Դ���в��Ϸ��쳣
    if (Thread.currentThread() != getExclusiveOwnerThread())
        throw new IllegalMonitorStateException();
    boolean free = false;
    //���c==0˵����Դ�ͷųɹ���������ֵ��Ϊtrue
    if (c == 0) {
        free = true;
        //����ռ���ĳ����߳���Ϊnull
        setExclusiveOwnerThread(null);
    }
    //�������ýڵ�״̬
    setState(c);
    return free;
}

unparkSuccessor()����������head����һ����Ϊnull�Ľڵ�
private void unparkSuccessor(Node node) {
    /*
     * If status is negative (i.e., possibly needing signal) try
     * to clear in anticipation of signalling.  It is OK if this
     * fails or if status is changed by waiting thread.
     */
    //���״̬Ϊ��(��(������Ҫ�ź�)�������Ԥ���źš����ʧ�ܣ����ߵ�
    //���̸߳�����״̬��Ҳû�й�ϵ��
    int ws = node.waitStatus;
    if (ws < 0)

        compareAndSetWaitStatus(node, ws, 0);

    /*
     * Thread to unpark is held in successor, which is normally
     * just the next node.  But if cancelled or apparently null,
     * traverse backwards from tail to find the actual
     * non-cancelled successor.
     */
     //ʹ��unpark����head�ĺͺ�̽ڵ㣬����head�ĺ�̽ڵ㱻ȡ������Ϊnull��
     //���tail�Ӻ���ǰѰ��head��һ����Ϊ����û��ȡ���Ľڵ�
     //ͷ���ĺ�̽ڵ�
    Node s = node.next;
    if (s == null || s.waitStatus > 0) {
        s = null;
        for (Node t = tail; t != null && t != node; t = t.prev)
            if (t.waitStatus <= 0)
                s = t;
    }
    //�����ҵ��Ľڵ�
    if (s != null)
        LockSupport.unpark(s.thread);
}


release()������unlock()�����ľ���ʵ�֡����Ȼ�ȡͷ���ĺ�̽ڵ㣬����̽ڵ㲻Ϊnull�������LockSupport.unpark()�������Ѻ�̽ڵ��װ���̡߳���ˣ�ÿһ�����ͷź�ͻỽ�Ѷ����иýڵ�ĺ�̽ڵ�����װ���̡߳�

--------------------------------------------------------------------------------

--------------------------------------------------------------------------------

�ܽ᣺��ռ����ȡ���ͷ��ܽ�:
1.�̻߳�ȡ��ʧ�ܣ����̵߳���addWaiter()��װ��Node������Ӳ�����
addWaiter()�з���enq()��ɶ�ͬ�����е�
ͷ����ʼ���Լ�CASβ��ʧ�ܺ�����Դ���

2.���֮���Ŷӻ�ȡ���ĺ��ķ���acquireQueued(),�ڵ��Ŷӻ�ȡ����һ���������̡�
���ҽ�����ǰ�ڵ��ǰ���ڵ�Ϊͷ��㲢�ҳɹ���ȡͬ��״̬ʱ��
�ڵ���Ӳ��Ҹýڵ����õ��̻߳�ȡ������
���򣬲���������ʱ�᲻��������ǰ���ڵ��״̬
��ΪSIGNAL�������LockSupport.park()����ǰ�߳�������

3.�ͷ���ʱ�ỽ�Ѻ�̽ڵ�(��̽ڵ㲻Ϊnull)

--------------------------------------------------------------------------------
���ж�ʽ��ȡ��
����Ӧ�ж�ʽ���ɵ��÷���lock.lockInterruptibly()���÷����ĵײ�����AQS��acqireInterruptibly������
acquireInterruptibly()������
public final void acquireInterruptibly(int arg)
        throws InterruptedException {
    //������ȼ���Ƿ��б��жϣ���������׳�һ���ж��쳣
    //������ȥ��ȡͬ��״̬���ɹ�ֱ���˳���ʧ�������doAcquireInterruptibly(arg)����
    if (Thread.interrupted())
        throw new InterruptedException();
    if (!tryAcquire(arg))
        doAcquireInterruptibly(arg);
}


doAcquireInterruptibly()�����������ǰ�̵߳�ǰ���ڵ�Ϊ��ͷʱ�����Ի�ȡͬ��״̬
����ȡ�ɹ��򽫶�ͷ�ڵ���ӣ���ǰ�߳���Ϊ�������̣߳�������ͷָ��ָ��ǰ�߳�����װ�Ľڵ㣬���򲻶�����ֱ����ȡ�ɹ������̱߳��ж�
/**
 * Acquires in exclusive interruptible mode.
 * @param arg the acquire argument
 */
private void doAcquireInterruptibly(int arg)
    throws InterruptedException {
        //���Խ��ڵ���뵽ͬ������
    final Node node = addWaiter(Node.EXCLUSIVE);
    boolean failed = true;
    try {
        for (;;) {
            //ȡ��ǰ�ڵ��ǰ���ڵ�
            final Node p = node.predecessor();
            //���ǰ���ڵ�Ϊͷ��㣬�ҳɹ���ȡͬ��״̬�򽫳������߳�
            //��Ϊ��ǰ�̣߳�������ͷ�ڵ����
            if (p == head && tryAcquire(arg)) {
                setHead(node);
                p.next = null; // help GC
                failed = false;
                return;
            }
            //��ȡͬ��״̬ʧ�ܣ��߳̽���ȴ�״̬�ȴ���ȡ��ռʽ
            if (shouldParkAfterFailedAcquire(p, node) &&
                parkAndCheckInterrupt())
                //�̱߳�����ʱ����⵽�ж��׳��ж��쳣���˳���
                throw new InterruptedException();
        }
    } finally {
        //��ȡͬ��״̬ʧ�ܽ���ǰ�ڵ�ȡ��
        if (failed)
            cancelAcquire(node);
    }
}



--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
��ʱ�ȴ���ȡ����tryAcquireNanos()������
����lock.tryLock(timeout��TimeUnit)��ʽ
boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
��ʱ�ȴ����ķ���������
��ָ��ʱ���ڻ�ȡ����
��ָ��ʱ����û�л�ȡ����
��ָ��ʱ���ڱ��ж�
public boolean tryLock(long timeout, TimeUnit unit)
        throws InterruptedException {
    return sync.tryAcquireNanos(1, unit.toNanos(timeout));
}

�÷�������AQS��tryAcquireNanos()���������ж��Ƿ��жϣ�Ȼ���Ի�ȡͬ��״̬
����ɹ���ֱ�ӷ��ᣬ������뵽doAcquireNanos()������
public final boolean tryAcquireNanos(int arg, long nanosTimeout)
        throws InterruptedException {
    if (Thread.interrupted())
        throw new InterruptedException();
    return tryAcquire(arg) ||
        doAcquireNanos(arg, nanosTimeout);
}

doAcquireNanos()������
/**
 * Acquires in exclusive timed mode.
 *
 * @param arg the acquire argument
 * @param nanosTimeout max wait time
 * @return {@code true} if acquired
 */
private boolean doAcquireNanos(int arg, long nanosTimeout)
        throws InterruptedException {
    //������õ�ʱ�䲻�Ϸ���ֱ�ӷ���
    if (nanosTimeout <= 0L)
        return false;
    //�����ֹʱ��
    final long deadline = System.nanoTime() + nanosTimeout;
    //���Խ���ǰ�ڵ���뵽ͬ������
    final Node node = addWaiter(Node.EXCLUSIVE);
    boolean failed = true;
    try {
        for (;;) {
            final Node p = node.predecessor();
            //��ǰ�̻߳��������
            if (p == head && tryAcquire(arg)) {
                setHead(node);
                p.next = null; // help GC
                failed = false;
                return true;
            }
            //���¼��㳬ʱʱ��
            nanosTimeout = deadline - System.nanoTime();
            //�����ʱֱ�ӷ���
            if (nanosTimeout <= 0L)
                return false;
            //�߳������ȴ�
            if (shouldParkAfterFailedAcquire(p, node) &&
                nanosTimeout > spinForTimeoutThreshold)
                LockSupport.parkNanos(this, nanosTimeout);
            //���̱߳��ж����׳��ж��쳣
            if (Thread.interrupted())
                throw new InterruptedException();
        }
    } finally {
        if (failed)
            cancelAcquire(node);
    }
}


--------------------------------------------------------------------------------

--------------------------------------------------------------------------------

�������ReentrantLock
ReentrantLock��Lock��ʹ��Ƶ����ߵ��ࣩ ---   ��������
�ڽ�����ʽ֧�������ԣ�synchronizedͨ����ȡ�������ͷ��Լ��ķ�ʽʵ������
--------------------------------------------------------------------------------
�����Ե�ʵ��ԭ��
�����������ص㣺
�̻߳�ȡ����ʱ������ѽ���ȡ�������߳��ǵ�ǰ�̣߳���ֱ���ٴλ�ȡ
�������ᱻ��ȡN�Σ����ֻ�б��ͷ�N�κ�������Ľ����ͷ�
���濴һ�£��ǹ�ƽ�������ʵ�ֿ������ȡ�ģ�
final boolean nonfairTryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    int c = getState();
    if (c == 0) {
        if (compareAndSetState(0, acquires)) {
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    //����������߳�Ϊ��ǰ�̣߳��������� + 1
    else if (current == getExclusiveOwnerThread()) {
        int nextc = c + acquires;
        //���⣺�������ﵽ����������ʱ���ͻὫ��������Ϊ����
        //�������ﵽ���ޣ��׳���������������쳣
        if (nextc < 0) // overflow
            throw new Error("Maximum lock count exceeded");
        //�����޸�������ֵ
        setState(nextc);
        return true;
    }
    return false;
}

��������ͷţ�
protected final boolean tryRelease(int releases) {
    //����������һ
    int c = getState() - releases;
    //�жϳ������߳��Ƿ�Ϊ��ǰ�߳�
    if (Thread.currentThread() != getExclusiveOwnerThread())
        throw new IllegalMonitorStateException();
    boolean free = false;
    //���������Ϊ0�������ɹ��ͷţ������������߳���Ϊnull
    if (c == 0) {
        free = true;
        setExclusiveOwnerThread(null);
    }
    //����������
    setState(c);
    //����true��������ȫ�ͷţ�����false������û�б���ȫ�ͷ�
    return free;
}


--------------------------------------------------------------------------------

��ƽ����ǹ�ƽ�� ��ReentryLockĬ��Ϊ�ǹ�ƽ����Ч�ʿ죬�ڸ߲���������£��ǹ�ƽ����Ч�ʼ����ǹ�ƽ����100����
��ƽ����nonFairSync�������Ļ�ȡ˳��һ������ʱ���ϵľ���˳�򣬵ȴ�ʱ������߳�һ�����Ȼ�ȡ��������ѭ�����ȳ���FIFO����
�ǹ�ƽ����FairSync�����빫ƽ���ĸ����෴����һ���ȴ�ʱ��Խ������߳̾�һ���Ȼ������

����֮��Աȣ�ȱ�����ŵ�
��ƽ����֤ÿ�λ�ȡ����Ϊͬ�����еĵ�һ���ڵ㣬��֤��������Դʱ���ϵľ���˳�򣬵���Ч�ʽϵͣ���ҪƵ���Ľ����������л�
�ǹ�ƽ���ή�����ܿ���������һ�����������л������ǿ��ܵ����������߳���Զ�޷���ȡ����������̡߳�����������
��ƽ��Դ�����£�FairSync������ʵ��ȽϷǹ�ƽ���Ͷ���һ��!hasQueuedPredecessors() 
/**
 * Fair version of tryAcquire.  Don't grant access unless
 * recursive call or no waiters or is first.
 */
protected final boolean tryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    int c = getState();
    if (c == 0) {
        if (!hasQueuedPredecessors() &&
            compareAndSetState(0, acquires)) {
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    else if (current == getExclusiveOwnerThread()) {
        int nextc = c + acquires;
        if (nextc < 0)
            throw new Error("Maximum lock count exceeded");
        setState(nextc);
        return true;
    }
    return false;
}

��ʵ��ȽϷǹ�ƽ���Ͷ���һ��!hasQueuedPredecessors() 
�÷���ֻ�ܴӶ��еĵ�һ���ڵ�ȡ����
public final boolean hasQueuedPredecessors() {
    // The correctness of this depends on head being initialized
    // before tail and on head.next being accurate if the current
    // thread is first in queue.
    Node t = tail; // Read fields in reverse initialization order
    Node h = head;
    Node s;
    return h != t &&
        ((s = h.next) == null || s.thread != Thread.currentThread());
}


ͨ����˵��û���ض��Ĺ�ƽ��Ҫ����ѡ��ƽ��

--------------------------------------------------------------------------------

--------------------------------------------------------------------------------

�������ReentrantReadWriteLock ��д��
���л�����ж�д����Ҫ���������Ķ�д���������ڴ�
��д����Ӧ�ó����������ʵ��
������ʱ�϶���������һ���߳���д����ʱ�����еĶ�����ȫ��ֹͣ�ȴ�д��ִ�����

��д��ģ�ͣ�
��д������ͬһʱ�̱�������̷߳��ʣ�������д�̷߳���ʱ�����еĶ��߳��Լ�����д�߳̾��ᱻ������

�������������������������д�̵߳�ʱ������������ȥ����������̡߳�

д�����  -- ��ռʽ��
д���Ļ�ȡ   -- tryAcquire��int acquire��
protected final boolean tryAcquire(int acquires) {
    /*
     * Walkthrough:
     * 1. If read count nonzero or write count nonzero
     *    and owner is a different thread, fail.
     * 2. If count would saturate, fail. (This can only
     *    happen if count is already nonzero.)
     * 3. Otherwise, this thread is eligible for lock if
     *    it is either a reentrant acquire or
     *    queue policy allows it. If so, update state
     *    and set owner.
     */
    Thread current = Thread.currentThread();
    //��ȡд����ǰͬ��״̬
    int c = getState();
    //��ȡ��ռʽ��״̬ -- ��д����ȡ�Ĵ���
    int w = exclusiveCount(c);
    if (c != 0) {
        // (Note: if c != 0 and w == 0 then shared count != 0)
        //��ǰ�ж��߳��õ�������д�߳��޷���ȡͬ��״̬
        if (w == 0 || current != getExclusiveOwnerThread())
            return false;
        //д���Ŀ���������Ѵﵽ���ֵ
        if (w + exclusiveCount(acquires) > MAX_COUNT)
            throw new Error("Maximum lock count exceeded");
        // Reentrant acquire
        //��ǰ�߳������ȡд��
        setState(c + acquires);
        return true;
    }
    //д����δ���κ��̻߳�ȡ����ǰ�̻߳�ȡд��
    if (writerShouldBlock() ||
        !compareAndSetState(c, c + acquires))
        return false;
    //���������̸߳�Ϊ��ǰ�߳�
    setExclusiveOwnerThread(current);
    return true;
}

����exclusiveCount(acquires)����
/** Returns the number of exclusive holds represented in count  */
static int exclusiveCount(int c) { return c & EXCLUSIVE_MASK; }


static final int SHARED_SHIFT   = 16;
static final int EXCLUSIVE_MASK = (1 << SHARED_SHIFT) - 1;

����ͼ��ʾ��EXCLUSIVE_MASK�ǽ�1����16Ϊ��һ����Ϊ0x0000FFFF���� exclusiveCount()�������ǽ�
ͬ��״̬��0x0000FFFF���룬ȡ��ͬ��״̬�ĵ�16λ����ʾȡ����ռ����д����ȡ������������ǾͿ��Եó�
ͬ��״̬�ĵ�16λ��ʾд���Ļ�ȡ������ͬ�����Եó�ͬ��״̬�ĸ�16λ��ʾ�����Ļ�ȡ����


д�����ͷ� tryRelease()������
protected final boolean tryRelease(int releases) {
    //�������̲߳��ǵ�ǰ�̣߳��׳��쳣
    if (!isHeldExclusively())
        throw new IllegalMonitorStateException();
    //ͬ��״̬��ȥд״̬
    int nextc = getState() - releases;
    //���д״̬Ϊ0�����ʾд���ͷųɹ�
    boolean free = exclusiveCount(nextc) == 0;
    //���д���ͷųɹ������������߳���Ϊnull
    if (free)
        setExclusiveOwnerThread(null);
    //����ͬ��״̬
    setState(nextc);
    return free;
}


--------------------------------------------------------------------------------

�����Ļ�ȡ