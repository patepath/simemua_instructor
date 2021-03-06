/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ctsim.simemua_instructor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Piyatat-MBP
 */
public class ATCDisplayFrame extends javax.swing.JFrame implements ActionListener {
    
    private Graphics2D g2;
    private final Timer timer;
    private int x;
    private int y;
    
    private final Map<String, SpeedMeterLocation> meters;
    private BufferedImage imgDraftSpeed; 
    
    
    /**
     * Creates new form ATCDisplayFrame
     */
    public ATCDisplayFrame() {
        initComponents();
        
        timer = new Timer(100, this);
        timer.start();
        
        meters = new HashMap<>();
        initImage();
        createMeters();
    }
    
    private void initGraphics() {
        try {
            x = (int) viewPanel.getMousePosition().getX();
            y = (int) viewPanel.getMousePosition().getY();
        } catch (Exception ex) {
            x = 0;
            y = 0;
        
        }
        
        //Test Rotate Image.
        int drawLocationX = 300;
        int drawLocationY = 300;
        
        double rotationRequired = Math.toRadians(45);
        double locationX = imgDraftSpeed.getWidth() / 2;
        double locationY = imgDraftSpeed.getHeight() / 2;
        
        AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        
        g2.drawImage(op.filter(imgDraftSpeed, null), drawLocationX, drawLocationY, null);
        
        //g2.drawImage(imgDraftSpeed, 250, 150, this);
        //g2.drawOval(250, 150, 350, 350);
        g2.drawString("(" + x + ", " + y + ")", 15, 15);
        
        drawMeters();
        
        
    }
    
    private void drawMeters() {
        ArrayList<SpeedMeterLocation> mets = new ArrayList(meters.values());
        
        mets.stream().forEach((meter) -> {
            g2.setColor(Color.WHITE);
            g2.drawLine(meter.getX1(), meter.getY1(), meter.getX2(), meter.getY2());
        });
    }
    
    private void initImage() {
        try {
            imgDraftSpeed = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/speed_meter.png").getFile()));
        } catch (Exception ex) {
            Logger.getLogger(ATCDisplayFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void createMeters() {
        File fXmlFile;
        DocumentBuilderFactory dbFactory;
        DocumentBuilder dBuilder;
        Document doc;
        NodeList nodeList;
        Element element;
        
        try {
            fXmlFile = new File(this.getClass().getClassLoader().getResource("config/atc_display.xml").getFile());
            dbFactory = DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            nodeList = doc.getElementsByTagName("meter");
            
            for(int i=0 ; i < nodeList.getLength(); i++) {
                element = (Element) nodeList.item(i);
                meters.put(element.getAttribute("id"), new SpeedMeterLocation());
                meters.get(element.getAttribute("id")).setX1(Integer.parseInt(element.getElementsByTagName("x1").item(0).getTextContent()));
                meters.get(element.getAttribute("id")).setY1(Integer.parseInt(element.getElementsByTagName("y1").item(0).getTextContent()));
                meters.get(element.getAttribute("id")).setX2(Integer.parseInt(element.getElementsByTagName("x2").item(0).getTextContent()));
                meters.get(element.getAttribute("id")).setY2(Integer.parseInt(element.getElementsByTagName("y2").item(0).getTextContent()));
            }
        } catch (Exception ex) {
            Logger.getLogger(DriverDeskFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        viewPanel = new javax.swing.JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g2 = (Graphics2D) g;
                initGraphics();
            }
        };

        viewPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewPanelMouseClicked(evt);
            }
        });

        // Code of sub-components - not shown here

        // Layout setup code - not shown here

        // Code adding the component to the parent container - not shown here
        ;

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        viewPanel.setPreferredSize(new java.awt.Dimension(1024, 768));
        viewPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewPanelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout viewPanelLayout = new javax.swing.GroupLayout(viewPanel);
        viewPanel.setLayout(viewPanelLayout);
        viewPanelLayout.setHorizontalGroup(
            viewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1024, Short.MAX_VALUE)
        );
        viewPanelLayout.setVerticalGroup(
            viewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 768, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(viewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(viewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void viewPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewPanelMouseClicked
        try {
            x = evt.getX();
            y = evt.getY();
            
            System.out.println("x : " + x + " y : " + y);
        } catch (Exception ex) {
            Logger.getLogger(ATCDisplayFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_viewPanelMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ATCDisplayFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ATCDisplayFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ATCDisplayFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ATCDisplayFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new ATCDisplayFrame().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel viewPanel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        viewPanel.repaint();
    }
}
