package www.Dyson;

import com.sun.xml.internal.bind.AnyTypeAdapter;

import java.lang.reflect.AnnotatedType;

interface ILinklist <Anytype>{
    void PrintList();
    //尾插
    void PushBack(Anytype data);
    //尾删
    void PopBack();
    //头插
    void PushFront(Anytype data);
    //头删
    void PopFront();
    //查找节点位置
    int FindPos(Anytype data);
    //指定位置插入
    void Insert(int pos, Anytype data);
    //指定位置删除
    void Erase(int pos);
    //删除指定元素
    void Remove(Anytype data);
    //删除所有指定元素
    void RemoveAll(Anytype data);
    //删除无头单链表的非尾节点（不能遍历链表）
    void EraseNotTailNode(int pos);
    //链表长度
    int GetListLength();
}
class MyLink <AnyType> implements ILinklist <AnyType>{
    private int size=0;
    private Node head;
    private class Node{
        private Node next=null;
        private AnyType data;
        public Node(AnyType data){
            this.data=data;
        }
    }
    public Node FindNode(AnyType data){
        if(head==null){
            return null;
        }
        Node tmp=head;
        if(data==null){
            while(tmp!=null){
                if(tmp.data==data){
                    return tmp;
                }
            }
        }else{
            while(tmp!=null){
                if(data.equals(tmp.data)){
                    return tmp;
                }
            }
        }
        return null;
    }
    //打印
    @Override
    public void PrintList() {
        Node cur=this.head;
        while(cur!=null){
            System.out.print(cur.data+" ");
            cur=cur.next;
        }
    }
    //尾插
    @Override
    public void PushBack(AnyType data) {
        Node newNode=new Node(data);
        if(head==null){
            head=newNode;
            size++;
            return;
        }
        Node tmp=head;
        while(tmp.next!=null){
            tmp=tmp.next;
        }
        tmp.next=newNode;
        size++;
    }
    //尾删
    @Override
    public void PopBack() {
        if(head==null){
            return;
        }
        if(head.next==null){
            head=null;
        }
        Node prev=head;
        Node tmp=prev.next;
        while(tmp.next!=null){
            prev=tmp;
            tmp=tmp.next;
        }
        prev.next=null;
        size--;
    }
    //头插
    @Override
    public void PushFront(AnyType data) {
        Node newNode=new Node(data);
        newNode.next=head;
        head=newNode;
        size++;
    }
    //头删
    @Override
    public void PopFront() {
        if(head==null){
            return;
        }
        head=head.next;
        size--;
    }
    //查找
    @Override
    public int FindPos(AnyType data) {
        Node tmp=head;
        int ret=1;
        if(data==null){
            while(tmp!=null){
                if(tmp.data==null){
                    return ret;
                }
                ret++;
                tmp=tmp.next;
            }
        }else {
            while (tmp != null) {
                if (data.equals(tmp.data)) {
                    return ret;
                }
                ret++;
                tmp=tmp.next;
            }
        }
        return -1;
    }
    //指定位置插入
    @Override
    public void Insert(int pos, AnyType data) {
        if(pos<1||pos>size){
            throw new RuntimeException("插入位置不存在!");
        }
        if(pos==1){
            PushFront(data);
            return;
        }
        if(pos==size){
            PushBack(data);
        }
        Node prev=head;
        Node tmp=head;
        Node newNode=new Node(data);
        --pos;
        while(pos!=0){
            prev=tmp;
            tmp=tmp.next;
            --pos;
        }
        newNode.next=tmp;
        prev.next=newNode;
        size++;
    }
    //指定位置删除
    @Override
    public void Erase(int pos) {
        if(head==null){
            return;
        }
        if(pos<1||pos>size){
            throw new RuntimeException("删除位置不存在!");
        }
        if(pos==1){
            PopFront();
            return;
        }
        if(pos==size){
            PopBack();
            return;
        }
        Node prev=head;
        Node tmp=head;
        --pos;
        while(pos!=0){
            prev=tmp;
            tmp=tmp.next;
            --pos;
        }
        prev.next=tmp.next;
        size--;
    }
    //删除指定元素
    @Override
    public void Remove(AnyType data) {
        if(head==null){
            return;
        }
        int pos=FindPos(data);
        if(pos==-1){
            throw new RuntimeException("删除的元素不存在！");
        }
        if(pos==1){
            head=head.next;
            --size;
            return;
        }
        if(pos==size){
            PopBack();
            return;
        }
        Node prev=head;
        Node tmp=head;
        --pos;
        while(pos!=0){
            prev=tmp;
            tmp=tmp.next;
            --pos;
        }
        prev.next=tmp.next;
        size--;
    }
    //删除所有指定元素
    @Override
    public void RemoveAll(AnyType data) {
        int i = 1;
        while (i <= size) {
            if (FindPos(data) != -1) {
                Remove(data);
            } else {
                ++i;
            }
        }
    }
    private Node FindNode(int pos){
        Node tmp=head;
        while(pos>0){
            tmp=tmp.next;
            --pos;
        }
        return tmp;
    }
    //删除无头单链表的非尾节点（不能遍历链表）
    public void EraseNotTailNode(int pos){
        if(pos<=1||pos>=this.size-1){
            throw new RuntimeException("删除节点不正确");
        }
        Node tmp=this.FindNode(pos);
        tmp.data=tmp.next.data;
        tmp.next=tmp.next.next;
    }
    //链表长度
    @Override
    public int GetListLength() {
        return size;
    }
}
public class Linklist {
    public static void main(String[] args) {
        ILinklist<String> list=new MyLink();
        list.PushBack("Dyson");
        list.PushBack("love");
        list.PushBack("Dyson");
        list.PushBack("Liyuxin");
        list.PushFront("啦啊啦啦");
        list.EraseNotTailNode(3);
//        list.RemoveAll("Dyson");
//        list.Remove("Dyson");
//        list.PushBack("Dyson");
        System.out.println(list.GetListLength());
//        list.Insert(3,"love");
//        list.PushFront("Liyuxin");
//        list.PrintList();
//        list.PopBack();
        list.PrintList();
    }
}
