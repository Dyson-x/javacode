package www.Dyson.java;

import java.util.Scanner;

abstract class caffeine{
    public void process(){
        this.boilWater();
        this.brew();
        this.pourCup();
        if(select()){
            this.addFlavour();
        }
    }
    protected abstract void brew();
    protected abstract void addFlavour();
    private void boilWater(){
        System.out.println("将水煮沸！");
    }
    private void pourCup(){
        System.out.println("将饮料倒进杯子中！");
    }
    boolean select(){
        return true;
    }
}
class coffee extends caffeine{
    protected void brew(){
        System.out.println("用水冲泡咖啡！");
    }
    protected void addFlavour(){
        System.out.println("加糖和牛奶！");
    }
    public boolean select(){
        String str=getSelect();
        if(str.equals("y")){
            return true;
        }else{
            return false;
        }
    }
    private String getSelect(){
        String answer=null;
        System.out.println("您想要在咖啡中加糖和牛奶吗？y/n");
        Scanner scanner=new Scanner(System.in);
        answer=scanner.nextLine();
        return answer;
    }
}
class tea extends caffeine{
    @Override
    protected void brew() {
        System.out.println("用水浸泡茶叶！");
    }
    @Override
    protected void addFlavour() {
        System.out.println("加柠檬！");
    }
}
/**
 * 星巴克咖啡冲泡法
 * 将水煮沸
 * 用沸水冲泡咖啡
 * 将咖啡倒进杯子
 *  加糖和牛奶
 * 星巴克茶冲泡法
 *  将水煮沸
 * 用沸水浸泡茶叶
 * 把茶倒进杯子
 *  加柠檬
 */
public class template {
    public static void main(String[] args) {
        caffeine Tea=new tea();
        Tea.process();
        caffeine cof=new coffee();
        cof.process();
    }
}
