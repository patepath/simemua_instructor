/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ctsim.simemua_instructor;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author patipat
 */
public class ACarControlPanelFrame extends javax.swing.JFrame implements ActionListener {

    private Graphics2D g2;
    private final Timer timer;
    private int x = 0;
    private int y = 0;

    private boolean mousePressed;

    private Image aCarControlPanelImg;

    private Image mainBreakerOnImg;
    private Image mainBreakerOffImg;

    private Image breakerOnImg;
    private Image breakerOffImg;

    private Image switchByPassOnImg;
    private Image switchByPassOffImg;

    public static final Map<String, Device> DEVS  = new HashMap<>();

    private BufferedImage switchPBPermitOnImg;
    private BufferedImage switchPBPermitOffImg;
    private BufferedImage switchYesNoBlackOnImg;
    private BufferedImage switchYesNoBlackOffImg;
    private BufferedImage switchPBSquareBlackOnImg;
    private BufferedImage switchPBSquareBlackOffImg;
    private BufferedImage switchRotaryBlackIdleImg;
    private BufferedImage switchRotaryYellowIdleOffImg;
    private BufferedImage switchRotaryYellowOnImg;
    private BufferedImage switchRotaryYellowOffImg;
    private BufferedImage switchRotaryYellowIdleOnImg;
    private BufferedImage lampRedOnImg;
    private BufferedImage lampRedOffImg;
    private BufferedImage lampYellowOnImg;
    private BufferedImage lampYellowOffImg;

    private JSONObject jsonObj;

    /**
     * Creates new form ACarControlPanelFrame
     */
    public ACarControlPanelFrame() {
        initComponents();
        super.setLocation(0, 0);
        mousePressed = false;

        initImage();

        timer = new Timer(50, this);
        timer.start();

        
        createDevices();
    }

    private void initImage() {
        try {
            aCarControlPanelImg = ImageIO.read(FileUtils.toFile(this.getClass().getClassLoader().getResource("img/layout_a-car_control_panel.png")));
            mainBreakerOnImg = ImageIO.read(FileUtils.toFile(this.getClass().getClassLoader().getResource("img/main_breaker_on.png")));
            mainBreakerOffImg = ImageIO.read(FileUtils.toFile(this.getClass().getClassLoader().getResource("img/main_breaker_off.png")));
            breakerOnImg = ImageIO.read(FileUtils.toFile(this.getClass().getClassLoader().getResource("img/breaker_on.png")));
            breakerOffImg = ImageIO.read(FileUtils.toFile(this.getClass().getClassLoader().getResource("img/breaker_off.png")));
            switchByPassOnImg = ImageIO.read(FileUtils.toFile(this.getClass().getClassLoader().getResource("img/switch_bypass_on.png")));
            switchByPassOffImg = ImageIO.read(FileUtils.toFile(this.getClass().getClassLoader().getResource("img/switch_bypass_off.png")));
            switchPBPermitOnImg = ImageIO.read(FileUtils.toFile(this.getClass().getClassLoader().getResource("img/switch_pb_permit_on.png")));
            switchPBPermitOffImg = ImageIO.read(FileUtils.toFile(this.getClass().getClassLoader().getResource("img/switch_pb_permit_off.png")));
            switchYesNoBlackOnImg = ImageIO.read(FileUtils.toFile(this.getClass().getClassLoader().getResource("img/switch_yes-no_black_on.png")));
            switchYesNoBlackOffImg = ImageIO.read(FileUtils.toFile(this.getClass().getClassLoader().getResource("img/switch_yes-no_black_off.png")));
            switchPBSquareBlackOnImg = ImageIO.read(FileUtils.toFile(this.getClass().getClassLoader().getResource("img/switch_pb_square_black_on.png")));
            switchPBSquareBlackOffImg = ImageIO.read(FileUtils.toFile(this.getClass().getClassLoader().getResource("img/switch_pb_square_black_off.png")));
            switchRotaryBlackIdleImg = ImageIO.read(FileUtils.toFile(this.getClass().getClassLoader().getResource("img/switch_rotary_black_idle.png")));
            switchRotaryYellowIdleOffImg = ImageIO.read(FileUtils.toFile(this.getClass().getClassLoader().getResource("img/switch_rotary_yellow_idle_off.png")));
            switchRotaryYellowOnImg = ImageIO.read(FileUtils.toFile(this.getClass().getClassLoader().getResource("img/switch_rotary_yellow_on.png")));
            switchRotaryYellowIdleOnImg = ImageIO.read(FileUtils.toFile(this.getClass().getClassLoader().getResource("img/switch_rotary_yellow_idle_on.png")));
            switchRotaryYellowOffImg = ImageIO.read(FileUtils.toFile(this.getClass().getClassLoader().getResource("img/switch_rotary_yellow_off.png")));
            lampRedOnImg = ImageIO.read(FileUtils.toFile(this.getClass().getClassLoader().getResource("img/lamp_red_on.png")));
            lampRedOffImg = ImageIO.read(FileUtils.toFile(this.getClass().getClassLoader().getResource("img/lamp_red_off.png")));
            lampYellowOnImg = ImageIO.read(FileUtils.toFile(this.getClass().getClassLoader().getResource("img/lamp_yellow_on.png")));
            lampYellowOffImg = ImageIO.read(FileUtils.toFile(this.getClass().getClassLoader().getResource("img/lamp_yellow_off.png")));

        } catch (IOException ex) {
            Logger.getLogger(ACarControlPanelFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initGraphics() {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        try {
            x = (int) viewPanel.getMousePosition().getX();
            y = (int) viewPanel.getMousePosition().getY();

        } catch (Exception ex) {
            x = 0;
            y = 0;
        }

        g2.drawImage(aCarControlPanelImg, null, this);
        //drawDevices();
        drawSwitchRotary();

        g2.drawString("(" + x + ", " + y + ")", 10, 10);
    }

    private void drawDevices() {
        ArrayList<Device> devs = new ArrayList(DEVS.values());

        devs.stream().forEach((dev) -> {
            if (dev.getImgCurr() != null) {
                g2.drawImage(dev.getImgCurr(), dev.getX(), dev.getY(), this);
            }
        });

    }

    private void drawSwitchRotary() {
        ArrayList<Device> sws = new ArrayList(DEVS.values());

        sws.stream().forEach((sw) -> {

            //Check type for some lamp use same picture.
            switch (sw.getType()) {
                case "Switch Rotary Black":
                    if (sw.getImgCurr() == switchYesNoBlackOnImg && !mousePressed) {
                        sw.setImgCurr(switchRotaryBlackIdleImg);
                    } else if (sw.getImgCurr() == switchYesNoBlackOffImg && !mousePressed) {
                        sw.setImgCurr(switchRotaryBlackIdleImg);
                    }
                    break;

                case "Switch Rotary Yellow":
                    if (sw.getImgCurr() == switchRotaryYellowOnImg && !mousePressed) {
                        sw.setImgCurr(switchRotaryYellowIdleOnImg);
                    } else if (sw.getImgCurr() == switchRotaryYellowOffImg && !mousePressed) {
                        sw.setImgCurr(switchRotaryYellowIdleOffImg);
                    }
                    break;

                case "Switch PB Permit":
                    if (!mousePressed & sw.getImgCurr() != switchPBPermitOffImg) {
                        sw.setImgCurr(switchPBPermitOffImg);

                        jsonObj = new JSONObject();
                        JSONObject jsonDevs = new JSONObject();
                        jsonDevs.put(sw.getId(), 0);
                        jsonObj.put("ACAR", jsonDevs);
                        App.OUT_QUEUE.add(jsonObj.toJSONString());
                    }
                    break;

                case "Switch PB Square Black":
                    if (!mousePressed & sw.getImgCurr() != switchPBSquareBlackOffImg) {
                        sw.setImgCurr(switchPBSquareBlackOffImg);

                        jsonObj = new JSONObject();
                        JSONObject jsonDevs = new JSONObject();
                        jsonDevs.put(sw.getId(), 0);
                        jsonObj.put("ACAR", jsonDevs);
                        App.OUT_QUEUE.add(jsonObj.toJSONString());
                    }
                    break;
            }

            if (sw.getImgCurr() != null) {
                g2.drawImage(sw.getImgCurr(), sw.getX(), sw.getY(), this);
            }

        });
    }

    private void createDevices() {
        File fXmlFile;
        DocumentBuilderFactory dbFactory;
        DocumentBuilder dBuilder;
        Document doc;
        NodeList nodeList;
        Element element;

        try {
            fXmlFile = new File(this.getClass().getClassLoader().getResource("config/a_car_control_panel.xml").getFile());
            dbFactory = DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            nodeList = doc.getElementsByTagName("device");

            for (int i = 0; i < nodeList.getLength(); i++) {
                element = (Element) nodeList.item(i);
                DEVS.put(element.getAttribute("id"), new Device());
                DEVS.get(element.getAttribute("id")).setId(element.getAttribute("id"));
                DEVS.get(element.getAttribute("id")).setName(element.getElementsByTagName("name").item(0).getTextContent());
                DEVS.get(element.getAttribute("id")).setType(element.getElementsByTagName("type").item(0).getTextContent());

                switch (element.getElementsByTagName("type").item(0).getTextContent()) {
                    case "Main Breaker":
                        initImageDev(element, mainBreakerOnImg, mainBreakerOffImg, mainBreakerOnImg);
                        break;

                    case "Breaker":
                        initImageDev(element, breakerOnImg, breakerOffImg, breakerOnImg);
                        break;

                    case "Switch By Pass":
                        initImageDev(element, switchByPassOnImg, switchByPassOffImg, switchByPassOffImg);
                        break;

                    case "Switch PB Permit":
                        initImageDev(element, switchPBPermitOnImg, switchPBPermitOffImg, switchPBPermitOffImg);
                        break;

                    case "Switch Yes-No Black":
                        initImageDev(element, switchYesNoBlackOnImg, switchYesNoBlackOffImg, switchYesNoBlackOffImg);
                        break;

                    case "Switch PB Square Black":
                        initImageDev(element, switchPBSquareBlackOnImg, switchPBSquareBlackOffImg, switchPBSquareBlackOffImg);
                        break;

                    case "Switch Rotary Black":
                        initImageDev(element, switchRotaryBlackIdleImg, switchRotaryBlackIdleImg, switchRotaryBlackIdleImg);
                        break;

                    case "Switch Rotary Yellow":
                        initImageDev(element, switchRotaryYellowIdleOnImg, switchRotaryYellowIdleOffImg, switchRotaryYellowIdleOffImg);
                        break;

                    case "Lamp Red":
                        initImageDev(element, lampRedOnImg, lampRedOnImg, lampRedOffImg);
                        break;

                    case "Lamp Yellow":
                        initImageDev(element, lampYellowOnImg, lampYellowOnImg, lampYellowOffImg);
                        break;
                }

                try {
                    DEVS.get(element.getAttribute("id")).setWidth(Integer.parseInt(element.getElementsByTagName("width").item(0).getTextContent()));
                } catch (DOMException | NumberFormatException e) {
                    DEVS.get(element.getAttribute("id")).setWidth(0);
                }

                try {
                    DEVS.get(element.getAttribute("id")).setHeight(Integer.parseInt(element.getElementsByTagName("height").item(0).getTextContent()));
                } catch (DOMException | NumberFormatException e) {
                    DEVS.get(element.getAttribute("id")).setHeight(0);
                }

                try {
                    DEVS.get(element.getAttribute("id")).setX(Integer.parseInt(element.getElementsByTagName("x").item(0).getTextContent()));
                } catch (NumberFormatException ex) {
                    DEVS.get(element.getAttribute("id")).setX(0);
                }

                try {
                    DEVS.get(element.getAttribute("id")).setY(Integer.parseInt(element.getElementsByTagName("y").item(0).getTextContent()));
                } catch (NumberFormatException ex) {
                    DEVS.get(element.getAttribute("id")).setY(0);
                }

                try {
                    DEVS.get(element.getAttribute("id")).setCmdOff(element.getElementsByTagName("cmdoff").item(0).getTextContent());
                } catch (Exception e) {
                    DEVS.get(element.getAttribute("id")).setCmdOff("");
                }

                try {
                    DEVS.get(element.getAttribute("id")).setCmdOn(element.getElementsByTagName("cmdon").item(0).getTextContent());
                } catch (Exception e) {
                    DEVS.get(element.getAttribute("id")).setCmdOn("");
                }

                try {
                    DEVS.get(element.getAttribute("id")).setLampOn(element.getElementsByTagName("lampon").item(0).getTextContent());
                } catch (Exception e) {
                    DEVS.get(element.getAttribute("id")).setLampOn("");
                }

                try {
                    DEVS.get(element.getAttribute("id")).setLampOff(element.getElementsByTagName("lampoff").item(0).getTextContent());
                } catch (Exception e) {
                    DEVS.get(element.getAttribute("id")).setLampOff("");
                }

            }

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(ACarControlPanelFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initImageDev(Element element, Image imgOn, Image imgOff, Image imgCurr) {
        DEVS.get(element.getAttribute("id")).setImgOn(imgOn);
        DEVS.get(element.getAttribute("id")).setImgOff(imgOff);
        DEVS.get(element.getAttribute("id")).setImgCurr(imgCurr);
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("A-Car Control Panel");
        setSize(new java.awt.Dimension(610, 840));

        viewPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                viewPanelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                viewPanelMouseReleased(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewPanelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout viewPanelLayout = new javax.swing.GroupLayout(viewPanel);
        viewPanel.setLayout(viewPanelLayout);
        viewPanelLayout.setHorizontalGroup(
            viewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 383, Short.MAX_VALUE)
        );
        viewPanelLayout.setVerticalGroup(
            viewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 543, Short.MAX_VALUE)
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
    }// </editor-fold>//GEN-END:initComponents

    private void doClickDevice(int x, int y) {
        Iterator<Device> devs = DEVS.values().iterator();
        Device dev;

        while (devs.hasNext()) {
            dev = devs.next();
            if ((x >= dev.getX() & x <= dev.getX() + dev.getWidth()) & (y >= dev.getY() & y <= dev.getY() + dev.getHeight())) {
                switch (dev.getType()) {

                    case "Main Breaker":
                        doClickDev(dev, mainBreakerOnImg, mainBreakerOffImg);
                        break;

                    case "Breaker":
                        doClickDev(dev, breakerOnImg, breakerOffImg);
                        break;
                }
            }
        }
    }

    private void doClickDev(Device dev, Image imgOn, Image imgOff) {
        if (dev.getImgCurr() == imgOn) {
            jsonObj = new JSONObject();
            JSONObject jsonDevs = new JSONObject();
            jsonDevs.put(dev.getId(), 0);

            jsonObj.put("ACAR", jsonDevs);
            App.OUT_QUEUE.add(jsonObj);
            dev.setImgCurr(imgOff);
        } 
    }

    private void viewPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewPanelMouseClicked
        doClickDevice(evt.getX(), evt.getY());
    }//GEN-LAST:event_viewPanelMouseClicked

    private void doRotarySwitchPress(int mouseButton, Device dev, Image imgOn, Image imgOff) {
        jsonObj = new JSONObject();
        JSONObject jsonDevs = new JSONObject();

        if (mouseButton == 1) {
            dev.setImgCurr(imgOff);
            jsonDevs.put(dev.getId(), 0);
            jsonObj.put("ACAR", jsonDevs);
            App.OUT_QUEUE.add(jsonObj.toJSONString());

        } else if (mouseButton == 3) {
            dev.setImgCurr(imgOn);
            jsonDevs.put(dev.getId(), 1);
            jsonObj.put("ACAR", jsonDevs);
            App.OUT_QUEUE.add(jsonObj.toJSONString());
        }
    }

    private void doPBSwitchPress(Device dev, Image imgOn) {
        jsonObj = new JSONObject();
        JSONObject jsonDevs = new JSONObject();

        dev.setImgCurr(imgOn);
        jsonDevs.put(dev.getId(), 1);
        jsonObj.put("ACAR", jsonDevs);
        App.OUT_QUEUE.add(jsonObj.toJSONString());
    }

    private void viewPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewPanelMousePressed
        Device dev;

        try {
            Iterator<Device> devs = DEVS.values().iterator();    //save data devices to iterator.
            while (devs.hasNext()) {                                //loop next to device
                dev = (Device) (devs.next());                       //save key

                //x >= current position x and x <= (current position x + width) become area x
                if ((x >= dev.getX() & x <= (dev.getX() + dev.getWidth())) & (y >= dev.getY() & y <= (dev.getY() + dev.getHeight()))) {

                    mousePressed = true;
                    //Check device type event on clicked. If type equal Switch Rotary Black.
                    if (dev.getType().equalsIgnoreCase("Switch Rotary Black")) {
                        doRotarySwitchPress(evt.getButton(), dev, switchYesNoBlackOnImg, switchYesNoBlackOffImg);

                    } else if (dev.getType().equalsIgnoreCase("Switch Rotary Yellow")) {
                        doRotarySwitchPress(evt.getButton(), dev, switchRotaryYellowOnImg, switchRotaryYellowOffImg);

                    } else if (dev.getType().equalsIgnoreCase("Switch PB Permit")) {
                        doPBSwitchPress(dev, switchPBPermitOnImg);

                    } else if (dev.getType().equalsIgnoreCase("Switch PB Square Black")) {
                        doPBSwitchPress(dev, switchPBSquareBlackOnImg);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(DriverDeskFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_viewPanelMousePressed

    private void viewPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewPanelMouseReleased
        mousePressed = false;
    }//GEN-LAST:event_viewPanelMouseReleased

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ACarControlPanelFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new ACarControlPanelFrame().setVisible(true);
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
