package Persistencia;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import Dominio.MyByteCollection;

public class PPMSaver {
    /**
     * Escritura de una imagen .ppm
     * @param matrix Matriz de 3 dimensiones con los pÃ­xeles de la imagen.
     * @param path Path del fichero a generar
     */
    public static void writePPM(final String path, ArrayList<Byte> contenido){
        //Saves the image as a .ppm file with the selected path
    	
    	MyByteCollection f = new MyByteCollection(contenido);
        final File output = new File(path);
        try {
            final DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(output)));
            //Write PPM Head
            out.write("P6\n".getBytes());
            int h = f.readInt();
            int w = f.readInt();
            out.write((Integer.toString(w) + " ").getBytes()); 
            out.write((Integer.toString(h) + "\n").getBytes());
            out.write((Integer.toString(255) + "\n").getBytes());
            
            //Write image pixels to the file
            for(int i = 0; i < h; ++i){
                for(int j = 0; j < w; ++j){
                    for (int k = 0; k < 3; ++k) out.write(f.readInt());
                }
            }
            out.close();
        } catch (final IOException ex) { System.err.println(ex); } 
    }

    /**
     * lectura de una imagen .ppm
     * @param input Fichero .ppm
     * @return Matriz de 3 dimensiones con los pÃ­xeles de la imagen
     */
    public static ArrayList<Byte> readPPM(final String ruta){
        MyByteCollection result = new MyByteCollection();
        try{	
        	/*
        	FileInputStream file = new FileInputStream(ruta);
    		InputStreamReader input = new InputStreamReader(file);
    		BufferedReader in = new BufferedReader(input);
        	*/
        	/*
        	 * ERROR: El lector utilitzat només funciona amb certs ordinadors:
        	 * Els valors de certs píxels no es llegeixen correctament. El seu valor es dispara molt
        	 * per sobre de 255, que hauria de ser el valor màxim possible. En altres ordinadors no 
        	 * trobem aquest problema, i els valors es llegeixen correctament. En cas de trobar una
        	 * solució abans de la presentació, es canviarà per poder tenir una execució correcta a
        	 * qualsevol sistema.
        	 */
        	final DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(ruta)));
           
    		in.readLine(); // We read the first line (Should be "P6")
            final String st = in.readLine(); // We read the first line (Should be the image dimensions)
            System.out.println("Maximum value: " + in.readLine()); // Read next line (Should be the maximum value of a pixel)

            final String[] vec =  st.split(" ");
            final int w = Integer.valueOf(vec[0]);
            final int h = Integer.valueOf(vec[1]);
            result.writeInteger(h);
            result.writeInteger(w);
            for(int i=0; i < h; ++i){
            	for (int j = 0; j<w; ++j) {
            		int r = in.read();
            		int g = in.read();
            		int b = in.read();
            		result.writeInteger(r); //R
                    result.writeInteger(g); //G
                    result.writeInteger(b); //B
                    //System.out.print(r + " " + g + " " + b + " | ");  //Descomentar per observar l'error en la lectura de valors
            	}
            	//System.out.println();  //Descomentar per observar l'error en la lectura de valors
            }
            in.close();
            return result.getContenido();
        }
        catch (final IOException ex) { 
            //System.err.println(ex);
            System.err.println("ERROR: La ruta especificada no existe");
            return null;
        } 
    }
}