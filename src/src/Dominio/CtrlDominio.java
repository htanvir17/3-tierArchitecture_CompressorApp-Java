package Dominio;

import Persistencia.CtrlPersistencia;
import Presentacion.ConstantesVistas;
import Presentacion.CtrlPresentacion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import Dominio.jpeg.JPEG;
import javafx.util.Pair;
/**
 * Controlador de la capa de dominio encarga de la logica del programa
 * */
public class CtrlDominio {
  	/**
     * Variable que guarda el algoritmo LZ78
     */
    private LZ78 lz78;
     /**
     * Variable que guarda el algoritmo LZSS
     */
    private LZSS lzss;
    /**
     * Variable que guarda el algoritmo LZW
     */
    private LZW lzw;
    /**
     * Variable que guarda el algoritmo JPEG
     */
    private JPEG jpeg;
    /**
     * Controlador de la capa de presentación
     */
    private CtrlPresentacion cp;
    /**
     * Controlador de la capa de datos
     */
    private CtrlPersistencia cd;
    /**
     * Variable que guarda el ObjetoComprimido
     */
    private ObjetoComprimido ob = null;
    /**
     * Variable que indica si se ha de ignorar un proceso
     */
    private boolean ignore = false;
    
    /**
     * Constructora del Controlador del dominio.
     */
    public CtrlDominio(CtrlPresentacion cp) throws Exception{
        
        try{
            this.cd = new CtrlPersistencia();
            this.cp = cp;
            crear_algoritmos();
        }
        catch(Exception e){
            throw new MyException("El mensaje de error es: " + e.toString());
        }
    }
    
     /**
     * Método que crea todos los algoritmos.
     */
    /*Creo una vez todos los algoritmos*/
    private void crear_algoritmos(){
        this.lz78 = LZ78.getInstances();
        this.lzss = LZSS.getInstances();
        this.lzw = LZW.getInstances();
        this.jpeg = JPEG.getInstances();
    }
	
	/**
     * Método que asigna la respuesta decidida por el usuario de si se quiere ignorar una decisión o no.
     * @param ignore
     */
    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }
    
    /**
     * Método que se encarga de inciar el proceso de la compresión y deja el resultado en la ubicación correspondiente.
     * @param rutas Lista que contienes las rutas seleccionadas por el usuario.
     * @param ubicacion String que indica la ruta destina donde se ha de guardar la compresión.
     * @param nom_archivo String que indica el nombre del archivo de la compresión.
     * @param algoritmo String que indica el nombre del algoritmo selecionado por el usuario o por defecto
     * @param calidad valor que indica la calidad del archivo generado (si el archivo es jpeg)
     * @param dSampling Downsampling utilizado durante la compresión (4 o 2 o 0), (si el archivo es jpeg)
     * @param contrasena String que indica la contraseña selccionada con la que se quiere archivar la compresión (por defecto es vacía)
     * @throws Exception Excepcion del problema.
     */
    public void iniciar_compresion(ArrayList<String> rutas,String ubicacion, String nom_archivo, String algoritmo, int calidad,int dSampling, String contrasena)throws Exception{
     
    	 ArrayList<Fichero> lista_ficheros = new ArrayList<>(); //Array de Carpeta y Fichero
    	 
         int n = rutas.size();
         if(n == 0) throw new MyException("No has seleccionado ningun archivo");
         String nueva_ubicacion = ubicacion + "/" + nom_archivo + ".comp";
    	try {
    		cd.crearFicheroDeSalida(nueva_ubicacion); 
         
         //verificacion de si tenemos que agregar la contrasena o no
         if(contrasena.length() == 0)
             cd.escribirByteEnFicheroDeSalida('N');
         else{ 
              cd.escribirByteEnFicheroDeSalida('Y');
              cd.escribirByteEnFicheroDeSalida(contrasena.length());
              cd.escribirStringEnFicheroDeSalida(contrasena);
         }
         
         cd.escribirShortEnFicheroDeSalida(n);
         //FOR de creacion de clasificadores (carpetas + ficheros) segun las rutas recibidas
         int tamano_total_ini = 0;
         for(int i=0; i<n && tamano_total_ini <= 1073741824; ++i){ //no puede superar mas de 1GB = 1073741824 B
             
             File f = new File(rutas.get(i));
             if(f.exists()){ //si en la ruta existe algun fichero analizo
                 tamano_total_ini += f.length();
                 //SI ES FICHERO
                 if(f.isFile()){
                     Fichero ff = new Fichero(f);
                     
                     if(ff.getTipo().equals(".txt")){
                          lista_ficheros.add(ff); //anado el objeto en mi lista que despues en el FOR de compresion tendre que pasar el objeto fichero
                         switch (algoritmo) {
                             //hacemos set segun el algoritmo que haya seleccionado y la estrategia se ejecutara en el FOR de compresion (que esta abajo)
                             case "LZ78":
                            	 ff.setEstrategia(this.lz78);  
                                 break;
                             case "LZW":
                                 ff.setEstrategia(this.lzw);
                                 break;
                             default:
                                 ff.setEstrategia(this.lzss);
                                 break;
                         }
                     }
                     else if(ff.getTipo().equals(".ppm")){
                         lista_ficheros.add(ff); //agrego el objeto en mi lista que despues en el FOR de compresion tendre que pasar el objeto fichero
                         ff.setCalidad(calidad);
                         ff.setDSampling(dSampling);
                         ff.setEstrategia(this.jpeg);
                     }
                     else{
                     	if(!ignore){
                     		System.out.println(f.getAbsolutePath() + "\n entro por que no se ignora ficheros no validos");
                             if(!(ignore = cp.muestraRetornaRespuestaIgnore())) {
                             System.out.println("termino espera");
                         		//si hay error el CtrlDominio tendra que  borrar todo lo que ha comprimido y luego lanzar el error a la capaPersistencia  
                         		cd.cerrarFicheroDeSalida();
                                cd.borrarFicheroDeSalida();
                         		throw new MyException();
                             }
                       }
                     }
                     if(!ignore) cd.escribirStringEnFicheroDeSalida(ff.getNombre());
                 }
                 
                 //SI ES CARPETA
                 else if(f.isDirectory()){
    
                     Algoritmo[] algoritmos = new Algoritmo[2];
                     algoritmos[1]= this.jpeg;
                     
                     switch (algoritmo) {
                         case "LZ78":
                        	 algoritmos[0]= this.lz78; 
                             break;
                         case "LZW":
                             algoritmos[0]= this.lzw;
                             break;
                         default:
                             algoritmos[0]= this.lzss;
                             break;
                     }
                     
                     Carpeta fc = new Carpeta(f, algoritmos, calidad,dSampling);
                     lista_ficheros.addAll(crear_y_devolver_estructura_carpeta(f, algoritmos, calidad ,dSampling,fc)); 
                     ArrayList<Pair<String,Integer>> org = fc.devolver_organizacion();//me devuelve la estructura (el contenido) de la carpeta
                     tamano_total_ini += fc.getTamano();
                     
                     //escribimos el nombre de la carpeta con .c para identificar que es una carpeta, seguido el numero de elementos que tiene
                     cd.escribirStringEnFicheroDeSalida(fc.getNombre() + ".c");
                     cd.escribirShortEnFicheroDeSalida(fc.getN_elementos()); // restriccion: esto nos limita a tener la cantidad de elementos de una carpeta (como maximo puede ser (2^15)-1) ya que ecribimos un short con signo(15bit )  
                     System.out.println("numero de elementos_carpeta:"+fc.getN_elementos());
                     for(Pair<String,Integer> e: org){    
                         cd.escribirStringEnFicheroDeSalida(e.getKey());
                         if(e.getValue() > -1) cd.escribirShortEnFicheroDeSalida(e.getValue());
                     }
                 }
                 else{
                     cd.borrarFicheroDeSalida();
                     throw new MyException("El archivo no es un fichero ni una carpeta.");
                 }
             }
             else{
                 cd.borrarFicheroDeSalida();
                 throw new MyException("No existe el archivo.");
             }
             
         }
         //Estamos fuera del bucle: en el for anterior hemos averiguado lista_entrada, la organizacion de una carpeta (esta org hemos escrito en el fichero unico)
         
         if(tamano_total_ini > 1073741824){
             cd.borrarFicheroDeSalida();
             throw new MyException("Tamaño máximo excedido, pesa más de 1GB");
         }
         
         //tenemos el numero de rutas del directorio y la organizacion completa
         //FOR DE COMPRESION
         
         //guardo los separadores, que al final tendre que escribir (cuando los algoritmos hayan escrito)
         
         int tam_fichero = cd.getTamano();
         ArrayList<Integer> control_separadores = new ArrayList<>();
         control_separadores.add(tam_fichero);
         int aux = tam_fichero;
         long tiempo_inicial = System.currentTimeMillis(); //para saber cuanto tarda la compresion
         String ruta_relativa = null;
         ArrayList<Byte> contenidoFichero,contenidoComprimido;
         int tamano_ini_deducido_por_ficheros = 0;
         for(int i=0; i<lista_ficheros.size(); ++i){
        	  	 tamano_ini_deducido_por_ficheros = (int) (tamano_ini_deducido_por_ficheros + lista_ficheros.get(i).getTamano());
        	     //Requisito era tratar un fichero a la vez y lo cumplimos
        		 //Solo tratamos el contenido de un fichero a la vez 
	          
        	    System.out.println("dominio: "+(lista_ficheros.get(i).getRuta()));
	        	 
	        	 ruta_relativa = lista_ficheros.get(i).getRuta();
	        	 if(lista_ficheros.get(i).getTipo() == ".ppm") contenidoFichero = cd.LeerImagenPPM(ruta_relativa);
	        	 else contenidoFichero = cd.lectura_fichero_bytes(ruta_relativa);
	        	 
	        	 contenidoComprimido = lista_ficheros.get(i).comprimir(contenidoFichero); //dependiento del objeto (carpeta o fichero) hara su compresion y devolvera un pair (tamanoDeLoQueHaEscrito,Contenido)
	             
	             //guardo los separadores
                 int t = contenidoComprimido.size();
                 control_separadores.add(aux+t);
                 aux += t;
	             //escribo el contenido en el fichero unico
                 cd.escribirBytesEnFicheroDeSalida(contenidoComprimido);
         }
         System.out.println("el tamano inicial es:"+ tamano_ini_deducido_por_ficheros);
         ignore = false;
         //en el fichero tenngo escrito la organizacion y el contenido de las rutas comprimido por los algoritmos; ahora hay que escribir los separadores
         System.out.println("Control de Separadores:"+ control_separadores);
         cd.escribirIntsEnFicheroDeSalida(control_separadores);
         cd.escribirShortEnFicheroDeSalida(control_separadores.size());//el tamano de los separadores,en short, la cantidad total de separadores sera como maximo (2^16)-1 -> en 2B
         long tiempo_final = System.currentTimeMillis();
         
         System.out.println("tiempo de compresion: " + (tiempo_final-tiempo_inicial) );
         //cd.cerrarFicheroDeSalida();
         
         
         File f1 = new File(nueva_ubicacion);
         
         ObjetoComprimido oc = new ObjetoComprimido(f1);
         
         Date d = new Date();
         
         MedidaEstadistica me = new MedidaEstadistica(d, tiempo_final-tiempo_inicial, tamano_total_ini, (int)f1.length(), oc);
         
         cd.guardarMedidaEstadistica(me.getFecha(), me.getTiempo(), me.getTamano_inicial(), me.getTamano_final(), me.getRatio_de_compresion(), me.getVelocidad_de_compresion());
         
	} catch (Exception e) {
		throw e;
	}finally {
		cd.cerrarFicheroDeSalida();
	}
 }
     
    
    /**
     * Función que retorna si el comprimido es un comprimido válido. Se acepta solament comprimido que acaben en .comp
     * @return Boolea. Es cierto si el comprimido acaba en .comp, falso en caso contrario.
     */
    public boolean es_comprimido_valido(File comprimido) throws MyException{
        
        String p = comprimido.getAbsolutePath();
        int n = p.length();
        //el objeto comprimido tiene que ser .comp
        if(p.charAt(n-5) != '.' && p.charAt(n-4) != 'c' && p.charAt(n-3) != 'o' && p.charAt(n-2) != 'm' && p.charAt(n-1) != 'p') {
            return false;
        }
        return true;
    }
	
	/**
     * Función que comprueba si un comprimido tiene una contraseña.
     * @return Boolea. Es cierto si el comprimido tiene contraseña, falso en caso contrario.
     */    
    public boolean comprimido_tiene_contrasena(String ruta_comprimido) throws MyException, FileNotFoundException, IOException{
    	return cd.comprimidoTieneContrasena(ruta_comprimido);
    }
    
   /**
     * Función que comprueba si la contraseña introducida es válida o no. También la contraseña según la función de hash.
     * @return Pair<Boolean,String>. El primer campo indica si la contraseña es válida o no. El segundo campo indica la contraseña codificada o vacio.
     */
    public Pair<Boolean,String> es_contrasena_valida(String ruta_comprimido, String password) throws FileNotFoundException, IOException{
    	String password_hashed = cd.devuelveComprimidoContrasena(ruta_comprimido);
    	if(Password.checkPassword(password, password_hashed)) {
    		return new Pair<Boolean, String>(true,password_hashed);
    	}
    	else return new Pair<Boolean, String>(false,"");
    }
    
    
    
    /**
     * Método que se encarga de inciar el proceso de la descompresión y deja el resultado en la ubicación correspondiente.
     * @param ruta_comprimido Indica la ruta del comprimido.
     * @param ubicacion String que indica la ruta destina donde se ha de guardar la descompresión.
     * @param nombre_raiz String que indica el nombre del archivo de la descompresión.
     * @param tam_hasta_contrasena Indica el número a partir del cual se ha de iniciar la descompresrion
     * @throws IOException,Exception Error.
     */
    public void iniciar_descompresion(String ruta_comprimido, String ubicacion, String nombre_raiz, int tam_hasta_contrasena) throws IOException, Exception{ //por ahora aceptamos un objeto comprimido (una ruta)
    /*
    primero leemos del objeto comprimido los separadores
    se ira generando el sistema de ficheros y cuando se encuentre un fichero se descomprime
    despues tiene que coger toda la lista de separadores
    se pensara crear ob comprimido con path que va creadando el dominio
    */  
    	
        File f_principal = new File(ruta_comprimido);
        if(!es_comprimido_valido(f_principal))throw  new MyException("El tipo de objeto comprimido no es adecuado para descomprimir.");
        
        ///Primero coger la lista de separadores
        
        ArrayList<Integer> separadores = cd.retornaListaSeparadoresComprimido(ruta_comprimido);
        System.out.println("lista de separadores: \n"+ separadores);
        ArrayList<Pair<Integer,String>> estructura = cd.leeEstructuraComprimido(ruta_comprimido);
        System.out.println(estructura);
        ArrayList<String> ficheros = creaEstructura(ubicacion+"/"+nombre_raiz,estructura);

        int n_ficheros = ficheros.size();
        ObjetoComprimido oc = new ObjetoComprimido(f_principal);
        ArrayList<Byte> contenido_fichero_descomprimido = null;
        System.out.println("ficheros size: " + n_ficheros);
        boolean es_texto = true;
        for(int i = 0 ; i < n_ficheros; i++ ) {
        	System.out.println(ficheros.get(i));
        	ArrayList<Byte> contenido_fichero_comprimido = cd.lectura_fichero_bytes_intervalo(ruta_comprimido,separadores.get(i),separadores.get(i+1)); 	
        	switch((char)(byte)contenido_fichero_comprimido.get(0)){
            case '8':
               oc.setEstrategia(this.lz78);
               break;
            case 'S':
                oc.setEstrategia(this.lzss);
                break;
            case 'W':
                oc.setEstrategia(this.lzw);
                break;
            case 'G':
            	es_texto = false;
                oc.setEstrategia(this.jpeg);
                break;
            default:
            	System.out.println((char)(byte)contenido_fichero_comprimido.get(0));
                throw new MyException("Algoritmo no identificado.");
        	}
        	
        	
        	contenido_fichero_descomprimido = oc.descomprimir(contenido_fichero_comprimido);
        	//System.out.println(contenido_fichero_descomprimido);
        	if(es_texto) {
        			cd.escribeBytes(ficheros.get(i),contenido_fichero_descomprimido);
        	}else {
        		es_texto = true;
        		cd.GuardarImagenPPM(ficheros.get(i), contenido_fichero_descomprimido);		
        	 }
        }
    }
     /**
     * Función que crea la estructura completa de un comprimido.
     * @param ubicacion Indica la ruta del comprimido.
     * @param estructura Lista de pareja que indica la estructura que se ha de crear.
     * @return ArrayList<String> Retorna los paths de los ficheros encontrados al momento de crear.
     * @throws IOException Error en el flujo de entrada/salida
     * */
    private ArrayList<String> creaEstructura(String ubicacion,ArrayList<Pair<Integer, String>> estructura) throws IOException {
    	cd.crearCarpeta(ubicacion);
    	return creaEstructura_rec(ubicacion, estructura,1,estructura.get(0).getKey()).getValue();
    }  
    
     /**
     * Función que crea la estructura completa de un comprimido recursivamente.
     * @param raiz Indica el path de la raíz..
     * @param estructura Lista de pareja que indica la estructura que se ha de crear.
     * @param pos Indica el numero de ficheros
     * @param n_elementos Indica numero de elementos de la estructura
     * @return Pair<Integer,ArrayList<String>> Retorna unicamente los paths de los ficheros creados. 
     * @throws IOException Error en el flujo de entrada/salida
     * */
    private Pair<Integer,ArrayList<String>> creaEstructura_rec(String raiz,ArrayList<Pair<Integer, String>> estructura,int pos,int n_elementos) throws IOException {
    	ArrayList<String> ficheros = new ArrayList<>();
    	for(int i = 0 ;i < n_elementos;i++) {
    		  String s = raiz+"/"+estructura.get(pos).getValue();
			  if(estructura.get(pos).getKey() == -1) {//Fichero 
				  ficheros.add(s);
				  cd.crearFichero(s);
				 ++pos;
			  }
			  else {//Carpeta
				  cd.crearCarpeta(s);
				  Pair<Integer,ArrayList<String>> p = creaEstructura_rec(s, estructura, pos + 1, estructura.get(pos).getKey());
				  pos = p.getKey();
				  ficheros.addAll(p.getValue());
			  }
		  }
    	return new Pair<Integer, ArrayList<String>>(pos,ficheros);
	}
    
    /**
     * Crea la estructura del sistema de ficheros al interior de una carpeta (Clasificador{Carpeta,Fichero}).
     * @param f Indica el file de un fichero o carpeta.
     * @param estrategias Indica los algoritmos.
     * @param calidad valor que indica la calidad del archivo generado (si el archivo es jpeg)
     * @param dSampling Downsampling utilizado durante la compresión (4 o 2 o 0), (si el archivo es jpeg)
     * @param c Indica instancia de una carpeta.
     * @return ArrayList<Fichero> Retorna una lista de las instancias de la clase Fichero creadas. 
     * @throws IOException,MyException
     * */
    private ArrayList<Fichero> crear_y_devolver_estructura_carpeta(File f, Algoritmo estrategias[], int calidad,int dSampling, Carpeta c) throws MyException, IOException{
        ArrayList<Fichero> lista_ficheros = new ArrayList<>();
       
        File elementos[] = f.listFiles(); 
        for (File elemento : elementos) {
            if (elemento.isFile()) {
                c.setTamano(elemento.length()+c.getTamano());
                Fichero aux_fi = new Fichero(elemento);
                if(aux_fi.getTipo().equals(".txt")){
                    aux_fi.setEstrategia(estrategias[0]);
                    c.addClasificador(aux_fi);
                    lista_ficheros.add(aux_fi);
                }
                else if(aux_fi.getTipo().equals(".ppm")){
                    aux_fi.setCalidad(calidad);
                    aux_fi.setDSampling(dSampling);
                    aux_fi.setEstrategia(estrategias[1]);
                    c.addClasificador(aux_fi);
                    lista_ficheros.add(aux_fi);
                }
                else{
                	if(!ignore){
                		System.out.println(f.getAbsolutePath() +" /n entro por que no se ignora ficheros no validos");
                        if(!(ignore = cp.muestraRetornaRespuestaIgnore())) {
                        System.out.println("termino espera");
                    		//si hay error el CtrlDominio tendra que  borrar todo lo que ha comprimido y luego lanzar el error a la capaPersistencia  
	                        cd.cerrarFicheroDeSalida();
	                        cd.borrarFicheroDeSalida();
                    		throw new MyException();	
                        }
                    }
                }
            } else if (elemento.isDirectory()) {
                Carpeta carp = new Carpeta(elemento, estrategias, calidad,dSampling);
                ArrayList<Fichero> aux = crear_y_devolver_estructura_carpeta(elemento, estrategias, calidad,dSampling,carp);
                c.setTamano(c.getTamano()+carp.getTamano());
                c.addClasificador(carp);
                lista_ficheros.addAll(aux);
                
            } else {
            	cd.cerrarFicheroDeSalida();
                cd.borrarFicheroDeSalida();
                throw new MyException("Archivo desconocido");
            }
        }
        
        return lista_ficheros;
    }
    
    /**
     * Función que calcula el tamaño total de un conjunto de rutas, que representan carpetas o ficheros.
     * @param rutas Indica un conjunto de paths.
     * @return long Retorna una cantidad que indica el tamaño de unos archivos. 
     * */
	public long calcularTamano(ArrayList<String> rutas) {
		File f ;
		int tamano_total = 0;
		for(String p: rutas) {
			f = new File(p);
			if(f.isDirectory()) {
				tamano_total +=  carpetaTamano(f);
			}
			else if(f.isFile()) {
				
				tamano_total += (f.length());
			}
		}
		return tamano_total;
	}
	/**
     * Función que calcula el tamaño de una carpeta pasada por párametro.
     * @param f Indica una carpeta.
     * @return long Retorna el tamaño de una carpeta. 
     * */
	private long carpetaTamano(File f) {
		File elementos[] = f.listFiles();
		int tamano = 0;
		for (File elemento : elementos) {
            if (elemento.isFile()) {
            	tamano += elemento.length();
            }else if(elemento.isDirectory()){
            	tamano += carpetaTamano(elemento);
            }
        }
		return tamano;
	}
	
	/**
     * Función que retorna una contraseña codificada según la función de hash.
     * @param password Indica una contraseña
     * @return String Retorna la contraseña pasada por párametro codificada.
     * */
	public String codificarContrasena(String password){
		return Password.hashPassword(password);
	}

	/**
     * Método que iniciar el rollback de una descompresión.Es decir borra todo el contenido de la descompresión.
     * @param ruta_raiz Indica paths de un descomprimido.
     * */
	public void rollback_descompresion(String ruta_raiz) {
		cd.borrarRuta(ruta_raiz);
	}
	
	/**
     * Función que retorna el contenido de un fichero del texto.
     * @param ruta Indica ruta de un fichero.
     * @return String Retorna el contenido.
     * @throws IOException
     * */
	public String obtenerContenidoTexto(String ruta) throws IOException {
		return cd.obtenerContenidoTexto(ruta);
	}
	
	/**
     * Método que iniciar el rollback de una compresión. Es decir borra todo el contenido de la compresión.
     * */
	public void roollback_compresion() {	
		cd.borrarFicheroDeSalida();
	}

	/**
     * Función que retorna el contenido de una imagen.
     * @param ruta Indica ruta de un fichero de imagen.
     * @return byte[] Retorna el contenido.
     * @throws IOException
     * */
	public byte[] obtenercontenidoImagen(String ruta) throws IOException {
		return cd.obtenerContenidoImagenParaVisualizar(ruta);
	}

	/**
     * Función que retorna la información de la ayuda.
     * @return String Retorna el contenido.
     * */
	public String getAyudas() {
		return cd.getAyudas();
	}
}
