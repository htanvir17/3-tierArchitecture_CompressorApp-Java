package Presentacion;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;
import javax.swing.JSeparator;


/**
 * Esta clase VistaConfiguaracionAlgoritmo implementa los métodos necesarios para poder mostrar la vista para configuarar los algoritmos.
 * 
 */

public class VistaConfiguracionAlgoritmo extends JPanel {

	private CtrlPresentacion cp;
	private String algTexto;
	private int calidad;
	
	
	/**
     * Creadora de la clase VistaConfiguracionAlgortimo.
     * @param cp El parámetro cp es un objeto de la clase CtrlPresentacion
     */
	public VistaConfiguracionAlgoritmo(CtrlPresentacion cp) {
		this.cp = cp;
		this.algTexto = cp.getAlgoritmo();
		this.calidad = cp.getCalidad();
		this.dSampling = cp.getdSampling();
		initComponents();
		pintarComboBoxAlgText();
		pintarDownSampling();
//		pintarsliderCalidad();
	}
	private JComboBox<String> comboBoxAlgText;
	private JComboBox<Object> comboBoxDSampling;
	private JSlider sliderCalidad = null;
	private int dSampling;
	/**
     * Método que inicializa los componentes para poder ver la VistaConfiguracionAlgoritmo.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents(){
		
		comboBoxAlgText = new JComboBox<String>();
		comboBoxAlgText.setModel(new DefaultComboBoxModel<>(new String[] {"LZSS", "LZ78", "LZW"}));
		pintarComboBoxAlgText();

		JComboBox<Object> comboBoxAlgImg = new JComboBox<Object>();
		comboBoxAlgImg.setModel(new DefaultComboBoxModel<Object>(new String[] {"JPEG"}));
		
		JLabel lblNewLabel = new JLabel("Algortimo texto");
		
		JLabel lblNewLabel_1 = new JLabel("Algortimo imagen");
		pintarsliderCalidad();
		
		JLabel lblCalidad = new JLabel("Calidad");
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(calidad);
				pintarsliderCalidad();
				pintarComboBoxAlgText();
				pintarDownSampling();
				cp.activarVista(ConstantesVistas.VISTA_COMPRIMIR);
			}
		});
		
		JLabel lblDownsampling = new JLabel("Downsampling");
		
		JLabel lblvalueSpinner = new JLabel(String.valueOf(calidad));
		
		
		
		
		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				algTexto = (String)comboBoxAlgText.getSelectedItem();
				calidad = sliderCalidad.getValue();
				System.out.println(calidad);
				dSampling = 2 * comboBoxDSampling.getSelectedIndex();
				cp.setAlgoritmo(algTexto);
				cp.setCalidad(calidad);
				cp.setdSampling(dSampling);
				cp.activarVista(ConstantesVistas.VISTA_COMPRIMIR);
			}
		});
		
		sliderCalidad = new JSlider();
		sliderCalidad.setMinimum(1);
		sliderCalidad.setMaximum(100);
		sliderCalidad.setValue(calidad);
		
		sliderCalidad.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				//calidad = sliderCalidad.getValue();
				//System.out.println(calidad);
				lblvalueSpinner.setText(String.valueOf(sliderCalidad.getValue()));
			}
		});
		comboBoxDSampling = new JComboBox<Object>();
		comboBoxDSampling.setModel(new DefaultComboBoxModel<Object>(new String[] {"4:2:0", "4:2:2", "4:4:4"}));
		
		JSeparator separator = new JSeparator();
		
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(22)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(separator, GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblNewLabel)
									.addGap(27)
									.addComponent(comboBoxAlgText, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
									.addGap(146))
								.addGroup(groupLayout.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(lblNewLabel_1)
										.addComponent(lblCalidad)
										.addComponent(lblDownsampling))
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(comboBoxDSampling, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
										.addGroup(groupLayout.createSequentialGroup()
											.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
												.addComponent(btnGuardar)
												.addComponent(sliderCalidad, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(lblvalueSpinner, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
										.addComponent(comboBoxAlgImg, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE))
									.addGap(10))))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(80)
							.addComponent(btnCancelar)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(comboBoxAlgText, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 7, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel_1)
								.addComponent(comboBoxAlgImg, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblCalidad)
								.addComponent(sliderCalidad, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(lblvalueSpinner, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDownsampling)
						.addComponent(comboBoxDSampling, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(24)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancelar)
						.addComponent(btnGuardar))
					.addGap(39))
		);
		setLayout(groupLayout);
	}// </editor-fold>//GEN-END:initComponents

	/**
     * Método que te pinta la calidad del algoritmo jpeg.
     */
	private void pintarsliderCalidad() {
		//sliderCalidad.setModel(new sliderNumberModel(calidad, 0, 100, 5));
		if(sliderCalidad != null) sliderCalidad.setValue(calidad);
	}

	 /**
     * Método que te pinta las diferentes opciones para escoger los diferentes algoritmos.
     */
	private void pintarComboBoxAlgText() {
		if(algTexto == "LZSS") comboBoxAlgText.setSelectedIndex(0);
		else if(algTexto == "LZ78") comboBoxAlgText.setSelectedIndex(1);
		else if(algTexto == "LZW") comboBoxAlgText.setSelectedIndex(2);
	}
	/**
     * Método que te pinta el desplegable para elegir que algoritmo quieres utilizar.
     */
	private void pintarDownSampling() {
		if(dSampling == 0) comboBoxDSampling.setSelectedIndex(0);
		else if(dSampling == 2) comboBoxDSampling.setSelectedIndex(1);
		else if(dSampling == 4) comboBoxDSampling.setSelectedIndex(2);
	}
}
