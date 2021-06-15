package Persistencia;

import java.util.Objects;
import org.json.simple.JSONObject;

public class Ayuda {

	 /**
     * Id de ayuda
     */
    private int id;
    
    /**
     * titulo de la descripcion
     */
    private String titulo;
    
    /**
     * Descripcion de la ayuda
     */
    private String texto;
    
	
    
    /**
     * Constructora
     * @param jsonAyuda 
     */
    public Ayuda(JSONObject jsonAyuda){
        id = Integer.parseInt(jsonAyuda.get("id").toString());
        titulo = jsonAyuda.get("titulo").toString();
        texto = jsonAyuda.get("texto").toString();
    }

            
    /**
     * Getter de id
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Setter de id
     * @param id 
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter 
     * @return 
     */
    public String getTitulo() {
        return titulo;
    }
    
    /**
     * Setter
     * @param titulo 
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    
    /**
     * Getter 
     * @return 
     */
    public String getTexto() {
        return texto;
    }
    
    /**
     * Setter
     * @param texto 
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }


    /**
     * Retorna ayuda en formato Json
     * @return Ayuda JSONObject
     */
    @SuppressWarnings("unchecked")
	public JSONObject toJson(){
    	JSONObject jsonAyuda = new JSONObject();
        jsonAyuda.put("id",String.valueOf(getId()));
        jsonAyuda.put("titulo",String.valueOf(getTitulo()));
        jsonAyuda.put("texto",String.valueOf(getTexto()));
        return  jsonAyuda;
    }
    	
	
	
}
