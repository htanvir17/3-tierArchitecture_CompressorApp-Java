package Dominio;

import java.io.IOException;
import java.util.ArrayList;

/**
 * LZSS es una clase Singleton que implementa las deficiciones de la interfice Algoritmo 
 * @author: Kenny Alejandro
 */
public class LZSS implements Algoritmo{
	/**
	 * Atributo privado con el nombre del algoritmo
	 * */
	private String nombre = "LZSS";
	
	/**
	 * Atributo privado del clase que 
	 * */
	private static LZSS instance = null;
    /*constructor*/
	
	/**Constructor privado solo accesible desde la propia clase */
	private LZSS(){} 

	/**Método que inicializa de la forma lazy el singleton.
     * @return Una instancia de la clase LZSS
	 * */
	public static LZSS getInstances(){
    if(instance == null)
        instance = new LZSS();
    return instance;
	}
	
	/**Getter que retorna el nombre del algortimo
	 * @return Retorna el nombre del algortimo LZSS
	 * */
	
	public String getNombre() {
		return this.nombre;
	}	
	//definicion de las constantes
	/**Constante del tamaño del search buffer */
	final private int SIZE_SEARCH_BUFFER = 4096;
	/**Constante del tamaño del lookahead buffer*/
	final private int SIZE_LOOKAHEAD_BUFFER = 16;
	/**
	 * Constante del que representa el tamaño minimo de coincidencia de para ser codificado con una pareja tamaño-offset*/
	final private int MIN_CODED  = 3;
	/**
	 * Constante del que representa el tamaño maximo de coincidencia para ser codificado con una pareja tamaño-offset*/
	final private int MAX_CODED  = 15;//SIZE_LOOKAHEAD_BUFFER - 1 => 16 - 1 = 15
	/** Constante que representa un flag de una coincidencia simple que no es codificada*/
	final private byte FLAG_LITERAL = 0;
	/** Constante que representa un flag de una coincidencia a ser codificada*/
	final private byte FLAG_CODEWORD = 1;

	//Variables globales
	/**Variable global que representa al search buffer que es tratado como un circular buffer*/
	private  char[] searchBuffer = new char[SIZE_SEARCH_BUFFER]; //circular buffer
	/**Variable global que representa al lookahead buffer que es tratado como un circular buffer*/
	private char[] lookaheadBuffer = new char[16];//circular buffer
	/**
	 * Variable global que indica la posicion relativa inicial del search-buffer(circular buffer)
	 * */
	private int search_ini;
	/**
	 * Variable global que indica el tamaño relativo del search-buffer 
	 * */
	private int search_size_act;
	/**
	 * Variable global que indica la posicion relativa inicial del lookahead-buffer(circular buffer)
	 * */
	private int lookahead_ini;
	/**
	 * Variable global que indica el tamaño relativo del looakahead-buffer 
	 * */
	private int lookahead_size_act;
	
	/**
	 * Clase privada de LZSS, que representa a un token
	 * 
	 * @author Kenny Alejandro
	 * */
	
	private class token
	{
		/**
		 * Atributo que representa la codificacion de un token (length,offset) codificado 
		 * solo en 16 bits de menor peso (length en los 4 bits de mayor peso y el offset 
		 * en 12bits de menor peso)
		 * */
		public int codeword;
		/**
		 * Representa a el tamaño del la coencidencia 
		 * */
		public int length; // solo ocupa 4 bits de los 32 que ofrece un int
		/**
		 * Representa la posicion de una match relativa de inicio del search-buffer. 
		 * */
		public int offset; // solo ocupa 12 bits de los 32 que ofrece un int
		/**Representa a los simbolos que contiene el match
		 * */
		public char[] w_match;
	    
		/**Creadora del token.
		 * */
		public token() {
			this.w_match = new char[SIZE_LOOKAHEAD_BUFFER]; 
		}
	
	}
	/**
	 * Método que agrega un caracter a el lookahead-buffer(circular buffer)
	 * @return retorna 0 si se ha podido agregar y -1 si ya estan ocupadas todas las posiciones del lookahead-buffer
	 * */
	private  int insert_circular_lookahead(char c)
	{
		if(lookahead_size_act == SIZE_LOOKAHEAD_BUFFER)return -1; //no se puede agregar si esta llena
	    else{
	        if(lookahead_size_act == 0) lookahead_ini = 0;
	        lookaheadBuffer[((lookahead_ini +(lookahead_size_act -1)) + 1 ) % SIZE_LOOKAHEAD_BUFFER] = c; // cq[(pos_final_cq+1)%maxim_size_cq]
	        lookahead_size_act = lookahead_size_act + 1 ;
	    }
	    return 0;
	}
	/**
	 * Método que retira un caracter del lookahead-buffer(circular buffer)
	 * @return retorna 0 si se ha podido retirar y -1 si no se ha podido 
	 * retirar normalmente este ultimo es cuando el numero de posiciones ocupadas es diferente de 0.
	 * */
	private int delete_circular_lookahead()
	{
	    if(lookahead_size_act == 0) return -1;//no se puede eliminar de una cola vacia
	    if(lookahead_size_act == 1) lookahead_ini = -1;
	    else lookahead_ini = (lookahead_ini+1)%SIZE_LOOKAHEAD_BUFFER;
	    lookahead_size_act = lookahead_size_act - 1;
	    return 0;
	}
	/**
	 * Método que agrega un caracter a el search-buffer(circular buffer)
	 * @return retorna 0 si se ha podido agregar y -1 si ya estan ocupadas todas las posiciones del search-buffer.
	 * */
	private int insert_circular_searchbuffer(char c)
	{
	    if(search_size_act == SIZE_SEARCH_BUFFER)return -1; //no se puede agregar si esta llena
	    else{
	        if(search_size_act == 0)search_ini = 0;
	        searchBuffer[((search_ini +(search_size_act -1)) + 1 ) % SIZE_SEARCH_BUFFER] = c; // cq[(pos_final_cq+1)%maxim_size_cq]
	        search_size_act = search_size_act + 1 ;
	    }
	    return 0;
	}
	/**
	 * Método que retira un caracter del search-buffer(circular buffer)
	 * @return retorna 0 si se ha podido retirar y -1 si no se ha podido 
	 * retirar normalmente este ultimo es cuando el numero de posiciones ocupadas es diferente de 0.
	 * */
	private int delete_circular_searchbuffer()
	{
	    if(search_size_act == 0) return -1;//no se puede eliminar de una cola vacia
	    if(search_size_act == 1) search_ini = -1;
	    else search_ini = (search_ini+1)%SIZE_SEARCH_BUFFER;
	    search_size_act = search_size_act - 1;
	    return 0;
	}
	/**
	 * Método que inicializa el search-buffer
	 */
	private void inicializar_searchBuffer()
	{
		search_ini = 0;
		search_size_act = 0;
	}
	/**
	 * Método encuentra la coencidencia más larga entre el search buffer y el lookahead buffer.
	 * @return Retorna un token con la coeincidencia mas larga encontrada.
	 * */
	//retorna el token del match mas grande encontrado entre search y lookahead
	token findmatch(){
	    token matchData = new token();
	    matchData.length = 0;
	    int i = search_ini;
	    int j = 0;
	    for(int s_it = 0; s_it < search_size_act; s_it++){//s_it controla que solo se realizen comparaciones con los valores validos dentro del searchBuffer
	        if(searchBuffer[i] == lookaheadBuffer[lookahead_ini]){
	            j = 1;
	            int aux_s_it = s_it + 1;//se usa un auxiliar para controlar de que no se salga del tamaño actual del search buffer
	            //se comienza con uno extra por que el while comienza evaluando al siguinte caracter
	            int l_it = 1;//se comienza con uno por que el while comienza evaluando al siguinte caracter al de inicio del lookahead buffer
	            // se usa un iterador para que no se salga del tamaño actual del lookahead buffer, este
	            //tambien servira para cuando el tamaño actual sea igual al tamaño maximo del lookahead buffer y se necesite comprobar que
	            // el tamaño del match(j) no exceda a el tamaño maximo de un match (lookahead_size_act igual a SIZE_LOOKAHEAD_BUFFER)
	            while((aux_s_it < search_size_act) && (l_it < lookahead_size_act)&& (j < lookahead_size_act) &&(j < MAX_CODED)
	                   && searchBuffer[(i+j)% SIZE_SEARCH_BUFFER] == lookaheadBuffer[(lookahead_ini + j)% SIZE_LOOKAHEAD_BUFFER]){
	                l_it++;
	                aux_s_it++;
	                j++;
	            }
	            if (j > matchData.length){
	                matchData.length = j;
	                matchData.offset = s_it;
	                if(matchData.length < MIN_CODED){
	                    for(int k = 0; k < matchData.length; k++) matchData.w_match[k] = lookaheadBuffer[(lookahead_ini+k)%SIZE_LOOKAHEAD_BUFFER];
	                }
	            }
	        }
	        i = (i + 1) % SIZE_SEARCH_BUFFER;
	    }
	    return matchData;
	}
	
	/**Método que comprime los datos de entrada usando el algoritmo LZSS
	 * 
	 * @param origen EL parametró origen representa el contenido a comprimir
	 * @return retorna un MyByteCollection con los datos comprimidos
	 * */	
    public MyByteCollection comprimir(MyByteCollection origen) throws IOException {
		MyByteCollection destino = new MyByteCollection();
    	int tamano_ori = (int) origen.getSize();
		destino.writeByte((byte)'S');
		boolean final_doc_llegit = false;
		lookahead_size_act = 0;
		int bytes_escritos = 0;
		int bytes_leidos = 0;
	    char ch;
	    //inicializacion de lookahead_buffer
	    while(lookahead_size_act < SIZE_LOOKAHEAD_BUFFER && bytes_leidos < tamano_ori) {
	    	 ch = (char) origen.readByte();
		     insert_circular_lookahead(ch);
		     ++bytes_leidos;
	    }
	    inicializar_searchBuffer();
	    token[] lista_tokens = new token[8];
	    for (int d = 0; d < 8; d++) {
	    	lista_tokens[d] = new token();
		}
	    // lista de tokens ,con los flags juntados en otra lista, (codeword,lentgh,offset,char c),si el flag es 1,(codeword,length y offset son validos
	    //Cuando flag es 0 solo vamos a acceder a leer un caracter ya que reutilizamos el vector de char w_match pero solo usaremos la posición w_match[0]
	    int  iterador_lista_tokens = 0;//el tamaño maximo es 16 bytes por que en el pero de los casos todos los tokens tienen flag 1 es decir de par offset/length
	    char[] flags = new char[8];//0 -- 7, flags juntados de 8 en 8, la posicion de flags[0] es la poscion del primer match guardado.
	    
	    byte byte_flags = 0;
	    int num_tokens_guardados = 0;
	    bytes_escritos= 0;
	    while(lookahead_size_act != 0){
	    	token tk = findmatch();
	        if(tk.length < 2)
	        {
	            if(tk.length == 0) {
	            		flags[num_tokens_guardados] = '0';
	                    byte_flags = (byte) (byte_flags << 1);
	                    byte_flags = (byte) (byte_flags | FLAG_LITERAL);//FLAG_LITERAL;
	            		++num_tokens_guardados;
	            		
	            		lista_tokens[iterador_lista_tokens].w_match[0] = lookaheadBuffer[lookahead_ini];
	                    ++iterador_lista_tokens;
	            	}
	            else{

	                for(int i= 0; i < tk.length;i++){
	                    flags[num_tokens_guardados] = '0';
	                    byte_flags = (byte) (byte_flags << 1);
	                    byte_flags = (byte) (byte_flags | FLAG_LITERAL);
	                    ++num_tokens_guardados;
	                    lista_tokens[iterador_lista_tokens].w_match[0] = tk.w_match[i];
	                    ++iterador_lista_tokens;
	                }
	            }
	        }
	        else{
	        	flags[num_tokens_guardados] = '1';
	            byte_flags = (byte) (byte_flags << 1);
	            byte_flags = (byte) (byte_flags | FLAG_CODEWORD);
	            ++num_tokens_guardados;

	            int aux_length = tk.length;
	            int aux_offset = tk.offset;

	            aux_offset = aux_offset & 0x00000FFF; // de los 32 bits solo nos quedamos con los 12 bits de menor peso
	            aux_length = aux_length & 0x0000000F; // de los 32 bits solo nos quedamos con los 4bits de menor peso
	            aux_length = aux_length << 12;//hago shift a la derecha 12 posiciones en bits
	            lista_tokens[iterador_lista_tokens].codeword = aux_length | aux_offset; // solo los 2 bytes de menor peso son los que contienen el codeword
	            //con lo cual al momento de escribir solo se ha de escribir a los 2 bytes de menor peso.
	            lista_tokens[iterador_lista_tokens].length = tk.length;
				lista_tokens[iterador_lista_tokens].offset = tk.offset;
				 ++iterador_lista_tokens;
	        }
	        //mover searchBuffer segun el numero del tamaño de match mas largo encontrado
	        if(tk.length == 0){
	        // excepcion si length es 0 se ha de quitar el caracter de la cabeza del lookahead buffer y agregarlo a el final del searchBuffer
	            if(search_size_act == SIZE_SEARCH_BUFFER){ //solo cuando el searchBuffer esta lleno el se ha de eliminar
	             delete_circular_searchbuffer();
	            }
	            insert_circular_searchbuffer(lookaheadBuffer[lookahead_ini]);
	            delete_circular_lookahead();
	            if(final_doc_llegit == false && bytes_leidos < tamano_ori){
	            	char c = (char)origen.readByte();
	            	insert_circular_lookahead(c);
	                ++bytes_leidos;
	            }
	            if(bytes_leidos == tamano_ori) final_doc_llegit = true;
	        }
	        else {
	            for(int i = 0;i < tk.length; i++){
	                if(search_size_act == SIZE_SEARCH_BUFFER){ //solo cuando el searchBuffer esta lleno el se ha de eliminar
	                    delete_circular_searchbuffer();
	                }
	                insert_circular_searchbuffer(lookaheadBuffer[lookahead_ini]);
	                delete_circular_lookahead();
	                if(final_doc_llegit == false && bytes_leidos < tamano_ori){
	                	char c = (char)origen.readByte();
	                	insert_circular_lookahead(c);
		                ++bytes_leidos;
		            }
		            if(bytes_leidos == tamano_ori) final_doc_llegit = true;
	             }
	        }
	        if (num_tokens_guardados == 8 || lookahead_size_act == 0 ){
	        	//escribe primero el flag seguido por los 8 matches consecutivos
	        	//Imprimir los flags , en el caso de estar guardados dentro de un byte solo se escribe el byte.
	            for(int i = num_tokens_guardados; i < 8 ; i++){
	                //solo entrara si el numero de tokens guardados es menor que 8,esto lo hacemos para que la integridad del orden de los flags se mantenga
	                //,es decir, que el bit de mayor peso del byte_flags corresponda al flag del primer token y el bit de menor peso corresponda a el flag
	                //del octavo y ultimo token
	                flags[i] = '0';
	                byte_flags =(byte) (byte_flags << 1);
	                byte_flags =(byte) (byte_flags | FLAG_LITERAL);
	            }
	            destino.writeByte(byte_flags);
	            ++bytes_escritos;
	        	//En general solo se entra cuando el numero de tokens guardados sera 8 y se escribira siempre 8 pero
	        	// que pasa se acaban los elementos del lookahead_buffer,que se usan para compara, antes de que nuestro
	        	// num de tokens guardados sea 8, en cualquiera de los dos casos se ha de imprimir la cantidad de tokens
	        	// guardados hasta ese momento.
	    		for(int i = 0; i < num_tokens_guardados;i++){
	    			if(flags[i] == '0'){
	    				destino.writeByte((byte)lista_tokens[i].w_match[0]);
	                    ++bytes_escritos;
	    			}else{
	                    //solo se desea escribir los 2 bytes de menor peso del codeword de tipo int
	    				destino.writeShort((short)lista_tokens[i].codeword);
	    				bytes_escritos = bytes_escritos + 2;
	    			}
	    		}
	    		num_tokens_guardados = 0;
	    		iterador_lista_tokens = 0;
	            byte_flags = 0;
	        }
	    }
	    return destino;
	}
    /**Método que descomprime los datos de entrada que han sido comprimidos usando el algoritmo LZSS
	 * 
	 * @param comprimido El parametró comprimido representa el contenido a descomprimir
	 * @return retorna un ArrayList<Byte> con los datos descomprimidos.
	 * */
	public ArrayList<Byte> descomprimir(MyByteCollection comprimido) throws IOException {
	    char[] flags= new char[8];
	    byte byte_flags;
	    inicializar_searchBuffer();
	    boolean final_doc_llegit = false;
	    ArrayList<Byte> buffer_escritura= new ArrayList<>();
	    int pos_ini = 0;
	    int pos_fi = comprimido.getSize();
	    int pos_lectura_bytes = pos_ini;
	    if((char)comprimido.readByte() != 'S') {
	    	System.out.println("Archivo no fue comprimido por el algoritmo LZSS");
			return null;
	    }
	    ++pos_lectura_bytes;
	    if(pos_lectura_bytes == pos_fi) final_doc_llegit = true;
	    while(final_doc_llegit == false){
	    	// optengo todos los flags de un grupo de 8
	    	byte_flags = comprimido.readByte();
	    	++pos_lectura_bytes;
	    	if(pos_lectura_bytes == pos_fi) final_doc_llegit = true;
	    	for(int i = 7; i >= 0; i--){
	            if((byte_flags & 0x01 ) == 1 ){
	    		  flags[i] = '1';
	            }else{
	              flags[i] = '0';
	            }
	            byte_flags = (byte)((int) byte_flags >> 1);
	    	}
	    	for(int i = 0; i < 8 && final_doc_llegit == false; i ++){
	    		if(flags[i] == '0'){
	    			char c = (char) comprimido.readByte();
	    			++pos_lectura_bytes;
	    			if(pos_lectura_bytes == pos_fi) final_doc_llegit = true;
	    			
		    		if(search_size_act == SIZE_SEARCH_BUFFER){ //solo cuando el searchBuffer esta lleno el se ha de eliminar
		            delete_circular_searchbuffer();
		            }
		            insert_circular_searchbuffer(c);
		            buffer_escritura.add((byte)c);
	    		}
	    		else{//leeremos primero la parte mas alta y despues la mas baja
	    			// leeremos el token escrito en short (16 bits)
	    			int code = comprimido.readShort();
	    			//System.out.println("leoShort"+code);
	    			pos_lectura_bytes = pos_lectura_bytes +2;
	                if(pos_lectura_bytes >= pos_fi) final_doc_llegit = true;
                    
	                int length = code >> 12;
	    			length = length & 0x0000000F;
	    			int offset = code & 0x00000FFF;
	    			
                    char[] palabra = new char[length];
                    for(int j = 0; j <length; j++){
                        palabra[j] = searchBuffer[(search_ini+ offset + j)%SIZE_SEARCH_BUFFER];
                        buffer_escritura.add((byte)palabra[j]);
                    }
                    //Despues añado la palabra al searchbuffer
                    for(int k = 0; k < length ; k++){
                        if(search_size_act == SIZE_SEARCH_BUFFER){ //si esta lleno el searchBuffer se ha de eliminar si no
                         delete_circular_searchbuffer();
                        }
                        insert_circular_searchbuffer(palabra[k]);
                    }
	            }
	    	}
	    } 
	    return buffer_escritura;
	}

	/**Método heredado del Algortimo pero que no se implementa alser este destinado hacia algortimos de imagenes. 
	 * @return retorna un MyByteCollection null.
	 * */
	@Override
	public MyByteCollection comprimir(MyByteCollection contenido_fichero, int calidad,int dSampling)
			throws  IOException {
		return null;
	}
}