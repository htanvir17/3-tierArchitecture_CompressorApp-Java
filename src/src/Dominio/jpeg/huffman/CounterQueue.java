package Dominio.jpeg.huffman;

/**
 * CounterQueue es una estructura de datos que extiende PriorityQueue que sirve para contar elementos
 * @author: Lucas Cajal
 */
public class CounterQueue extends PriorityQueue<Integer>{
    public void counter(final int priority){
        Item<Integer> a = first;
        while(a != null && a.priority() != priority){
            a = a.next();
        }
        if (a == null){
            insert(1, priority);
        }
        else {
            a.setValue(a.value()+1);
        }
    }

    public void addResults(final int val, final int priority){
        Item<Integer> a = first;
        while(a != null && a.priority() != priority){
            a = a.next();
        }
        if (a == null){
            insert(val, priority);
        }
        else {
            a.setValue(a.value()+val);
        }
    }
}