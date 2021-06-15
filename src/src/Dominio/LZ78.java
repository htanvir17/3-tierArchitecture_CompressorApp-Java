
package Dominio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * LZ78 es una clase Singleton que implementa las deficiciones de la interfice Algoritmo 
 * @author: Tanvir Hossain
 */
public class LZ78 implements Algoritmo{ 

	/**
	 * Atributo privado con el nombre del algoritmo
	 * */
    private static LZ78 instance = null;

	/**
     * Método contructor. Solamente accesible desde la propia clase
     */
    private LZ78(){}
	/**
     * Método que empieza incializacion lazy del singleton y la devuelve una instancia de LZ78.
     * @return Una instanceia de la clase LZ78
     */
    public static LZ78 getInstances(){
        if(instance == null)
            instance = new LZ78();
        return instance;
    }
    
	/**
     * Función que devuelve el nombre del algoritmo.
     * @return Nombre del algoritmo.
     */
     @Override
    public String getNombre() {
        return "LZ78";
    }
    
    
	/**
      * Función que devulve la compresión de un texto.
      * @param origen Indica el contenido a ser comprimido.
      * @return MyByteCollection Indica el contenido comprimido del párametro origen.
      */
    @Override
    public MyByteCollection comprimir(MyByteCollection origen)throws IOException{ 
        /*
        for(int i = 0; i < origen.getSize() ; i++) {
        	System.out.println(origen.readByte());
        }*/
        MyByteCollection destino = new MyByteCollection();
        
            int tam_origen = origen.getSize(); //tengo el tamano del fichero a comprimir
            HashMap<String,Integer> diccionario = new HashMap<> (); // el indice lo defino com integer (admite hasta (2^31)-1), pero yo 
            
            destino.writeByte((byte)'8'); //8 ultimo caracater de mi algoritmo, a la hora de descomprimir hay que saber que algoritmo ha comprimido
            //System.out.println("Byte:"+(byte)'8');
            String prefijo = "";
            int index = 1;
            int code_num;
            byte valor;
            
            int i=0;
            while (i < tam_origen){ //leo byte a byte del objeto fichero y con tam_origen control para leer hasta el final del fichero
                
                valor = origen.readByte();
                char caracter = (char)valor;
                //System.out.println("Byte:"+valor);
                if (diccionario.containsKey(prefijo + caracter))
                    prefijo = prefijo + caracter;
                else{
                    
                    if (prefijo.isEmpty())
                        code_num = 0; //representa que no ha parecido el prefijo anteriormente o no ha encontrado ninguna coincidencia
                    else
                        code_num = diccionario.get(prefijo);
                    
                    destino.writeShort((short)code_num); //escribo el indice en 2B en SHORT
                    destino.writeByte((byte)caracter);   //escribo el caracter en 1B en BYTE
                    //System.out.println("writeShort:"+(short)code_num+"\nwriteByte"+(byte)caracter);
                    diccionario.put(prefijo + caracter, index);
                    ++index;
                    //si pasa el limite reseteo el diccionario
                    if(index > 65535){ diccionario = new HashMap<> ();}
                    prefijo = "";
                }
                
                ++i;
            }   
            //si he acabo de leer todos los caracteres y el prefijo que se ha contruido no es vacio entonces tengo escribirlo (esto sucede al final del contenido a leer) 
            if (!prefijo.isEmpty()) {
                code_num = diccionario.get(prefijo);
                destino.writeShort((short)code_num);
                destino.writeByte((byte)(char)0);
               // System.out.println("writeShort:"+(short)code_num+"\nwriteByte"+(byte)(char)0);
            }
        return destino;
    }
    
    /**
     * Método que no realiza ninguna operación ya que no se corresponde a este algorimo sino al jpeg.
     * @return null Indica el mal diseño o error.
     */
    /*No hace nada, es para el algoritmo JPEG*/
    @Override
    public MyByteCollection comprimir(MyByteCollection origen,int calidad,int dSampling) throws  IOException{
        return null;
    }
    
    /**
     * Función que devulve la descompresión de un texto.
     * @param origen Indica el contenido comprimido a ser descomprimido.
     * @return ArrayList<Byte> Indica el contenido descomprimido.
     */
    /*Para Descomprimir, devuelve un array de CHARACTERES*/
    @Override
    public ArrayList<Byte> descomprimir(MyByteCollection origen) throws  IOException{ 
                     
    	ArrayList<Byte> destino = new ArrayList<>();
            
            int pos_ini = 0;
            int pos_final = origen.getSize();
            System.out.println("pos_final: "+ pos_final);
            if(((char)origen.readByte())!= '8') {
                System.out.println("El documento no fue comprimido con el algoritmo LZ78.");
                return null;
            }   
            ++pos_ini;
            
            HashMap<Integer, String> diccionario = new HashMap<> ();
            int indice, i =1;
            char caracter;
            String nueva_entrada;
            while (pos_ini < pos_final) {/*la parte a descomprimir va desde pos_ini hasta pos_final*/
                //System.out.println("pos_it: " + pos_ini);
                /*leo 2B (que es un short); no puedo leer un short directamente porque si leo read.Short() apartir del (2^15)-1 el numero lo interpretara en negativo, ya que el short lo tiene en cuenta en complemento a 2 -> por tanto tendre que leer como unsigned*/
                indice = (int)origen.readShort() & 0x0000FFFF;
                pos_ini+=2;
                caracter = (char)origen.readByte();
                pos_ini+=1;
                
                if (indice == 0) {
                    destino.add((byte) caracter);
                    diccionario.put(i, String.valueOf(caracter));
                }
                else {
                    nueva_entrada = diccionario.get(indice);
                    if (caracter != '\u0000') nueva_entrada += caracter;
                    
                    for(int l=0; l<nueva_entrada.length(); ++l)
                        destino.add((byte) nueva_entrada.charAt(l));
                    diccionario.put(i, nueva_entrada);
                }
                
                ++i;
            }
        return destino;
    }
}
