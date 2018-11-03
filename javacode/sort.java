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
                    int cur=data[j];
                    data[j]=data[j+1];
                    data[j+1]=cur;
                }
            }
        }
    }
}
class selectionSort{
    public void SelectSort(int []data){
        for(int i=0;i>data.length;i++){
            int max=i;
            for(int j=i+1;j<data.length;j++){
                if(data[max]<data[j]){
                    max=j;
                }
            }
            if(max!=i){
                int cur=data[max];
                data[max]=data[i];
                data[i]=cur;
            }
        }
    }
}
public class sort {
    public static void main(String[] args) {
        int []data=new int[]{5,2,4,6,1,3,9,7,8};
        //insertSort sort=new insertSort();
        //sort.InsertSort(data);
        //sort.ShellSort(data);
        selectionSort sort=new selectionSort();
        sort.SelectSort(data);
        print(data);
    }
    public static void print(int data[]){
        for(int temp:data){
            System.out.print(temp+" ");
        }
    }
}
