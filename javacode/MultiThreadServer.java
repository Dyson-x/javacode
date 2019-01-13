package multiple;


import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 如何保存多个客户端的连接，使用Map实现,ConcurrentHashMap保证线程安全
 */
public class MultiServer {
    //使用哈希表包装客户端
    private static Map<String,Socket> clientLists=new ConcurrentHashMap<>();
    /**
     * 处理每个客户端的输入输出请求
     */
    private static class ExecuteClientRequest implements Runnable{
        private  Socket client;
        public ExecuteClientRequest(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                //获取用户输入流
                Scanner in=new Scanner(client.getInputStream());
                while(true){
                    String msgFromClient="";
                    if(in.hasNext()) {
                        msgFromClient = in.nextLine();
                        //   /r/n
                        //windows下消除用户输入自带的\r
                        //Pattern与Matcher是正则表达式
                        Pattern pattern = Pattern.compile("\r");
                        //检测字符串中包不包括\r
                        Matcher matcher = pattern.matcher(msgFromClient);
                        msgFromClient = matcher.replaceAll("");
                        //注册
                        //userName:Dyson
                        if (msgFromClient.startsWith("userName")) {
                            String userName = msgFromClient.split("\\:")[1];
                            userRegister(userName, client);
                        }
                        //群聊
                        //G:群聊内容
                        if (msgFromClient.startsWith("G:")) {
                            String groupMsg = msgFromClient.split("\\:")[1];
                            groupChat(groupMsg);
                        }
                        //私聊
                        //P:用户名-私聊内容
                        if (msgFromClient.startsWith("P:")) {
                            String userName = msgFromClient.split("\\:")[1].split("\\-")[0];
                            String privateMsg = msgFromClient.split("\\:")[1].split("\\-")[1];
                            privateChat(userName, privateMsg);
                        }
                        //用户退出
                        //用户名:byebye
                        if (msgFromClient.contains("byebye")) {

                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //注册实现
        private void userRegister(String userName,Socket socket){
            //判断是否用户存在，客户端是否退出
            clientLists.put(userName,socket);
            System.out.println("用户"+userName+"正在注册...");
            System.out.println("当前聊天室人数为："+clientLists.size());
            try {
                PrintStream out=new PrintStream(socket.getOutputStream(),true,"UTF-8");
                out.println("注册成功！");
                out.println("当前聊天室人数为："+clientLists.size());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //群聊实现
        //将哈希表中所存储的客户端遍历一遍，给每一个客户端发送消息
        private void groupChat(String groupMsg){
            Set<Map.Entry<String,Socket>> clientEntry=clientLists.entrySet();
            //使用迭代器输出
            Iterator<Map.Entry<String,Socket>> iterator=clientEntry.iterator();
            //对哈希表进行遍历
            while(iterator.hasNext()){
                Socket client=iterator.next().getValue();
                try {
                    PrintStream out=new PrintStream(client.getOutputStream(),true,"UTF-8");
                    out.println("群聊信息为："+groupMsg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //私聊实现
        private void privateChat(String userName,String privateMsg){
            //获取私聊对象的客户端
            Socket client=clientLists.get(userName);
            try {
                PrintStream out=new PrintStream(client.getOutputStream(),true,"UTF-8");
                out.println("私聊信息为："+privateMsg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket=new ServerSocket(6666);
        //使用线程池来同时处理多个客户端连接,并设置线程池数量
        ExecutorService executorService=Executors.newFixedThreadPool(20);
        System.out.println("等待客户端连接...");
        for(int i=0;i<20;i++){
            Socket client=serverSocket.accept();
            System.out.println("有新用户连接，端口号为："+client.getPort());
            ExecuteClientRequest executeClientRequest=new ExecuteClientRequest(client);
            executorService.submit(executeClientRequest);
        }
        //关闭线程池与客户端
        executorService.shutdown();
        serverSocket.close();
    }
}
