package Dominio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;



/**
*Esta clase representa a los objetos resultantes de una compresión.
*@autor Kenny Alejandro
*/
public class ObjetoComprimido {
    /**
     * Fecha de la compresión.
     */
    private File f;
    
    /**
     * Es la ruta(path) del archivo comprimido resultante.
     */
    private String ruta_inicial;
    
    /**
     * Es el nombre del archivo comprimido resultante.
     */
    private String nombre;
    
    /**
     * Es el identificador del archivo resultante.
     */
    private Integer id;
    
    /**
     * Es el listado de identificadores de objeto comprimido existentes. 
     */
    private static HashSet<Integer> identificadores = new HashSet<Integer>();
   
    /**
     * Es la MedidaEstadistica que contine la información sobre la compresión del objetoComprimido.
     */
    private MedidaEstadistica me;
    /**
     * Es la instancia del algorítmo que se ha de usar para la descompresíon de ficheros contenidos al interior del comprimido.
     */
    private Algoritmo estrategia;
    
    /**
     * Método de la clase que retorna un nuevo identificador de ObjetoComprimido
     * Es la instancia del algorítmo que se ha de usar para la descompresíon de ficheros contenidos al interior del comprimido.
     @return Retorna un nuevo identificador para un objeto comprimido.
    */
    private static Integer crea_nuevo_identificador() {
    	 Integer nuevo_id = identificadores.size() + 1;
    	 identificadores.add(nuevo_id);
    	 return nuevo_id;
    }
    /**
     * Creadora del Objetoscomprimidos
     *@param f El parámetro f es un fichero comprimido(.comp).
    */
    public ObjetoComprimido(File f) {
        this.f = f;
    	this.ruta_inicial = f.getAbsolutePath();
    	this.nombre = f.getName();
        this.id = crea_nuevo_identificador();
       // this.me = null;
        this.estrategia = null;
    }
    /**
     * Método que retorna la ruta(path) del comprimido.
     * @return Retorna la ruta(path) del comprimido.
     */
    public String getRuta_inicial() {
        return ruta_inicial;
    }
    /**
     * Método que retorna el nombre del comprimido.
     * @return Retorna el nombre del comprimido.
     */

    public String getNombre() {
        return nombre;
    }

    /**
     * Método que retorna el identificador del comprimido.
     * @return Retorna el tiempo de compresión.
     */
    public Integer getId() {
        return id;
    }
    /**
     * Método que asigna una instancia de MedidaEstadistica.
     * @param me El parámetro me es una instancia de MedidaEstadistica.
     */
    public void setMedidaEstadistica(MedidaEstadistica me){
        this.me = me;
    }
    /**
     * Método que asigna un algoritmo.
     * @param estrategia El parámetro estrategia es una instancia de Algortimo.
     */
    public void setEstrategia(Algoritmo estrategia) {
        this.estrategia = estrategia;
    }
    /**
     * Método que descomprime una parte del comprimido usando la estrategia previamente asignada.
     * @param pos_ini El parámetro pos_ini nos indica la posición inicial de lectura del fichero comprido.
     * @param pos_final El parámetro pos_final nos indica la posición final de lectura del fichero comprido.
     * @throws ClassNotFoundException 
     */
    public ArrayList<Byte> descomprimir(ArrayList<Byte> comprimido) throws IOException, MyException, ClassNotFoundException{
       if(estrategia==null) throw new MyException("No se ha asignado una estrategia previamente");
       else return estrategia.descomprimir(new MyByteCollection(comprimido));
    }
}