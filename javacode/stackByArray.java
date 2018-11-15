package www.Dyson;

import java.util.Iterator;

class mystackbyArray<Item> implements Iterable<Item>{
    private Item[] a=(Item[]) new Object[1];
    private int N=0;
    public boolean isEmpty(){
        return N==0;
    }
    public int size(){
        return N;
    }
    //动态调整数组大小以保持数组大小和栈大小之比小于一个常数
    private void reavse(int max){
        Item[] temp=(Item[])new Object[max];
        for(int i=0;i<N;i++){
            temp[i]=a[i];
        }
        a=temp;
    }
    public void push(Item item){
        if(N==a.length){
            reavse(2*a.length);
        }
        a[N++]=item;
    }
    public Item pop(){
        Item item=a[--N];
        //避免对象游离
        a[N]=null;
        //防止空间过多开辟
        if(N>0&&N==a.length/4){
            reavse(a.length/2);
        }
        return item;
    }
    @Override
    public Iterator<Item> iterator() {
        return new ReverseArrayIterator();
    }
    //迭代将数组反转
    //支持后进先出的迭代
    private class ReverseArrayIterator implements Iterator<Item>{
        private int i=N;
        @Override
        public boolean hasNext() {
            return i>0;
        }
        @Override
        public Item next() {
            return a[--i];
        }
        @Override
        public void remove() {
        }
    }
}
public class stackByArray {
    public static void main(String[] args) {
        mystackbyArray<String> s=new mystackbyArray<String>();
        String[] str=new String[]{"lalalal","dyson","love","liyuxin"};
        for(int i=0;i<str.length;i++){
            s.push(str[i]);
        }
        for(String tmp:s){
            System.out.print(tmp+" ");
        }
    }
}
