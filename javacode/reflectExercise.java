package www.Dyson.java;


import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

interface IFruit{
    public void eat();
}
class Apple implements IFruit{
    @Override
    public void eat() {
        System.out.println("eat an apple");
    }
}

class Orange implements IFruit{
    @Override
    public void eat() {
        System.out.println("eat an orange");
    }
}
class fruitFactory{
    public static IFruit getInstance(String fruitName){
        IFruit iFruit=null;
        try {
            Class<?> cls=Class.forName(fruitName);
            try {
                iFruit= (IFruit) cls.newInstance();  //通过反射得到实例化对象
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return iFruit;
    }
}
interface A{};
interface B extends A{};
class C implements B,A{};
class Person{
    private String name;
    private int age;
    private Person(){};
    public Person(String name,int age){
        this.name=name;
        this.age=age;
    }
    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
class PresonOne{
    private String name;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
class PersonTwo{
    public int age;
}
class PersonThree{
    private int age;
}
public class Test{
    //将字符串首字母大写的方法
    public static String initCap(String str){
        return str.substring(0,1).toUpperCase()+str.substring(1);
    }
    public static void main(String[] args) throws Exception {
        /**
         * 属性为private的情况下，通过破坏封装的方法，设置与取得属性值
         */
        Class<PersonThree> cls=PersonThree.class;
        Field field=cls.getDeclaredField("age");
        PersonThree per=cls.newInstance();
        //破坏封装
        field.setAccessible(true);
        field.set(per,18);
        System.out.println(field.get(per));
        /**
         * 属性为public的情况下，设置与取得属性值
         */
        /*
        Class<PersonTwo> cls=PersonTwo.class;
        Field field=cls.getField("age");
        PersonTwo per=cls.newInstance();
        field.set(per,20);
        System.out.println(field.get(per));
        System.out.println(field.getType());
        */
        /**
         *反射调用普通方法
         */
        /*
        //取得实例化对象
        Class<PresonOne> cls=PresonOne.class;
        //组装方法名称
        String setMethodName="set"+initCap(args[0]);
        String getMethodName="get"+initCap(args[0]);
        //取得Method对象
        Method setMethod=cls.getMethod(setMethodName,String.class);
        Method getMethod=cls.getMethod(getMethodName);
        //取得PersonOne类实例化对象，调用方法
        PresonOne per=cls.newInstance();
        setMethod.invoke(per,"Dyson");
        System.out.println(getMethod.invoke(per));
        */
        /**
         *反射实现工厂模式，反射与工厂模式
         */
        /*
        Class<Person> cls=Person.class;
        //取得有参构造
        Constructor constructor=cls.getConstructor(String.class,int.class);
        //实例化对象
        Person per= (Person) constructor.newInstance("Dyson",20);
        System.out.println(per);
        IFruit iFruit=fruitFactory.getInstance("www.Dyson.java.Apple");
        iFruit.eat();
        */
        /**
         *取得父接口信息
         */
        /*
        Class<?> c=C.class;
        System.out.println(c.getPackage());
        System.out.println(c.getSuperclass());
        Class<?>[] classes=c.getInterfaces();
        for (Class<?> T:classes) {
            System.out.println(T);
        }
        */
    }
}
