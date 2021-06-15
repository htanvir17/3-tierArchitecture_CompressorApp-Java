
package Dominio;

import java.util.Date;


/**
    *Esta clase define las medidas estadísticas de una compresión.
    *@autor Kenny Alejandro
*/
public class MedidaEstadistica {
    /**
     * Fecha de la compresión.
     */
    private final Date fecha;
    /**
     * Tiempo(milisegundos)o duración de la compresión.
     */
    private final long tiempo;
    /**
     * Tamaño total(Bytes) de todos los ficheros seleccionados.
     */
    private final int tamano_inicial;
    /**
     * Tamaño(Bytes) del comprimido resultante.
     */
    private final int tamano_final;
    /**
     * Ratio de compresíon (tamano_final/tamano_inicial).
     */
    private final double ratio_de_compresion;
    /**
     * Velocidad de compresíon en Byte por milisegundo (tamano_inicial/tiempo);
     */
    private final float velocidad_de_compresion;//Bytes/milisegundo
    
    /**
     * Instancia del ObjetoComprimido 
     */
    private final ObjetoComprimido oc;
    /**
     * Creadora de una medida estadistica.
     * @param fecha  El parámetro fecha nos indica la fecha de creacíon de la compresión
     * @param tiempo El parámetro tiempo nos indica el tiempo de demora de la compresión.
     * @param tamano_inicial El parámetro tamano_inicial nos indica el tamaño total de los ficheros seleccionados para la compresión.
     * @param tamano_final El parámetro tamano_final nos indica el tamaño del comprido resultante.
     * @param oc El parámetro oc indica la instancia de ObjetoComprimdo a la que hace referencia la Medida Estadisca.
     */
    public MedidaEstadistica(Date fecha, long tiempo, int tamano_inicial, int tamano_final, ObjetoComprimido oc) {
        this.fecha = fecha;
        this.tiempo = tiempo;
        this.tamano_inicial = tamano_inicial;
        this.tamano_final = tamano_final;
        System.out.println("TI: "+ tamano_inicial + "\n TF: " +tamano_final);
        if(tamano_inicial != 0) { 
        	double aux = ((double)tamano_final/(double)tamano_inicial);
        	ratio_de_compresion = (double)(Math.round(aux* 100d) / 100d);
        }
        else ratio_de_compresion =-1 ;
        System.out.println(ratio_de_compresion);
        if(tiempo != 0)  this.velocidad_de_compresion = tamano_inicial/tiempo;
        else this.velocidad_de_compresion = -1;
        this.oc = oc;
        oc.setMedidaEstadistica(this);
    }
    /**
     * Método que retorna la fecha de compresión.
     * @return Retorna la fecha de compresión.
     */
    public Date getFecha() {
        return fecha;
    }
    /**
     * Método que retorna el tiempo de compresión.
     * @return Retorna el tiempo de compresión.
     */
    public long getTiempo() {
        return tiempo;
    }

    /**
     * Método que retorna el tamaño total inicial.
     * @return Retorna el tamaño total inicial.
     */
    public int getTamano_inicial() {
        return tamano_inicial;
    }
    /**
     * Método que retorna el tamaño(Bytes) del comprimido resultante.
     * @return Retorna el tamaño(Bytes) del comprimido resultante.
     */
    public int getTamano_final() {
        return tamano_final;
    }
    /**
     * Método que retorna el ratio de compresíon.
     * @return Retorna el ratio de compresíon.
     */
    public double getRatio_de_compresion() {
        return ratio_de_compresion;
    }
    /**
     * Método que retorna la velocidad(Bytes/milisegundos) de compresíon.
     * @return Retorna la velocidad(Bytes/milisegundos) de compresíon.
    */
    public float getVelocidad_de_compresion() {
        return velocidad_de_compresion;
    }
}