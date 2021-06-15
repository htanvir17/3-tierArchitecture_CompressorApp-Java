package Presentacion;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.GroupLayout.Alignment;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JSeparator;
import javax.swing.GroupLayout;
import javax.swing.JEditorPane;

/**
 * Esta clase VistaAyuda implementa los métodos necesarios para poder mostrar la vista  de ayuda que te muestra undescripción breve de la aplicación.
 */
public class VistaAyuda extends JPanel {

	/**
     * Referencia al controlador de presentacion.
     */
    private CtrlPresentacion cp;
    private ArrayList<JLabel> cjlb = new ArrayList<JLabel>();
    
    
    /**
     * Creadora de la clase VistaAyuda.
     * @param cp El parámetro cp es un objeto de la clase CtrlPresentacion
     */
    public VistaAyuda(CtrlPresentacion cp) {
    	this.cp = cp;    
            
        initComponents();
        initAyudas();
    }
	/**
	 * Método que inicializa los valores de los campos de VistaAyuda.
	 */
    private void initAyudas() {
    	
    	ArrayList<String> ayudas = this.cp.getAyudas();
    	String[] ayuda = ayudas.get(0).split("\t");
    	cjlb.get(0).setText(ayuda[1]);
    	textoIntro.setText(ayuda[2]);
    	 
        for (int i = 1, j=1; i < ayudas.size(); ++i,j+=2) {
            ayuda = ayudas.get(i).split("\t");
            //String id = ayuda[0]; no es importante ahora
            String titulo = ayuda[1];
            String texto = ayuda[2];
            cjlb.get(j).setText(titulo);
            cjlb.get(j+1).setText(texto);
        }   	
    }
    
    /**
     * Método que inicializa los componentes para poder ver la VistaAyuda.
     */
    private void initComponents() {
    	
    	
    	JButton btnVolver = new JButton("Volver");
    	btnVolver.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			VolverActionPerformed(evt);
    		}
    	});
    	
    	/*importar imagen*/
    	/*String path = "gestor.png";  
    	java.net.URL url = this.getClass().getResource(path);  
    	ImageIcon icon = new ImageIcon(url);  */
    	JLabel imagen = new JLabel();
    	//imagen.setIcon(icon);
    	
    	nombre = new JLabel("Ayuda de Compresor");
    	nombre.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 18));
    	
    	
    	lb1 = new JLabel(".");
    	lb1.setForeground(new Color(210, 105, 30));
    	cjlb.add(lb1);
    	
    	textoIntro = new JEditorPane();
    	textoIntro.setEditable(false);
    	textoIntro.setText(".");
    	
    	separator1 = new JSeparator();
    	
    	lb3 = new JLabel(".");
    	lb3.setForeground(new Color(210, 105, 30));
    	cjlb.add(lb3);
    	
    	lb4 = new JLabel(".");
    	lb4.setFont(new Font("Dialog", Font.PLAIN, 11));
    	cjlb.add(lb4);
    	
    	separator2 = new JSeparator();
    	
    	lb5 = new JLabel(".");
    	lb5.setForeground(new Color(210, 105, 30));
    	cjlb.add(lb5);
    	
    	lb6 = new JLabel(".");
    	lb6.setFont(new Font("Dialog", Font.PLAIN, 11));
    	cjlb.add(lb6);
    	
    	separator3 = new JSeparator();
    	
    	lb7 = new JLabel(".");
    	lb7.setForeground(new Color(210, 105, 30));
    	cjlb.add(lb7);
    	
    	lb8 = new JLabel(".");
    	lb8.setFont(new Font("Dialog", Font.PLAIN, 11));
    	cjlb.add(lb8);
    	
    	javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    	layout.setHorizontalGroup(
    		layout.createParallelGroup(Alignment.LEADING)
    			.addGroup(layout.createSequentialGroup()
    				.addGroup(layout.createParallelGroup(Alignment.LEADING)
    					.addGroup(layout.createParallelGroup(Alignment.LEADING)
    						.addGroup(layout.createSequentialGroup()
    							.addContainerGap()
    							.addGroup(layout.createParallelGroup(Alignment.LEADING)
    								.addGroup(layout.createSequentialGroup()
    									.addComponent(imagen)
    									.addPreferredGap(ComponentPlacement.UNRELATED)
    									.addComponent(nombre))
    								.addGroup(layout.createSequentialGroup()
    									.addGap(12)
    									.addGroup(layout.createParallelGroup(Alignment.LEADING)
    										.addComponent(textoIntro, GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
    										.addComponent(lb1)))))
    						.addGroup(layout.createSequentialGroup()
    							.addGap(23)
    							.addGroup(layout.createParallelGroup(Alignment.LEADING)
    								.addComponent(btnVolver)
    								.addComponent(lb8, GroupLayout.PREFERRED_SIZE, 331, GroupLayout.PREFERRED_SIZE)
    								.addComponent(lb7, GroupLayout.PREFERRED_SIZE, 238, GroupLayout.PREFERRED_SIZE)
    								.addComponent(separator3, GroupLayout.PREFERRED_SIZE, 382, GroupLayout.PREFERRED_SIZE)
    								.addComponent(separator2, GroupLayout.PREFERRED_SIZE, 382, GroupLayout.PREFERRED_SIZE)
    								.addComponent(lb5, GroupLayout.PREFERRED_SIZE, 238, GroupLayout.PREFERRED_SIZE)
    								.addComponent(lb6, GroupLayout.PREFERRED_SIZE, 331, GroupLayout.PREFERRED_SIZE))))
    					.addGroup(layout.createSequentialGroup()
    						.addGap(24)
    						.addGroup(layout.createParallelGroup(Alignment.LEADING)
    							.addComponent(lb3, GroupLayout.PREFERRED_SIZE, 238, GroupLayout.PREFERRED_SIZE)
    							.addComponent(separator1, GroupLayout.PREFERRED_SIZE, 382, GroupLayout.PREFERRED_SIZE)
    							.addComponent(lb4))))
    				.addContainerGap())
    	);
    	layout.setVerticalGroup(
    		layout.createParallelGroup(Alignment.TRAILING)
    			.addGroup(layout.createSequentialGroup()
    				.addGroup(layout.createParallelGroup(Alignment.LEADING)
    					.addComponent(imagen)
    					.addGroup(layout.createSequentialGroup()
    						.addContainerGap()
    						.addComponent(nombre)))
    				.addPreferredGap(ComponentPlacement.RELATED)
    				.addComponent(lb1)
    				.addPreferredGap(ComponentPlacement.RELATED)
    				.addComponent(textoIntro, GroupLayout.PREFERRED_SIZE, 50, Short.MAX_VALUE)
    				.addPreferredGap(ComponentPlacement.UNRELATED)
    				.addComponent(separator1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
    				.addPreferredGap(ComponentPlacement.RELATED)
    				.addComponent(lb3)
    				.addPreferredGap(ComponentPlacement.RELATED)
    				.addComponent(lb4)
    				.addGap(10)
    				.addComponent(separator2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
    				.addPreferredGap(ComponentPlacement.RELATED)
    				.addComponent(lb5)
    				.addPreferredGap(ComponentPlacement.RELATED)
    				.addComponent(lb6, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
    				.addPreferredGap(ComponentPlacement.UNRELATED)
    				.addComponent(separator3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
    				.addPreferredGap(ComponentPlacement.RELATED)
    				.addComponent(lb7)
    				.addPreferredGap(ComponentPlacement.RELATED)
    				.addComponent(lb8, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
    				.addPreferredGap(ComponentPlacement.RELATED)
    				.addComponent(btnVolver)
    				.addGap(15))
    	);
        this.setLayout(layout);
    }
    
    /*comportamineto cuando indica volver*/
    /**
     * Método que implementa la acción del boton volver
     * @param evt El parámetro e es un objeto del tipo java.awt.event.ActionEvent
     * */
    private void VolverActionPerformed(java.awt.event.ActionEvent evt) {
        cp.volverVistaPreviaAyuda();
     }
    
    private JLabel nombre;
    private JLabel lb1;
    private JLabel lb4;
    private JSeparator separator2;
    private JLabel lb5;
    private JLabel lb6;
    private JSeparator separator3;
    private JLabel lb7;
    private JLabel lb8;
    private JLabel lb3;
    private JSeparator separator1;
    private JEditorPane textoIntro;
}
