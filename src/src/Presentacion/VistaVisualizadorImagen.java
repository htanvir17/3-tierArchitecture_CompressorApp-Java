package Presentacion;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;


/**
 * Esta clase VistaVisualizadorImagen implementa los metodos necesarios para poder mostrar una imagen.
 */
public class VistaVisualizadorImagen extends JScrollPane {

	private JLabel imagen;
	
	JScrollPane scrollPanel; 
	/**
     * Creadora de la clase VistaVisualizadorImagen.
     * @param BufferedImage  Este parametro contine el contenido de la imagen a representar en la vista
     * */
	public VistaVisualizadorImagen(BufferedImage bImageFromConvert) {
		imagen = new JLabel(new ImageIcon(bImageFromConvert));
		super.setViewportView(imagen);
	}
}