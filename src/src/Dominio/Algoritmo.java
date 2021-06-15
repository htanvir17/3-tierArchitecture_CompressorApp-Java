
package Dominio;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Esta interficie define los metodos de los algoritmos usados por el compresor.
 * @author: Kenny Alejandro
 */
public interface Algoritmo {
     /**
     * Método que realiza la compresión de un texto.
     * @param contenido_fichero El parámetro f nos indica el fichero de tipo texto(.txt) a comprimir.
	 * @return retorna los datos compridos y los deja en MyByteCollection   
     */
    public MyByteCollection comprimir(MyByteCollection contenido_fichero) throws IOException;
     
     /**
     * Metodo que realiza la compresion de una imagen.
     * @param contenido_fichero El parÃ¡metro f nos indica el fichero de tipo imagen(.ppm) a comprimir.
	 * @param calidad  El párametro calidad define el valor de calidad de la descompresiÃ³n de la imagen.
	 * @param dSampling El parametro downsampling define la resolucion grafica.
	 * @return retorna los datos compridos y los deja en MyByteCollection 
     */
    public MyByteCollection comprimir(MyByteCollection contenido_fichero, int calidad,int dSampling) throws IOException;
    
    /**
     * Método que realiza la descompresión de un fichero.
     * @param contenido_fichero El parámetro f nos indica un fichero de comprimido.
	 * @return retorna los datos descompridos.
     */
    public ArrayList<Byte> descomprimir(MyByteCollection contenido_fichero) throws IOException, ClassNotFoundException; 
    
    /**
     * Método que retorna el nombre del Algortimo.
	 * @return retorna el nombre del Algortimo.
     */
    public String getNombre();
}