package Persistencia;

import Dominio.MyException;
import javafx.util.Pair;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.imageio.ImageIO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Controlador de la capa de persistencia encargada de la obtención y escritura de datos.
 * */
public class CtrlPersistencia {
    /**
     * Indica una referencia al comprimido
     */
    private RandomAccessFile comprimido;
    /**
     * variable que guarda la ruta del comprimido
     */
    private String ruta_comprimida;
    /**
     * Ruta donde se guarda los datos de la estadistica
     */
    private String fileME = "./medidasEstadisticas.txt";
    
    /**
     * Contiene todos las ayudas almacenadas en el sistema
     */
    private Collection<Ayuda> ayudas;
    
    /**
     *Método constructor
     */
    public CtrlPersistencia() throws Exception{ 
    	ayudas = new ArrayList<>();
   	    new DatosAyudas(ayudas);
   
    }
    /**
     * Método que crea el comprimido.
     * @param ubicacion Indica la ruta del comprimido.
     * @throws IOException Error en el flujo de entrada/salida
     */
    public void crearFicheroDeSalida(String ubicacion) throws IOException {  
        this.ruta_comprimida = ubicacion;
        System.out.println("La ruta pasada es"+ ubicacion);
        crearFichero(ubicacion);
        comprimido = new RandomAccessFile(ubicacion, "rw"); //doy permiso para escribir en el fichero creado
    }
    /**
     * Método que escribe un short en el comprimido.
     * @param n Indica el número a escribir en short.
     * @throws IOException Error en el flujo de entrada/salida
     */
    public void escribirShortEnFicheroDeSalida(int n) throws IOException{
        comprimido.writeShort(n);
    }
    /**
     * Método que escribe un byte en el comprimido.
     * @param n Indica el número a escribir en byte. 
     * @throws IOException Error en el flujo de entrada/salida
     */
    public void escribirByteEnFicheroDeSalida(int n) throws IOException{
        comprimido.writeByte(n); 
    }
    
     /**
     * Método que escribe un array de integer en el comprimido.
     * @param contenido Indica los números a escribir en int. 
     * @throws IOException Error en el flujo de entrada/salida
     */
    public void escribirIntsEnFicheroDeSalida(ArrayList<Integer> contenido) throws IOException{
    	int size = contenido.size();
    	for(int i=0; i< size; ++i){
    		  comprimido.writeInt(contenido.get(i));
        }
    }
    
    /**
     * Método que escribe un String en el comprimido.
     * @param s Indica la cadena de caracteres a escribir en byte.
     * @throws IOException Error en el flujo de entrada/salida 
     */
    public void  escribirStringEnFicheroDeSalida (String s) throws IOException{
        comprimido.writeBytes(s);
    }
    /**
     * Función que devuelve el tamaño del comprimido
     * @return Devuelve el tamaño del comprimido
     * @throws IOException Error en el flujo de entrada/salida
     */
    public int getTamano() throws IOException{
        return (int)comprimido.length();
    }
    /**
     * Método que escribe bytes en el comprimido.
     * @param contenido Indica los bytes a escribir. 
     */
    public void  escribirBytesEnFicheroDeSalida (ArrayList<Byte> contenido) throws IOException{
    	int size = contenido.size();	
    	for(int i = 0; i< size; i++){
    		comprimido.write(contenido.get(i));
    	}
    }
    
   	/**
     * Método que cierra el fichero comprimido.
     * @throws IOException Error en el flujo de entrada/salida
     */
    public void cerrarFicheroDeSalida() throws IOException{
        if(comprimido != null) comprimido.close();
    }
     /**
     * Método que borra el comprimido.
     */
    public void borrarFicheroDeSalida(){
    	System.out.println("path_comprimido:" + ruta_comprimida);
    	Path p = Paths.get(ruta_comprimida);
    	try {
			Files.delete(p);
			System.out.println("se borro");
		} catch (IOException e) {
			System.out.println("o o error");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	/**
     * Método que crear un fichero en una ubicación.
     * @param ubicacion Indica el lugar en el que se ha de crear el fichero 
     * @throws IOException Error en el flujo de entrada/salida
     */    
    public void crearFichero(String ubicacion) throws IOException{
    	System.out.println("Intento crear fichero: "+ ubicacion);
        File f = new File(ubicacion);
        f.createNewFile();//creo un fichero en la ubicacion solicitada por el usuario (es un fichero unico donde ire escribiendo toda la informacion, que despues necesitare para descomprimir)
    }
    
    /**
     * Método que crear una carpeta en una ubicación.
     * @param ubicacion Indica el lugar en el que se ha de crear la carpeta 
     * @throws IOException Error en el flujo de entrada/salida
     */
    public void crearCarpeta(String ubicacion) throws IOException {  
        File  c = new File(ubicacion);
        c.mkdir();
    }
    /**
     * Método que escribe el contenido de un descomprimido.
     * @param ubicacion Indica el lugar en el que se ha de crear la carpeta 
     * @throws IOException Error en el flujo de entrada/salida
     */
    public void escribirDescomprimido(String ruta,ArrayList<Character> d) throws IOException{
        RandomAccessFile descomprimido = new RandomAccessFile(ruta, "rw");
        for (int j = 0; j < d.size(); j++)
            descomprimido.writeByte(d.get(j));
        descomprimido.close();
    }
    
    /**
     * Método que guarda los valores de la estadistica.
     * @param fecha
     * @param tiempo
     * @param tamano_inicial
     * @param tamano_final
     * @param ratio_de_compresion
     * @param Velocidad_de_compresion
     * @throws IOException, MyException
     */
    public void guardarMedidaEstadistica(Date fecha, long tiempo, int tamano_inicial, int tamano_final, double ratio_de_compresion, float Velocidad_de_compresion)throws IOException, MyException{
        String s = "Fecha: "+fecha+ " Tiempo: "+tiempo+" Tamaño inicial: "+tamano_inicial+" Tamaño final: "+tamano_final+" Ratio de compresión: "+ratio_de_compresion+" Velocidad de compresión: "+Velocidad_de_compresion;
            RandomAccessFile raf = new RandomAccessFile(fileME, "rw");
            raf.seek(new File(fileME).length());
            raf.writeBytes(s);
            raf.writeBytes("\r\n");
            raf.close();
    }
    /**
     * Función que comprueba si el comprimido tiene una contraseña.
     * @param ruta_compri Indica la ruta del comprimido.
     * @return Devuelve cierto si el comprimido tiene una contraseña, falso en caso contrario.
     * @throws IOException
     */
	public boolean comprimidoTieneContrasena(String ruta_compri) throws IOException {
		File f_principal = new File(ruta_compri);
        RandomAccessFile raf = new RandomAccessFile(f_principal, "r");  
        raf.seek(0);
        char c = (char)raf.readByte();
        raf.close();
        return (c == 'Y');
	}    
    
    /**
     * Función que devuelve la contraseña codificada del comprimido.
     * @param ruta_compri Indica la ruta del comprimido.
     * @return Devuelve la contraseña codificada del comprimido.
     * @throws IOException
     */
	public String devuelveComprimidoContrasena(String ruta_comprimido) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(ruta_comprimido, "r");  
		raf.seek(0);
		String s = new String();
		char c = (char)raf.readByte();
		if(c == 'N') s = "";
		else {
			int tam_contra = (int)raf.readByte(); 
	        for(int i=0; i<tam_contra; ++i) {
	        	s = s.concat(String.valueOf((char)raf.readByte()));
	        }
		}    
        raf.close();
        return s;
	}
	/**
     * Función que devuelve el contenido leido byte a byte 
     * @param ruta Indica la ruta de un fichero.
     * @return Devuelve la contraseña codificada del comprimido.
     * @throws IOException
     */
	public ArrayList<Byte> lectura_fichero_bytes(String ruta) throws IOException {
		RandomAccessFile rf = new RandomAccessFile(ruta, "r");
		ArrayList<Byte> leido = new  ArrayList<>();
		for(int i = 0; i <(int)rf.length(); i++) {
			leido.add(rf.readByte());
		}
		rf.close();
		return leido;
	}
	
	/**
     * Método que se encarga de escribir el contenido en una ruta.
     * @param ruta Indica la ruta de un fichero.
     * @param contenido Indica los datos a escribir.
     * @throws IOException
     */
	public void escribeBytes(String ruta, ArrayList<Byte> contenido) throws IOException {
		 RandomAccessFile destino = new RandomAccessFile(ruta, "rw");
	        for (int j = 0; j < contenido.size(); j++)
	            destino.writeByte(contenido.get(j));
	        destino.close();
	}
	/**
     * Función que devuelve la lista de los separadores de un comprimido.
     * @param ruta_comprimido Indica la ruta de un comprimido.
     * @return Devuelve los separadores.
     * @throws IOException
     */
	public ArrayList<Integer> retornaListaSeparadoresComprimido(String ruta_comprimido) throws IOException {
		RandomAccessFile rf = new RandomAccessFile(ruta_comprimido, "r");
		int tam_ob = (int)rf.length();
        rf.seek(tam_ob-2);
        int n_sep = rf.readShort();
        System.out.println("numero de separadores:" + n_sep);
        rf.seek((tam_ob-2)-(n_sep*4));
		ArrayList<Integer> separadores = new ArrayList<>();
        for(int i=0; i< n_sep; ++i){
            separadores.add(rf.readInt());
        }
        rf.close();
		return separadores;
	}
	/**
     * Función que lee y devuelve la estructura del comprimido.
     * @param ruta_comprimido Indica la ruta de un comprimido.
     * @return Devuelve una lista con los nombres de los ficheros o carpetas (la estructura) del comprimido.
     * @throws Exception
     */
	public ArrayList<Pair<Integer, String>> leeEstructuraComprimido(String ruta_comprimido) throws Exception {
		// TODO Auto-generated method stub
		RandomAccessFile raf = null;
		try {
			raf= new RandomAccessFile(ruta_comprimido, "r");
			int size_hasta_password = devuelveComprimidoContrasena(ruta_comprimido).length();
			if(size_hasta_password == 0) size_hasta_password++;
			else size_hasta_password += 2;
			raf.seek(size_hasta_password);
			int n_elementos_raiz = raf.readShort();
			ArrayList<Pair<Integer, String>> estructura = new ArrayList<>();
			estructura.add(new Pair<Integer, String>(n_elementos_raiz,""));	//Este primer guarda el numero de clasificadores raiz.
			estructura.addAll(leer_estructura_comprido_recursivo(raf, n_elementos_raiz));
			return estructura;
		}catch (Exception e) {
			throw e;
		}finally {
			if(raf!=null) raf.close();
		}
	}
	
    
    /**
     * Función que lee y devuelve la estructura del comprimido recursivamente.
     * @param raf Apunta al fichero comprimido.
     * @param n_elementos Indica número de elementos de la carpeta (o raíz)
     * @return Devuelve una lista con los nombres de los ficheros o carpetas (la estructura) que contiene el comprimido.
     * @throws FileNotFoundException, IOException, Exception
     */
    private ArrayList<Pair<Integer, String>> leer_estructura_comprido_recursivo(RandomAccessFile raf,int n_elementos) throws FileNotFoundException, IOException, Exception{
    	ArrayList<Pair<Integer, String>> estructura = new ArrayList<>();	
            for(int i=0; i<n_elementos; ++i){
                StringBuffer nombre = new StringBuffer();
                byte b;
                while((char)(b =raf.readByte()) != '.'){
                    nombre.append((char)b);
                }
                System.out.println("nombre:"+ nombre);
                
                //comprobar el tipo (despues del punto que es lo que viene)
                if((char)(b =raf.readByte()) == 'c'){
                    int n_e = (int)raf.readShort();
                    estructura.add(new Pair<Integer, String>(n_e,nombre.toString()));
                    estructura.addAll(leer_estructura_comprido_recursivo(raf,n_e));
                }
                
                //si es un fichero
                else if( ((char)b == 't') || ((char)b == 'p')){
                        nombre.append('.');
                        nombre.append((char)b);   
                        b =raf.readByte();
                        nombre.append((char)b);
                        b =raf.readByte();
                        nombre.append((char)b);
                        estructura.add(new Pair<Integer, String>(-1,nombre.toString()));
                }
                else {
                	throw new MyException("No soy carpeta ni fichero");
                }
            } 
      return estructura;
    }
	  
	/**
      * Función que lee el contenido de un comprimido en un rango indicado.
      * @param ruta Indica la ruta del comprimido.
      * @param pos_ini Indica la posición desde se inciar la lectura.
      * @param pos_final Indica la posición hasta donde se ha de leer.
      * @return Devuelve el contenido en bytes.
      * @throws Exception
      */   
	public ArrayList<Byte> lectura_fichero_bytes_intervalo(String ruta, int pos_ini, int pos_final) throws Exception {
		ArrayList<Byte> contenido = new ArrayList<>();
		RandomAccessFile raf = null;
		try {
			raf= new RandomAccessFile(ruta, "r");
			raf.seek(pos_ini);
			while(pos_ini < pos_final) {
				contenido.add(raf.readByte());
				pos_ini++;
			}
			return contenido;
		}catch (Exception e) {
			throw e;
		}finally {
			if(raf!=null) raf.close();
		}
	}
	
	/**
      * Método que borra ficheros o carpetas que están en una ruta.
      * @param ruta_raiz Indica la ruta en la que se ha inciar el borrado.     
      */
	public void borrarRuta(String ruta_raiz) {
		File f = new File(ruta_raiz);
		if(f.exists()) borrado_rec(f);	
	}
	
	/**
     * Método que borra la estructura de una carpeta recursivamente, o sino borra directamente si es un fichero.
     * @param f Indica el file
     */
	private void borrado_rec(File f) {
		if(f.isDirectory()) {
			for(File f2: f.listFiles()) {
				if(f2.isDirectory()) {
					borrado_rec(f2);
				}
				f2.delete();
			}
		}
		f.delete(); 
	}
	/**
     * Función que retorna el contenido de texto txt.
     * @param ruta Indica la ruta de un fichero de texto.
     * @return Devuelve el contenido de texto.
     */
	public String obtenerContenidoTexto(String ruta) throws IOException {
		
		FileInputStream file = new FileInputStream(ruta);	
		InputStreamReader input = new InputStreamReader(file,"utf-8"); 
		BufferedReader lector = new BufferedReader(input);
		
		
		String texto = new String();
		String aux;
	
		if((aux = lector.readLine()) != null){
			texto = texto.concat(aux);
			while ((aux = lector.readLine()) != null){
				texto = texto.concat("\n" + aux); 
			}
		}			
		System.out.println("tamano de texto:"+ texto.length());
		lector.close();
		return texto;
		
	}
	
	
	
	
	
	/**
	 * Metodo que retorna un byte[] con el contenido de una imagen .ppm convertida a .bpm
	 * @throws IOException 
	 * 
	 */
   public byte[] obtenerContenidoImagenParaVisualizar(String ruta) throws IOException {
	   int numOfRows = 0, numOfCols = 0;
       File f = new File(ruta);
       FileInputStream fileReader = new FileInputStream(f);
       BufferedInputStream bufferedReader = new BufferedInputStream(fileReader);
       String magicnum = "";
       while (true) {
           int b = bufferedReader.read();
           if (b != -1) {
               char c = (char) b;
               if (!Character.isWhitespace(c)) {
                   magicnum += c;
               } else {
                   break;
               }
           } else {
               break;
           }
       }
       char tipus1 =0;
       String altu="";
       String anchu="";
       while((tipus1 =(char)bufferedReader.read())!= ' ') {
           altu+=Character.toString(tipus1);
       }
       altu = altu.trim();
       while((tipus1 =(char)bufferedReader.read())!= '\n') {
           anchu+=Character.toString(tipus1);
       }
       anchu = anchu.trim();

       numOfRows = Integer.valueOf(altu);
       numOfCols = Integer.valueOf(anchu);
       bufferedReader.read();
       BufferedImage imatge = new BufferedImage(numOfRows, numOfCols, BufferedImage.TYPE_INT_RGB);
       ByteArrayOutputStream baos = new ByteArrayOutputStream();
       byte[] imageInByte = null;
       if (magicnum.equals("P6")) {

           for (int y = 0; y < numOfCols; y++) {
               for (int x = 0; x < numOfRows; x++) {
                   int[] RGB = new int[3];
                   RGB[0] = bufferedReader.read();
                   RGB[1] = bufferedReader.read();
                   RGB[2] = bufferedReader.read();
                   Color n = new Color(RGB[0], RGB[1], RGB[2],1);

                   imatge.setRGB(x, y, n.getRGB());
               }
           }
           ImageIO.write(imatge, "bmp", baos);
           baos.flush();
           imageInByte = baos.toByteArray();
       }
       bufferedReader.close();
	return imageInByte;
   }
   /**
    * Función que retorna el contenido de una image ppm.
    * @param ruta Indica la ruta de un fichero de imagem.
    * @return Devuelve el contenido del fichero ppm.
    */
   public ArrayList<Byte> LeerImagenPPM(String ruta) {
	   return PPMSaver.readPPM(ruta);
   }
   
    /**
    * Método que guarda el contenido del fichero ppm.
    * @param ruta Indica la ruta de un fichero de imagem.
    * @param contenido Indica los datos que se ha de guardar.
    */
   public void GuardarImagenPPM(String ruta, ArrayList<Byte> contenido) {
	   PPMSaver.writePPM(ruta, contenido);
   }
   
   /**
   * Función que lee los datos almacenados en disco
   * @param filename
   * @return retorna json con el contenido del fichero
   * @throws Exception 
   */
  public static JSONObject readJsonFile(String filename) throws Exception {  
      FileReader reader = new FileReader(filename);
      JSONParser jsonParser = new JSONParser();
      return (JSONObject) jsonParser.parse(reader);
  }
  
   /**
    * Retorna todos las ayudas del sistema
    * @return json ayudas
    */
@SuppressWarnings("unchecked")
public String getAyudas(){
       JSONObject jsonObj = new JSONObject();
       JSONArray jsonArr = new JSONArray();
       for(Ayuda ayud : ayudas){
           JSONObject jsonAyuda = new JSONObject();  
           jsonAyuda = ayud.toJson();
           jsonArr.add(jsonAyuda);
       }
       jsonObj.put("Ayudas",jsonArr);
       return jsonObj.toString();
   }
   
   
   
}
