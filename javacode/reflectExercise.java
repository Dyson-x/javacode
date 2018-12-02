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
                iFruit= (IFruit) cls.newInstance();  //ͨ������õ�ʵ��������
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
    //���ַ�������ĸ��д�ķ���
    public static String initCap(String str){
        return str.substring(0,1).toUpperCase()+str.substring(1);
    }
    public static void main(String[] args) throws Exception {
        /**
         * ����Ϊprivate������£�ͨ���ƻ���װ�ķ�����������ȡ������ֵ
         */
        Class<PersonThree> cls=PersonThree.class;
        Field field=cls.getDeclaredField("age");
        PersonThree per=cls.newInstance();
        //�ƻ���װ
        field.setAccessible(true);
        field.set(per,18);
        System.out.println(field.get(per));
        /**
         * ����Ϊpublic������£�������ȡ������ֵ
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
         *���������ͨ����
         */
        /*
        //ȡ��ʵ��������
        Class<PresonOne> cls=PresonOne.class;
        //��װ��������
        String setMethodName="set"+initCap(args[0]);
        String getMethodName="get"+initCap(args[0]);
        //ȡ��Method����
        Method setMethod=cls.getMethod(setMethodName,String.class);
        Method getMethod=cls.getMethod(getMethodName);
        //ȡ��PersonOne��ʵ�������󣬵��÷���
        PresonOne per=cls.newInstance();
        setMethod.invoke(per,"Dyson");
        System.out.println(getMethod.invoke(per));
        */
        /**
         *����ʵ�ֹ���ģʽ�������빤��ģʽ
         */
        /*
        Class<Person> cls=Person.class;
        //ȡ���вι���
        Constructor constructor=cls.getConstructor(String.class,int.class);
        //ʵ��������
        Person per= (Person) constructor.newInstance("Dyson",20);
        System.out.println(per);
        IFruit iFruit=fruitFactory.getInstance("www.Dyson.java.Apple");
        iFruit.eat();
        */
        /**
         *ȡ�ø��ӿ���Ϣ
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
