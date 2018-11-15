package www.Dyson;
import java.util.Iterator;
class mystackMpl <Item> implements Iterable<Item>{
    private Node first;
    private int N=0;
    private class Node{
        Item item;
        Node next;
        private Node(Item item){
            this.item=item;
        }
    }
    public boolean isEmpty(){
        return first==null;
    }
    public int size(){
        return N;
    }
    public void push(Item item){
        Node oldfirst=first;
        first=new Node(item);
        first.next=oldfirst;
        N++;
    }
    public Item pop(){
        Item item=first.item;
        first=first.next;
        N--;
        return item;
    }
    @Override
    public Iterator<Item> iterator() {
        return new ListItertor();
    }
    //迭代器会遍历链表并将当前结点保存在current变量中
    private class ListItertor implements Iterator<Item>{
        private Node current=first;
        @Override
        public boolean hasNext() {
            return current!=null;
        }
        public void remove(){};
        public Item next(){
            Item item=current.item;
            current=current.next;
            return item;
        }
    }
}
public class myStack {
    public static void main(String[] args) {
        mystackMpl<String> s = new mystackMpl();
        String[] str=new String[]{"Dyson","LiLi","doudou","balabala"};
        for(int i=0;i<str.length;i++){
            s.push(str[i]);
        }
        for (String tmp:s) {
            System.out.println(tmp+" ");
        }
    }
}
