/**
 * 反射与工厂模式
 */
package www.Dyson;
interface IFruit{
    public void eat();
}
class Orange implements IFruit{

    @Override
    public void eat() {
        System.out.println("eat an orange");
    }
}
class Apple implements IFruit{

    @Override
    public void eat() {
        System.out.println("eat an apple");
    }
}
class fruitFactroy{
    //使用static修饰，当主方法中调用该方法通过反射实例化一个对象时
    //使用过静态方法调用的，所以getInstance方法必须也是静态方法，否则
    //报无法从静态上下文中引用非静态方法的错误
    public static IFruit getInstance(String fruitName){
        IFruit iFruit=null;
        try {
            //通过.forName取得class对象
            Class<?> cls=Class.forName(fruitName);
            try {
                //通过反射取得实例化对象
                iFruit = (IFruit) cls.newInstance();
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
public class ReflectAndFactory {
    public static void main(String[] args) {
        //通过传递类名实例化对象，这样就避免了每当增加一个子类都要修改工厂类
        IFruit iFruit=fruitFactroy.getInstance("www.Dyson.Orange");
        iFruit.eat();
    }
}
