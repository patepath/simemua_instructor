/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trainsim.instructor;

import java.util.Queue;

/**
 *
 * @author patipat
 */
public class App {

    public static Queue msgLoop;

    public App() {
        java.awt.EventQueue.invokeLater(() -> {
            new ACarControlPanelFrame().setVisible(true);
            new CCarControlPanelFrame().setVisible(true);
            new C1CarControlPanelFrame().setVisible(true);
            new DriverDeskFrame().setVisible(true);
            new FaultGenerateFrame().setVisible(true);
        });
    }

    public void run() {

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
            java.util.logging.Logger.getLogger(ACarControlPanelFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        App app = new App();
        app.run();
    }
}
