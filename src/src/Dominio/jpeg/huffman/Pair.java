package Dominio.jpeg.huffman;

import java.io.Serializable;

/**
 * Pair es una estructura de datos que implementa una pareja de dos elementos de tipo especificado.
 * @author: Lucas Cajal
 */
public class Pair<T1, T2> implements Serializable{
    private static final long serialVersionUID = 2; //Needed by the serializable interface
    public T1 first;
    public T2 second;

    public Pair(){

    }
    public Pair(T1 a, T2 b){
        first = a;
        second = b;
    }
}