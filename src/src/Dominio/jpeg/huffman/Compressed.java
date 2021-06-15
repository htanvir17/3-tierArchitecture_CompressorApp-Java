package Dominio.jpeg.huffman;

import java.util.ArrayList;
import java.io.Serializable;

/**
 * Compressed es una estructura de datos que representa un .ppm comprimido
 * @author: Lucas Cajal
 */
public class Compressed implements Serializable{
    private static final long serialVersionUID = 1; //Needed by the serializable interface
    private int height, width;
    private byte downType, quality;

    private byte[][] datos;
    private byte[] extraBits;
    private ArrayList< ArrayList<Pair<String, Integer>> > codeArray;
    
    public void setDatos(final byte[][] d){
        datos = d;
    }
    public byte[][] getDatos(){
        return datos;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(final int w) {
        width = w;
    }

    public int getDownType() {
        return downType;
    }

    public void setDownType(final int d) {
        downType = (byte) d;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(final int q) {
        quality = (byte) q;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(final int h) {
        height = h;
    }

    public void setExtraBits(final byte[] e) {
        extraBits = e;
    }

    public byte[] getExtraBits() {
        return extraBits;
    }

    public void setCodeArray(final ArrayList< ArrayList<Pair<String, Integer>> > codes) {
        codeArray = codes;
    }

    public ArrayList<ArrayList<Pair<String, Integer>>> getCodeArray() {
        return codeArray;
    }
}