package www.Dyson.java;
/*
5  2  4  6  1  3
2  5  4  6  1  3
 */
//插入排序
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
//交换排序
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
    //快速排序
    public void QuickSort(int []data,int left,int right){
        if(left>=right){
            return;
        }
        int div=partionThree(data,left,right);
        QuickSort(data,left,div-1);
        QuickSort(data,div+1,right);
    }
    //左右指针法
    public int partionOne(int []data,int begin,int end){
        int index=end;
        int key=data[end];
        while(begin<end){
            while(begin<end&&data[begin]<=key){
                begin++;
            }
            while(begin<end&&data[end]>=key){
                end--;
            }
            swap(data,begin,end);
        }
        swap(data,begin,index);
        return begin;
    }
    //挖坑法
    public int partionTwo(int []data,int begin,int end){
        int key=data[end];
        while(begin<end){
            while(begin<end&&data[begin]<=key){
                begin++;
            }
            data[end]=data[begin];
            while(begin<end&&data[end]>=key){
                end--;
            }
            data[begin]=data[end];
        }
        data[begin]=key;
        return begin;
    }
    //前后指针法
    public int partionThree(int []data,int begin,int end){
        int next=begin;
        int prev=next-1;
        int key=end;
        while(next<end){
            if(data[next]<data[key]&&++prev!=next){
                swap(data,prev,next);
            }
            next++;
        }
        //重要：这里prev容易遗忘++，如果没有+1，会造成栈溢出
        swap(data,++prev,key);
        return prev;
    }
    //交换节点数据
    public void swap(int []data,int n,int m) {
        int cur=data[n];
        data[n]=data[m];
        data[m]=cur;
    }
}
//选择排序
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
//归并排序
class mergeSort{
    public void MergeSort(int []data,int left,int right){
        //当left==right，return
        if(left>=right){
            return;
        }
        int mid=left+((right-left)>>1);
        //使用递归不断的去将数组拆分
        MergeSort(data,left,mid);
        MergeSort(data,mid+1,right);
        _Merge(data,left,mid,mid+1,right);
    }
    public void _Merge(int []data,int begin1,int end1,int begin2,int end2){
        //将begin1和begin2的位置记录下来
        int []tmp=new int[data.length];
        int index=begin1;
        int i=begin1;
        int j=begin2;
        while(i<=end1&&j<=end2){
            //保证其稳定性
            if(data[i]<=data[j]){
                tmp[index++]=data[i++];
            }else{
                tmp[index++]=data[j++];
            }
        }
        while(i<=end1){
            tmp[index++]=data[i++];
        }
        while(j<=end2){
            tmp[index++]=data[j++];
        }
        //最后将tmp数组中内容拷贝到原数组中相应位置即可
        System.arraycopy(tmp,begin1,data,begin1,end2-begin1+1);
        //while(begin1<=end2){
        //   data[begin1]=tmp[begin1++];
        //}
    }
}
public class sort {
    public static void main(String[] args) {
        int []data=new int[]{5,2,4,6,1,3,9,7,8};
        //insertSort sort=new insertSort();
        //sort.InsertSort(data);
        //sort.ShellSort(data);
        //selectionSort sort=new selectionSort();
        //sort.SelectSort(data);
        //sort.HeapSort(data);
        //swapSort sort=new swapSort();
        //sort.QuickSort(data,0,data.length-1);
        System.out.print("排序前结果为：");
        print(data);
        mergeSort sort=new mergeSort();
        sort.MergeSort(data,0,data.length-1);
        System.out.print("排序后结果为：");
        print(data);
    }
    public static void print(int data[]){
        for(int temp:data){
            System.out.print(temp+" ");
        }
        System.out.println("");
    }
}
