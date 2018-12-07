/**
 * �����빤��ģʽ
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
    //ʹ��static���Σ����������е��ø÷���ͨ������ʵ����һ������ʱ
    //ʹ�ù���̬�������õģ�����getInstance��������Ҳ�Ǿ�̬����������
    //���޷��Ӿ�̬�����������÷Ǿ�̬�����Ĵ���
    public static IFruit getInstance(String fruitName){
        IFruit iFruit=null;
        try {
            //ͨ��.forNameȡ��class����
            Class<?> cls=Class.forName(fruitName);
            try {
                //ͨ������ȡ��ʵ��������
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
        //ͨ����������ʵ�������������ͱ�����ÿ������һ�����඼Ҫ�޸Ĺ�����
        IFruit iFruit=fruitFactroy.getInstance("www.Dyson.Orange");
        iFruit.eat();
    }
}
