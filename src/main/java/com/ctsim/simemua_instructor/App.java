/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ctsim.simemua_instructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author patipat
 */
public class App {

    public static final BlockingQueue OUT_QUEUE = new LinkedBlockingQueue<>();
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private Calendar watchdogStart;

    public App() {
        initCommunication();
    }

    public void run() {
        while (true) {
            if (socket == null) {
                initCommunication();

            } else {
                try {
                    if (socket.getInputStream().available() > 0) {
                        receiveMessage();
                    } else {
                        sendMessage();
                    }
                } catch (IOException | ParseException ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (Calendar.getInstance().getTimeInMillis() - watchdogStart.getTimeInMillis() > 5000) {
                    socket = null;
                    System.out.println("connection fail.");
                }
            }

        }
    }

    private void receiveMessage() throws IOException, ParseException {
        String msg = in.readLine();
        JSONParser jsonParser = new JSONParser();
        JSONObject json = (JSONObject) jsonParser.parse(msg);
        JSONObject jsonDev;

        Iterator<String> keys = json.keySet().iterator();
        String key;
        Device dev;

        boolean isUpdated;

        while (keys.hasNext()) {
            key = keys.next();

            switch (key.toUpperCase()) {
                case "WATCHDOG":
                    watchdogStart = Calendar.getInstance();
                    break;

                case "ACAR":
                    jsonDev = (JSONObject) json.get(key);
                    keys = jsonDev.keySet().iterator();

                    while (keys.hasNext()) {
                        key = keys.next();

                        dev = ACarControlPanelFrame.DEVS.get(key);

                        try {
                            if ((long) jsonDev.get(key) == 1) {
                                dev.setImgCurr(dev.getImgOn());
                                isUpdated = true;

                            } else if ((long) jsonDev.get(key) == 0) {
                                dev.setImgCurr(dev.getImgOff());
                                isUpdated = true;
                            }

                        } catch (Exception ex) {
                            System.out.println("update fail.");
                        }

                    }

                    break;

                case "CCAR":
                    break;

                case "C1CAR":
                    break;

                case "DRIVERDESK":
                    jsonDev = (JSONObject) json.get(key);
                    keys = jsonDev.keySet().iterator();

                    while (keys.hasNext()) {
                        key = keys.next();

                        dev = DriverDeskFrame.DEVS.get(key);

                        try {
                            if ((long) jsonDev.get(key) == 1) {
                                dev.setImgCurr(dev.getImgOn());
                                isUpdated = true;

                            } else if ((long) jsonDev.get(key) == 0) {
                                dev.setImgCurr(dev.getImgOff());
                                isUpdated = true;
                            }

                        } catch (Exception ex) {
                            System.out.println("update fail.");
                        }

                    }
                    break;
            }

        }

    }

    private void sendMessage() {
        while (!OUT_QUEUE.isEmpty()) {
            out.println(OUT_QUEUE.poll());

            if (out.checkError()) {
                socket = null;
            }
        }
    }

    private void initCommunication() {
        try {
            System.out.println("try to connecting.");
            socket = new Socket("192.168.1.10", 2510);

            if (socket.isConnected()) {
                System.out.println("Conneted.");
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println("SESSIONID=INSTRUCTOR");
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                watchdogStart = Calendar.getInstance();
            }

        } catch (NullPointerException | IOException ex) {
            System.out.println("connection fail.");
        }
    }

    public static void main(String[] argc) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ACarControlPanelFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new ACarControlPanelFrame().setVisible(true);
            new CCarControlPanelFrame().setVisible(true);
            new C1CarControlPanelFrame().setVisible(true);
            new DriverDeskFrame().setVisible(true);
            new FaultGenerateFrame().setVisible(true);
            new VideoControlFrame().setVisible(true);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

        App app = new App();
        app.run();
    }

}
