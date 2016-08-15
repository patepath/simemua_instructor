/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ctsim.simemua_instructor;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
public class DriverDeskFrame extends javax.swing.JFrame implements ActionListener {

    private Graphics2D g2;
    private final Timer timer;
    private int x = 0;
    private int y = 0;

    private Image imgDriverDesk;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
//    private final String msg = "";

    public static final Map<String, Device> DEVS = new HashMap<>();

    private boolean mousePressed;

    //Switch yes-no.
    private BufferedImage switchYesNoBlackOnImg;
    private BufferedImage switchYesNoBlackOffImg;
    //PB square red.
    private BufferedImage switchPBSquareRedOnImg;
    private BufferedImage switchPBSquareRedOffImg;
    //PB square black.
    private BufferedImage switchPBSquareBlackOnImg;
    private BufferedImage switchPBSquareBlackOffImg;
    //PB square green.
    private BufferedImage switchPBSquareGreenOnImg;
    private BufferedImage switchPBSquareGreenOffImg;
    //PB square yellow.
    private BufferedImage switchPBSquareYellowOnImg;
    private BufferedImage switchPBSquareYellowOffImg;
    //PB permit.
    private BufferedImage switchPBPermitOnImg;
    private BufferedImage switchPBPermitOffImg;
    //Rotary red.
    private BufferedImage switchRotaryRedIdleOnImg;
    private BufferedImage switchRotaryRedIdleOffImg;
    private BufferedImage switchRotaryRedOnImg;
    private BufferedImage switchRotaryRedOffImg;
    //Rotary yellow.
    private BufferedImage switchRotaryYellowIdleOnImg;
    private BufferedImage switchRotaryYellowIdleOffImg;
    private BufferedImage switchRotaryYellowOnImg;
    private BufferedImage switchRotaryYellowOffImg;

    /**
     * Creates new form DriverDeskFrame
     */
    public DriverDeskFrame() {
        initComponents();
        super.setLocation(1000, 500);

        //initStatus.
        mousePressed = false;

        initImage();

        timer = new Timer(100, this);
        timer.start();

        //initCommunication();
        createDevices();
    }

    private void initCommunication() {
        try {
            socket = new Socket("192.168.1.10", 2510);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException ex) {
            Logger.getLogger(ACarControlPanelFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initImage() {
        try {
            imgDriverDesk = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/layout_driver_desk.png").getFile()));

            switchYesNoBlackOnImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/switch_yes-no_black_on.png").getFile()));
            switchYesNoBlackOffImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/switch_yes-no_black_off.png").getFile()));

            switchPBSquareRedOnImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/switch_pb_square_red_on.png").getFile()));
            switchPBSquareRedOffImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/switch_pb_square_red_off.png").getFile()));

            switchPBSquareBlackOnImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/switch_pb_square_black_on.png").getFile()));
            switchPBSquareBlackOffImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/switch_pb_square_black_off.png").getFile()));

            switchPBSquareGreenOnImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/switch_pb_square_green_on.png").getFile()));
            switchPBSquareGreenOffImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/switch_pb_square_green_off.png").getFile()));

            switchPBSquareYellowOnImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/switch_pb_square_yellow_on.png").getFile()));
            switchPBSquareYellowOffImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/switch_pb_square_yellow_off.png").getFile()));

            switchPBPermitOnImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/switch_pb_permit_on.png").getFile()));
            switchPBPermitOffImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/switch_pb_permit_off.png").getFile()));

            switchRotaryRedIdleOnImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/switch_rotary_red_idle_on.png").getFile()));
            switchRotaryRedIdleOffImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/switch_rotary_red_idle_off.png").getFile()));
            switchRotaryRedOnImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/switch_rotary_red_on.png").getFile()));
            switchRotaryRedOffImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/switch_rotary_red_off.png").getFile()));

            switchRotaryYellowIdleOnImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/switch_rotary_yellow_idle_on.png").getFile()));
            switchRotaryYellowIdleOffImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/switch_rotary_yellow_idle_off.png").getFile()));
            switchRotaryYellowOnImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/switch_rotary_yellow_on.png").getFile()));
            switchRotaryYellowOffImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/switch_rotary_yellow_off.png").getFile()));

        } catch (IOException ex) {
            Logger.getLogger(ACarControlPanelFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initGraphics() {

        try {
            x = (int) viewPanel.getMousePosition().getX();
            y = (int) viewPanel.getMousePosition().getY();

        } catch (Exception ex) {
            x = 0;
            y = 0;
        }

        g2.drawImage(imgDriverDesk, null, this);
        g2.drawString("(" + x + ", " + y + ")", 15, 15);

        drawDevices();

        drawSwitchRotary();
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
            if (sw.getImgCurr() != null) {
                g2.drawImage(sw.getImgCurr(), sw.getX(), sw.getY(), this);
            }

            //Check type for some lamp use same picture.
            switch (sw.getType()) {
                case "Switch Rotary Red":
                    if (sw.getImgCurr() == switchRotaryRedOnImg && !mousePressed) {
                        sw.setImgCurr(switchRotaryRedIdleOnImg);
                    } else if (sw.getImgCurr() == switchRotaryRedOffImg && !mousePressed) {
                        sw.setImgCurr(switchRotaryRedIdleOffImg);
                    }
                    break;

                case "Switch Rotary Yellow":
                    if (sw.getImgCurr() == switchRotaryYellowOnImg && !mousePressed) {
                        sw.setImgCurr(switchRotaryYellowIdleOnImg);
                    } else if (sw.getImgCurr() == switchRotaryYellowOffImg && !mousePressed) {
                        sw.setImgCurr(switchRotaryYellowIdleOffImg);
                    }
                    break;
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
            fXmlFile = new File(this.getClass().getClassLoader().getResource("config/driver_desk.xml").getFile());
            dbFactory = DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            nodeList = doc.getElementsByTagName("device");

            for (int i = 0; i < nodeList.getLength(); i++) {
                element = (Element) nodeList.item(i);
                DEVS.put(element.getAttribute("id"), new Device());
                DEVS.get(element.getAttribute("id")).setName(element.getElementsByTagName("name").item(0).getTextContent());
                DEVS.get(element.getAttribute("id")).setType(element.getElementsByTagName("type").item(0).getTextContent());

                switch (element.getElementsByTagName("type").item(0).getTextContent()) {
                    case "Switch Yes-No Black":
                        initImageDev(element, switchYesNoBlackOnImg, switchYesNoBlackOffImg, switchYesNoBlackOffImg);
                        break;
                    case "Switch Yes-No Yellow":
                        initImageDev(element, switchRotaryYellowOnImg, switchRotaryYellowOffImg, switchRotaryYellowOffImg);
                        break;
                    case "Switch PB Square Black":
                        initImageDev(element, switchPBSquareBlackOnImg, switchPBSquareBlackOffImg, switchPBSquareBlackOffImg);
                        break;
                    case "Switch PB Square Green":
                        initImageDev(element, switchPBSquareGreenOnImg, switchPBSquareGreenOffImg, switchPBSquareGreenOffImg);
                        break;
                    case "Switch PB Square Red":
                        initImageDev(element, switchPBSquareRedOnImg, switchPBSquareRedOffImg, switchPBSquareRedOffImg);
                        break;
                    case "Switch PB Square Yellow":
                        initImageDev(element, switchPBSquareYellowOnImg, switchPBSquareYellowOffImg, switchPBSquareYellowOffImg);
                        break;
                    case "Switch Rotary Red":
                        initImageDev(element, switchRotaryRedIdleOnImg, switchRotaryRedIdleOffImg, switchRotaryRedIdleOffImg);
                        break;
                    case "Switch Rotary Yellow":
                        initImageDev(element, switchRotaryYellowIdleOnImg, switchRotaryYellowIdleOffImg, switchRotaryYellowIdleOffImg);
                        break;
                    case "Switch PB Permit":
                        initImageDev(element, switchPBPermitOnImg, switchPBPermitOffImg, switchPBPermitOffImg);
                        break;
                }

                try {
                    DEVS.get(element.getAttribute("id")).setX(Integer.parseInt(element.getElementsByTagName("x").item(0).getTextContent()));
                } catch (DOMException | NumberFormatException e) {
                    DEVS.get(element.getAttribute("id")).setX(0);
                }

                try {
                    DEVS.get(element.getAttribute("id")).setY(Integer.parseInt(element.getElementsByTagName("y").item(0).getTextContent()));
                } catch (DOMException | NumberFormatException e) {
                    DEVS.get(element.getAttribute("id")).setY(0);
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
        } catch (ParserConfigurationException | SAXException | IOException | DOMException ex) {
            Logger.getLogger(DriverDeskFrame.class.getName()).log(Level.SEVERE, null, ex);
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
        setTitle("Driver Desk");
        setSize(new java.awt.Dimension(800, 579));

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
            .addGap(0, 456, Short.MAX_VALUE)
        );
        viewPanelLayout.setVerticalGroup(
            viewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 449, Short.MAX_VALUE)
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

    private void viewPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewPanelMouseClicked
        x = evt.getX();
        y = evt.getY();

        try {
            Iterator<String> device = DEVS.keySet().iterator();            //save data devices to iterator.
            while (device.hasNext()) {                                         //loop next to device
                String key = (String) (device.next());                         //save key

                //x >= current position x and x <= (current position x + width) become area x
                if ((x >= DEVS.get(key).getX() && x <= (DEVS.get(key).getX() + DEVS.get(key).getWidth()))
                        && (y >= DEVS.get(key).getY() && y <= (DEVS.get(key).getY() + DEVS.get(key).getHeight()))) {

                    //Check device type event on clicked. If type equal Switch Yes-No Black.
                    if (DEVS.get(key).getType().equalsIgnoreCase("Switch Yes-No Black")) {
                        if (DEVS.get(key).getImgCurr() != switchYesNoBlackOnImg) {         //Check image is show not equal breaker On.
                            out.println(DEVS.get(key).getLampOn());
                            out.flush();
                            DEVS.get(key).setImgCurr(switchYesNoBlackOnImg);              //Set image breaker On.
                        } else {
                            out.println(DEVS.get(key).getLampOff());
                            out.flush();
                            DEVS.get(key).setImgCurr(switchYesNoBlackOffImg);
                        }
                    } else if (DEVS.get(key).getType().equalsIgnoreCase("Switch Yes-No Yellow")) {
                        if (DEVS.get(key).getImgCurr() != switchRotaryYellowOnImg) {
                            out.println(DEVS.get(key).getLampOn());
                            out.flush();
                            DEVS.get(key).setImgCurr(switchRotaryYellowOnImg);
                        } else {
                            out.println(DEVS.get(key).getLampOff());
                            out.flush();
                            DEVS.get(key).setImgCurr(switchRotaryYellowOffImg);

                        }
                    } else if (DEVS.get(key).getType().equalsIgnoreCase("Switch PB Square Black")) {
                        if (DEVS.get(key).getImgCurr() != switchPBSquareBlackOnImg) {
                            out.println(DEVS.get(key).getLampOn());
                            out.flush();
                            DEVS.get(key).setImgCurr(switchPBSquareBlackOnImg);
                        } else {
                            out.println(DEVS.get(key).getLampOff());
                            out.flush();
                            DEVS.get(key).setImgCurr(switchPBSquareBlackOffImg);
                        }
                    } else if (DEVS.get(key).getType().equalsIgnoreCase("Switch PB Square Red")) {
                        if (DEVS.get(key).getImgCurr() != switchPBSquareRedOnImg) {
                            out.println(DEVS.get(key).getLampOn());
                            out.flush();
                            DEVS.get(key).setImgCurr(switchPBSquareRedOnImg);
                        } else {
                            out.println(DEVS.get(key).getLampOff());
                            out.flush();
                            DEVS.get(key).setImgCurr(switchPBSquareRedOffImg);
                        }
                    } else if (DEVS.get(key).getType().equalsIgnoreCase("Switch PB Square Green")) {
                        if (DEVS.get(key).getImgCurr() != switchPBSquareGreenOnImg) {
                            out.println(DEVS.get(key).getLampOn());
                            out.flush();
                            DEVS.get(key).setImgCurr(switchPBSquareGreenOnImg);
                        } else {
                            out.println(DEVS.get(key).getLampOff());
                            out.flush();
                            DEVS.get(key).setImgCurr(switchPBSquareGreenOffImg);
                        }
                    } else if (DEVS.get(key).getType().equalsIgnoreCase("Switch PB Square Yellow")) {
                        if (DEVS.get(key).getImgCurr() != switchPBSquareYellowOnImg) {
                            out.println(DEVS.get(key).getLampOn());
                            out.flush();
                            DEVS.get(key).setImgCurr(switchPBSquareYellowOnImg);
                        } else {
                            out.println(DEVS.get(key).getLampOff());
                            out.flush();
                            DEVS.get(key).setImgCurr(switchPBSquareYellowOffImg);
                        }

                    } else if (DEVS.get(key).getType().equalsIgnoreCase("Switch Rotary Yellow")) {
                        //Left mouse click. Set lamp off.
                        if (evt.getButton() == 1) {
                            DEVS.get(key).setImgCurr(switchRotaryYellowOffImg);
                            out.println(DEVS.get(key).getLampOff());
                            out.flush();
                        } else if (evt.getButton() == 3) {
                            DEVS.get(key).setImgCurr(switchRotaryYellowOnImg);
                            out.println(DEVS.get(key).getLampOn());
                            out.flush();
                        }
                    } else if (DEVS.get(key).getType().equalsIgnoreCase("Switch PB Permit")) {
                        if (DEVS.get(key).getImgCurr() != switchPBPermitOnImg) {
                            out.println(DEVS.get(key).getLampOn());
                            out.flush();
                            DEVS.get(key).setImgCurr(switchPBPermitOnImg);
                        } else {
                            out.println(DEVS.get(key).getLampOff());
                            out.flush();
                            DEVS.get(key).setImgCurr(switchPBPermitOffImg);
                        }
                    }
                }

            }
        } catch (Exception ex) {
            Logger.getLogger(DriverDeskFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        //System.out.println("x : " + x + " y : " + y);
    }//GEN-LAST:event_viewPanelMouseClicked

    private void viewPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewPanelMousePressed
        JSONObject json = new JSONObject();
        JSONObject jsonDev = new JSONObject();

        try {
            Iterator<String> device = DEVS.keySet().iterator();            //save data devices to iterator.
            while (device.hasNext()) {                                         //loop next to device
                String key = (String) (device.next());                         //save key

                //x >= current position x and x <= (current position x + width) become area x
                if ((x >= DEVS.get(key).getX() && x <= (DEVS.get(key).getX() + DEVS.get(key).getWidth()))
                        && (y >= DEVS.get(key).getY() && y <= (DEVS.get(key).getY() + DEVS.get(key).getHeight()))) {

                    mousePressed = true;
                    //Check device type event on clicked. If type equal Switch Rotary Red
                    if (DEVS.get(key).getType().equalsIgnoreCase("Switch Rotary Red")) {
                        if (evt.getButton() == 1) {
                            DEVS.get(key).setImgCurr(switchRotaryRedOffImg);
                            jsonDev.put(key, 0);
                            json.put("DRIVERDESK", jsonDev);
                            App.OUT_QUEUE.add(json.toJSONString());

                        } else if (evt.getButton() == 3) {
                            DEVS.get(key).setImgCurr(switchRotaryRedOnImg);
                            jsonDev.put(key, 1);
                            json.put("DRIVERDESK", jsonDev);
                            App.OUT_QUEUE.add(json.toJSONString());
                        }

                    } else if (DEVS.get(key).getType().equalsIgnoreCase("Switch Rotary Yellow")) {
                        if (evt.getButton() == 1) {
                            DEVS.get(key).setImgCurr(switchRotaryYellowOffImg);
                            out.println(DEVS.get(key).getLampOff());
                            out.flush();
                        } else if (evt.getButton() == 3) {
                            DEVS.get(key).setImgCurr(switchRotaryYellowOnImg);
                            out.println(DEVS.get(key).getLampOn());
                            out.flush();
                        }
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
            java.util.logging.Logger.getLogger(DriverDeskFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new DriverDeskFrame().setVisible(true);
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
