package bearmaps.proj2ab;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    private ArrayList<Node> mList;
    private int size = 0;
    private HashMap<T, Integer> items = new HashMap<>();
    public class Node{
        private T val;
        double priority;
        Node(T val, double priority){
            this.val = val;
            this.priority = priority;
        }
        double getPriority() {
            return priority;
        }
        void setPriority(double priority) {
            this.priority = priority;
        }
    }
    public boolean contains(T item){
        return items.containsKey(item);
    }

    public void add(T item, double priority) {
        if(contains(item)) {
            throw new IllegalArgumentException();
        }
        Node x = new Node(item, priority);
        mList.add(x);
        items.put(item, size+1);
        rise(x, size+1);
        size +=1;
    }
    private int rise(Node x, int index) {
        if(!(np(parent(index)))){
            return 0;
        }
        if (x.priority < mList.get(parent(index)).priority) {
            mList.set(index, mList.get(parent(index)));
            mList.set(parent(index), x);
            items.replace(x.val, parent(index));
            items.replace(mList.get(index).val, index);
            return rise(x, parent(index));
        }
        return 0;
    }

    private int drop(Node x, int index){
        if(!(np(leftChild(index))) &&!(np(rightChild(index)))){
            return 0;
        }
        if(!(np(leftChild(index)))){
            if(x.priority > mList.get(rightChild(index)).priority){
                return 0;
            }
            mList.set(index, mList.get(rightChild(index)));
            mList.set(rightChild(index),x);
            items.replace(x.val, rightChild(index));
            items.replace(mList.get(index).val, index);
            return drop(x, rightChild(index));
        }
        if(!(np(rightChild(index)))){
            if(x.priority > mList.get(leftChild(index)).priority){
                return 0;
            }
            mList.set(index, mList.get(leftChild(index)));
            mList.set(leftChild(index), x);
            items.replace(x.val, leftChild(index));
            items.replace(mList.get(index).val, index);
            return drop(x, leftChild(index));
        }
        if((x.priority<= mList.get(leftChild(index)).priority) &&(x.priority<= mList.get(rightChild(index)).priority)){
            return 0;
        }
        mList.set(index, mList.get(rightChild(index)));
        mList.set(rightChild(index),x);
        items.replace(x.val, rightChild(index));
        items.replace(mList.get(index).val, index);
        return drop(x, rightChild(index));
    }
    private int parent(int x){
        return x/2;
    }
    private int leftChild(int x){
        return x*2;
    }
    private int rightChild(int x){
        return x*2+1;
    }
    public T getSmallest(){
        return mList.get(1).val;
    }
    public ArrayHeapMinPQ() {
        mList = new ArrayList<>();
        items = new HashMap<>();
        mList.add(null);
    }
    public T removeSmallest(){
        T retval = mList.get(1).val;
        if(size==1){
            mList.remove(1);
            size-=1;
            return retval;
        }
        mList.set(1, mList.get(size));
        items.remove(mList.get(size).val);
        drop(mList.get(1), 1);
        mList.remove(size);
        size-=1;
        return retval;

    }
    private boolean np(int x){
        if((x<1)|| (x>=size)){
            return false;
        }
        return true;
    }
    public int size(){
        return size;
    }
    public void changePriority(T item, double priority){
        if(!contains(item)){
            throw new NoSuchElementException();
        }
        int index = items.get(item);
        mList.get(index).priority = priority;
        if(size ==1){
            return;
        }
        rise(mList.get(index), index);
        drop(mList.get(index), index);

    }


}