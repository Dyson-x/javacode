/**
 *
 2.��дһ���������������̣߳������̵߳����Ʒֱ��� A��B��C��
 ÿ���߳̽��Լ�����������Ļ�ϴ�ӡ5�飬��ӡ˳����ABCABC...
 */
package www.Dyson.java;
class Print{
    private int flag=1;
    public int count=1;
    public synchronized void PrintA(){
        while(flag!=1){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.print(Thread.currentThread().getName());
        flag=2;
        ++count;
        notifyAll();
    }
    public synchronized void PrintB(){
        while(flag!=2){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.print(Thread.currentThread().getName());
        flag=3;
        notifyAll();
    }
    public synchronized void PrintC(){
        while(flag!=3){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.print(Thread.currentThread().getName());
        flag=1;

        notifyAll();
    }
}
class MyThread implements Runnable{
    private Print print;
    public MyThread(Print print){
        this.print=print;
    }
    @Override
    public void run() {
        //�����ѭ��������ʵ�Ǹ���PrintA�Ĵ���������ѭ��
        //ֻ�е�PrintAִ�������֮�󣬲�����ֹѭ��
        while(print.count<6){
            if(Thread.currentThread().getName().equals("A")){
                print.PrintA();
            }else if(Thread.currentThread().getName().equals("B")){
                print.PrintB();
            }else if(Thread.currentThread().getName().equals("C")){
                print.PrintC();
            }
        }
    }
}
public class Test{
    public static void main(String[] args) throws InterruptedException {
        Print print=new Print();
        MyThread myThread=new MyThread(print);
        Thread thread1=new Thread(myThread,"A");
        Thread thread2=new Thread(myThread,"B");
        Thread thread3=new Thread(myThread,"C");
        thread1.start();
        thread2.start();
        thread3.start();
    }
}
