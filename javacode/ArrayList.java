package www.Dyson;

import java.util.Scanner;

interface IList<AnyType> {
    //打印
    void PrintSeqList();
    //初始化
    void InitSeqList(int myLength);
    //尾插
    void PushBack(AnyType data);
    //尾删
    void PopBack();
    //头插
    void PushFront(AnyType data);
    //头删
    void PopFront();
    //查找
    int Find(AnyType data);
    //指定位置插入
    void Insert(int pos,AnyType data);
    //指定位置删除
    void Erase(int pos);
    //指定元素删除
    void Remove(AnyType data);
    //顺序表大小
    int Size();
    //顺序表是否为空
    boolean Empty();
    //删除指定所有元素
    void RemoveAll(AnyType data);
}
class Sqlist<AnyType> implements IList<AnyType> {
    private static int len=0;
    private static int capcity=0;
    private AnyType[] Seqlist;
    //打印
    @Override
    public void PrintSeqList() {
        for(int i=0;i<len;i++){
            System.out.print(Seqlist[i]+" ");
        }
    }
    //初始化
    @Override
    public void InitSeqList(int myLength) {
        Seqlist = (AnyType[]) new Object[myLength];
        this.capcity=myLength;
    }
    //扩容
    private void Dilatation(){
        capcity*=2;
        AnyType[] newSeqlist=(AnyType[])new Object[capcity];
        System.arraycopy(Seqlist,0,newSeqlist,0,len);
        Seqlist=newSeqlist;
    }
    //尾插
    @Override
    public void PushBack(AnyType data){
        if(!this.isDila()){
                return;
        }
        Seqlist[this.len]=data;
        len++;
    }
    //查询是否需要扩容
    private boolean isDila(){
        if(this.len>=Seqlist.length){
            System.out.println("是否需要扩容y/n");
            Scanner sca=new Scanner(System.in);
            String str=sca.nextLine();
            if(str.equals("y")){
                this.Dilatation();
                return true;
            }else{
                throw new RuntimeException("空间已经满了");
            }
        }
        return true;
    }
    //尾删
    @Override
    public void PopBack() {
        this.len--;
    }
    //头插
    @Override
    public void PushFront(AnyType data) {
        if(!this.isDila()){
            return;
        }
        for(int i=this.len-1;i>=0;i--){
            Seqlist[i+1]=Seqlist[i];
        }
        Seqlist[0]=data;
    }
    //头删
    @Override
    public void PopFront() {
        if(!Empty()){
            throw new RuntimeException("表为空，删除失败！");
        }
        for(int i=0;i<this.len-1;i++){
            Seqlist[i]=Seqlist[i+1];
        }
        this.len--;
    }
    //查找
    @Override
    public int Find(AnyType data) {
        if(data==null){
            for(int i=0;i<this.len;i++){
                if(Seqlist[i]==null){
                    return i;
                }
            }
        }
        else{
            for(int i=0;i<this.len;i++){
                if(data.equals(Seqlist[i])){
                    return i;
                }
            }
        }
        //找不见返回-1
        return -1;
    }
    //插入
    @Override
    public void Insert(int pos, AnyType data){
        if(this.len==this.capcity){
            throw new RuntimeException("内存已经满了！");
        }
        if(pos>=len-1){
            throw new RuntimeException("插入位置不正确！");
        }else{
            for(int i=this.len-1;i>=pos;i--){
                Seqlist[i+1]=Seqlist[i];
            }
            Seqlist[pos]=data;
            this.len++;
        }
    }
    //指定位置删除
    @Override
    public void Erase(int pos) {
        if(pos>this.len-1){
            throw new RuntimeException("要删除位置不存在！");
        }
        for(int i=pos;i<this.len-1;i++){
            Seqlist[i]=Seqlist[i+1];
        }
        this.len--;
    }
    //指定元素删除
    @Override
    public void Remove(AnyType data) {
        int index=Find(data);
        if(index==-1){
            throw new RuntimeException("要删除的元素不存在");
        }
        for(int i=index;i<this.len-1;i++){
            Seqlist[i]=Seqlist[i+1];
        }
        this.len--;
    }
    //顺序表大小
    @Override
    public int Size() {
        return this.len;
    }
    //判断表是否为空
    @Override
    public boolean Empty() {
        if(this.len==0){
            return false;
        }
        return true;
    }
    @Override
    public void RemoveAll(AnyType data){
        if(Find(data)==-1){
            throw new RuntimeException("删除元素不存在！");
        }
        for(int i=0;i<this.len;i++){
            if(data.equals(Seqlist[i])){
                for(int j=i;j<this.len-1;j++){
                    Seqlist[j]=Seqlist[j+1];
                }
                --this.len;
            }
        }
    }
}
public class ArrayList {
    public static void main(String[] args) {
        IList<Integer> list=new Sqlist();
        list.InitSeqList(10);
        list.PushBack(2);
        list.PushBack(3);
        list.PushBack(4);
        list.PushBack(5);
        list.PushBack(6);
        list.PushBack(7);
        list.PushBack(8);
        list.PushBack(9);
        list.PushBack(10);
//        list.PopBack();
//        list.PopBack();
        list.PushFront(1);
        list.PopFront();
        list.PushFront(1);
        list.Insert(3,99);
        list.Erase(5);
        list.Remove(99);
        list.PushBack(2);
        list.RemoveAll(2);
        System.out.println("顺序表大小："+list.Size());
        list.PrintSeqList();
    }
}
