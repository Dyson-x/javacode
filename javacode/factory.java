package www.Dyson.java;
interface Computer{
    void printComputer();
}
class macBookProComputer implements Computer{
    @Override
    public void printComputer() {
        System.out.println("This is MacBookProComputer;");
    }
}
class surfaceBookProComputer implements Computer{
    @Override
    public void printComputer() {
        System.out.println("This is SurfaceBookProComputer;");
    }
}
interface computerFactory{
    Computer creatComputer();
}
class AppleFactory implements computerFactory{
    @Override
    public Computer creatComputer() {
        return new macBookProComputer();
    }
}
class MsFactory implements computerFactory{
    @Override
    public Computer creatComputer() {
        return new surfaceBookProComputer();
    }
}
public class factory{
    public void buyComputer(Computer computer){
        computer.printComputer();
    }
    public static void main(String[] args) {
        factory clint=new factory();
        computerFactory Factoy=new AppleFactory();
        clint.buyComputer(Factoy.creatComputer());
    }
}
/*
public class factory {
    public static void main(String[] args) {
        computerFactory Factory=new MsFactory();
        Computer com=Factory.creatComputer();
        com.printComputer();
    }
}
 */

