package www.Dyson.java;
//自定义相乘受查异常
class MultiplyExcepion extends Exception{
    public MultiplyExcepion(String msg) {
        super(msg);
    }
}
//自定义相除非受查异常
class DivideException extends RuntimeException{
    public DivideException(String msg){
        super(msg);
    }
}
public class Test{
    public static void main(String[] args) {
        int num1=0;
        //受查异常，强制处理
        try {
            num1=MulException(10,10);
            throw new MultiplyExcepion("两数相乘为100异常");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("两数相乘为："+ num1);
        int num2=0;
        //非受查异常，不用强制处理
        num2=DivException(4,2);
        if(num2==2){
            throw new DivideException("两数相除为2异常");
        }
        System.out.println("两数相除结果为："+num2);

    }
    public static int DivException(int n,int m) throws RuntimeException{
        int tmp;
        tmp=n/m;
        if(tmp==2){
            try {
                System.out.println("两数相除出现异常");
            } catch (Exception e) {
                throw e;
            }
        }
        return tmp;
    }
    public static int MulException(int n,int m) throws Exception{
        int tmp;
        tmp=n*m;
        if(tmp==100){
            try{
                System.out.println("两数相乘出现异常");
            } catch (Exception e) {
                throw e;
            }
        }
        return tmp;
    }
}
/*
两数相乘出现异常
www.Dyson.java.MultiplyExcepion: 两数相乘为100异常
两数相乘为：100
	at www.Dyson.java.Test.main(Test.java:17)
两数相除出现异常
Exception in thread "main" www.Dyson.java.DivideException: 两数相除为2异常
	at www.Dyson.java.Test.main(Test.java:25)
 */