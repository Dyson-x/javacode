package www.Dyson;

import com.sun.xml.internal.bind.AnyTypeAdapter;

import java.lang.reflect.AnnotatedType;

interface ILinklist <Anytype>{
    void PrintList();
    //β��
    void PushBack(Anytype data);
    //βɾ
    void PopBack();
    //ͷ��
    void PushFront(Anytype data);
    //ͷɾ
    void PopFront();
    //����
    int Find(Anytype data);
    //ָ��λ�ò���
    void Insert(int pos, Anytype data);
    //ָ��λ��ɾ��
    void Erase(int pos);
    //ɾ��ָ��Ԫ��
    void Remove(Anytype data);
    //ɾ������ָ��Ԫ��
    void RemoveAll(Anytype data);
    //ɾ����ͷ������ķ�β�ڵ㣨���ܱ�������
    void EraseNotTailNode(int pos);
    //������
    int GetListLength();
}
class MyLink <AnyType> implements ILinklist <AnyType>{
    private int size;
    private Node head;
    private class Node{
        private Node next=null;
        private AnyType data;
        public Node(AnyType data){
            this.data=data;
        }
    }
    //��ӡ
    @Override
    public void PrintList() {
        Node cur=this.head;
        while(cur!=null){
            System.out.print(cur.data+" ");
            cur=cur.next;
        }
    }
    //β��
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
    //βɾ
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
    //ͷ��
    @Override
    public void PushFront(AnyType data) {
        Node newNode=new Node(data);
        newNode.next=head;
        head=newNode;
        size++;
    }
    //ͷɾ
    @Override
    public void PopFront() {
        if(head==null){
            return;
        }
        head=head.next;
        size--;
    }
    //����
    @Override
    public int Find(AnyType data) {
        Node tmp=head;
        int ret=0;
        if(data==null){
            while(tmp!=null){
                if(tmp.data==null){
                    return ret;
                }
                ret++;
            }
        }
        while(tmp!=null){
            if(data.equals(tmp.data)){
                return ret;
            }
            ret++;
        }
        return -1;
    }
    //ָ��λ�ò���
    @Override
    public void Insert(int pos, AnyType data) {
        if(pos<1||pos>size){
            throw new RuntimeException("����λ�ò�����!");
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
    //ָ��λ��ɾ��
    @Override
    public void Erase(int pos) {
        if(head==null){
            return;
        }
        if(pos<1||pos>size){
            throw new RuntimeException("ɾ��λ�ò�����!");
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
    //ɾ��ָ��Ԫ��
    @Override
    public void Remove(AnyType data) {
        if(head==null){
            return;
        }
        int pos=Find(data);
        if(pos==-1){
            throw new RuntimeException("ɾ����Ԫ�ز����ڣ�");
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
    //ɾ������ָ��Ԫ��
    @Override
    public void RemoveAll(AnyType data) {

    }
    //ɾ����ͷ������ķ�β�ڵ㣨���ܱ�������
    @Override
    public void EraseNotTailNode(int pos) {

    }
    //������
    @Override
    public int GetListLength() {
        return size;
    }
}
public class Linklist {
    public static void main(String[] args) {
        ILinklist<String> list=new MyLink();
        list.PushBack("Dyson");
        list.PushBack("Dyson");
        list.PushBack("Dyson");
        System.out.println(list.GetListLength());
        list.Insert(3,"love");
        list.PushFront("Liyuxin");
        list.PopBack();
        list.PrintList();
    }
}
