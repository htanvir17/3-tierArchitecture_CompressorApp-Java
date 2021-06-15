package Persistencia;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
/**
 * Clase encargada de gestionar el acceso a el fichero de ayudas escrito en JSON
 * */
public class DatosAyudas {

	private static String file = "Ayudas.json";
    

    /**
    * Constructora, se ecnarga de cargar los datos almacenados en disco
    * @param problemas
    * @throws Exception 
    */
   public DatosAyudas(Collection<Ayuda> ayudas) throws Exception{
       File aux = new File("./");
	   System.out.println(aux.getAbsolutePath());
	   try {
           new FileReader(file);
       } catch (FileNotFoundException f) {
           file = "Ayudas.json";
       }
       
       JSONObject jsonObject = CtrlPersistencia.readJsonFile(file);
       JSONArray arrayAyudas = (JSONArray) jsonObject.get("Ayudas");
       for(int i =0; i< arrayAyudas.size(); i++){           
           JSONObject jsonAyuda = (JSONObject) arrayAyudas.get(i);
           Ayuda ayuda = new Ayuda(jsonAyuda);
           ayudas.add(ayuda);
       }
   }
	
	
	
	
	
	
	
	
	
	
	
}
