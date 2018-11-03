package www.Dyson.java;
/*
5  2  4  6  1  3
2  5  4  6  1  3
 */
class insertSort{
    //直接插入排序
    public void InsertSort(int []data){
        for(int i=0;i<data.length-1;i++){
            int end=i+1;
            int cur=data[end];
            if(data[i]>cur){
                while(end>0&&data[end-1]>cur){
                    data[end]=data[end-1];
                    end--;
                }
                data[end]=cur;
            }
        }
    }
    //希尔排序
    public void ShellSort(int []data){
        //interval为每次插入排序所需要的间隔
        int interval=data.length;
        while(interval>1){
            interval=interval/3+1;
            for(int i=0;i<data.length-interval;i++){
                int end=i+interval;
                int cur=data[end];
                if(data[i]>cur){
                    while(end>0&&data[end-interval]>cur){
                        data[end]=data[end-interval];
                        end-=interval;
                    }
                    data[end]=cur;
                }
            }
        }
    }
}
class swapSort{
    //冒泡排序
    public void BubbleSort(int []data){
        for(int i=data.length;i>0;i--){
            for(int j=0;j<i-1;j++){
                if(data[j]>data[j+1]){
                    swap(data,j+1,j);
                }
            }
        }
    }
    //交换节点数据
    public void swap(int []data,int n,int m) {
        int cur=data[n];
        data[n]=data[m];
        data[m]=cur;
    }
}
class selectionSort{
    //直接交换排序
    public void SelectSort(int []data){
        for(int i=0;i<data.length;i++){
            int min=i;
            for(int j=i+1;j<data.length;j++){
                if(data[min]>data[j]){
                    min=j;
                }
            }
            if(min!=i){
                swap(data,min,i);
            }
        }
    }
    //向下调整
    public void AdjustDown(int []data,int i,int len){
        int parent=i;
        int bord=len/2-1;
        while(parent<=bord){
            int child=parent*2+1;
            if(child+1<len){
                child=data[child+1]>data[child]?child+1:child;
            }
            if(data[child]>data[parent]){
                swap(data,parent,child);
            }
            ++parent;
        }
    }
    //堆排序
    public void HeapSort(int []data){
        for(int parent=data.length/2-1;parent>=0;parent--){
            AdjustDown(data,parent,data.length);
        }
        for(int i=data.length-1;i>0;i--){
            swap(data,0,i);
            AdjustDown(data,0,i);
        }
    }
    //交换节点数据
    public void swap(int []data,int n,int m) {
        int cur=data[n];
        data[n]=data[m];
        data[m]=cur;
    }
}
public class sort {
    public static void main(String[] args) {
        int []data=new int[]{5,2,4,6,1,3,9,7,8};
        //insertSort sort=new insertSort();
        //sort.InsertSort(data);
        //sort.ShellSort(data);
        selectionSort sort=new selectionSort();
        //sort.SelectSort(data);
        sort.HeapSort(data);
        print(data);
    }
    public static void print(int data[]){
        for(int temp:data){
            System.out.print(temp+" ");
        }
    }
}