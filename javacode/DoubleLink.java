package www.Dyson.java;
interface ILink{
    void add(Object obj); 
    boolean remowe(Object obj);
    Object set(int index,Object newData);
    Object get(int index);
    int contains(Object data);
    int size();
    void clear();
    Object[] toArray();
    void printLink();
}
class Link implements ILink{
    private int size;
    private Node frist;
    private Node last;
    private class Node{
        private Node prev;
        private Node next;
        private Object data;
        public Node(Node prev,Node next,Object data){
            this.data=data;
            this.next=next;
            this.prev=prev;
        }
    }
    @Override
    public void add(Object obj){
        Node temp=this.last;
        Node newNode=new Node(temp,null,obj);
        /***************************************/
        this.last=newNode;
        if(this.frist==null){
            this.frist=newNode;
        }else{
            temp.next=newNode;
        }
        this.size++;
    }

    @Override
    public boolean remowe(Object obj) {
        if(obj==null){
            for(Node temp=this.frist;temp!=null;temp=temp.next){
                if(temp.data==null){
                    unLink(temp);
                    return true;
                }
            }
        }else{
            for(Node temp=this.frist;temp!=null;temp=temp.next){
                if(obj.equals(temp.data)){
                    unLink(temp);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Object set(int index, Object newData) {
        if(index<0||index>=size){
            return null;
        }
        Node temp=node(index);
        Object ret=temp.data;
        temp.data=newData;
        return ret;
    }
    @Override
    public Object get(int index) {
        if(!isLinkindex(index)){
            return null;
        }
        Node temp=node(index);
        return temp.data;
    }
    private boolean isLinkindex(int index){
        if(!isLinkindex(index)){
            return false;
        }
        return true;
    }
    //查找节点
    private Node node(int index){
        int num=size>>1;
        Node temp;
        if(index<num){
            temp=frist;
            for(int i=0;i<num;i++){
                temp=temp.next;
            }
            return temp;
        }
        else{
            temp=last;
            for(int i=size-1;i>num;i--){
                temp=temp.prev;
            }
            return temp;
        }
    }
    @Override
    public int contains(Object data) {
        Node temp=frist;
        int ret=0;
        if(data==null){
            for(;temp!=null;temp=temp.next){
                if(temp.data==null){
                    return ret;
                }
                ret++;
            }
        }
        else{
            for(;temp!=null;temp=temp.next){
                if(data.equals(temp.data)){
                    return ret;
                }
                ret++;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }
    @Override
    public void clear() {
        for(Node temp=frist;temp!=null;){
            Node ret=temp;
            temp=temp.next=temp.prev=null;
            temp=ret;
        }

    }

    @Override
    public Object[] toArray() {
        Object []data=new Object[size];
        Node temp=frist;
        for(int i=0;i<size;i++){
            data[i]=temp.data;
            temp=temp.next;
        }
        return data;
    }

    @Override
    public void printLink() {
        Object []data=toArray();
        for(Object temp:data){
            System.out.println(temp);
        }
    }
    private void unLink(Node node) {
        Node prev = node.prev;
        Node next = node.next;
        if (prev == null) {
            this.frist = next;
        } else {
            prev.next = next;
            node.prev = null;
        }
        if (next == null) {
            this.last = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }
    }
}
public class DoubleLink {
    public static void main(String[] args) {
        ILink link=new Link();
        link.add("车头");
        link.add("车厢1");
        link.add("车厢2");
        link.add("车尾");
        Object obj=link.set(2,"Dyson");
        System.out.println(obj);
        link.printLink();
    }
}


