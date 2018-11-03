package www.Dyson.java;
//饿汉式
//先new为敬
class single1{
    private static final single1 SINGLE=new single1();
    private single1(){} //禁止产生实例化对象
    public static single1 Single (){
        return SINGLE;
    }
}
//懒汉式
//用时在new
class single2{
    private static single2 SINGLE;
    private single2(){}
    public static single2 Single(){
        if(SINGLE==null){
            SINGLE=new single2();
        }
        return SINGLE;
    }
}
public class singleton {
    public static void main(String[] args) {
        single1 sin1=single1.Single();
        single1 sin2=single1.Single();
        System.out.println(sin1);
        System.out.println(sin2);
        single2 sin3=single2.Single();
        single2 sin4=single2.Single();
        System.out.println(sin3);
        System.out.println(sin4);
    }
}
