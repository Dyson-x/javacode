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
    //����һ��Lock��
    private Lock lock=new ReentrantLock();
    //����lock����������Condition����,һ����������ߣ�һ�����������
    private Condition produceCondition=lock.newCondition();
    private Condition consumeCondition =lock.newCondition();
    public void SetGood(String name){
        lock.lock();  //����
        try{
            while(num==maxnum){
                System.out.println(Thread.currentThread().getName()+"��Ʒ���ﵽ���ޣ��ȵ����������ѣ�");
                produceCondition.await();  //�����Լ��ȴ�����
            }
            Thread.sleep(200);  //С˯һ��
            this.name=name;
            num++;
            System.out.println(Thread.currentThread().getName()+"����"+toString());
            consumeCondition.signalAll();    //����������ȥ����
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
                System.out.println(Thread.currentThread().getName()+"û����Ʒ�������ѣ��ȴ�������������");
                consumeCondition.await();  //�����Լ��ĵȴ�����
            }
            Thread.sleep(200);   //С˯һ��
            num--;
            System.out.println(Thread.currentThread().getName()+"����"+toString());
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
            goods.SetGood("С��Ƥ");
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
            Thread thread=new Thread(produce,"������"+i);
            list.add(thread);
        }
        for(int i=0;i<5;i++){
            Thread thread=new Thread(consume,"������"+i);
            list.add(thread);
        }
        for(Thread th:list){
            th.start();
        }
    }
}
