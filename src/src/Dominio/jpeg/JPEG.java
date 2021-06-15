package Dominio.jpeg;

import Dominio.*;

import Dominio.MyByteCollection;
import Dominio.jpeg.huffman.*;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * JPEG es una clase Singleton que implementa las deficiciones de la clase Algoritmo
 * @author: Lucas Cajal
 */
public class JPEG implements Algoritmo{
    private static JPEG instance = null;
    private JPEG(){ 
    }

    public static JPEG getInstances(){
        if(instance == null)
            instance = new JPEG();
        return instance;
    }
    
    private final static int[][] luminanceMatrix = new int[][]{
        { 16, 11, 10, 16, 24, 40, 51, 61 },
        { 12, 12, 14, 19, 26, 58, 60, 55 },
        { 14, 13, 16, 24, 40, 57, 69, 56 },
        { 14, 17, 22, 29, 51, 87, 80, 62 },
        { 18, 22, 37, 56, 68, 109, 103, 77 },
        { 24, 35, 55, 64, 81, 104, 113, 92 },
        { 49, 64, 78, 87, 103, 121, 120, 101 },
        { 72, 92, 95, 98, 112, 100, 103, 99 }
    };
    private final static int[][] chrominanceMatrix = new int[][]{
        { 17, 18, 24, 47, 99, 99, 99, 99 },
        { 18, 21, 26, 66, 99, 99, 99, 99 },
        { 24, 26, 56, 99, 99, 99, 99, 99 },
        { 47, 66, 99, 99, 99, 99, 99, 99 },
        { 99, 99, 99, 99, 99, 99, 99, 99 },
        { 99, 99, 99, 99, 99, 99, 99, 99 },
        { 99, 99, 99, 99, 99, 99, 99, 99 },
        { 99, 99, 99, 99, 99, 99, 99, 99 }
    };
    
    @Override
    public String getNombre() {
        return "JPEG";
    }

    /**
     * Compresión de una imagen.
     * @param f Indica el fichero .ppm a comprimir.
     * @param quality  Calidad del archivo generado.
     * @param downType  Downsampling utilizado durante la compresión. 4=4:4:4, 2=4:2:2, 0=4:2:0.
     * @return Una Pareja, el primer parámetro indica el número de bytes escritos y el segundo los datos comprimidos.
     */
    public MyByteCollection comprimir(MyByteCollection f, int quality, int downType) throws IOException {
    	final int h = f.readInt();
        final int w = f.readInt();
        int[][][] channelMatrix = new int[h][w][3];
        for (int i = 0; i < h; ++i){
            for (int j = 0; j < w; ++j){
                channelMatrix[i][j][0] = f.readInt();
                channelMatrix[i][j][1] = f.readInt();
                channelMatrix[i][j][2] = f.readInt();
            }
        }
        channelMatrix = RGBtoYUV(channelMatrix); //Convert from RGB to YCbCr

        //Y channel compression
        int[][] y = channelMatrix[0];
        final int [][][] yBlocks = blockSplit(y);
        y = compressionL(quality, yBlocks);

        //U channel compression
        int[][] u = channelMatrix[1];
        u = downsample(u, downType); //Downsample Cb
        final int [][][] uBlocks = blockSplit(u);
        u = compressionC(quality, uBlocks);
        
        //V channel compression
        int[][] v = channelMatrix[2];
        v = downsample(v, downType); //Downsample CR        
        final int [][][] vBlocks = blockSplit(v);
        v = compressionC(quality, vBlocks);

        final int[] yFlat = flattenBlocks(y);
        final int[] uFlat = flattenBlocks(u);
        final int[] vFlat = flattenBlocks(v);
        
        final int[][] data = new int[3][];
        data[0] = yFlat;
        data[1] = uFlat;
        data[2] = vFlat;
        
        final Compressed comp = encode(data);
        comp.setHeight(h);
        comp.setWidth(w);
        comp.setDownType(downType);
        comp.setQuality(quality);

        //Transformación de comprimido a pareja
        final ByteArrayOutputStream bs= new ByteArrayOutputStream();
        final ObjectOutputStream os = new ObjectOutputStream (bs);
        os.writeObject(comp);
        os.close();
        final byte[] bytes = bs.toByteArray();
        
        MyByteCollection result = new MyByteCollection();
        result.writeByte((byte)'G');
        
        for (int i = 0; i < bytes.length; ++i){
            result.writeByte(bytes[i]);
        }
        return result;
    }

    /**
     * Descompresión de una imagen.
     * @param input Objeto de MyByteCollection con los datos del fichero.
     * @return Una Matriz de 3 dimensiones con los valores descomprimidos listos para guardar
     * @throws ClassNotFoundException 
     */
    @Override
    public ArrayList<Byte> descomprimir(MyByteCollection input) throws IOException, ClassNotFoundException{
        if(input.readByte() != 'G') {
            System.out.println("El fichero no es una imagen");
        }
        byte[] bytes = new byte[input.getSize()-1];

        for(int i = 0; i < bytes.length; ++i) {
        	bytes[i] = input.readByte();
        }

        //Creación objeto
        final ByteArrayInputStream bs= new ByteArrayInputStream(bytes);
        final ObjectInputStream is = new ObjectInputStream(bs);
        final Compressed compressed = (Compressed)is.readObject();
        is.close();

        final int h = compressed.getHeight();
        final int w = compressed.getWidth();
        final int quality = compressed.getQuality();
        final int downType = compressed.getDownType();
        
        final int[][] data = decode(compressed);
        final int[] yFlat = data[0];
        final int[] uFlat = data[1];
        final int[] vFlat = data[2];

        int[][] y = unflattenBlocks(yFlat);
        int[][] u = unflattenBlocks(uFlat);
        int[][] v = unflattenBlocks(vFlat);

        final int [][][] yBlocks = decompressionL(quality, y);
        final int [][][] uBlocks = decompressionC(quality, u);
        final int [][][] vBlocks = decompressionC(quality, v);

        y = blockMerge(yBlocks, h, w);
        u = blockMerging(downType, uBlocks, h, w);
        v = blockMerging(downType, vBlocks, h, w);

        u = upsample(u, downType, h, w);
        v = upsample(v, downType, h, w);
        
        int[][][] channelMatrix = new int[3][][];
        channelMatrix[0] = y;
        channelMatrix[1] = u;
        channelMatrix[2] = v;
        channelMatrix = YUVtoRGB(channelMatrix);
        
        MyByteCollection result = new MyByteCollection();
        result.writeInteger(h);
        result.writeInteger(w);
        for (int i = 0; i < h; ++i){
            for (int j = 0; j < w; ++j){
                result.writeInteger(channelMatrix[i][j][0]);
                result.writeInteger(channelMatrix[i][j][1]);
                result.writeInteger(channelMatrix[i][j][2]);
            }
        }
        return result.getContenido();
    }

    /**
     * Compresión Huffman de una imagen
     * @param vector Matriz con los 3 canales de color de la imagen tras aplicar las transformaciones de jpeg.
     * @return Objeto compressed con los canales de color comprimidos.
     */
    private static Compressed encode(final int[][] vector){
        final Compressed result = new Compressed();
        final byte[][] data = new byte[3][];
        final byte[] extraBits = new byte[3];
        final ArrayList< ArrayList<Pair<String, Integer>> > codesArray = new ArrayList< ArrayList<Pair<String, Integer>> >();

        //Preparing for parallel execution
        final int processors = Runtime.getRuntime().availableProcessors();

        for (int channel = 0; channel < 3; ++channel){
            ExecutorService pool = Executors.newFixedThreadPool(processors); // number of threads
            final int[][] vSplitted = new int[processors][];
            for (int i = 0; i < processors; ++i){
                if (i < processors-1) { vSplitted[i] = new int[vector[channel].length/processors]; } //first segments
                else { vSplitted[i] = new int[vector[channel].length/processors + vector[channel].length%processors]; } //last segment
                System.arraycopy(vector[channel], vector[channel].length/processors*i, vSplitted[i], 0, vSplitted[i].length);
            }
            
            //Count instances of each value
            final ArrayList<CounterQueue> partialQ = new ArrayList<CounterQueue>();
            for (int i = 0; i < processors; ++i){
                partialQ.add(new CounterQueue()); 
                final int index = i;
                pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        for (int j = 0; j < vSplitted[index].length; ++j){
                            partialQ.get(index).counter(vSplitted[index][j]);
                        }
                    }
                });
            }
            shutdownAndAwaitTermination(pool);
            //Combine answers
            final CounterQueue counter = new CounterQueue();
            for (int i = 0; i < processors; ++i){
                final CounterQueue aux = partialQ.get(i);
                while (aux.size() > 0){
                    final Pair<Integer, Integer> p = aux.pop();
                    counter.addResults(p.first, p.second);
                }
            }
            
            //Create priority queue of nodes
            final PriorityQueue<Node> creator = new PriorityQueue<Node>();
            while (counter.size() > 0){
                final Pair<Integer, Integer> aux = counter.pop();
                creator.insert(new Node(aux.first, aux.second), aux.first);
            }
            
            //Construct tree using the priority queue
            while (creator.size() > 1){
                Pair<Node, Integer> aux1, aux2;
                aux1 = creator.pop();
                aux2 = creator.pop();
                final Node n = new Node(aux1.first, aux2.first);
                creator.insert(n, n.weight());
            }
            
            //Traverse the tree to get the huffman table as an ArrayList
            final ArrayList<Pair<String, Integer>> codeArray = new ArrayList<Pair<String, Integer>>();
            final Node root = creator.pop().first;
            root.traverse(codeArray, "");
            
            //Create hashmap
            final HashMap<Integer, String> codeHashMap = new HashMap<Integer, String>();
            for (int i = 0; i < codeArray.size(); ++i){
                final Pair<String, Integer> p = codeArray.get(i);
                codeHashMap.put(p.second, p.first);
            }

            //Encode the values
            final String[] partial = new String[processors];
            pool = Executors.newFixedThreadPool(processors); // number of threads
            for (int i = 0; i < processors; ++i){
                final int index = i;
                pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        final StringBuilder s = new StringBuilder();
                        for (int i = 0; i < vSplitted[index].length; ++i){
                            s.append(codeHashMap.get(vSplitted[index][i]));
                        }
                        partial[index] = s.toString();
                    }
                });
            }
            shutdownAndAwaitTermination(pool);
            
            //Combine answers
            String encoded = String.join("", partial);

            //Add extra bits to complete the final byte
            extraBits[channel] = (byte)(8 - encoded.length()%8);
            for (int i = 0; i < extraBits[channel]; ++i) encoded += "0";

            //Convert from string to byte array
            data[channel] = new byte[encoded.length()/8];
            for (int i = 0; i < data[channel].length; ++i){
                final String aux = encoded.substring(i*8, (i+1)*8);
                data[channel][i] = (byte)Integer.parseInt(aux, 2);
            }
            codesArray.add(codeArray);
        }
        //Create compressed object
        result.setDatos(data);
        result.setExtraBits(extraBits);
        result.setCodeArray(codesArray);
        return result;
    }

    /**
     * Descompresión Huffman de una imagen
     * @param c Imagen comprimida en forma de un objeto Compressed.
     * @return Matriz con los 3 canales de color descomprimidos.
     */
    private static int[][] decode(final Compressed c){
        final byte[][] rawData = c.getDatos();
        final ArrayList< ArrayList<Pair<String, Integer>> > codeArray = c.getCodeArray();
        final int[][] result = new int[3][];
        final int processors = Runtime.getRuntime().availableProcessors();

        for (int channel = 0; channel < 3; ++channel){
            final byte[][] vSplitted = new byte[processors][];
            for (int i = 0; i < processors; ++i){
                if (i < processors-1) { vSplitted[i] = new byte[rawData[channel].length/processors]; } //first segments
                else { vSplitted[i] = new byte[rawData[channel].length/processors + rawData[channel].length%processors]; } //last segment
                System.arraycopy(rawData[channel], rawData[channel].length/processors*i, vSplitted[i], 0, vSplitted[i].length);
            }

            //Get binary string
            final ExecutorService pool = Executors.newFixedThreadPool(processors); // number of threads
            final String[] partial = new String[processors];
            for (int i = 0; i < processors; ++i){
                final int index = i;
                pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        final StringBuilder s = new StringBuilder();
                        for (int i = 0; i < vSplitted[index].length; ++i){
                            s.append(Integer.toBinaryString((vSplitted[index][i] & 0xFF) + 0x100).substring(1));
                        }
                        partial[index] = s.toString();
                    }
                });
            }
            shutdownAndAwaitTermination(pool);

            //Combine answers
            String encoded = String.join("", partial);
            
            //Delete extra bits
            encoded = encoded.substring(0, encoded.length() - c.getExtraBits()[channel]);

            //Create the huffman tree
            final ArrayList<Pair<Node, String>> tree = new ArrayList<Pair<Node, String>>();
            for (int i = 0; i < codeArray.get(channel).size(); ++i){
                tree.add(new Pair<Node, String>(new Node(0, codeArray.get(channel).get(i).second), codeArray.get(channel).get(i).first));
            }
            while(tree.size() > 1){
                for (int i = 0; i < tree.size() - 1; ++i){
                    String aux1 = tree.get(i).second;
                    final String aux2 = tree.get(i+1).second;
                    if ((aux1.length() == aux2.length()) && (aux1.substring(0, aux1.length()-1).equals(aux2.substring(0, aux1.length()-1)))){
                        final Node newN = new Node(tree.get(i).first, tree.get(i+1).first);
                        aux1 = aux1.substring(0, aux1.length()-1);
                        tree.set(i, new Pair<Node, String>(newN, aux1));
                        tree.remove(i+1);
                    }
                }
            }
            final Node root = tree.get(0).first;
            tree.clear();
            
            //In case the three has only one node, all items have the same value
            if (root.isLeaf()) { 
                result[channel] = new int[encoded.length()];
                Arrays.fill(result[channel], root.value());
            }
            else{
                //Decode the bitstring to obtain an integer array
                final ArrayList<Integer> a = new ArrayList<Integer>();
                Node current = root;
                for (int i = 0; i < encoded.length(); ++i){
                    final char bit = encoded.charAt(i);
                    if(bit == '0') current = current.left();
                    else current = current.right();

                    if (current.isLeaf()){
                        a.add(current.value());
                        current = root;
                    }
                }
                result[channel] = a.stream().mapToInt(i -> i).toArray();
            }
        }
        return result;
    }
    
    /**
     * Conversion de color RGB a YUV
     * @param vector Matriz de 3 dimensiones con los píxeles de la imagen.
     * @return Matriz de 3 dimensiones con los píxeles de la imagen en espacio de color YUV.
     */
    private static int[][][] RGBtoYUV(final int[][][] matrixRGB){
        //Convert the RGB image to YUV
        // YUV channel matrices initialization
        final int h = matrixRGB.length;
        final int w = matrixRGB[0].length;
        final int[][][] result = new int[3][h][w];
        // We convert the RGB matrix to YUV channels matrices
        for(int i = 0; i < h; ++i){
            for(int j = 0; j < w; ++j){
                final int r = matrixRGB[i][j][0];
                final int g = matrixRGB[i][j][1];
                final int b = matrixRGB[i][j][2];
                
                result[0][i][j] = Math.max(0, Math.min(255, (int)(0.299*r + 0.587*g + 0.114*b))); // Y component (between 0 and 255)
                result[1][i][j] = Math.max(0, Math.min(255, (int)(128 - 0.168736*r - 0.331264*g + 0.500*b))); // Cb component (between 0 and 255)
                result[2][i][j] = Math.max(0, Math.min(255, (int)(128 + 0.500*r - 0.418688*g - 0.081312*b))); // Cr component (between 0 and 255)
            }
        }
        return result;
    }

    /**
     * Conversion de color YUV a RGB
     * @param matrixYUV Matriz de 3 dimensiones con los píxeles de la imagen en espacio de color YUV.
     * @return Matriz de 3 dimensiones con los píxeles de la imagen.
     */
    private static int[][][] YUVtoRGB(final int[][][] matrixYUV){
        final int h = matrixYUV[0].length;
        final int w = matrixYUV[0][0].length;
        final int[][][] result = new int[h][w][3];
        for(int i = 0; i < h; ++i){
            for(int j = 0; j < w; ++j){
                final int y = matrixYUV[0][i][j];
                final int cb = matrixYUV[1][i][j];
                final int cr = matrixYUV[2][i][j];

                result[i][j][0] = Math.max(0, Math.min(255, (int)(y + 1.402*(cr-128)))); // R component (between 0 and 255)
                result[i][j][1] = Math.max(0, Math.min(255, (int)(y - (0.34414*(cb-128)) - (0.71414*(cr-128))))); // G component (between 0 and 255)
                result[i][j][2] = Math.max(0, Math.min(255, (int)(y + 1.772*(cb-128)))); // B component (between 0 and 255)
            }
        }
        return result;
    }

    /**
     * Fusión de los bloques de 8x8 de un canal de color.
     * @param downType Tipo de downsampling.
     * @param blockMatrix Matriz de 3 dimensiones representando un canal dividido en bloques de 8x8.
     * @param h Altura de la imagen.
     * @param w Anchura de la imagen.
     * @return Matriz representando un canal dividido en bloques de 64.
     */
    private static int[][] blockMerging(final int downType, final int[][][] blockMatrix, final int h, final int w){
        //Merges the blocks into channels
        int[][] result = new int[h][w];
        if (downType == 2) {
            result = blockMerge(blockMatrix, h, w/2 + w%2);
        }
        else if (downType == 0){
            result = blockMerge(blockMatrix, h/2 + h%2, w/2 + w%2);
        }
        else {
            result = blockMerge(blockMatrix, h, w);
        }
        return result;
    }

    /**
     * Codificación JPEG del canal de luminancia.
     * @param quality Calidad de la compresión.
     * @param blockMatrix Matriz de 3 dimensiones representando el canal de luminancia dividido en bloques de 8x8.
     * @return Matriz representando el canal de luminancia dividido en bloques de 64.
     */
    private static int[][] compressionL(final int quality, final int[][][] blockMatrix){
        //Applies all the JPEG numeric transforms to the channels in a parallel manner
        final int [][] array = new int[blockMatrix.length][64];
        final ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()); // number of threads
        for(int i = 0; i < blockMatrix.length; ++i){
            final int index = i;
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    array[index] = blockCompressionLuminance(blockMatrix[index], quality);
                }
            });
        }
        shutdownAndAwaitTermination(pool);
        return array;
    }

    /**
     * Codificación JPEG de un canal de crominancia.
     * @param quality Calidad de la compresión.
     * @param blockMatrix Matriz de 3 dimensiones representando un canal de crominancia dividido en bloques de 8x8.
     * @return Matriz representando un canal de crominancia dividido en bloques de 64.
     */
    private static int[][] compressionC(final int quality, final int[][][] blockMatrix){
        //Applies all the JPEG numeric transforms to the channels in a parallel manner
        final int [][] array = new int[blockMatrix.length][64];
        final ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()); // number of threads
        for(int i = 0; i < blockMatrix.length; ++i){
            final int index = i;
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    array[index] = blockCompressionChrominance(blockMatrix[index], quality);
                }
            });
        }
        shutdownAndAwaitTermination(pool);
        return array;
    }

    /**
     * Decodificación JPEG del canal de luminancia.
     * @param quality Calidad de la compresión.
     * @param array Matriz representando el canal de luminancia dividido en bloques de 64.
     * @return Matriz de 3 dimensiones representando el canal de luminancia dividido en bloques de 8x8.
     */
    private static int[][][] decompressionL(final int quality, final int[][] array){
        final int[][][] result = new int[array.length][8][8];
        //Reconstructs the JPEG in a parallel manner
        final ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()); // number of threads
        for(int i = 0; i < array.length; ++i){
            final int index = i;
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    result[index] = blockDecompressionLuminance(array[index], quality);
                }
            });
        }
        shutdownAndAwaitTermination(pool);
        return result;
    }

    /**
     * Decodificación JPEG de un canal de crominancia.
     * @param quality Calidad de la compresión.
     * @param array Matriz representando un canal de crominancia dividido en bloques de 64.
     * @return Matriz de 3 dimensiones representando un canal de crominancia dividido en bloques de 8x8.
     */
    private static int[][][] decompressionC(final int quality, final int[][] array){
        final int[][][] result = new int[array.length][8][8];
        //Reconstructs the JPEG in a parallel manner
        final ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()); // number of threads
        for(int i = 0; i < array.length; ++i){
            final int index = i;
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    result[index] = blockDecompressionChrominance(array[index], quality);
                }
            });
        }
        shutdownAndAwaitTermination(pool);
        return result;
    }

    /**
     * Espera y limpieza de una sección paralelizada.
     * @param pool Pool de threads a esperar y eliminar.
     */
    private static void shutdownAndAwaitTermination(final ExecutorService pool) {
        //Waits for the selected pool to end execution of all its threads, kills them if they do not respond after 60 seconds
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(180, TimeUnit.SECONDS)) {
                System.err.println("Time limit exeeded. Shutting down pool...");
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(180, TimeUnit.SECONDS)) System.err.println("Pool did not terminate");
            }
        } catch (final InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Transformaicón DCT de un píxel dentro de un bloque de 8x8.
     * @param u Posición vertical del píxel dentro del bloque.
     * @param u Posición horizontal del píxel dentro del bloque.
     * @param matrix Bloque de 8x8 píxeles.
     * @return Double con el valor resultante de la DCT.
     */
    private static double elementDCT(final int u, final int v, final int[][] matrix){
        //Input: Element index (u, v) and 8x8 original matrix
        //Result: DCT transform for the element
        double suma = 0;
        for (int x = 0; x < 8; ++x){
            for (int y = 0; y < 8; ++y){
                suma += matrix[x][y] * Math.cos(u*(2*x+1)*Math.PI/16) * Math.cos(v*(2*y+1)*Math.PI/16);
            }
        }
        if (u == 0) suma *= 1/Math.sqrt(2);
        if (v == 0) suma *= 1/Math.sqrt(2);
        return suma * 2.0 / Math.sqrt(8*8);
    }

    /**
     * Transformaicón DCT de un bloque de 8x8.
     * @param matrix Bloque de 8x8 píxeles.
     * @return Matriz de 8x8 con el valor resultante de la DCT.
     */
    private static double[][] DCT(final int[][] submatrix){
        //Input: 8x8 matrix
        //Output: DCT transform for this matrix
        final double[][] result = new double[8][8];
        for (int v = 0; v < 8; ++v){
            for (int u = 0; u < 8; ++u){
                result[v][u] = elementDCT(u, v, submatrix);
            }
        }
        return result;
    }


    /**
     * Transformaicón DCT inversa de un píxel dentro de un bloque de 8x8.
     * @param u Posición vertical del píxel dentro del bloque.
     * @param u Posición horizontal del píxel dentro del bloque.
     * @param matrix Bloque de 8x8 píxeles.
     * @return Double con el valor resultante de la DCT inversa.
     */
    private static double elementIDCT(final int u, final int v, final double[][] matrix){
        //Input: Element index (u, v) and 8x8 original matrix
        //Result: DCT transform for the element
        double suma = 0;
        double c1, c2;
        for (int x = 0; x < 8; ++x){
            for (int y = 0; y < 8; ++y){
                if (x == 0) c1 = 1/Math.sqrt(2);
                else c1 = 1;
                if (y == 0) c2 = 1/Math.sqrt(2);
                else c2 = 1;
                suma += c1 * c2 * matrix[x][y] * Math.cos(x*(2*u+1)*Math.PI/16) * Math.cos(y*(2*v+1)*Math.PI/16);
            }
        }
        return suma * 2.0 / Math.sqrt(8*8);       
    }

    /**
     * Transformaicón DCT inversa de un bloque de 8x8.
     * @param matrix Bloque de 8x8 píxeles.
     * @return Matriz de 8x8 con el valor resultante de la DCT inversa.
     */
    private static int[][] IDCT(final double[][] submatrix){
        //Input: 8x8 matrix
        //Output: Inverse DCT transform for this matrix
        final double[][] result = new double[8][8];
        for (int v = 0; v < 8; ++v){
            for (int u = 0; u < 8; ++u){
                result[u][v] = elementIDCT(v, u, submatrix);
            }
        }
        return round(result);
    }

    /**
     * Cálculo del multiplicador de calidad a partir del valor de calidad.
     * @param quality Valor de calidad.
     * @return Valor multiplicador de calidad.
     */
    private static double alphaQuality(int quality){
        //Input: Integer from 1 to 100
        //Result: Returns a double representing the alpha quality factor in JPEG
        if (quality < 1) quality = 1;
        else if (quality > 100) quality = 100;
        if (quality == 100) return 0.01;
        else if (quality <= 50) return 50/quality;
        else return 2 - quality/50;
    }

    /**
     * Quantización de un bloque de 8x8 píxeles del canal de luminancia.
     * @param submatrix Bloque de 8x8 píxeles.
     * @param quality Calidad de la compresión.
     * @return Bloque de 8x8 píxeles con la quantificación aplicada.
     */
    private static int[][] luminanceQuantization(final double[][] submatrix, final int quality){
        //Input: 8x8 Matrix and a quality integer from 1 to 100
        //Result: Returns the input matrix with luminance quantization applied
        int[][] result = new int[8][8];
        final double a = alphaQuality(quality);
        result = round(divide(submatrix, multiply(luminanceMatrix, a))); //result = round(submatrix/(a*luminanceM))
        return result;
    }

    /**
     * Quantización de un bloque de 8x8 píxeles de un canal de crominancia.
     * @param submatrix Bloque de 8x8 píxeles.
     * @param quality Calidad de la compresión.
     * @return Bloque de 8x8 píxeles con la quantificación aplicada.
     */
    private static int[][] chrominanceQuantization(final double[][] submatrix, final int quality){
        //Input: 8x8 Matrix and a quality integer from 1 to 100
        //Result: Returns the input matrix with luminance quantization applied
        int[][] result = new int[8][8];
        final double a = alphaQuality(quality);
        result = round(divide(submatrix, multiply(chrominanceMatrix, a))); //result = round(submatrix/(a*luminanceM))
        return result;
    }

    /**
     * Quantización inversa de un bloque de 8x8 píxeles de un canal de crominancia.
     * @param submatrix Bloque de 8x8 píxeles.
     * @param quality Calidad de la compresión.
     * @return Bloque de 8x8 píxeles con la quantificación revertida.
     */
    private static double[][] chrominanceQuantizationReverse(final int[][] submatrix, final int quality){
        //Input: 8x8 Matrix and a quality integer from 1 to 100
        //Result: Returns the input matrix with luminance quantization reversed
        double[][] result = new double[8][8];
        final double a = alphaQuality(quality);
        result = multiply(submatrix, multiply(chrominanceMatrix, a)); //result = round(submatrix/(a*luminanceM))
        return result;
    }

    /**
     * Quantización inversa de un bloque de 8x8 píxeles del canal de luminancia.
     * @param submatrix Bloque de 8x8 píxeles.
     * @param quality Calidad de la compresión.
     * @return Bloque de 8x8 píxeles con la quantificación revertida.
     */
    private static double[][] luminanceQuantizationReverse(final int[][] submatrix, final int quality){
        //Input: 8x8 Matrix and a quality integer from 1 to 100
        //Result: Returns the input matrix with luminance quantization reversed
        double[][] result = new double[8][8];
        final double a = alphaQuality(quality);
        result = multiply(submatrix, multiply(luminanceMatrix, a)); //result = round(submatrix/(a*luminanceM))
        return result;
    }

    /**
     * Conversión de un bloque de 8x8 en un vector de 64 elementos mediante un patrón de zig-zag.
     * @param submatrix Bloque de 8x8 píxeles.
     * @return Vector de 64 elementos.
     */
    private static int[] zigZag(final int[][] matrix){
        //Input: Matrix 8x8
        //Result: Returns as a vector a flatenned version of the matrix with a ZigZag pattern
        final int[] result = new int[64];
        int x = 0; 
        int y = 0; 
        int i = 0;
        boolean up = true;
        while (i < 64){
            result[i] = matrix[x][y];
            if (up == true){
                if (x == 0){
                    up = false;
                    y += 1;
                } else if (y == 7){
                    up = false;
                    x += 1;
                } else {
                    x -= 1;
                    y += 1;
                }
            }
            else{
                if (x == 7){
                    up = true;
                    y += 1;
                } else if (y == 0){
                    up = true;
                    x += 1;
                }
                else {
                    x += 1;
                    y -= 1;
                }
            }
            ++i;
        }
        return result;
    }

    /**
     * Conversión de un vector de 64 elementos en un bloque de 8x8 mediante un patrón de zig-zag.
     * @param vector Vector de 64 elementos.
     * @return Bloque de 8x8 píxeles.
     */
    private static int[][] zigZag(final int[] vector){
        //Input: Vector of size 64
        //Result: Returns a matrix reconstructed from the vector with a ZigZag pattern
        final int[][] result = new int[8][8];
        int x = 0; 
        int y = 0; 
        int i = 0;
        boolean up = true;
        while (i < 64){
            result[x][y] = vector[i];
            if (up == true){
                if (x == 0){
                    up = false;
                    y += 1;
                } else if (y == 7){
                    up = false;
                    x += 1;
                } else {
                    x -= 1;
                    y += 1;
                }
            }
            else{
                if (x == 7){
                    up = true;
                    y += 1;
                } else if (y == 0){
                    up = true;
                    x += 1;
                }
                else {
                    x += 1;
                    y -= 1;
                }
            }
            ++i;
        }
        return result;
    }
    
    /**
     * Codificación JPEG de un bloque de 8x8 del canal de luminancia.
     * @param block Bloque de 8x8 píxeles.
     * @param quality Calidad de la compresión.
     * @return Vector de 64 elementos.
     */
    public static int[] blockCompressionLuminance(final int[][] block, final int quality){
        //Input: 8x8 matrix
        //Result: Returns a vector with JPEG algorithms applied, ready to be compressed losslessly
        return zigZag(luminanceQuantization(DCT(add(block, -128)), quality));
    }

    /**
     * Codificación JPEG de un bloque de 8x8 de un canal de crominancia.
     * @param block Bloque de 8x8 píxeles.
     * @param quality Calidad de la compresión.
     * @return Vector de 64 elementos.
     */
    public static int[] blockCompressionChrominance(final int[][] block, final int quality){
        //Input: 8x8 matrix
        //Result: Returns a vector with JPEG algorithms applied, ready to be compressed losslessly
        return zigZag(chrominanceQuantization(DCT(add(block, -128)), quality));
    }

    /**
     * Decodificación JPEG de un bloque de 8x8 del canal de luminancia.
     * @param block Vector de 64 elementos.
     * @param quality Calidad de la compresión.
     * @return Bloque de 8x8 píxeles.
     */
    public static int[][] blockDecompressionLuminance(final int[] block, final int quality){
        //Input: 64 element matrix
        //Result: Returns a reconstructed matrix with JPEG algorithms reversed
        return add(IDCT(luminanceQuantizationReverse(zigZag(block), quality)), 128);
    }

    /**
     * Decodificación JPEG de un bloque de 8x8 de un canal de crominancia.
     * @param block Vector de 64 elementos.
     * @param quality Calidad de la compresión.
     * @return Bloque de 8x8 píxeles.
     */
    public static int[][] blockDecompressionChrominance(final int[] block, final int quality){
        //Input: 64 element matrix
        //Result: Returns a reconstructed matrix with JPEG algorithms reversed
        return add(IDCT(chrominanceQuantizationReverse(zigZag(block), quality)), 128);
    }

    /**
     * Suma de un valor a todos los elementos de una matriz.
     * @param matrix Matriz original.
     * @param value Valor a sumar.
     * @return Matriz resultante de la operación.
     */
    private static int[][] add(final int[][] matrix, final int value){        
        //Input: Matrix and integer value
        //Result: Adds value to each element of the matrix
        final int[][] result = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; ++i){
            for (int j = 0; j < matrix[i].length; ++j){
                result[i][j] = matrix[i][j] + value;
            }
        }
        return result;
    }
    
    /**
     * Multiplicación de un valor a todos los elementos de una matriz.
     * @param matrix Matriz original.
     * @param value Valor a multiplicar.
     * @return Matriz resultante de la operación.
     */
    private static double[][] multiply(final int[][] matrix, final double value){
        //Input: Matrix and number value
        //Result: Returns the matrix with each element multiplied by the value
        final double[][] result = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; ++i){
            for (int j = 0; j < matrix[i].length; ++j){
                result[i][j] = matrix[i][j] * value;
            }
        }
        return result;
    }

    /**
     * Multiplicación de dos matrices elemento a elemento.
     * @param m1 Matriz 1.
     * @param m2 Matriz 2.
     * @return Matriz resultante de la operación.
     */
    private static double[][] multiply(final int[][] m1, final double[][] m2){
        //Input: Two matrices of same shape
        //Result: Returns the matrix resulting from multiplying the elemens fo same position in input matrices
        final double[][] result = new double[m1.length][m1[0].length];
        for (int i = 0; i < m1.length; ++i){
            for (int j = 0; j < m1[i].length; ++j){
                result[i][j] = m1[i][j]*m2[i][j];
            }
        }
        return result;
    }

    /**
     * División de dos matrices de doubles elemento a elemento.
     * @param m1 Matriz 1.
     * @param m2 Matriz 2.
     * @return Matriz resultante de la operación.
     */
    private static double[][] divide(final double[][] m1, final double[][] m2){
        //Input: Two matrices of same shape
        //Result: Returns the matrix resulting from dividing the elemens fo same position in input matrices
        final double[][] result = new double[m1.length][m1[0].length];
        for (int i = 0; i < m1.length; ++i){
            for (int j = 0; j < m1[i].length; ++j){
                result[i][j] = m1[i][j]/m2[i][j];
            }
        }
        return result;
    }

    /**
     * Redondeo de los elementos de una matriz.
     * @param matrix Matriz original.
     * @return Matriz resultante de la operación.
     */
    private static int[][] round(final double[][] matrix){
        //Input: Matrix with elements of type double
        //Result: Returns the matrix with its elements rounded and as an integer
        final int[][] result = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; ++i){
            for (int j = 0; j < matrix[i].length; ++j){
                result[i][j] = (int) Math.round(matrix[i][j]);
            }
        }
        return result;
    }

    /**
     * División de un canal en bloques de 8x8.
     * @param matrix Canal de píxeles.
     * @return Matriz de 3 dimensiones con el canal dividido en bloques.
     */
    private static int[][][] blockSplit(final int[][] matrix){
        //Input: 2D matrix of ints
        //Result: Reshapes the matrix to a 3D matrix composed of 8x8 blocks
        final int rows = matrix.length + 8 - matrix.length%8;
        final int columns = matrix[0].length + 8 - matrix[0].length%8;
        final int blocks = (rows*columns / 64);

        final int[][][] result = new int[blocks][8][8];
        int block = 0;

        for (int x = 0; x < matrix.length; x += 8){
            for (int y = 0; y < matrix[x].length; y += 8){
                //For each 8x8 block
                for (int row = 0; row < 8; ++row){
                    for (int col = 0; col < 8; ++col){
                        if ((x+row < matrix.length) && (y+col< matrix[0].length)){
                            result[block][row][col] = matrix[x+row][y+col];
                        }
                        else if ((x+row >= matrix.length) && (y+col >= matrix[0].length)){
                            result[block][row][col] = result[block][row-1][col-1];
                        }
                        else if (x+row >= matrix.length){
                            result[block][row][col] = result[block][row-1][col];
                        }
                        else if (y+col >= matrix[0].length){
                            result[block][row][col] = result[block][row][col-1];
                        }
                    }
                }
                ++block;
            }
        }
        return result;
    }

    /**
     * Fusión de bloques de 8x8 en un canal.
     * @param blocks Matriz de 3 dimensiones con el canal dividido en bloques.
     * @param altura Altura de la imagen.
     * @param amplada Anchura de la imagen.
     * @return Canal reconstruido.
     */
    private static int[][] blockMerge(final int blocks[][][], final int altura, final int amplada){
        //Input: 3D matrix composed of 8x8 blocks and dimensions H x W
        //Result: Reshapes the matrix to a 2D matrix with the dimensions specified (discarding info outside this simensions)
        final int[][] result = new int[altura][amplada];
        int amplada2;
        if (amplada%8 > 0) amplada2 = ((amplada/8)+1)*8;
        else amplada2 = amplada;
        
        for(int i = 0; i < altura; ++i){
            for(int j = 0; j < amplada; ++j){
                final int posBlock = (i/8) * (amplada2/8) + (j/8);
                result[i][j] = blocks[posBlock][i%8][j%8];
            }
        }
        return result;
    }

    /**
     * Downsampling de un canal.
     * @param matrix Canal original.
     * @param type Tipo de downsampling: 4=4:4:4, 2=4:2:2, 0=4:2:0.
     * @return Canal downsampleado.
     */
    private static int[][] downsample(final int[][] matrix, final int type){
        //Input: 2D matrix of ints, type of downsampling (4 = 4:4:4/none, 2 = 4:2:2, 0 = 4:2:0)
        //Result: Downsamples the matrix
        if (type == 4) return matrix;
        int[][] result;
        int factor;
        if (type == 2) factor = 1;
        else if (type == 0) factor = 2;
        else return matrix;
        result = new int[matrix.length / factor + matrix.length % factor][matrix[0].length / 2 + matrix[0].length % 2];
        for (int x = 0; x < result.length; ++x){
            for (int y = 0; y < result[0].length; ++y){
                int avg;
                if (y + 1 == result[0].length && x + 1 == result.length) avg = matrix[x*factor][y*2];
                else if (y + 1 == result[0].length) avg = (matrix[x*factor][y*2]+matrix[x*factor+factor/2][y*2])/(2);
                else if (x + 1 == result.length) avg = (matrix[x*factor][y*2]+matrix[x*factor][y*2+1])/(2);
                else avg = (matrix[x*factor][y*2]+matrix[x*factor][y*2+1]+matrix[x*factor+factor/2][y*2]+matrix[x*factor+factor/2][y*2+1])/(4);
                result[x][y] = avg;
            }
        }
        return result;
    }

    /**
     * Upsampling de un canal.
     * @param matrix Canal downsampleado.
     * @param type Tipo de downsampling: 4=4:4:4, 2=4:2:2, 0=4:2:0.
     * @param h Altura de la imagen
     * @param w Anchura de la imagen
     * @return Canal upsampleado.
     */
    private static int[][] upsample(final int[][] matrix, final int type, final int h, final int w){
        //Input: 2D matrix of ints, type of upsampling (4 = 4:4:4/none, 2 = 4:2:2, 0 = 4:2:0)
        //Result: Upsamples the matrix
        if (type == 4) return matrix;
        final int[][] result = new int[h][w];
        int factor;
        if (type == 2) factor = 1;
        else if (type == 0) factor = 2;
        else return matrix;
        for (int x = 0; x < h; ++x){
            for (int y = 0; y < w; ++y){
                result[x][y] = matrix[x/factor][y/2];        
            }
        }
        return result;
    }

    /**
     * Aplanamiento de un canal codificado con JPEG a un vector.
     * @param blocks Canal codificado.
     * @return Canal en formato plano.
     */
    private static int[] flattenBlocks(final int[][] blocks){
        final int[] result = new int[blocks.length*64];
        for (int i = 0; i < blocks.length; ++i){
            for (int j = 0; j < 64; ++j){
                result[i*64 + j] = blocks[i][j];
            }
        }
        return result;
    }

    /**
     * Recuperación de un canal codificado con JPEG desde un vector.
     * @param vector Vector con un canal plano.
     * @return Canal codificado.
     */
    private static int[][] unflattenBlocks(final int[] vector){
        final int[][] result = new int[vector.length/64][64];
        for (int i = 0; i < result.length; ++i){
            for (int j = 0; j < 64; ++j){
                result[i][j] = vector[i*64 + j];
            }
        }
        return result;
    }

	@Override
	public MyByteCollection comprimir(MyByteCollection contenido_fichero) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}