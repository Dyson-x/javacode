/**
* �������߶�������ģʽ
*/
package www.Dyson;
 
import java.util.ArrayList;
 
//����һ����Ʒ�࣬���ڽ���
class Goods{
    private String goodsName;  //������Ʒ����
    private int goodsNums=0;   //������Ʒ���
    //������Ʒ����
    public synchronized void setGoodsName(String goodsName){
        this.goodsName=goodsName;
        //��������0ʱ֤������Ʒ����ʱ��Ҫ�ȴ�������������Ʒ
        //�޸Ķ�
        while(goodsNums>0){
            System.out.println("�ȵ����������ѣ�");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.goodsNums++;
        System.out.println("����"+toString());
        //����������������Ʒ����Ҫ���Ѵ��ڵȴ����еķ���ȥ����������Ʒ
        //�޸���
        notifyAll();
    }
    //������Ʒ����
    public synchronized void getGoods(){
        //�����С��1ʱ��֤��û����Ʒ����ʱ��Ҫ�ȴ�������������Ʒ
        while (this.goodsNums<1){
            System.out.println("�ȴ�������������");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        --goodsNums;
        System.out.println("����"+toString());
        //����������������Ʒ����Ҫ���Ѵ��ڵȴ����еķ���ȥ����������Ʒ
        notifyAll();
    }
    //��ӡ��Ʒ�����Լ���Ʒ���
    @Override
    public String toString() {
        return "Goods{" +
                "goodsName='" + goodsName + '\'' +
                ", goodsNums=" + goodsNums +
                '}';
    }
}
//��������
class produce implements Runnable{
    private Goods goods;
    public produce(Goods goods){
        this.goods=goods;
    }
    @Override
    public void run() {
        while(true){
            this.goods.setGoodsName("һ�¼���ϣ�ں죡С��Ƥ");
        }
    }
}
//��������
class consum implements Runnable{
    private Goods goods;
    public consum(Goods goods){
        this.goods=goods;
    }
    @Override
    public void run() {
        //�޸�һ
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
