package www.Dyson;

import java.util.Iterator;

class myqueueMpl<Item> implements Iterable<Item>{
    private Node last;
    private Node first;
    private int N;
    private class Node{
        Item item;
        Node next;
        public Node(Item item){
            this.item=item;
        }
    }
    public boolean isEmpty(){
        return first==null;
    }
    public int size(){
        return N;
    }
    public void addqueue(Item item){
        Node oldlast=last;
        last=new Node(item);
        last.next=null;
        if(isEmpty()){
            first=last;
        }else{
            oldlast.next=last;
        }
        N++;
    }
    public Item removequeue(){
        Item item=first.item;
        first=first.next;
        if(isEmpty()){
            last=null;
        }
        N--;
        return item;
    }
    @Override
    public Iterator<Item> iterator() {
        return new ListItertor();
    }
    private class ListItertor implements Iterator<Item>{
        private Node current=first;
        @Override
        public boolean hasNext() {
            return current!=null;
        }
        @Override
        public Item next() {
            Item item=current.item;
            current=current.next;
            return item;
        }
    }
}
public class myQueue {
    public static void main(String[] args) {
        myqueueMpl<String> q=new myqueueMpl<String>();
        String[] str=new String[]{"dayson","love","liyuxin","lalala"};
        for(int i=0;i<str.length;i++){
            q.addqueue(str[i]);
        }
        q.removequeue();
        for (String tmp:q){
            System.out.print(tmp+" ");
        }
    }
}
