package Dominio;

import java.util.*;
import java.io.File;
import javafx.util.Pair;

/**
 * Carpeta es una clase hija de la clase padre de Clasificador implementa el método abstracto comprimir de la clase Clasificador.
 * @author: Moisés Balcells
 */
public class Carpeta extends Clasificador {

    private ArrayList<Clasificador> contenido = new ArrayList<>();
    private Algoritmo[] estrategias = new Algoritmo[2];
    private int calidad;
    private int dSampling;
    private int n_elementos;

    /**
     * Constructora de la clase Carpeta.
     * @param f El parámetro f nos indica el fichero este tiene que ser una carpeta.
     * @param estrategias El parámetro estrategias nos indica que algoritmo a seleccionado el usuario en la primera posición un algoritmo para los archivos de texto y en el segunda posición un algoritmo para los archivos de imagen.
     * @param calidad El parámetro calidad nos indica la calidad con la que se tiene que descomprimir una imagen.
     * @param dSampling El parámetro dSampling nos indica el factor de Sampling de la imagen.
     */
    public Carpeta(File f, Algoritmo estrategias[], int calidad, int dSampling) throws NullPointerException {

        super(f, f.getAbsolutePath(), f.getName());
        this.calidad = calidad;
        this.dSampling = dSampling;
        this.estrategias[0] = estrategias[0];
        this.estrategias[1] = estrategias[1];
        this.n_elementos = 0;
    }
    
    /**
     * Método que devuelve la forma en la que esta organizada la carpeta.
     * @return Un ArrayList<Pair<String, Integer> donde se encuentran el nombre de esa carpeta o fichero y en el caso de que sea una carpeta el numero de fichero que esa contiene.
     */
    public ArrayList<Pair<String,Integer>> devolver_organizacion() {

        ArrayList<Pair<String,Integer>> org = new ArrayList<>();
        
        String str = new String(); 
        for (int i = 0; i < contenido.size(); ++i) {
            if(contenido.get(i) instanceof Fichero){
                org.add(new  Pair<>(contenido.get(i).getNombre(),-1));
            }
            else{
                Carpeta ca = (Carpeta)contenido.get(i);
                org.add(new  Pair<>(contenido.get(i).getNombre() + ".c",ca.contenido.size()));
                if(ca.contenido.size() > 0)org.addAll(ca.devolver_organizacion());
                
            }
        }
        return org;
    
    }
    /**
     * Método que retorn el numero de elementos que contiene la carpeta
     * */
    
    public int getN_elementos(){
    	return this.n_elementos; // equivalente return this.contenido.size();
    }
    /**
     * Método que añade un Clasificador
     */
    void addClasificador(Clasificador aux_fi) {
        this.contenido.add(aux_fi);
        this.n_elementos++;
    }
    
}
