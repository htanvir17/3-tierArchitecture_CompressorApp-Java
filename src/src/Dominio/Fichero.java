
package Dominio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
* La clase Fichero hereda de la clase Clasificador. Esta clase actua como el "contexto" del patrón estrategia
*/
public class Fichero extends Clasificador{ //la clase contexto del patron estrategia
    /**
     * Indica el tipo del fichero, puede ser .txt o .ppm
     */
    private String tipo;
    /**
     * Indica el algoritmo, puede ser lz78, lzss, lzw, jpeg
     */
    private Algoritmo estrategia;
    /**
     * Indica la calidad de la imagen, por defecto es 100
     */
    private int calidad;
    /**
     * Indica la resolución de la imagen, por defecto es 4
     */
    private int dSampling;
    
    /**
     * Método constructor
     */
    public Fichero(File f) throws MyException{
        super(f, f.getAbsolutePath(),f.getName(), f.length());
        this.calidad = 100;
        this.dSampling = 4;
        if(es_txt()) tipo = ".txt";
        else if(es_ppm()) tipo = ".ppm";
        else  tipo = "";//throw new MyException("El tipo de fichero no es valido."); //el primer mensaje de la array -> que esta en MyException
    }
    
    /**
     * Función que devuelve el tipo del fichero.
     * @return Tipo del fichero.
     */
    public String getTipo(){
        return this.tipo;
    }
    
     /**
     * Método para asignar valor del atributo calidad.
     * @param calidad Indica la calidad de la imagen
     */
    public void setCalidad(int calidad){
        this.calidad = calidad;
    }
    
    /**
     * Función que confirma si el fichero es un texto txt
     * @return Devuelve cierto si el fichero es txt, falso en caso contrario.
     */
    private boolean es_txt() throws MyException{
        String r = super.getRuta();
        int n = r.length();
        if(n >= 4)return r.charAt(n-4) == '.' & r.charAt(n-3) == 't' & r.charAt(n-2) == 'x' & r.charAt(n-1) == 't';
        return false;
    }
    
    /**
     * Función que confirma si el fichero es una imagen ppm
     * @return Devuelve cierto si el fichero es ppm, falso en caso contrario.
     */
    private boolean es_ppm() throws MyException{
        String r = super.getRuta();
        int n = r.length();
        if(n >= 4) return r.charAt(n-4) == '.' & r.charAt(n-3) == 'p' & r.charAt(n-2) == 'p' & r.charAt(n-1) == 'm';
        return false;
    }
    
    /**
     * Método para asignar valor del atributo estrategia.
     * @param estrategia Indica el algoritmo.
     */
    public void setEstrategia(Algoritmo estrategia){
        this.estrategia = estrategia;

    }
    
    /**
     * Función que devuelve el contenido de un texto.
     * @return Devuelve contenido de un fichero texto.
     */
    private MyByteCollection ejecutar_estrategia_texto(MyByteCollection contenido_fichero) throws IOException{
    	boolean b = !(estrategia == null); 
    	if(b)System.out.println("estrategia es "+ estrategia.getNombre());
    	else System.out.println("no tiene estrategia");
        return estrategia.comprimir(contenido_fichero);
    }
    
    /**
     * Función que devuelve el contenido de una imagen
     * @return Devuelve contenido de un fichero imagen.
     */
    private MyByteCollection ejecutar_estrategia_imagen(MyByteCollection contenido_fichero, int calidad,int dSampling) throws IOException{
        return estrategia.comprimir(contenido_fichero, calidad,dSampling);
    }
    
    
    /**
     * Método que comprime un fichero.
     * @param contenido_fichero Indica el contenido de un fichero.
     * @return ArrayList<Byte> Indica el contenido del comprimido.
     * @throws MyException
     */
    public ArrayList<Byte> comprimir(ArrayList<Byte> contenido_fichero) throws MyException{
        MyByteCollection comprimido = new MyByteCollection(contenido_fichero);
        if(tipo.equals(".txt")) {
            try {
                comprimido = ejecutar_estrategia_texto(comprimido);
            } catch (IOException ex) {
                throw new MyException("El mensaje de error es :" + ex.getMessage());
            }
        } else if(tipo.equals(".ppm")){
            try {
                comprimido = ejecutar_estrategia_imagen(comprimido,this.calidad,this.dSampling);
            } catch (IOException ex) {
                throw new MyException("El mensaje de error es :" + ex.getMessage());
            }
        }
        return comprimido.getContenido();
    }

	/**
     * Método para asignar valor del atributo dSampling.
     * @param dSampling Indica la resolución de la imagen.
     */
	public void setDSampling(int dSampling) {
		this.dSampling = dSampling;
	}
    
}
