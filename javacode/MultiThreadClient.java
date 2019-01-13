package multiple;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.security.Principal;
import java.util.Scanner;

/**
 * 读取服务器发信息的线程
 */
class ReadFromServer implements Runnable{
    private Socket client;
    //构造方法
    public ReadFromServer(Socket client) {
        this.client = client;
    }
    @Override
    public void run() {
        //获取输入流取得服务器发来的信息
        try {
            Scanner in=new Scanner(client.getInputStream());
            while(true){
                //如果客户端关闭则直接break退出，否则不断的读取信息
                if(client.isClosed()){
                    System.out.println("客户端已经关闭！");
                    //关闭流
                    in.close();
                    break;
                }
                //判断是否还有信息需要读取
                if(in.hasNext()){
                    System.out.println("从服务器所发的信息为："+in.nextLine());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
/**
 * 向服务器发送线程信息
 */
class SendMagToServer implements Runnable{
    private Socket client;
    public SendMagToServer(Socket client) {
        this.client = client;
    }
    @Override
    public void run() {
        //获取键盘输入，向服务器发送信息
        Scanner in=new Scanner(System.in);
        in.useDelimiter("\n");
        //获取客户端输出流
        try {
            PrintStream out=new PrintStream(client.getOutputStream(),true,"UTF-8");
            while(true){
                System.out.println("请输入客户端所要发送的信息：");
                String msgToServer="";
                if(in.hasNext()){
                    //信息输入
                    msgToServer=in.nextLine();
                    //向服务器发送信息
                    out.println(msgToServer);
                    if(msgToServer.contains("byebye")){
                        System.out.println("客户端退出！");
                        //关闭客户端
                        in.close();
                        out.close();
                        client.close();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
public class MultiClient {
    public static void main(String[] args) throws IOException {
        //创建客户端连接服务器
        Socket client=new Socket("127.0.0.1",6666);
        Thread readThread=new Thread(new ReadFromServer(client));
        Thread sendThread=new Thread(new SendMagToServer(client));
        readThread.start();
        sendThread.start();
    }
}
