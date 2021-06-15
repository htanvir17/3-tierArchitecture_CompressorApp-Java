package Presentacion;
import Dominio.CtrlDominio;

import Dominio.MyException;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javafx.util.Pair;
import javax.swing.JFrame;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Controlador de la capa de presentación encargada de la recepción e interación con el usuario
 * */
public class CtrlPresentacion implements WindowListener {
    
    /**Acceso a la capa de dominio*/
    private final CtrlDominio ctrldominio;
    /**Acceso a la vista inicial*/
    private final VistaInicial vistaInicial;
    /**Acceso a la vista comprimir */
    private VistaComprimir vistaComprimir;
    /**Acceso a la vista de seleccion */
    private VistaSeleccion vistaSeleccion;
    /**Acceso a la vista configuracion algorimo*/
    private VistaConfiguracionAlgoritmo vistaConfiguracionAlgortimo;
    /**Acceso a la vista de descomprimir */
    private VistaDescomprimir vistaDescomprimir;
    /**Acceso a la vista de aviso ayuda */
    private VistaAyuda vistaAyuda;

	
    /** El Frame principal de la aplicacion */
    private final JFrame frame;
    /** El Frame secundario para visualizacion de ficheros(.txt,.ppm)*/
    private JFrame frameVisualizacion;
    /** Variable que guarda la vista_previa a una vista */
	private String vista_previa_ayuda = "VISTA_INICIAL";
    
    /**ArrayList que contiene las rutas selecionadas por el usuario*/
    private ArrayList<String> rutas;
    /** Valor que indica que si se ha selecionado la comprrimir o descomprimir*/
    private boolean es_comprimir = true; //su valor se sabe en la primera ventana
    /**Valor que indica la ruta de destino de la compresión o descompresión
     * */
    private String rutaDestino = "./";
    /**Valor que almacena el nombre del fichero(compresion) o de la carpeta(descompresion) resultante al realizar la compresion o descommpresion respectivamente.
     * */
    private String nombreArchivo = "";
    /**Valor del algoritmo seleccionado para la compresión 
     * */
    private String algoritmo = "LZSS";
    /**valor del algoritmo de la calidad para la compresion de una imagen
     * */
    private int calidad = 100;
    /**Valor de dsmapling para la compresion de una imagen
     * */
    private int dSampling = 4;
    /**Valor de la contraseña 
     * */
    private String contrasena = "";
    /**Almacena las rutas a visualizar
     * */
	private ArrayList<String> rutasAVisualizar;
	
	public ArrayList<String> getRutasAVisualizar() {
		return rutasAVisualizar;
	}
	
    public void setRutasAVisualizar(ArrayList<String> rutasAVisualizar) {
    	this.rutasAVisualizar = rutasAVisualizar;
    }
   
    public String getRutaDestino() {
		return rutaDestino;
	}

	public void setRutaDestino(String rutaDestino) {
		this.rutaDestino = rutaDestino;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public String getAlgoritmo() {
		return algoritmo;
	}

	public void setAlgoritmo(String algoritmo) {
		this.algoritmo = algoritmo;
	}

	public int getCalidad() {
		return calidad;
	}

	public void setCalidad(int calidad) {
		this.calidad = calidad;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	
	/*se llama desde VistInicial para asignar el valor del atributo y debe ir a la siguiente ventana: Vista principal*/
	public void setEs_comprimir(boolean comprimir){
        this.es_comprimir = comprimir;
    }
    
    public boolean getEs_comprimir( ){
        return this.es_comprimir;
    }
    
    /*se coge las rutas desde la vistaSeleccion*/
    public void setRutas(ArrayList<String> rutas){
        this.rutas = rutas;
    }
    public ArrayList<String> getRutas() {
    	return this.rutas;
    }
    
	public int getdSampling() {
		return dSampling;
	}

	public void setdSampling(int dSampling) {
		this.dSampling = dSampling;
	}
    
    /**
     * Creadora del controlador de presentación
     * */ 
    public CtrlPresentacion() throws Exception {
        
        this.frame = new JFrame("Compresor");
        this.frame.addWindowListener(this);  
        this.frame.setResizable(false);
        this.ctrldominio = new CtrlDominio(this);
        this.vistaInicial = new VistaInicial(this);
        this.rutas = new ArrayList<>();
        this.rutasAVisualizar = new ArrayList<>();
        
        this.vistaComprimir = new VistaComprimir(this);
        this.vistaSeleccion = new VistaSeleccion(this,rutas);
        this.vistaConfiguracionAlgortimo = new VistaConfiguracionAlgoritmo(this);
        this.vistaDescomprimir = new VistaDescomprimir(this);
        this.vistaAyuda = new VistaAyuda(this);
        activarVista(ConstantesVistas.VISTA_INICIAL);
    }    
    
    /*Añade la vista especificada por parametro (vista = la vista a mostrar) al contentPane del frame*/
   /***
    * Método que activa una vista pasada por parametros
    * @param vista El parámetro vista representa a la vista que se desea que se muestre en el frame
    * */
    public void activarVista(String vista){
    
        switch(vista){
            
            case ConstantesVistas.VISTA_INICIAL:
            	resetearDatos();
                if(this.frame.getContentPane() instanceof VistaComprimir) {
                	vistaComprimir = new VistaComprimir(this);
                    //se ha de eliminar la vista que se conservaba en un ingreso a vista compresion previo.
                    vistaConfiguracionAlgortimo = new VistaConfiguracionAlgoritmo(this);
                }
                else if(this.frame.getContentPane() instanceof VistaDescomprimir ) {
                	vistaDescomprimir = new VistaDescomprimir(this);
                }
                
                this.frame.setTitle("COMPRESOR");
                this.frame.setContentPane(vistaInicial);
                
                this.vista_previa_ayuda = "VISTA_INICIAL";
                break;                
            case ConstantesVistas.VISTA_COMPRIMIR:
            
                this.frame.setTitle("Compresión");
	                this.frame.setContentPane(vistaComprimir);
	                this.vista_previa_ayuda = "VISTA_COMPRIMIR";
                break;  
            case ConstantesVistas.VISTA_SELECCCION:
                this.frame.setTitle("Selección");
                vistaSeleccion = new VistaSeleccion(this, rutas);
                this.frame.setContentPane(vistaSeleccion);
                break;      
                
            case ConstantesVistas.VISTA_CONFIGURACION_ALGORITMO:
                this.frame.setTitle("Configuración Algortimos");
                this.frame.setContentPane(vistaConfiguracionAlgortimo);
                break;
            case ConstantesVistas.VISTA_DESCOMPRIMIR:
                this.frame.setTitle("Descompresión");
                this.frame.setContentPane(vistaDescomprimir);
                this.vista_previa_ayuda = "VISTA_DESCOMPRIMIR";
                break;
            case ConstantesVistas.VISTA_AYUDA:
                this.frame.setTitle("Ayuda");
                this.frame.setContentPane(vistaAyuda);
                break;     
        }
        
        //empaquetar el frame
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
        if (!this.frame.isVisible())
            this.frame.setVisible(true);
    }

    /**
     * Método que activa una vista de visualización texto o imagen segun corresponda a los ficheros almacenados en el prarametro global rutasAVisualizar 
     * Precondición es que las rutas a visualizar sean de archivos: .txt, .ppm, .comp 
     * */
    public void activaFrameVisualizacion() {
    	if(es_comprimir) {
    		for(String ruta :rutasAVisualizar) {
    			File f = new File(ruta);
    			this.frameVisualizacion = new JFrame(f.getName());
    			if(f.getName().contains(".txt")) {	
    				VistaVisualizarTexto vistaVisualizarTexto;
					try {
						vistaVisualizarTexto = new VistaVisualizarTexto(ctrldominio.obtenerContenidoTexto(ruta));
						this.frameVisualizacion.setContentPane(vistaVisualizarTexto);
					} catch (IOException e) {
						//Para el programador
						e.printStackTrace();
					}
    			}else if(f.getName().contains(".ppm")) {
    				        try {
								byte[] imageInByte = ctrldominio.obtenercontenidoImagen(ruta);
								InputStream in = new ByteArrayInputStream(imageInByte);
								BufferedImage bImageFromConvert = ImageIO.read(in);
								
								VistaVisualizadorImagen  a = new VistaVisualizadorImagen(bImageFromConvert); 
								/*
							    JLabel picLabel = new JLabel(new ImageIcon(bImageFromConvert)); 
							    JScrollPane scrollPanel = new JScrollPane(picLabel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
							    this.frameVisualizacion.setContentPane(scrollPanel);*/
								this.frameVisualizacion.setContentPane(a);
							} catch (IOException e) {
								//Para el programador
								e.printStackTrace();
							}
    			}else {
    				JOptionPane.showMessageDialog(null, "El tipo de archivo selecionado no se puede visualizar");
    			}
    			this.frameVisualizacion.pack();
    	        this.frameVisualizacion.setLocationRelativeTo(null);
    	        this.frameVisualizacion.setVisible(true);
    		}
    	}else {
    		
    	}
    	
    }

    /**
     * Método que activa la vista anterior guardara en la variable vista_previa_ayuda
     * */
    public void volverVistaPreviaAyuda() {
        activarVista(vista_previa_ayuda);
    }
    
   /**
    * Método que inicia la compresion del conjunto de rutas selccionadas
    * @return Retorna un entero 0 = Acabado correctamente,1 = Se ha cancelado por el usuario ,2 = Ha acabado con errores  
    * */
 public int  iniciarCompresion(){ // estas son las excepciones que se pueden dar throws IOException, FileNotFoundException,MyException
    	try {
    	    if(contrasena.length() != 0)contrasena = ctrldominio.codificarContrasena(contrasena);
    		/*System.out.println("tamaño de la contraseña es: "+ contrasena.length());
    		System.out.println("Valores de compresion para dominio:\n ---------Inicio");
    		System.out.println("ruta:"+ rutas+ "\n "+rutaDestino+ "\n "+nombreArchivo +"\n "+ algoritmo + "\n "+ contrasena + "\n ----------Final");*/
    		 ctrldominio.iniciar_compresion(rutas,rutaDestino,nombreArchivo,algoritmo,calidad,dSampling,contrasena);
    		return 0;
    	}catch(Exception e){
    		if(e instanceof MyException) {
    			MyException m = (MyException) e;
    			if(m.getEsPorDefecto()) {
    				return 1;
    			}
    			else{
    			  // Esto para el programador
    				System.out.println("Inicio Informacion Programador----------");
      			    e.printStackTrace();
      			  System.out.println("-------------Fin Informacion Programador");
      			  ///Esto para el usuario
    				JOptionPane.showMessageDialog(this.frame,"No se ha podido comprimir ha surgido un error: " +m.getMessage());
    			}
    		}else if(e instanceof FileNotFoundException) {
    			e.printStackTrace();
    			JOptionPane.showMessageDialog(this.frame,"Uno de los ficheros seleccionados no existe:"+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
    		}
    		else {
    			// Esto para el programador
    			System.out.println("Inicio Informacion Programador----------");
  			    e.printStackTrace();
  			    System.out.println("-------------Fin Informacion Programador");
    			  ///Esto para el usuario
    			String mensaje = "No se ha podido comprimir ha surgido un error.\n";
    			if (e.getMessage() != null) mensaje += e.getMessage();
    			else mensaje += "No hay informacion adicional.";
                mensaje +=  "\n¿Desea eliminar el comprimido resultante?";
    			String opciones_continuar[] = {"Si","No"};
    			int botonseleccionado = JOptionPane.showOptionDialog(this.frame,mensaje,"Error", JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE,null,opciones_continuar,opciones_continuar[0]);
    			if(botonseleccionado == 0) ctrldominio.roollback_compresion();
    		}
    		return 2;
    	}
    }
 
 
 /**
  * Método que inicia la descompresión de las rutas selccionadas 
  * @return Retorna un entero 0 = Acabado correctamente,1 = Se ha cancelado por el usuario ,2 = Ha acabado con errores  
  * */  
    public int iniciarDescompresion() {
	      try {int i = 0;
	      	  boolean almenos_una_contrasena_correcta = false;
	      	  boolean almenos_un_comprimido_sin_contrasena = false;
	    	  for(String rut: rutas) {
	    		  System.out.println("ite:"+ i);
		    	  if(ctrldominio.comprimido_tiene_contrasena(rut)) {
		    		  Pair<Boolean, String> p = null;
		    		  boolean salir,correcto;
		    		  salir = correcto= false;
		    		  while(!salir) {
				    		String contrasena_introducida = (String) JOptionPane.showInputDialog(this.frame, "El comprimido "+ rut + " tiene contraseña\nContraseña:", "Contraseña", JOptionPane.QUESTION_MESSAGE, null, null, null);
				    		if(contrasena_introducida != null) {
					    		p = ctrldominio.es_contrasena_valida(rut,contrasena_introducida);
					    		if(!p.getKey()) {
					    			String texto = "La contraseña no es correcta.Desea intentarlo otra vez.";
					    	    	String opciones_continuar[] = {"Si","No"};
					    	    	int botonseleccionado = JOptionPane.showOptionDialog(this.frame, texto, "Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, opciones_continuar, opciones_continuar[0]);
					    	    	if(botonseleccionado != 0) salir = true;
					    		}
					    		else{
					    			almenos_una_contrasena_correcta= true;
					    			correcto = true;
					    			salir = true;
					    			
					    		}
				    		}else salir = true;
		    		  }
		    		  if(correcto) {
		    			  ctrldominio.iniciar_descompresion(rut, rutaDestino, nombreArchivo,p.getValue().length()+2);
		    		  } 
		    		  
		    	  }else {
		    		  almenos_un_comprimido_sin_contrasena = true;
		    		  ctrldominio.iniciar_descompresion(rut, rutaDestino, nombreArchivo, 1);
		    	  }
		    	  ++i;
	    	}
	    	if(almenos_un_comprimido_sin_contrasena || almenos_una_contrasena_correcta) return 0;
	    	else return 1;  
		} catch (Exception e) {
			e.printStackTrace();
			String texto = "Error: "+e.getMessage();
	    	texto  = texto.concat("\nNo se ha podido descomprimir correctamente");
	    	texto  = texto.concat("\n¿Desea borrar los archivos corruptos?");
			String opciones_continuar[] = {"Si","No"};
	    	int botonseleccionado = JOptionPane.showOptionDialog(this.frame, texto, "Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, opciones_continuar, opciones_continuar[0]);
	    	if(botonseleccionado == 0) {
	    		ctrldominio.rollback_descompresion(rutaDestino+"/"+nombreArchivo);
	    	}
			return 2;
		}
	      
     }
	/**
	 * Metodo que calcula el tamaño total en bytes de los ficheros pasados por parametro.
	 * @param rutas El parámetro rutas nos indica un listado de paths de archivos.
	 * @return retorna El tamaño total(bytes) de las rutas ingresadas.
	 * */
	public long calcularTamano(ArrayList<String> rutas) {
		
		return ctrldominio.calcularTamano(rutas);
	}
	/**
	 * Método que resetea los valores globales usados en compresion y/o descompresión. 
	 * */
	public void resetearDatos() {
		rutas = new ArrayList<>();
	    rutaDestino = "./";
	    nombreArchivo = "";
	    algoritmo = "";
	    calidad = 100;
	    contrasena = "";
	    dSampling = 4;
	}

	/**
	 * Método que muestra una ventana con las opciones de ignorar o cancelar la compresion cuando se encuentra un archivo que no sea de tipo(.txt o .ppm) 
	 * @return Retorna verdadero si el valor escogido es ignorar y false si es cancelar.
	 * */
	public boolean muestraRetornaRespuestaIgnore() {
    	String texto = "Se ha encontrado almenos un fichero con un tipo de dato no valido.\nSi desea continuar la compresión sin tener en cuenta ficheros no validos. Haga click en \\\"Ignorar\\\".En caso contrario haga click en \\\"Cancelar\\\".";
    	String opciones[] = {"Cancelar","Ignorar"};
    	int botonseleccionado = JOptionPane.showOptionDialog(this.frame, texto, "Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);
    	if(botonseleccionado == 0 ) {
    		this.resetearDatos();
			System.out.println("Cancelar");
			if(this.getEs_comprimir())this.activarVista(ConstantesVistas.VISTA_COMPRIMIR);
			else this.activarVista(ConstantesVistas.VISTA_DESCOMPRIMIR);
			return false;
    	}else {
    		System.out.println("Ignorar");
			if(this.getEs_comprimir())this.activarVista(ConstantesVistas.VISTA_COMPRIMIR);
			else this.activarVista(ConstantesVistas.VISTA_DESCOMPRIMIR);
    		return true;
    	}
	}
    /**
     * Mostrara las rutas selecionadas actuales(guardadas).
     * */
	public void actualizarDatosComprimirDescomprimir() {
		if(es_comprimir) vistaComprimir.actualizarDatos();
		else vistaDescomprimir.actualizarDatos();
	}
	
	/**
	 * Método que retorna las ayudas almacenadas
     * @return Un ArrayList<String> con las ayudas.
     */
    public ArrayList<String> getAyudas() {
        String ayudas = this.ctrldominio.getAyudas();
        ArrayList<String> ayudasList = new ArrayList<String>();
        
        try {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonAyuda = (JSONObject) jsonParser.parse(ayudas);
        JSONArray arrayAyudas = (JSONArray) jsonAyuda.get("Ayudas");
        
        for (int i = 0; i < arrayAyudas.size(); i++) {
            JSONObject jsonTmp = (JSONObject) arrayAyudas.get(i);
            String id = (String) jsonTmp.get("id");
            String titulo = (String) jsonTmp.get("titulo");
            String texto = (String) jsonTmp.get("texto");
            
            ayudasList.add(id + "\t" + titulo + "\t" + texto);
        }
        
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this.frame, "ERROR:" + e.getMessage());
        }
        return ayudasList;
    }
	
	@Override
	public void windowActivated(WindowEvent e) {
		
	}
	@Override
	public void windowClosed(WindowEvent e) {
	}
	@Override
	public void windowClosing(WindowEvent e) {
		this.frame.dispose();
		System.out.println("entro en cerrar");
		System.exit(0);//esto terminar el proceso
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

}