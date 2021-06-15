
package Presentacion;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Esta clase VistaInicial implementa los métodos necesarios para poder mostrar la vista inicial.
 */
public class VistaInicial extends JPanel{

    /*objeto controlador de presentacion*/
    private CtrlPresentacion cp;
    /*Imagen del fondo*/
    private BufferedImage img;
    
    /*Creadora por defecto*/
    /**
     * Creadora de la clase Clasificador.
     * @param cp El parámetro cp es un objeto de la clase CtrlPresentacion
     */
    public VistaInicial(CtrlPresentacion cp) {
        this.cp = cp;  
        /*try {
        	File f = new File("./");
        	System.out.println(f.getAbsolutePath());
            InputStream resource = VistaInicial.class.getResourceAsStream("C:\\Users\\kenny\\eclipse-workspace\\PROP_v2\\gestor.jpg");
            this.img = ImageIO.read(resource);
        }
        catch(Exception e) {
        	e.printStackTrace();
            System.out.println(e.toString());
        }*/
        initComponents();
    }
    
  
    /**
     * Método que inicializa los componentes para poder ver la VistaInicial.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Image aux = this.img.getScaledInstance(this.getWidth(), this.getHeight(),Image.SCALE_SMOOTH);
        g.drawImage(this.img, 0, 0, this);
    }
    
    /**
     * Método que inicializa los componentes para poder ver la VistaInicial.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelCompresor = new javax.swing.JLabel();
        jButtonComprimir = new javax.swing.JButton();
        jButtonDescomprimir = new javax.swing.JButton();

        jLabelCompresor.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabelCompresor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCompresor.setText("COMPRESOR");
        jLabelCompresor.setToolTipText("");

        jButtonComprimir.setText("Comprimir");
        jButtonComprimir.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButtonComprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonComprimirActionPerformed(evt);
            }
        });

        jButtonDescomprimir.setText("Descomprimir");
        jButtonDescomprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDescomprimirActionPerformed(evt);
            }
        });
        
        button = new JButton("?");
        button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
        		jButtonAyudaActionPerformed(evt);
        	}
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap(163, Short.MAX_VALUE)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(Alignment.TRAILING, layout.createSequentialGroup()
        					.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        						.addComponent(jButtonDescomprimir, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jButtonComprimir, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE))
        					.addGap(155))
        				.addGroup(Alignment.TRAILING, layout.createSequentialGroup()
        					.addComponent(jLabelCompresor)
        					.addGap(137))
        				.addComponent(button, Alignment.TRAILING)))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addComponent(button)
        			.addGap(20)
        			.addComponent(jLabelCompresor)
        			.addGap(30)
        			.addComponent(jButtonComprimir, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addComponent(jButtonDescomprimir, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(92, Short.MAX_VALUE))
        );
        this.setLayout(layout);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método en el cual se implementa la acción del botón para mostrar ayuda
     * @param evt El parámetro evt es un objeto del tipo java.awt.event.ActionEvent
     */
    
    private void jButtonAyudaActionPerformed(ActionEvent evt) {
    	cp.activarVista(ConstantesVistas.VISTA_AYUDA);
	}


	/*comportamineto cuando indica comprimir*/
    /**
     * Método en el cual se implementa la acción del botón para comprimir
     * @param evt El parámetro evt es un objeto del tipo java.awt.event.ActionEvent
     */
    private void jButtonComprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonComprimirActionPerformed
        cp.setEs_comprimir(true);
        cp.activarVista(ConstantesVistas.VISTA_COMPRIMIR);
    }//GEN-LAST:event_jButtonComprimirActionPerformed

    /*comportamineto cuando indica descomprimir*/
    /**
     * Método en el cual se implementa la acción del botón para descomprimir
     * @param evt El parámetro evt es un objeto del tipo java.awt.event.ActionEvent
     */
    private void jButtonDescomprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDescomprimirActionPerformed
        cp.setEs_comprimir(false);
        cp.activarVista(ConstantesVistas.VISTA_DESCOMPRIMIR);
    }//GEN-LAST:event_jButtonDescomprimirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonComprimir;
    private javax.swing.JButton jButtonDescomprimir;
    private javax.swing.JLabel jLabelCompresor;
    private JButton button;
    // End of variables declaration//GEN-END:variables
}
