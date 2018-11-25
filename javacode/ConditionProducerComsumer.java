package www.Dyson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Good{
    private String name;
    private int num;
    private int maxnum;
    public Good(int maxnum) {
        this.maxnum = maxnum;
    }
    //创建一个Lock锁
    private Lock lock=new ReentrantLock();
    //依据lock锁创建两个Condition队列,一个存放生产者，一个存放消费者
    private Condition produceCondition=lock.newCondition();
    private Condition consumeCondition =lock.newCondition();
    public void SetGood(String name){
        lock.lock();  //上锁
        try{
            while(num==maxnum){
                System.out.println(Thread.currentThread().getName()+"商品数达到上限，等到消费者消费！");
                produceCondition.await();  //进入自己等待对列
            }
            Thread.sleep(200);  //小睡一会
            this.name=name;
            num++;
            System.out.println(Thread.currentThread().getName()+"生产"+toString());
            consumeCondition.signalAll();    //唤醒消费者去消费
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public void GetGood(){
        lock.lock();
        try{
            while(num==0){
                System.out.println(Thread.currentThread().getName()+"没有商品可以消费，等待生产者生产！");
                consumeCondition.await();  //进入自己的等待队列
            }
            Thread.sleep(200);   //小睡一会
            num--;
            System.out.println(Thread.currentThread().getName()+"消费"+toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    @Override
    public String toString() {
        return "Good{" +
                "name='" + name + '\'' +
                ", num=" + num +
                '}';
    }
}
class Producer implements Runnable{
    private Good goods;
    public Producer(Good goods) {
        this.goods = goods;
    }
    @Override
    public void run() {
        while(true){
            goods.SetGood("小羊皮");
        }
    }
}
class Consumer implements Runnable{
    private Good goods;
    public Consumer(Good goods) {
        this.goods = goods;
    }

    @Override
    public void run() {
        while(true){
            goods.GetGood();
        }
    }
}
public class produceConsum {
    public static void main(String[] args) {
        Good good=new Good(20);
        List<Thread> list=new ArrayList<>();
        Producer produce=new Producer(good);
        Consumer consume=new Consumer(good);
        for(int i=0;i<10;i++){
            Thread thread=new Thread(produce,"生产者"+i);
            list.add(thread);
        }
        for(int i=0;i<5;i++){
            Thread thread=new Thread(consume,"消费者"+i);
            list.add(thread);
        }
        for(Thread th:list){
            th.start();
        }
    }
}
