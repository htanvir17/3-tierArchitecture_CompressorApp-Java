package Dominio.jpeg.huffman;

/**
 * CounterQueue es una estructura de datos que implementa una cola de prioridad
 * @author: Lucas Cajal
 */
public class PriorityQueue<Type>{
    //Insertion cost: n
    //Needs to be improved to allow log(n) insertion cost
    public class Item<T>{
        private T value;
        private int priority;
        private Item<T> next;

        public Item(final T val, final int pri){
            value = val;
            priority = pri;
        }
        public Item(final T val, final int pri, final Item<T> ne){
            value = val;
            priority = pri;
            next = ne;
        }

        public Item<T> next() {
            return next;
        }
        public T value() {
            return value;
        }
        public void setValue(T val) {
            value = val;
        }
        public int priority() {
            return priority;
        }
    }

    protected Item<Type> first;
    private int size;

    public PriorityQueue(){
        size = 0;
    }
    public PriorityQueue(Item<Type> i){
        first = i;
        size = 1;
    }
    public int size(){
        return size;
    }
    public Item<Type> front(){
        return first;
    }
    public Pair<Type, Integer> pop(){
        Pair<Type, Integer> result = new Pair<Type, Integer>(first.value, first.priority);
        first = first.next;
        --size;
        return result;
    }
    public void insert(Item<Type> i){
        if (first == null) first = i;
        else if (first.priority > i.priority){
            i.next = first;
            first = i;
        }
        else {
            Item<Type> aux = first;
            while(aux.next != null && aux.next.priority <= i.priority){
                aux = aux.next;
            }
            i.next = aux.next;
            aux.next = i;
        }
        ++size;
    }
    public void insert(final Type i, final int priority){
        Item<Type> a = new Item<Type>(i, priority);
        insert(a);
    }
}