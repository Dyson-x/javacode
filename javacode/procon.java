/**
* 多生产者多消费者模式
*/
package www.Dyson;
 
import java.util.ArrayList;
 
//定义一个商品类，用于解耦
class Goods{
    private String goodsName;  //定义商品名称
    private int goodsNums=0;   //定义商品库存
    //生产商品方法
    public synchronized void setGoodsName(String goodsName){
        this.goodsName=goodsName;
        //当库存大于0时证明有商品，这时需要等待消费者消费商品
        //修改二
        while(goodsNums>0){
            System.out.println("等到消费者消费！");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.goodsNums++;
        System.out.println("生产"+toString());
        //当生产者生产完商品后，需要唤醒处于等待队列的方法去继续消费商品
        //修改三
        notifyAll();
    }
    //消费商品方法
    public synchronized void getGoods(){
        //当库存小于1时，证明没有商品，这时需要等待生产者生产商品
        while (this.goodsNums<1){
            System.out.println("等待生产者生产！");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        --goodsNums;
        System.out.println("消费"+toString());
        //当消费者消费完商品后需要唤醒处于等待队列的方法去继续生产商品
        notifyAll();
    }
    //打印商品名称以及商品库存
    @Override
    public String toString() {
        return "Goods{" +
                "goodsName='" + goodsName + '\'' +
                ", goodsNums=" + goodsNums +
                '}';
    }
}
//生产者类
class produce implements Runnable{
    private Goods goods;
    public produce(Goods goods){
        this.goods=goods;
    }
    @Override
    public void run() {
        while(true){
            this.goods.setGoodsName("一致纪梵希口红！小羊皮");
        }
    }
}
//消费者类
class consum implements Runnable{
    private Goods goods;
    public consum(Goods goods){
        this.goods=goods;
    }
    @Override
    public void run() {
        //修改一
        while(true){
            this.goods.getGoods();
        }
    }
}
public class procon {
    public static void main(String[] args) {
        Goods goods=new Goods();
        ArrayList<Thread> a=new ArrayList<>();
        for(int i=0;i<10;i++){
            a.add(new Thread(new produce(goods)));
        }
        for(int i=0;i<5;i++){
            a.add(new Thread(new consum(goods)));
        }
        for(Thread tmp:a){
            tmp.start();
        }
    }
}
