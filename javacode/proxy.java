package www.Dyson.java;
//抽象主题类
interface abstractTheme{
    void buylipstick();
}
//真实主题类
class realTheme implements abstractTheme{
    public void buylipstick(){
        System.out.println("买口红！");
    }
}
//代理类
class proxyTheme implements abstractTheme{
    private realTheme subject=null;
    public proxyTheme(realTheme subject){
        this.subject=subject;
    }
    private void before(){
        System.out.println("取钱，排队！");
    }
    public void buylipstick(){
        before();
        this.subject.buylipstick();
        after();
    }
    private void after(){
        System.out.println("买完，收工！");
    }
}
public class proxy {
    public static void main(String[] args) {
        abstractTheme object=new proxyTheme(new realTheme());
        object.buylipstick();
    }
}
