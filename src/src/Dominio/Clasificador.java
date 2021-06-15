
package Dominio;

import java.io.File;
import java.util.ArrayList;
import javafx.util.Pair;

/**
 * Esta clase Clasificador es una clase padre abstracta que define el método abstracto comprimir e implementa otros métodos.
 */
abstract public class Clasificador {
    
    private File f;
    private String ruta;
    private String nombre;
    private long tamano;
    
    /**
     * Constructora de la clase Clasificador.
     * @param f El parámetro f nos indica el fichero puede ser tanto una carpeta como un fichero.
     * @param ruta El parámetro ruta nos indica la ruta donde se encuentra el fichero pasado por parámetro.
     * @param nombre El parámetro nombre nos indica el nombre del archivo pasado por parámetro.
     */
     public Clasificador(File f,String ruta, String nombre){
        this.f = f;
        this.ruta = ruta;
        this.nombre = nombre;
        this.tamano = 0;
    }
    
    /**
      * Constructora de la clase Clasificador.
      * @param f El parámetro f nos indica el fichero puede ser tanto una carpeta como un fichero.
      * @param ruta El parámetro ruta nos indica la ruta donde se encuentra el fichero pasado por parámetro.
      * @param nombre El parámetro nombre nos indica el nombre del archivo pasado por parámetro.
      * @param tamano El parámetro tamano nos indica el tamaño del archivo pasado por parámetro.
      */ 
    public Clasificador(File f, String ruta, String nombre, long tamano) {
        this.f = f;
        this.ruta = ruta;
        this.tamano = tamano;
        this.nombre = nombre;
    }

	/**
     * Método que retorna el nombre del Clasificador.
     * @return retorna el nombre del Clasificador.
     */
    public String getNombre() {
        return nombre;
    }    
    /**
     * Método que retorna la Ruta en la que se encuentra el Clasificador.
     * @return retorna el nombre de la Ruta.
     */
    public String getRuta() {
        return ruta;
    }
	/**
     * Método en el cual le das valor al atributo ruta.
     @param ruta El parámetro ruta nos indica la ruta en la que se encontrara el Clasificador.
     */
    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

	/**
     * Método que retorna el tamaño del Clasificador.
     * @return retorna el tamaño del Clasificador.
     */
    public long getTamano() {
        return tamano;
    }

	/**
     * Método en el cual le das valor al atributo tamano.
     @param tamano El parámetro tamano nos indica el tamaño del Clasificador.
     */
    public void setTamano(long tamano) {
        this.tamano = tamano;
    }
    
    
}
