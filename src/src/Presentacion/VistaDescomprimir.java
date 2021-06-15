package Presentacion;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 * Esta clase VistaDescomprimir implementa los métodos necesarios para poder mostrar la vista para descomprimir los archivos y/o carpetas.
 */
public class VistaDescomprimir extends JPanel {
	/*objeto controlador de presentacion*/
    private CtrlPresentacion cp;
    private JTextField textFieldNombre;
    private JTextField textAreaRutasaDestino;
    private JTextArea textAreaRutas;
  
    /**
     * Creadora de la clase VistaDescomprimir.
     * @param cp El parámetro cp es un objeto de la clase CtrlPresentacion
     */
	public VistaDescomprimir(CtrlPresentacion cp) {	
		this.cp = cp;
		initComponents();
		
	}
	/**
     * Método que inicializa los componentes para poder ver la VistaDescomprimir.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		
		
		textFieldNombre = new JTextField();
		textFieldNombre.setColumns(10);
		textFieldNombre.addKeyListener(new KeyListener(){
			
			public void keyTyped(java.awt.event.KeyEvent e){
				if (textFieldNombre.getText().length() == 16)
			     e.consume();
			}
			@Override
			public void keyPressed(java.awt.event.KeyEvent arg0) {
			}

			@Override
			public void keyReleased(java.awt.event.KeyEvent arg0) {
			}
		});
		JLabel lblNombre = new JLabel("Nombre:");
		
		JLabel lblNewLabel = new JLabel("Ficheros:");
		
		JLabel lblRutaDestino = new JLabel("Destino:");
		
		textAreaRutasaDestino = new JTextField();
		textAreaRutasaDestino.setBackground(Color.WHITE);
		textAreaRutasaDestino.setColumns(10);
		
		JButton btnSeleccionar = new JButton("Seleccionar");
		btnSeleccionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//guardarParametros();
				cp.activarVista(ConstantesVistas.VISTA_SELECCCION);
			}
		});
		
		JButton btnCancelar = new JButton("Volver");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cp.resetearDatos();
				cp.activarVista(ConstantesVistas.VISTA_INICIAL);
			}
		});
		
		JButton btnCrear = new JButton("Crear");
		btnCrear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					jButtonCrearActionPerformed(e);
			}
		});
		
	    JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonBuscarActionPerformed(e,btnBuscar);
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNombre)
								.addComponent(lblRutaDestino)
								.addComponent(lblNewLabel))
							.addGap(30)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(textAreaRutasaDestino)
								.addComponent(textFieldNombre)
								.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(btnSeleccionar)
								.addComponent(btnBuscar, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE))
							.addGap(82))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(116)
							.addComponent(btnCancelar)
							.addGap(85)
							.addComponent(btnCrear, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(25)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSeleccionar))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRutaDestino)
						.addComponent(textAreaRutasaDestino, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnBuscar))
					.addGap(8)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNombre)
						.addComponent(textFieldNombre, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCrear, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnCancelar))
					.addGap(24))
		);
		
		 textAreaRutas = new JTextArea();
		textAreaRutas.setEditable(false);
		scrollPane.setViewportView(textAreaRutas);
		setLayout(groupLayout);
		
	}// </editor-fold>//GEN-END:initComponents
	/**
     * Método que guarda los parámetros.
     */
	private void  guardarParametros() {
		System.out.println("Parametros en la ventana ");
		System.out.println(textFieldNombre.getText());
		System.out.println(textAreaRutasaDestino.getText());
		System.out.println("Fin Parametros en la ventana");
		
		cp.setNombreArchivo(textFieldNombre.getText());
		if(textAreaRutasaDestino.getText().length() == 0)  cp.setRutaDestino("./");
		else cp.setRutaDestino(textAreaRutasaDestino.getText());
		
	}
	/**
     * Metodo que implementa la acción del boton
     * @param evt El parámetro e es un objeto del tipo java.awt.event.ActionEvent
     * @param buscar El parámetro buscar es un objeto del tipo jButton
     * */
    private void jButtonBuscarActionPerformed(java.awt.event.ActionEvent evt,JButton buscar) {
    	JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("./"));
		fc.setDialogTitle("");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if(fc.showOpenDialog(buscar) == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			if(f.exists()) {
				textAreaRutasaDestino.setText(f.getAbsolutePath());
			}else {
				JOptionPane.showMessageDialog(this,"El destino no existe: \n"+ f.getAbsolutePath() ,"Advertencia",JOptionPane.WARNING_MESSAGE);
			}
		}
    }
    /**
     * Metodo que implementa la acción del boton
     * @param evt El parámetro e es un objeto del tipo java.awt.event.ActionEvent
     * */
    private void jButtonCrearActionPerformed(java.awt.event.ActionEvent evt){
    	guardarParametros(); 
    	File f;
    	if(cp.getRutas().size() == 0) {
    		JOptionPane.showMessageDialog(this,"No hay ficheros a comprimir","Advertencia",JOptionPane.WARNING_MESSAGE);
    	}
    	else if(!( f = new File(cp.getRutaDestino())).exists()) {
    		JOptionPane.showMessageDialog(this,"El destino no existe: \n"+ f.getAbsolutePath() ,"Advertencia",JOptionPane.WARNING_MESSAGE);
    	}
    	else if(textFieldNombre.getText().length() == 0) {
    		JOptionPane.showMessageDialog(this,"Se ha de introducir un nombre." ,"Advertencia",JOptionPane.WARNING_MESSAGE);
    	}
    	else {
    			String s= new String();
    			s = textFieldNombre.getText();
    		    char[] ilegal = { '/', '%', ' ','.' ,'\'', '?', '*', '\\', '<', '>', '|', '\"', ':', ';' };
    		    
    		    boolean encontrado = false;
    		    for(int i=0; i<ilegal.length && !encontrado; ++i)
    		    	if(s.contains(String.valueOf(ilegal[i]))) encontrado = true;
    		    
    		    if (encontrado) { 
	    	    	 JOptionPane.showMessageDialog(this,"El nombre del archivo contine caracteres no permitidos\n {vacio,coma,punto, ;, \\, /, %, ', ?, *, <, >, |, :, }","",JOptionPane.DEFAULT_OPTION); 
    		    }else {
    		    	if(new File(cp.getRutaDestino() + "/"+ s).exists()) {
    		    		 JOptionPane.showMessageDialog(this,"Ya existe una carpeta con el nombre asignado: " + s ,"",JOptionPane.DEFAULT_OPTION);	
    		    	}
    		    	else {
    		    		cp.setNombreArchivo(s);
    		    		if(cp.iniciarDescompresion() == 0 ) {
    		    			JOptionPane.showMessageDialog(this,"Se ha descomprimido correctamente.","",JOptionPane.DEFAULT_OPTION); 
			    			cp.setRutas(new ArrayList<String>());
			    			actualizarDatos();
			    			textFieldNombre.setText("");
			    			textAreaRutas.setText("");
			    			textAreaRutasaDestino.setText("");
    		    		}
    		    	}
    		    }
    	}    
    }
    /**
     * Método que actuliza los datos de los campos vacios con información por defecto, se pondra el nombre y la direccion de la primera ruta de la lista de ficheros seleccionados.
     * */
    public void actualizarDatos() {
    	//Evaluacion de las rutas
    	ArrayList<String> aux =  cp.getRutas();
    	int size = aux.size();
        System.out.println("mostrarRutas,size: "+size);
        if (size == 0)textAreaRutas.setText("");
        else {
        	if (size == 1)textAreaRutas.setText(aux.get(0));
        	else if (size == 2)textAreaRutas.setText(aux.get(0)+", "+aux.get(1));
        	else textAreaRutas.setText(aux.get(0)+", "+aux.get(1)+", ...");
        	
        	if(textAreaRutasaDestino.getText().length() == 0) {
        		File carpetaPadre = new File(aux.get(0)).getParentFile();
        		if (carpetaPadre != null) {
        			textAreaRutasaDestino.setText(carpetaPadre.getAbsolutePath());
        		}
        	}
        	if(textFieldNombre.getText().length() == 0) {
        		String[] nombre = new File(aux.get(0)).getName().split("\\.");
        		textFieldNombre.setText(nombre[0]);
        	}
        }   	
    }
}
