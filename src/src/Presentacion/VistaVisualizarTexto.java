package Presentacion;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JEditorPane;
/**
 * Esta clase VistaVisualizarTexto implementa los metodos necesarios para poder mostrar un texto.
 */
public class VistaVisualizarTexto extends JPanel {
	
	private String texto;
	private JEditorPane textArea;
	/**
     * Creadora de la clase VistaVisualizarTexto.
     * @param texto  Este parametro contine el contenido el texto a representar en la vista
     * */
	public VistaVisualizarTexto(String texto) {
		this.texto =texto;
		initComponents();
		pintar_texto();
	}
	private void pintar_texto() {
		textArea.setText(texto);
	}
	/**
     * Método que inicializa los componentes para poder ver la VistaVisualizacio.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 866, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
		);
		
		textArea = new JEditorPane();
		scrollPane.setViewportView(textArea);
		setLayout(groupLayout);
	}// </editor-fold>//GEN-END:initComponents
}
