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

	private JSONObject jsonObj;
	private JSONParser jsonParser;

	private Calendar watchdogStart;

	public App() {

	}

	public void run() {
		String msg;
		jsonParser = new JSONParser();

		while (true) {

			if (socket != null) {

				try {

					if (socket.getInputStream().available() > 0) {
						msg = in.readLine();

						try {
							jsonObj = (JSONObject) jsonParser.parse(msg);

							if ((long) jsonObj.get("watchdog") == 1) {	// reset watchdog.
								watchdogStart = Calendar.getInstance();
							}

						} catch (ParseException ex) {
							Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
						}

					} else {
						jsonObj = new JSONObject();

						if (!OUT_QUEUE.isEmpty()) {
							jsonObj = new JSONObject();

							synchronized (this) {
								while (!OUT_QUEUE.isEmpty()) {
									out.println(OUT_QUEUE.poll());
									out.flush();

									if (out.checkError()) {
										socket.close();
									} else {
										System.out.println(jsonObj.toJSONString());
									}
								}

							}
						}

						if (Calendar.getInstance().getTimeInMillis() - watchdogStart.getTimeInMillis() > 5000) {
							System.out.println("Server disconnected.");
							socket = null;
						}
					}

				} catch (NullPointerException | IOException ex) {

				}

			} else {
				try {
					System.out.println("try to connecting.");
					socket = new Socket("localhost", 2510);

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
			java.util.logging.Logger.getLogger(ACarControlPanelFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}

		java.awt.EventQueue.invokeLater(() -> {
			new ACarControlPanelFrame().setVisible(true);
			new CCarControlPanelFrame().setVisible(true);
			new C1CarControlPanelFrame().setVisible(true);
//			new DriverDeskFrame().setVisible(true);
//			new FaultGenerateFrame().setVisible(true);
		});

		App app = new App();
		app.run();
	}
}
