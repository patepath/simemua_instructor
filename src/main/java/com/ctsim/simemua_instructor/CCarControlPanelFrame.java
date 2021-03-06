/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ctsim.simemua_instructor;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
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
public class CCarControlPanelFrame extends javax.swing.JFrame implements ActionListener {

	private Graphics2D g2;
	private final Timer timer;
	private int x = 0;
	private int y = 0;
	private boolean isMousePress = false;

	private Image imgCCarControlPanel;

	private Image mainBreakerOnImg;
	private Image mainBreakerOffImg;

	private Image breakerOnImg;
	private Image breakerOffImg;

	private Image lampSpareImg;

	private final Map<String, Device> devices;

	private BufferedImage lampRedOnImg;
	private BufferedImage lampRedOffImg;
	private BufferedImage lampYellowOnImg;
	private BufferedImage lampYellowOffImg;
	private BufferedImage switchPBSquareBlackOnImg;
	private BufferedImage switchPBSquareBlackOffImg;

	/**
	 * Creates new form CCarControlPanelFrame
	 */
	public CCarControlPanelFrame() {
		initComponents();
		super.setLocation(700, 0);

		initImage();

		devices = new HashMap<>();

		timer = new Timer(100, this);
		timer.start();
		createDevices();
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

		g2.drawImage(imgCCarControlPanel, null, this);
		drawDevices();

		g2.drawString("(" + x + ", " + y + ")", 15, 15);

		Iterator<String> device = devices.keySet().iterator();
		while (device.hasNext()) {
			String key = (String) device.next();

			//x >= current position x and x <= (current position x + width) become area x
			if ((x >= devices.get(key).getX() && x <= (devices.get(key).getX() + devices.get(key).getWidth()))
				&& (y >= devices.get(key).getY() && y <= (devices.get(key).getY() + devices.get(key).getHeight()))) {

				//save device name to variable.
				String name = devices.get(key).getName();
				FontMetrics fm = g2.getFontMetrics();
				Rectangle2D rect = fm.getStringBounds(name, g2);

				g2.setColor(Color.YELLOW);
				g2.fillRect(devices.get(key).getX(), ((devices.get(key).getY() + devices.get(key).getHeight()) + 20) - fm.getAscent(), (int) rect.getWidth(), (int) rect.getHeight());

				g2.setColor(Color.red);
				g2.drawString(name, devices.get(key).getX(), (devices.get(key).getY() + devices.get(key).getHeight()) + 20);
			}
		}
	}

	private void drawDevices() {
		JSONObject jsonObj = new JSONObject();
		JSONObject jsonDevs = new JSONObject();
		ArrayList<Device> devs = new ArrayList(devices.values());

		devs.stream().forEach((dev) -> {

			if (dev.getType().equalsIgnoreCase("Switch PB Square Black")) {
				if (dev.getImgCurr() != switchPBSquareBlackOffImg && !isMousePress) {
					jsonDevs.put(dev.getId(), 0);
					jsonObj.put("CCar", jsonDevs);
					App.OUT_QUEUE.add(jsonObj.toJSONString());

					dev.setImgCurr(switchPBSquareBlackOffImg);
				}
			}

			if (dev.getImgCurr() != null) {
				g2.drawImage(dev.getImgCurr(), dev.getX(), dev.getY(), this);
			}
		});
	}

	private void initImage() {
		try {
			imgCCarControlPanel = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/layout_c-car_control_panel.png").getFile()));
			mainBreakerOnImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/main_breaker_on.png").getFile()));
			mainBreakerOffImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/main_breaker_off.png").getFile()));

			breakerOnImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/breaker_on.png").getFile()));
			breakerOffImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/breaker_off.png").getFile()));

			lampRedOnImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/lamp_red_on.png").getFile()));
			lampRedOffImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/lamp_red_off.png").getFile()));

			lampYellowOnImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/lamp_yellow_on.png").getFile()));
			lampYellowOffImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/lamp_yellow_off.png").getFile()));

			switchPBSquareBlackOnImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/switch_pb_square_black_on.png").getFile()));
			switchPBSquareBlackOffImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/switch_pb_square_black_off.png").getFile()));

			lampSpareImg = ImageIO.read(new File(this.getClass().getClassLoader().getResource("img/lamp_spare.png").getFile()));

		} catch (IOException ex) {
			Logger.getLogger(CCarControlPanelFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void createDevices() {
		File fXmlFile;
		DocumentBuilderFactory dbFactory;
		DocumentBuilder dBuilder;
		Document doc;
		NodeList nodeList;
		Element element;

		try {
			fXmlFile = new File(this.getClass().getClassLoader().getResource("config/c_car_control_panel.xml").getFile());
			dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			nodeList = doc.getElementsByTagName("device");

			for (int i = 0; i < nodeList.getLength(); i++) {
				element = (Element) nodeList.item(i);
				devices.put(element.getAttribute("id"), new Device());
				devices.get(element.getAttribute("id")).setId(element.getAttribute("id"));
				devices.get(element.getAttribute("id")).setName(element.getElementsByTagName("name").item(0).getTextContent());
				devices.get(element.getAttribute("id")).setType(element.getElementsByTagName("type").item(0).getTextContent());

				switch (element.getElementsByTagName("type").item(0).getTextContent()) {
					case "Breaker":
						initImageDev(element, breakerOnImg, breakerOffImg, breakerOnImg);
						break;

					case "Lamp Red":
						initImageDev(element, lampRedOnImg, lampRedOffImg, lampRedOnImg);
						break;

					case "Lamp Yellow":
						initImageDev(element, lampYellowOnImg, lampYellowOffImg, lampYellowOnImg);
						break;

					case "Switch PB Square Black":
						initImageDev(element, switchPBSquareBlackOnImg, switchPBSquareBlackOffImg, switchPBSquareBlackOffImg);
						break;

					case "Spare":
						initImageDev(element, lampSpareImg, lampSpareImg, lampSpareImg);
						break;

				}

				try {
					devices.get(element.getAttribute("id")).setX(Integer.parseInt(element.getElementsByTagName("x").item(0).getTextContent()));
				} catch (DOMException | NumberFormatException e) {
					devices.get(element.getAttribute("id")).setX(0);
				}

				try {
					devices.get(element.getAttribute("id")).setY(Integer.parseInt(element.getElementsByTagName("y").item(0).getTextContent()));
				} catch (NumberFormatException ex) {
					devices.get(element.getAttribute("id")).setY(0);
				}

				try {
					devices.get(element.getAttribute("id")).setWidth(Integer.parseInt(element.getElementsByTagName("width").item(0).getTextContent()));
				} catch (DOMException | NumberFormatException e) {
					devices.get(element.getAttribute("id")).setWidth(0);
				}

				try {
					devices.get(element.getAttribute("id")).setHeight(Integer.parseInt(element.getElementsByTagName("height").item(0).getTextContent()));
				} catch (DOMException | NumberFormatException e) {
					devices.get(element.getAttribute("id")).setHeight(0);
				}

				try {
					devices.get(element.getAttribute("id")).setCmdOff(element.getElementsByTagName("cmdccaroff").item(0).getTextContent());
				} catch (Exception e) {
					devices.get(element.getAttribute("id")).setCmdOff("");
				}
			}

		} catch (ParserConfigurationException | SAXException | IOException | DOMException ex) {
			Logger.getLogger(CCarControlPanelFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void initImageDev(Element element, Image imgOn, Image imgOff, Image imgCurr) {
		devices.get(element.getAttribute("id")).setImgOn(imgOn);
		devices.get(element.getAttribute("id")).setImgOff(imgOff);
		devices.get(element.getAttribute("id")).setImgCurr(imgCurr);
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
        setTitle("C-Car Control Panel");
        setPreferredSize(new java.awt.Dimension(411, 680));
        setSize(new java.awt.Dimension(411, 680));

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
            .addGap(0, 399, Short.MAX_VALUE)
        );
        viewPanelLayout.setVerticalGroup(
            viewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 668, Short.MAX_VALUE)
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

		Device dev;
		Iterator<Device> devs = devices.values().iterator();
		
		JSONObject jsonObj = new JSONObject();
		JSONObject jsonDevs = new JSONObject();

		while (devs.hasNext()) {
			dev = (Device) devs.next();

			//x >= current position x and x <= (current position x + width) become area x
			if ((x >= dev.getX() & x <= (dev.getX() + dev.getWidth())) & (y >= dev.getY() & y <= (dev.getY() + dev.getHeight()))) {

				//Check device type event on clicked. If type equal Main Breaker.
				if (dev.getType().equalsIgnoreCase("Breaker")) {
					if (dev.getImgCurr() != breakerOnImg) {
						dev.setImgCurr(breakerOnImg);

					} else {
						dev.setImgCurr(breakerOffImg);

						jsonDevs.put(dev.getId(), 0);
						jsonObj.put("CCar", jsonDevs);
						App.OUT_QUEUE.add(jsonObj.toJSONString());
					}

				}
			}
		}
    }//GEN-LAST:event_viewPanelMouseClicked

    private void viewPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewPanelMousePressed
		isMousePress = true;
		x = evt.getX();
		y = evt.getY();

		JSONObject jsonObj = new JSONObject();
		JSONObject jsonDevs = new JSONObject();

		Device dev;
		Iterator<Device> devs = devices.values().iterator();

		while (devs.hasNext()) {
			dev = (Device) devs.next();

			if ((x >= dev.getX() & x <= (dev.getX() + dev.getWidth())) & (y >= dev.getY() & y <= (dev.getY() + dev.getHeight()))) {
				if (dev.getType().equalsIgnoreCase("Switch PB Square Black")) {
					if (dev.getImgCurr() != switchPBSquareBlackOnImg) {
						dev.setImgCurr(switchPBSquareBlackOnImg);

						jsonDevs.put(dev.getId(), 1);
						jsonObj.put("CCar", jsonDevs);
						App.OUT_QUEUE.add(jsonObj.toJSONString());
					}
				}
			}
		}
    }//GEN-LAST:event_viewPanelMousePressed

    private void viewPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewPanelMouseReleased
		isMousePress = false;
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
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(CCarControlPanelFrame.class
				.getName()).log(java.util.logging.Level.SEVERE, null, ex);

		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(CCarControlPanelFrame.class
				.getName()).log(java.util.logging.Level.SEVERE, null, ex);

		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(CCarControlPanelFrame.class
				.getName()).log(java.util.logging.Level.SEVERE, null, ex);

		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(CCarControlPanelFrame.class
				.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(() -> {
			new CCarControlPanelFrame().setVisible(true);
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		viewPanel.repaint();
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel viewPanel;
    // End of variables declaration//GEN-END:variables
}
