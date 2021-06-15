package Presentacion;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JPanel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

/**
 * Esta clase VistaSeleccion implementa los metodos necesarios para poder mostrar la vista para seleccionar los archivos y/o carpetas.
 */

public class VistaSeleccion extends JPanel {

	/*objeto controlador de presentacion*/
    private CtrlPresentacion cp;
    
    private JList<String> list;
    
    private long tamano;
    private boolean esVisualizar;
    private JLabel lbltamano;
   // JList<String> jLista;
    
    
    DefaultListModel<String> modeloLista;  
    /**
     * Creadora de la clase VistaSeleccion.
     * @param cp El par‡metro cp es un objeto de la clase CtrlPresentacion
     * @param rutas El par‡metro rutas es un ArrayList<String> donde se guardan las rutas.
     */
	public VistaSeleccion(CtrlPresentacion cp,ArrayList<String> rutas) {
		this.cp = cp;
		//this.rutas = rutas;
		modeloLista = new DefaultListModel<String>();
	    list = new JList<String>();
		list.setModel(modeloLista);
		esVisualizar = cp.getEs_comprimir();
		initComponents();
		this.tamano =  cp.calcularTamano(rutas);
		pintaRutas(rutas);
		pintarTamano();
	}
	/**
	 * Metodo que se encarga de completar las rutas 
	 * */
	private void  pintaRutas(ArrayList<String> rutas) {
		for(String r:rutas) {
			modeloLista.addElement(r);
		}
	}
	/**
     * Método que inicializa los componentes para poder ver la VistaSeleccion.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {
		JButton btnAgregar = new JButton("Agregar");
		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			     jButtonAgregarActionPerformed(e,btnAgregar);
			}
		});
		
		JButton btnQuitar = new JButton("Quitar");
		btnQuitar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonQuitarActionPerformed(e);
			}
		});
		
		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonGuardarActionPerformed(e);
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		
		JLabel lblTa = new JLabel("Tama\u00F1o:");
		
		 lbltamano = new JLabel("0");
		 JButton VisualizarNavegar;
		 if(esVisualizar) {
			 VisualizarNavegar = new JButton("Visualizar");
		 }
		 else VisualizarNavegar = new JButton("Navegar");
		 
		 VisualizarNavegar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(esVisualizar) { 
					jButtonVisualizarActionPerformed(e);//cuando es la seleccion de ficheros a comprimir
				}else {
					jButtonNavegarActionPerformed(e);//Cuando es la seleccion de comprimidos
				}
			}
		});
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(24)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblTa)
							.addGap(18)
							.addComponent(lbltamano, GroupLayout.PREFERRED_SIZE, 139, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 310, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(btnAgregar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnQuitar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnGuardar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(VisualizarNavegar))))
					.addContainerGap(26, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(28)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 274, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblTa)
								.addComponent(lbltamano, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(62)
							.addComponent(btnAgregar)
							.addGap(32)
							.addComponent(btnQuitar)
							.addGap(34)
							.addComponent(btnGuardar)
							.addGap(33)
							.addComponent(VisualizarNavegar)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		/*
		JList list = new JList();
		
		modeloLista.add(0,"Hola");
		list.setModel(modeloLista);
		*/
		/*
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"1", "1", "1", "1", "1", "1", "1", "1", "2", "2", "2", "2", "2", "2", "2", "2", "2", "3", "4", "5", "2"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});*/
		scrollPane.setViewportView(list);
		setLayout(groupLayout);
	}// </editor-fold>//GEN-END:initComponents
	/**
     * Método en el cual se implementa la acción del botón para quitar archivos y/o carpetas que hemos seleccionado
     * @param e El par‡metro e es un objeto del tipo ActionEvent
     */
	private void jButtonQuitarActionPerformed(ActionEvent e) {
		int seleccionados[] = list.getSelectedIndices();
		Arrays.sort(seleccionados);
		ArrayList<String> rutasAQuitar = new ArrayList<>();
		for(int i = (seleccionados.length - 1) ; i >=0 ;i--) {
			rutasAQuitar.add(modeloLista.get(seleccionados[i]));
			modeloLista.remove(seleccionados[i]);
		}
		tamano -= cp.calcularTamano(rutasAQuitar);
		pintarTamano();
	}
	/**
     * MŽtodo en el cual muestra por en la pantalla el tama–o de todos los archivos seleccionados
     */
	
	private void pintarTamano(){
		if( tamano > 1073741824) {
			lbltamano.setForeground(Color.red);
			//mas de un giga ha de saltar una advertencia.
		}
		else lbltamano.setForeground(Color.black);
		
		lbltamano.setText(String.valueOf(tamano));
	}

	/**
	 * Metodo que implementa la acción del boton "Agregar", si se viene de comprimir solo permitira sseleciconar carpetas y ficheros .txt),si viene de descomprimir solo dejara selecionar archivos ".comp"
	 * */	
	private void jButtonAgregarActionPerformed(java.awt.event.ActionEvent evt,JButton b) {
		JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(true);
		fc.setCurrentDirectory(new File("./"));
		fc.setDialogTitle("Compresor");
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		//FileNameExtensionFilter filtro = new FileNameExtensionFilter("*.TXT", "txt");
		//fc.setFileFilter(filtro);
		boolean esComprimir = cp.getEs_comprimir();
		if(esComprimir) {
			fc.addChoosableFileFilter(new FileNameExtensionFilter("*.TXT","txt"));
			fc.addChoosableFileFilter(new FileNameExtensionFilter("*.PPM","ppm"));
		}
		else fc.addChoosableFileFilter(new FileNameExtensionFilter("*.COMP","comp"));
		fc.setAcceptAllFileFilterUsed(false);
	
		if(fc.showOpenDialog(b) == JFileChooser.APPROVE_OPTION) {
				File[] seleccionados = fc.getSelectedFiles();
				ArrayList<String> validas  = new ArrayList<>();
				String no_validas = new String();
				//String texto_mensaje ="";
				boolean  ya_mostrado_carpeta_descomprimir = false;
				for(File fil: seleccionados) {
				  if(fil.exists()) {
					if(!esComprimir && fil.isDirectory()) {
						if(!ya_mostrado_carpeta_descomprimir) {
							JOptionPane.showMessageDialog(this,"No se puede seleccionar carpetas para descomprimir","Advertencia",JOptionPane.WARNING_MESSAGE);
							ya_mostrado_carpeta_descomprimir = true;
						}
					}  
					else {
						validas.add(fil.getAbsolutePath());
						modeloLista.addElement(fil.getAbsolutePath());
					}
				  }else no_validas = no_validas.concat(fil.getAbsolutePath()+"\n");
				}
				if(no_validas.length() > 0) {
 					JOptionPane.showMessageDialog(this,"Las siguientes rutas no son validas:\n"+ no_validas,"Advertencia",JOptionPane.WARNING_MESSAGE);
				}
				
				
				//es muy costoso el calcular el tamaño de una carpeta, el de ficheros es mas simple
				tamano += cp.calcularTamano(validas);
				pintarTamano();
		}
	}
	
	/**
     * Método que implementa la acci—n del boton "Guardar"
     @param e El par‡metro e es un objeto del tipo ActionEvent
     * */
	private void jButtonGuardarActionPerformed(ActionEvent e) {
		
		if( tamano > 1073741824) {
			JOptionPane.showMessageDialog(this,"El tamaño de los ficheros no puede ser mayor a 1GB (1073741824 bytes)  ","Advertencia",JOptionPane.WARNING_MESSAGE);
		}
		else {
			int n = modeloLista.getSize();
			ArrayList<String> rutasSeleccionadas = new ArrayList<>();
			for(int i = 0 ; i < n ; i++) {
				rutasSeleccionadas.add(modeloLista.get(i));
			}
			cp.setRutas(rutasSeleccionadas);
			if(cp.getEs_comprimir()) {
				cp.activarVista(ConstantesVistas.VISTA_COMPRIMIR);
			}else {
				cp.activarVista(ConstantesVistas.VISTA_DESCOMPRIMIR);
			}
			cp.actualizarDatosComprimirDescomprimir();
		}	
	}
	
	
	/**
	 * Método implementa la acción del boton visualizar que permite la visualizacion de textos(.txt) y imagenes(.ppm) del lista de selecionados.
	 *  @param e El par‡metro e es un objeto del tipo ActionEvent
	 * */
	
	private void jButtonVisualizarActionPerformed(ActionEvent e) {
		
		int seleccionados[] = list.getSelectedIndices();
		if(seleccionados.length != 0) {
			Arrays.sort(seleccionados);
			ArrayList<String> rutasAVisualizar = new ArrayList<>();
			for(int i = 0 ; i < seleccionados.length ;i++) {
				if(new File(modeloLista.get(seleccionados[i])).isDirectory()) {
					JOptionPane.showMessageDialog(this, "No se puede visualizar carpetas solo ficheros (.txt o .ppm)");
				}	
				else rutasAVisualizar.add(modeloLista.get(seleccionados[i]));
			}
			cp.setRutasAVisualizar(rutasAVisualizar);
			cp.activaFrameVisualizacion();
		}
	}
	
	/**
	 * Método que sirve paa navega al interior de un comprimido ".comp· 
	 * */
	private void jButtonNavegarActionPerformed(ActionEvent e){
	
		
	}
	
	
}