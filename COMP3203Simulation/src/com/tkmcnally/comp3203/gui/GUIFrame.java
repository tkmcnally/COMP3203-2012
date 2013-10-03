package com.tkmcnally.comp3203.gui;

import java.awt.Color;

import javax.swing.JFrame;

public class GUIFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	public GUI gui;
	public GUIFrame(){
		gui = new GUI();
		gui.createSim();
	}

	public void createAndShowGUIFrame(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(gui);
		setBackground(Color.white);
		pack();
		setVisible(true);
	}

}
