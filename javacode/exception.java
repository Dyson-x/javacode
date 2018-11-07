package www.Dyson.java;
//继承受查异常必须要强制处理
class MultiplyExcepion extends Exception{
    public MultiplyExcepion(String msg) {
        super(msg);
    }
}
//继承非首查异常不需要强制处理
class DivideException extends RuntimeException{
    public DivideException(String msg){
        super(msg);
    }
}
public class exception{
    public static void main(String[] args) {
        try {
            MulException();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DivException();
    }
    public static void DivException(){
        int n=2,m=1;
        int tmp;
        tmp=n/m;
        if(tmp==2){
            throw new  DivideException("两数相除不能等于2");
        }
    }
    public static void MulException() throws Exception{
        int n=100,m=1;
        int tmp;
        tmp=n*m;
        if(tmp==100){
            throw new MultiplyExcepion("两数相乘不能等于100");
        }
    }
}
/*
www.Dyson.java.DivideException: 两数相除不能等于2
	at www.Dyson.java.Test.DivException(Test.java:30)
	at www.Dyson.java.Test.main(Test.java:15)
www.Dyson.java.MultiplyExcepion: 两数相乘不能等于100
	at www.Dyson.java.Test.MulException(Test.java:38)
	at www.Dyson.java.Test.main(Test.java:20)

 */