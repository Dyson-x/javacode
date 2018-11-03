package www.Dyson.java;
class Node{
    private Object data;
    private Node next;
    public Node(Object data) {
        super();
        this.data = data;
    }
    public Object getData() {
        return data;
    }
    public Node getNext() {
        return next;
    }
    public void setData(Object data) {
        this.data = data;
    }
    public void setNext(Node next) {
        this.next = next;
    }
}
public class List {
    public static void main(String[] args) {
        Node first=new Node("车头");
        Node node1=new Node("车厢1");
        Node node2=new Node("车厢2");
        Node last=new Node("车尾");
        first.setNext(node1);
        node1.setNext(node2);
        node2.setNext(last);
        getNodeData(first);
        System.out.println("null");
    }
    public static void getNodeData(Node node){
        while(node!=null){
            System.out.print(node.getData()+"->");
            node=node.getNext();
        }
    }
}


