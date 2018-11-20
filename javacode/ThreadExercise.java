/**
 * 1.写两个线程，一个线程打印 1~52，另一个线程打印A~Z，
 * 打印顺序是12A34B...5152Z；
 */
package www.Dyson.java;
class Print{
    private boolean flag=true;
    private int num=1;
    public synchronized void PrintNumber(){
        if(flag!=true){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.print(2*num-1);
        System.out.print(2*num);
        flag=false;
        notify();
    }
    public synchronized void PrintChara(){
        if(flag!=false){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.print((char)(num-1+'A'));
        ++num;
        flag=true;
        notify();
    }
}
public class Test{
    public static void main(String[] args) throws InterruptedException {
        Print print=new Print();
        //使用匿名内部类,直接覆写run方法，并启动线程
        new Thread(()->{
            for(int i=0;i<26;i++){
                print.PrintNumber();
            }
        }).start();
        new Thread(()->{
            for(int i=0;i<26;i++){
                print.PrintChara();
            }
        }).start();
    }
}
