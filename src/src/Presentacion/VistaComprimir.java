package Presentacion;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextArea;
import javax.swing.JPasswordField;
import java.awt.Color;

public class VistaComprimir extends JPanel {

	/**objeto controlador de presentacion*/
    private CtrlPresentacion cp;
    private JPasswordField textFieldContrasena;
    private JTextField textFieldNombre;
    private JTextField textAreaRutasaDestino;
    private JTextArea textAreaRutas;
    
    /**
     * Creadora de la clase VistaComprimir.
     * @param cp El parámetro cp es un objeto de la clase CtrlPresentacion
     */
	public VistaComprimir(CtrlPresentacion cp) {	
		this.cp = cp;
		initComponents();
		
	}
	/**
     * Método que inicializa los componentes para poder ver la VistaComprimir.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {
		
		textFieldContrasena = new JPasswordField();
		textFieldContrasena.setColumns(8);
		textFieldContrasena.addKeyListener(new KeyListener(){
		
			public void keyTyped(java.awt.event.KeyEvent e){
				if (textFieldContrasena.getPassword().length == 16)
			     e.consume();
			}

			@Override
			public void keyPressed(java.awt.event.KeyEvent arg0) {
				
			}

			@Override
			public void keyReleased(java.awt.event.KeyEvent arg0) {				
			}
		});

		
		
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
		
		JLabel lblContrasena = new JLabel("Contrase\u00F1a:");
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
		
		JButton btnAlgoritmo = new JButton("Algoritmo");
		btnAlgoritmo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//guardarParametros();
				cp.activarVista(ConstantesVistas.VISTA_CONFIGURACION_ALGORITMO);
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
		
		JLabel lblOpciones = new JLabel("Opciones:");
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNombre)
								.addComponent(lblRutaDestino)
								.addComponent(lblOpciones)
								.addComponent(lblNewLabel))
							.addGap(26)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(textAreaRutasaDestino)
								.addComponent(textFieldNombre)
								.addGroup(groupLayout.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(btnAlgoritmo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(textFieldContrasena)
										.addComponent(btnCancelar)))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)))
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(btnSeleccionar)
										.addComponent(btnBuscar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
									.addGap(82))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(15)
									.addComponent(btnCrear, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE))))
						.addComponent(lblContrasena))
					.addContainerGap(29, Short.MAX_VALUE))
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
					.addGap(13)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblContrasena)
						.addComponent(textFieldContrasena, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAlgoritmo)
						.addComponent(lblOpciones))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancelar)
						.addComponent(btnCrear, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
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
		System.out.println(String.valueOf(textFieldContrasena.getPassword()));
		System.out.println(textFieldNombre.getText());
		System.out.println(textAreaRutasaDestino.getText());
		System.out.println("Fin Parametros en la ventana");
		
		cp.setNombreArchivo(textFieldNombre.getText());
		cp.setContrasena(String.valueOf(textFieldContrasena.getPassword()));
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
    		    	if(new File(cp.getRutaDestino() + "/"+ s+".comp").exists()) {
    		    		 JOptionPane.showMessageDialog(this,"Ya existe un comprimido con el nombre asignado: " + s +".comp\n Elige otro nombre.","",JOptionPane.DEFAULT_OPTION);	
    		    	}
    		    	else {
    		    		/*cp.setNombreArchivo(s);
    		    		String texto = "";*/
    		    		if(cp.iniciarCompresion() == 0) {
    		    			JOptionPane.showMessageDialog(this,"Se ha comprimido correctamente.","",JOptionPane.DEFAULT_OPTION);
    		    			cp.setRutas(new ArrayList<String>());
    		    			actualizarDatos();
    		    			textFieldNombre.setText("");
    		    			textFieldContrasena.setText("");
    		    			textAreaRutas.setText("");
    		    			textAreaRutasaDestino.setText("");
    		    		}
    		    		/*{
    		    			texto = "Se ha comprimido correctamente";
    		    			//JOptionPane.showMessageDialog(this,,"",JOptionPane.DEFAULT_OPTION);
    		    		}else
    		    		texto = texto + "\nDesea continuar o volver a inicio";
    		    		String opciones_continuar[] = {"Continuar","Inicio"};
		    	    	int botonseleccionado = JOptionPane.showOptionDialog(this, texto, "Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION, null, opciones_continuar, opciones_continuar[0]);
		    	    	if(botonseleccionado != 0) cp.activarVista(ConstantesVistas.VISTA_INICIAL);*/
    		    	}
    		    }
    	}    
    }
    /**
     * Metodo que actuliza los datos de los campos vacios con información por defecto, se pondra el nombre y la direccion de la primera ruta de la lista de ficheros seleccionados.
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
