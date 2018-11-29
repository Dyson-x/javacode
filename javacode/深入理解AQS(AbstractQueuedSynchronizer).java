                                                                AQS详解
独占式锁
同步队列
独占式锁获取 -- 源码分析：
当获取锁失败时调用如下方法：
独占式锁的获取流程图如下：
独占式锁释放 --  源码分析:
总结：独占锁获取与释放总结:
可中断式获取锁
超时等待获取锁（tryAcquireNanos()方法）
深入理解ReentrantLock
ReentrantLock（Lock中使用频率最高的类） ---   可重入锁
重入性的实现原理：
公平锁与非公平锁 （ReentryLock默认为非公平锁，效率快，在高并发的情况下，非公平锁的效率几乎是公平锁的100倍）
深入理解ReentrantReadWriteLock 读写锁

--------------------------------------------------------------------------------
在同步组件中，AQS是最核心部分，同步组件的实现依赖AQS提供的模板方法来实现同
步组件语义。

AQS实现了对同步状态的管理，以及对阻塞线程进行排队、等待通知等等底层实现。

AQS核心组成:同步队列、独占锁的获取与释放、共享锁的获取与释放、可中断锁、超时锁。
这一系列功能的实现依赖于AQS提供的模板方法。
独占式锁
void acquire(int arg) : 独占式获取同步状态，如果获取失败插入同步队列进行等待。
void acquireInteruptibly(int arg)：在1的基础上，此方法可以在同步队列中响应中断
boolean tryAcquireNanos(int arg,long nanosTimeOut)：在2的基础增加了超时等待功能，
//到了预计时间还未获得锁直接返回。
boolean tryAcquire(int arg)：获取锁成功返回true，否则返回false
boolean release(int arg) : 释放同步状态，该方法会唤醒在同步队列的下一个节点。

共享式锁
void acquireShared(int arg) : 共享获取同步状态,同一时刻多个线程获取同步状态
void acquireSharedInterruptibly(int arg) : 在1的基础上增加响应中断
boolean tryAcquireSharedNanos(int arg,long nanosTimeOut):在2的基础上增加
//超时等待
boolean releaseShared(int arg) : 共享式释放同步状态

同步队列
在AQS内部有一个静态内部类Node，这是同步队列中每个具体的节点。
节点有如下属性:
int waitStatus:节点状态
Node prev:同步队列中前驱节点
Node next:同步队列中后继节点
Thread thread:当前节点包装的线程对象
Node nextWaiter:等待队列中下一个节点

节点状态值如下:

/** waitStatus value to indicate thread has cancelled */
//当前节点由于超时或者中断在同步队列中取消
static final int CANCELLED =  1; 
/** waitStatus value to indicate successor's thread needs unparking */
//当前的节点的前驱节点被阻塞，当前节点在执行release或者cancel时需要执行unpark
//来唤醒后继节点
static final int SIGNAL    = -1;
/** waitStatus value to indicate thread is waiting on condition */
//节点处于等待队列中。当其他线程对Condition调用signal()方法后，该节点会
//从等待队列移到同步队列中
static final int CONDITION = -2;
/**
 * waitStatus value to indicate the next acquireShared should
 * unconditionally propagate
 */
 //共享式同步状态会无条件传播
static final int PROPAGATE = -3;


AQS同步队列采用带有头尾节点的双向链表
--------------------------------------------------------------------------------
独占式锁获取 -- 源码分析：
独占式锁的获取，如下图所示为ReentrantLock源码：
/**
 * Performs lock.  Try immediate barge, backing up to normal
 * acquire on failure.
 */
final void lock() {
    //使用CAS操作尝试将同步状态从0改为1，如果成功则将同步状态持有
    //线程置为当前线程，否则调用acquire()方法
    if (compareAndSetState(0, 1))  
        setExclusiveOwnerThread(Thread.currentThread());
    else
        acquire(1);
}


--------------------------------------------------------------------------------
acquire()方法：
//再次尝试获取同步状态，如果成功将当前线程置为持有锁线程，方法退出
//失败则继续向下执行
public final void acquire(int arg) {
    if (!tryAcquire(arg) &&
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
        selfInterrupt();
}

tryAcquire()方法：
protected boolean tryAcquire(int arg) {
    throw new UnsupportedOperationException();
}

尝试获取锁资源，成功返回true。具体资源获取/释放范式交由自定义同步器实现。ReentrantLock中默认为非公平锁，公平锁后面在讨论，关于非公平锁的具体实现方式如下：
nonfairTryAcquire()方法：尝试去获取锁，若当前锁资源没有被初始化，则直接将当前线程置为持有锁线程，若持有锁线程就是当前线程，重入，修改同步状态，若为以上两种情况返回true，否则返回false
/**
 * Performs non-fair tryLock.  tryAcquire is implemented in
 * subclasses, but both need nonfair try for trylock method.
 * 执行非公平tryLock，tryAcquire是在子类中实现的，但是都
 * 需要tryLock方法的非公平尝试
 */
 
final boolean nonfairTryAcquire(int acquires) {
     final Thread current = Thread.currentThread();  //获取当前线程
    int c = getState();  //取得当前同步状态
    //若当前同步状态为0，表明还没有被初始化，则进行CAS操作修改同步状态
    //并当前线程置为持有锁线程，返回true
    if (c == 0) {
        if (compareAndSetState(0, acquires)) {
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    //如果同步状态已经被初始化，则需要判断持有锁线程是否为当前线程
    //锁的可重入性，如果是当前线程，则将当前持有锁线程的状态值加一
    else if (current == getExclusiveOwnerThread()) {
        int nextc = c + acquires;
        //异常处理
        if (nextc < 0) // overflow
            throw new Error("Maximum lock count exceeded");
        //重新设置同步状态
        setState(nextc);
        return true;
    }
    return false;
}


--------------------------------------------------------------------------------
addWaiter()方法：将当前线程封装成节点尾插入同步队列当中
/**
 * Creates and enqueues node for current thread and given mode.
 * 按照给定模式（独占式）为用节点封装当前线程并置入同步队列
 * @param mode Node.EXCLUSIVE for exclusive, Node.SHARED for shared
 * @return the new node
 */
private Node addWaiter(Node mode) {
    //将当前线程封装为节点
    Node node = new Node(Thread.currentThread(), mode);
    // Try the fast path of enq; backup to full enq on failure
    Node pred = tail;    //获取当前队列的尾节点
    //如果尾节点不为空则，使用CAS操作尝试将当前节点尾插入同步队列
    //如果成功返回当前节点
    if (pred != null) {
        node.prev = pred;
        if (compareAndSetTail(pred, node)) {
            pred.next = node;
            return node;
        }
    }
    //当前尾节点为空，或者CAS尾插失败就会执行该方法
    enq(node);
    return node;
}

enq()方法：
1.  在当前线程是第一个加入同步队列时，调用compareAndSetHead(new Node())方法，完成链式队列的头结点 的初始化； 
2. 如果CAS尾插入节点失败后负责自旋进行尝试；
/**
 * Inserts node into queue, initializing if necessary. See picture above.
 * 将节点插入到同步队列中，必要时初始化
 * @param node the node to insert
 * @return node's predecessor
 */
private Node enq(final Node node) {
    for (;;) {
        //当前节点为空
        Node t = tail;
        if (t == null) { // Must initialize
            //头节点初始化
            if (compareAndSetHead(new Node()))
                tail = head;
        } else {
            node.prev = t;
            //CAS尾插，失败则不断进行自旋重试直到成功为止
            if (compareAndSetTail(t, node)) {
                t.next = node;
                return t;
            }
        }
    }
}


--------------------------------------------------------------------------------
acquireQueued()方法： （排队获取锁）----  重要  ---- 
如果当前节点的前驱节点是头结点，并且能够获得同步状态的话，当前线程能够获得锁，该方法执行结束退出。
获取锁失败的话，先将节点状态设置为SIGNAL，然后调用LookSupport.park()方法使得当前线程阻塞。
/**
 * Acquires in exclusive uninterruptible mode for thread already in
 * queue. Used by condition wait methods as well as acquire.
 *
 * @param node the node
 * @param arg the acquire argument
 * @return {@code true} if interrupted while waiting
 */
// 自旋等待获取资源
final boolean acquireQueued(final Node node, int arg) {
    boolean failed = true;
    try {
        boolean interrupted = false;
        for (;;) {
            //当前节点的前驱节点
            final Node p = node.predecessor();
            //如果前驱节点为头节点，则尝试获取同步状态
            if (p == head && tryAcquire(arg)) {
                //将队头指针指向当前节点
                setHead(node);
                //释放前驱节点
                p.next = null; // help GC
                failed = false;
                return interrupted;
            }
            //获取同步状态失败，线程进入等待状态等待获取独占式
            //这里的interrupt只是一个单纯的标记而已
            if (shouldParkAfterFailedAcquire(p, node) &&
                parkAndCheckInterrupt())
                interrupted = true;
        }
    } finally {
        //如果获取资源失败，将当前节点置为取消状态
        if (failed)
            cancelAcquire(node);
    }
}


思考一，关于中断标记位问题：
关于acquireQueued详解：这里当线程进入到自旋尝试获取锁资源时，会发现当获取同步失败时，线程进入到shouldParkAfterFaildAcquire()方法将当前驱节点状态置为SIGNAL表示当前节点被阻塞，然后调用parlAndCheckInterrupt()方法将当前节点阻塞，如下为parlAndCheckInterrupt()方法：
private final boolean parkAndCheckInterrupt() {
    LockSupport.park(this);
    return Thread.interrupted();
}

当阻塞在该方法里边有会调用 interrupted()方法：该方法的含义就为当线程第一次被中断的时候会返回true，并且将中断状态清除，当线程再连续调用该方时候返回false。

就可以理解为该方法只是单纯的该线程在自旋获取同步状态时，判断有没有被中断过，只要有一次被中断了，并不需要真正的将该线程中断，只需要让标志位interrupted知道该线程被中断过，当线程再次被中断没必要再给interrupted重新赋值，直接返回false即可。（个人理解）
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

这时假如说线程在获取锁的过程中被中断了，但是并没有处理它，而是会返回interrupted，而真正将其中断处理的是调用selfInterrupt()
public final void acquire(int arg) {
    if (!tryAcquire(arg) &&
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
        selfInterrupt();
}

selfInterrupt()方法：在该方法中真正意义上将线程中断
/**
 * Convenience method to interrupt current thread.
 */
static void selfInterrupt() {
    Thread.currentThread().interrupt();
}


--------------------------------------------------------------------------------
思考二：关于节点处理和如何退出自旋
当进入到acquireQueued方法中不断自旋获取同步状态，独占式锁的获取和可中断锁的获取存在着区别，
当可中断获取锁时，只要线程被中断就会直接报异常然后退出，但是独占式锁不一样，它不会因为
中断而退出，而是会将中断标记改变而已，并且一直自旋直到获取锁，那么它在什么情况下会推出呢？
下面进入到tryAcquire()方法，tryAcquire()默认调用的是nofiretryAcquire()方法覆写方法：
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

根据上面的源码，当线程在自旋一直尝试获取锁资源时，如果要自旋停止，那唯一的方法就是抛异常，那会抛什么异常呢，这里有很多种
就比如上面源码所示的锁计数超过最大限制异常，它的意思就是说，锁的重入达到最大上限，又比如当你正在自旋尝试获取同步状态时，
这时调用await()方法，将线程置入等待队列这时也可能会抛出异常，还有就比如调用AQS的await()方法就会在里边调用一个 fullyRelease()方法
该方法的大概意思就是如果该节点状态如果是取消状态的话就会抛IllegalMonitorStateException()异常，如果是其余状态不做过多处理。
当然还有很多种异常，这里就不在深究，总之要想跳出acquireQueued的自旋要么获取锁成功，要么抛异常，进入tryfinally块。

当进入到tryfinally块之后就会处理异常
下面看一下cancelAcquire()源码：其实里边的核心代码就是node.waitStatus = Node.CANCELLED; 将节点状态改为取消状态
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
如果前驱节点是头结点的并且成功获得同步状态的时候（if (p == head && tryAcquire(arg))），当前节点所指向的线程能够获取锁。示意图如下：

锁获取成功，进行出队操作：
//将队头指针指向当前节点
setHead(node);
//释放前驱节点
p.next = null; // help GC
failed = false;
return interrupted;

setHead() 方法：重新设置头结点，然后将当前节点prev指针置为null，将节点的thread置为null
private void setHead(Node node) {
    head = node;
    node.thread = null;
    node.prev = null;
}

通过上面流程，将原来的头结点next域置为null，并且它的prev域原本就是null，而新的队头指针
已经指向了当前节点，这时无任何引用的原有头结点被GC进行回收。示意图如下：


--------------------------------------------------------------------------------
当获取锁失败时调用如下方法：
shouldParkAfterFailedAcquire()方法：尝试将当前节点的前驱节点状态设置为阻塞状态
private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
    int ws = pred.waitStatus;   //获取前驱节点状态
    //如果当前节点被阻塞，则直接点返回
    if (ws == Node.SIGNAL)
        /*
         * This node has already set status asking a release
         * to signal it, so it can safely park.
         */
        return true;
    //若当前节点被取消，则不断重试直到找到下一个不为取消状态的节点  
    if (ws > 0) {
        /*
         * Predecessor was cancelled. Skip over predecessors and
         * indicate retry.
         */
        do {
            node.prev = pred = pred.prev;
        } while (pred.waitStatus > 0);
        pred.next = node;
    //尝试将前驱节点设置为阻塞状态，如果修改成功那么下次进入该方法就直接返回true，不需要过多的判断处理
    } else {
        /*
         * waitStatus must be 0 or PROPAGATE.  Indicate that we
         * need a signal, but don't park yet.  Caller will need to
         * retry to make sure it cannot acquire before parking.
         */
        compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
    }
    return false;

当compareAndSetWaitStatus设置失败则说明 shouldParkAfterFailedAcquire方法返回false，然后会在acquireQueued()方法中for (;;)死循环中会继续重试，直至 compareAndSetWaitStatus设置节点状态位为SIGNAL时shouldParkAfterFailedAcquire返回true时才会执行方法 parkAndCheckInterrupt()方法；

parkAndCheckInterrupt()方法：调用LockSupport.park()方法（后面讨论），阻塞当前线程
private final boolean parkAndCheckInterrupt() {
    LockSupport.park(this);
    return Thread.interrupted();
}


--------------------------------------------------------------------------------
独占式锁的获取流程图如下：


--------------------------------------------------------------------------------

--------------------------------------------------------------------------------

独占式锁释放 --  源码分析:


unlock()方法：调用AQS中的release方法
public void unlock() {
    sync.release(1);
}

release()方法：当同步状态释放成功（返回true），执行if语句，当head指向的头结点不为null，并且该节点状态不为0时，执行unparkSuccessor()方法；
public final boolean release(int arg) {
    if (tryRelease(arg)) {  //尝试去释放资源
        Node h = head;      //头结点
        if (h != null && h.waitStatus != 0)
            unparkSuccessor(h);  //唤醒head的下一个不为null的节点
        return true;
    }
    return false;
}

tryRelease()方法：与tryAcquire一样，该方法需要通过同步器自定义实现。一般来说，释放资源直接用state减去给定参数releases，释放后state==0，说明释放成功，
protected final boolean tryRelease(int releases) {
    int c = getState() - releases;
    //当要释放的资源不是当前线程，就会抛出一个锁资源持有不合法异常
    if (Thread.currentThread() != getExclusiveOwnerThread())
        throw new IllegalMonitorStateException();
    boolean free = false;
    //如果c==0说明资源释放成功，将返回值改为true
    if (c == 0) {
        free = true;
        //将独占锁的持有线程置为null
        setExclusiveOwnerThread(null);
    }
    //重新设置节点状态
    setState(c);
    return free;
}

unparkSuccessor()方法：唤醒head的下一个不为null的节点
private void unparkSuccessor(Node node) {
    /*
     * If status is negative (i.e., possibly needing signal) try
     * to clear in anticipation of signalling.  It is OK if this
     * fails or if status is changed by waiting thread.
     */
    //如果状态为负(即(可能需要信号)试着清除预期信号。如果失败，或者等
    //待线程更改了状态，也没有关系。
    int ws = node.waitStatus;
    if (ws < 0)

        compareAndSetWaitStatus(node, ws, 0);

    /*
     * Thread to unpark is held in successor, which is normally
     * just the next node.  But if cancelled or apparently null,
     * traverse backwards from tail to find the actual
     * non-cancelled successor.
     */
     //使用unpark唤醒head的和后继节点，但若head的后继节点被取消或者为null，
     //则从tail从后往前寻找head下一个不为空且没被取消的节点
     //头结点的后继节点
    Node s = node.next;
    if (s == null || s.waitStatus > 0) {
        s = null;
        for (Node t = tail; t != null && t != node; t = t.prev)
            if (t.waitStatus <= 0)
                s = t;
    }
    //唤醒找到的节点
    if (s != null)
        LockSupport.unpark(s.thread);
}


release()方法是unlock()方法的具体实现。首先获取头结点的后继节点，当后继节点不为null，会调用LockSupport.unpark()方法唤醒后继节点包装的线程。因此，每一次锁释放后就会唤醒队列中该节点的后继节点所包装的线程。

--------------------------------------------------------------------------------

--------------------------------------------------------------------------------

总结：独占锁获取与释放总结:
1.线程获取锁失败，将线程调用addWaiter()封装成Node进行入队操作。
addWaiter()中方法enq()完成对同步队列的
头结点初始化以及CAS尾插失败后的重试处理。

2.入队之后排队获取锁的核心方法acquireQueued(),节点排队获取锁是一个自旋过程。
当且仅当当前节点的前驱节点为头结点并且成功获取同步状态时，
节点出队并且该节点引用的线程获取到锁。
否则，不满足条件时会不断自旋将前驱节点的状态
置为SIGNAL而后调用LockSupport.park()将当前线程阻塞。

3.释放锁时会唤醒后继节点(后继节点不为null)

--------------------------------------------------------------------------------
可中断式获取锁
可响应中断式锁可调用方法lock.lockInterruptibly()；该方法的底层会调用AQS的acqireInterruptibly方法；
acquireInterruptibly()方法：
public final void acquireInterruptibly(int arg)
        throws InterruptedException {
    //这里会先检查是否有被中断，如果有则抛出一个中断异常
    //否则尝试去获取同步状态，成功直接退出，失败则进入doAcquireInterruptibly(arg)方法
    if (Thread.interrupted())
        throw new InterruptedException();
    if (!tryAcquire(arg))
        doAcquireInterruptibly(arg);
}


doAcquireInterruptibly()方法：如果当前线程的前驱节点为队头时，尝试获取同步状态
若获取成功则将队头节点出队，当前线程置为持有锁线程，并将队头指针指向当前线程所封装的节点，否则不断自旋直到获取成功或者线程被中断
/**
 * Acquires in exclusive interruptible mode.
 * @param arg the acquire argument
 */
private void doAcquireInterruptibly(int arg)
    throws InterruptedException {
        //尝试将节点插入到同步队列
    final Node node = addWaiter(Node.EXCLUSIVE);
    boolean failed = true;
    try {
        for (;;) {
            //取当前节点的前驱节点
            final Node p = node.predecessor();
            //如果前驱节点为头结点，且成功获取同步状态则将持有锁线程
            //置为当前线程，并将队头节点出队
            if (p == head && tryAcquire(arg)) {
                setHead(node);
                p.next = null; // help GC
                failed = false;
                return;
            }
            //获取同步状态失败，线程进入等待状态等待获取独占式
            if (shouldParkAfterFailedAcquire(p, node) &&
                parkAndCheckInterrupt())
                //线程被阻塞时若检测到中断抛出中断异常后退出。
                throw new InterruptedException();
        }
    } finally {
        //获取同步状态失败将当前节点取消
        if (failed)
            cancelAcquire(node);
    }
}



--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
超时等待获取锁（tryAcquireNanos()方法）
调用lock.tryLock(timeout，TimeUnit)方式
boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
超时等待锁的返回条件：
在指定时间内获取到锁
在指定时间内没有获取到锁
在指定时间内被中断
public boolean tryLock(long timeout, TimeUnit unit)
        throws InterruptedException {
    return sync.tryAcquireNanos(1, unit.toNanos(timeout));
}

该方法调用AQS的tryAcquireNanos()方法：先判断是否被中断，然后尝试获取同步状态
如果成功则直接返会，否则进入到doAcquireNanos()方法：
public final boolean tryAcquireNanos(int arg, long nanosTimeout)
        throws InterruptedException {
    if (Thread.interrupted())
        throw new InterruptedException();
    return tryAcquire(arg) ||
        doAcquireNanos(arg, nanosTimeout);
}

doAcquireNanos()方法：
/**
 * Acquires in exclusive timed mode.
 *
 * @param arg the acquire argument
 * @param nanosTimeout max wait time
 * @return {@code true} if acquired
 */
private boolean doAcquireNanos(int arg, long nanosTimeout)
        throws InterruptedException {
    //如果设置的时间不合法，直接返回
    if (nanosTimeout <= 0L)
        return false;
    //计算截止时间
    final long deadline = System.nanoTime() + nanosTimeout;
    //尝试将当前节点插入到同步队列
    final Node node = addWaiter(Node.EXCLUSIVE);
    boolean failed = true;
    try {
        for (;;) {
            final Node p = node.predecessor();
            //当前线程获得锁出队
            if (p == head && tryAcquire(arg)) {
                setHead(node);
                p.next = null; // help GC
                failed = false;
                return true;
            }
            //重新计算超时时间
            nanosTimeout = deadline - System.nanoTime();
            //如果超时直接返回
            if (nanosTimeout <= 0L)
                return false;
            //线程阻塞等待
            if (shouldParkAfterFailedAcquire(p, node) &&
                nanosTimeout > spinForTimeoutThreshold)
                LockSupport.parkNanos(this, nanosTimeout);
            //若线程被中断则抛出中断异常
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

深入理解ReentrantLock
ReentrantLock（Lock中使用频率最高的类） ---   可重入锁
内建锁隐式支持重入性，synchronized通过获取自增，释放自减的方式实现重入
--------------------------------------------------------------------------------
重入性的实现原理：
重入性锁的特点：
线程获取锁的时候，如果已将获取到锁的线程是当前线程，则直接再次获取
由于锁会被获取N次，因此只有被释放N次后才真正的将锁释放
下面看一下，非公平锁是如何实现可重入获取的：
final boolean nonfairTryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    int c = getState();
    if (c == 0) {
        if (compareAndSetState(0, acquires)) {
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    //如果持有锁线程为当前线程，则将锁计数 + 1
    else if (current == getExclusiveOwnerThread()) {
        int nextc = c + acquires;
        //问题：当计数达到锁计数上限时，就会将锁计数置为负数
        //锁计数达到上限，抛出超过最大锁计数异常
        if (nextc < 0) // overflow
            throw new Error("Maximum lock count exceeded");
        //重新修改锁计数值
        setState(nextc);
        return true;
    }
    return false;
}

可重入的释放：
protected final boolean tryRelease(int releases) {
    //将锁计数减一
    int c = getState() - releases;
    //判断持有锁线程是否为当前线程
    if (Thread.currentThread() != getExclusiveOwnerThread())
        throw new IllegalMonitorStateException();
    boolean free = false;
    //如果锁计数为0表明锁成功释放，并将锁持有线程置为null
    if (c == 0) {
        free = true;
        setExclusiveOwnerThread(null);
    }
    //更新锁计数
    setState(c);
    //返回true表明锁完全释放，返回false表明锁没有被完全释放
    return free;
}


--------------------------------------------------------------------------------

公平锁与非公平锁 （ReentryLock默认为非公平锁，效率快，在高并发的情况下，非公平锁的效率几乎是公平锁的100倍）
公平锁（nonFairSync）：锁的获取顺序一定满足时间上的绝对顺序，等待时间最长的线程一定最先获取到锁，遵循先入先出（FIFO）。
非公平锁（FairSync）：与公平锁的概念相反，不一定等待时间越长大的线程就一定先获得锁。

两者之间对比：缺点与优点
公平锁保证每次获取锁均为同步队列的第一个节点，保证了请求资源时间上的绝对顺序，但是效率较低，需要频繁的进行上下文切换
非公平锁会降低性能开销，降低一定的上下文切换，但是可能到导致其他线程永远无法获取到锁，造成线程“饥饿”现象。
公平锁源码如下（FairSync）：其实相比较非公平锁就多了一个!hasQueuedPredecessors() 
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

其实相比较非公平锁就多了一个!hasQueuedPredecessors() 
该方法只能从队列的第一个节点取到锁
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


通常来说，没有特定的公平性要求尽量选择公平锁

--------------------------------------------------------------------------------

--------------------------------------------------------------------------------

深入理解ReentrantReadWriteLock 读写锁
所有缓存必有读写锁（要经历大量的读写），比如内存
读写锁的应用场景：缓存的实现
读数据时上读锁，当有一个线程来写数据时，所有的读数据全部停止等待写锁执行完毕

读写锁模型：
读写锁运行同一时刻被多个读线程访问，但是在写线程访问时，所有的读线程以及其他写线程均会被阻塞。

读锁不等于无锁，区别就在于写线程的时候，无锁并不会去阻塞其余读线程。

写锁详解  -- 独占式锁
写锁的获取   -- tryAcquire（int acquire）
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
    //获取写锁当前同步状态
    int c = getState();
    //获取独占式锁状态 -- 即写锁获取的次数
    int w = exclusiveCount(c);
    if (c != 0) {
        // (Note: if c != 0 and w == 0 then shared count != 0)
        //当前有读线程拿到读锁，写线程无法获取同步状态
        if (w == 0 || current != getExclusiveOwnerThread())
            return false;
        //写锁的可重入次数已达到最大值
        if (w + exclusiveCount(acquires) > MAX_COUNT)
            throw new Error("Maximum lock count exceeded");
        // Reentrant acquire
        //当前线程重入获取写锁
        setState(c + acquires);
        return true;
    }
    //写锁还未被任何线程获取，当前线程获取写锁
    if (writerShouldBlock() ||
        !compareAndSetState(c, c + acquires))
        return false;
    //将持有锁线程改为当前线程
    setExclusiveOwnerThread(current);
    return true;
}

关于exclusiveCount(acquires)方法
/** Returns the number of exclusive holds represented in count  */
static int exclusiveCount(int c) { return c & EXCLUSIVE_MASK; }


static final int SHARED_SHIFT   = 16;
static final int EXCLUSIVE_MASK = (1 << SHARED_SHIFT) - 1;

如上图所示，EXCLUSIVE_MASK是将1左移16为减一，即为0x0000FFFF。而 exclusiveCount()方法又是将
同步状态和0x0000FFFF相与，取到同步状态的低16位，表示取到独占锁即写锁获取次数，因此我们就可以得出
同步状态的低16位表示写锁的获取次数，同样可以得出同步状态的高16位表示读锁的获取次数


写锁的释放 tryRelease()方法：
protected final boolean tryRelease(int releases) {
    //持有锁线程不是当前线程，抛出异常
    if (!isHeldExclusively())
        throw new IllegalMonitorStateException();
    //同步状态减去写状态
    int nextc = getState() - releases;
    //如果写状态为0，则表示写锁释放成功
    boolean free = exclusiveCount(nextc) == 0;
    //如果写锁释放成功，则将锁持有线程置为null
    if (free)
        setExclusiveOwnerThread(null);
    //更新同步状态
    setState(nextc);
    return free;
}


--------------------------------------------------------------------------------

读锁的获取