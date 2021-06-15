package Dominio;

import java.io.IOException;
import java.util.ArrayList;


public class MyByteCollection {
		
		/**
     	* Variable que guarda el contenido en Byte
     	*/
		private ArrayList<Byte> contenido = null ;
		/**
     	* Variable que indica el indice del ArrayList<Byte>
     	*/
		int indice;
		/**
     	* Constructora del MyByteCollection.
     	*/
	    public MyByteCollection() {
	    	this.contenido = new ArrayList<>();
	    	this.indice = 0;
	    }
	    /**
    	 * Constructora del MyByteCollection que asigna el contenido.
     	* @param contenido Indica el contenido en Byte
     	*/
	    public MyByteCollection(ArrayList<Byte> contenido) {
	        if(contenido == null) this.contenido = new ArrayList<>();
	    	this.contenido = contenido;
	        this.indice = 0;
	    }
	    /**
     	* Funció que retorna el contenido de MyByteCollection
     	* @return Retorna el contenido.
     	*/
	    public ArrayList<Byte> getContenido() {
	        return contenido;
	    }
	    /**
     	* Método que escribe un Byte en el contenido y aumenta el indice del array.
     	* @param b Indica un Byte 
     	*/
	    public void writeByte(Byte b){
	        this.contenido.add(b);
	        ++indice;
	    }
	    /**
     	* Método que escribe un Integer en el contenido y aumenta el indice del array. 
     	* @param v Indica un Integer 
     	*/
	    public void writeInteger(Integer v) throws IOException {
	        Byte b;
	        for (int i = 24; i >=0 ; i = i-8 ){
	            b = (byte)(v >>> i);
	            this.contenido.add(b);
	        }
	        indice += 4; 
	    }
	    
	    /**
     	* Método que escribe un Short en el contenido y aumenta el indice del array. 
     	* @param v Indica un Short
     	*/
	    public void writeShort(Short v) throws IOException {
	        Byte b;
	        for (int i = 8; i >=0 ; i = i-8 ){
	            b = (byte)(v >>> i);
	            this.contenido.add(b);
	        }
	        indice +=2;
	    }
	    
	    /**
     	* Función que devuelve un Byte del contenido.
     	* @return Devuelve un Byte
     	*/
	    public byte readByte(){
	    	++indice;
	        return this.contenido.get(indice-1);
	    }
	    
	    /**
     	* Función que devuelve un Integer del contenido.
     	* @return Devuelve un Integer
     	*/
	    public int readInt( ) throws IOException {
	        
	        int valor,aux;
	        valor = 0;
	        for (int i = 24; i >=0  ; i = i-8 ){
	        	aux = (int) this.contenido.get(indice)& (int)0x000000FF;
	            aux = (aux << i);
	            valor = aux |valor;
	            ++indice;
	        }   
	        return valor;
	    }
	    
	    /**
     	* Función que devuelve un Short del contenido.
     	* @return Devuelve un Short
     	*/
	    public short readShort() throws IOException {
	        //forma 1
	       int aux,valor;
	       valor = 0;
	        for (int i = 8; i >=0 ; i = i-8 ){
	        	aux = (int) this.contenido.get(indice) & (int)0x000000FF;
	            aux = (aux << i);
	            valor = aux |valor;
	            ++indice;
	        }
	        return (short)valor;
	        //forma 2
	        /*int valor;
	        valor = 0;
	        int alta = this.contenido.get(indice) & (int)0x000000FF;
	        alta = alta << 8;
	        ++indice;
	        int baja =  this.contenido.get(indice) & (int)0x000000FF;
	        ++indice;
	        return (short) (alta | baja);*/
	    }
	    
	    /**
     	* Función que devuelve un UnsignedShort del contenido.
     	* @return Devuelve un UnsignedShort
     	*/
	    public int readUnsignedShort() throws IOException {
	    	return readShort() & 0x0000FFFF;
	    	
	    }
	    /**
     	* Función que devuelve el tamaño del contenido.
     	* @return Devuelve el tamaño
     	*/
		public int getSize() {
			return this.contenido.size();
		}
		
		/**
     	* Método que se posiciona en un indice valido del contenido 
     	* @param indice Indica indice
     	*/
		public void setIndice(int indice) {
			if(indice >= 0 && indice < contenido.size()) {
					this.indice = indice;
			}
		}
		
		
		
}
