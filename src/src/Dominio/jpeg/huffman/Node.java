package Dominio.jpeg.huffman;

import java.util.ArrayList;

/**
 * Node es una estructura de datos representa un nodo Huffman. Sirve para crear un arbol.
 * @author: Lucas Cajal
 */
public class Node{
    private int weight;
    private int value;
    private Node left, right;

    public Node(){
        //Class creator
        left = right = null;
    }
    public Node(final int w, final int val){
        //Class creator for leaf
        weight = w;
        value = val;
        left = null;
        right = null;
    }
    public Node(final Node l, final Node r){
        //Class creator for regular node
        weight = l.weight() + r.weight();
        left = l;
        right = r;
    }
    public int weight(){
        return weight;
    }
    public int value(){
        return value;
    }
    public Node left(){
        return left;
    }
    public Node right(){
        return right;
    }

    public void traverse(ArrayList<Pair<String, Integer>> codeArray, String s){
        if (left == null){
            codeArray.add(new Pair<String, Integer>(s, value));
        }
        else {
            left.traverse(codeArray, s + "0");
            right.traverse(codeArray, s + "1");
        }
    }

    public boolean isLeaf(){
        if (left == null) return true;
        else return false;
    }
}
