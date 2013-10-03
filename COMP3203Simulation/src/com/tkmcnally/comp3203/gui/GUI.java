package com.tkmcnally.comp3203.gui;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.tkmcnally.comp3203.StopWatch;
import com.tkmcnally.comp3203.TimeLoop;
import com.tkmcnally.comp3203.node.Sensor;

public class GUI extends JPanel {

	private static final long serialVersionUID = 1L;
	public Point node1, node2;
	public int range;
	public Sensor s1, s2;
	public boolean start = false;
	private TimeLoop tl;
	public JButton sim1, sim2, sim3, stop;
	private Shape[] sensorCover;
	private Shape[] sensor;
	private double angle1, angle2, angle3;
	public JRadioButton partSelection1, partSelection2;
	public boolean sigStopped;
	private StopWatch sw;

	public GUI(){

		node1 = new Point();
		node2 = new Point();
		ButtonGroup b = new ButtonGroup();
		partSelection1 = new JRadioButton();
		partSelection1.setText("PART 1");
		b.add(partSelection1);

		partSelection2 = new JRadioButton();
		partSelection2.setText("PART 2");
		b.add(partSelection2);

		partSelection1.setSelected(true);
		partSelection2.setSelected(false);

		add(partSelection1);
		add(partSelection2);

		sim1 = new JButton();
		sim1.setText("sim1");
		sim1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				int nodeNum = 0;
				if(partSelection2.isSelected()){
					tl.part = 1;
					nodeNum = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter number of sensors (MAX 100) : ", "Settings", 1));
				}else if(partSelection1.isSelected()){
					nodeNum = 2;
					tl.part = 0;
				}


				tl.numNodes = nodeNum;

				start = true;
				sensorCover = new Shape[nodeNum];
				sensor = new Shape[nodeNum];
				range = tl.range;
				stop.setEnabled(true);
				tl.runSim();
				sw = new StopWatch();
			}
		});
		sim1.setSize(50, 50);
		add(sim1);

		sim2 = new JButton();
		sim2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				int nodeNum;
				if(partSelection2.isSelected()){
					tl.part = 1;
					nodeNum = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter number of sensors (MAX 100) : ", "Settings", 1));
				}else{
					nodeNum = 2;
					tl.part = 0;
				}
				tl.numNodes = nodeNum;
				start = true;
				sensorCover = new Shape[nodeNum];
				sensor = new Shape[nodeNum];
				range = tl.range;
				start = true;
				stop.setEnabled(true);
				tl.runSim2();
				sw = new StopWatch();

				tl.calForLog(sw);
				try {
					tl.writeFile();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				tl.clearLog();
			}
		});
		sim2.setText("sim2");
		sim2.setSize(50, 50);
		add(sim2);

		sim3 = new JButton();
		sim3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				int nodeNum;
				if(partSelection2.isSelected()){
					tl.part = 1;
					nodeNum = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter number of sensors (MAX 100) : ", "Settings", 1));
				}else{
					nodeNum = 2;
					tl.part = 0;
				}
				tl.numNodes = nodeNum;
				start = true;
				sensorCover = new Shape[nodeNum];
				sensor = new Shape[nodeNum];
				range = tl.range;
				start = true;
				stop.setEnabled(true);
				tl.runSim3();
				sw = new StopWatch();

				tl.calForLog(sw);
				try {
					tl.writeFile();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				tl.clearLog();
			}
		});
		sim3.setText("sim3");
		sim3.setSize(50, 50);
		add(sim3);

		stop = new JButton();
		stop.setEnabled(false);
		stop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				simRunning();
				sigStopped = true;
				stop.setEnabled(false);

				tl.calForLog(sw);
				try {
					tl.writeFile();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				tl.clearLog();
			}
		});
		stop.setText("STOP");
		stop.setSize(50, 50);

		add(stop);
		stop.setLocation(400, 400);


		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(500, 500));
		setVisible(true);

	}
	public void createSim(){
		tl = new TimeLoop(this);


	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(start){
			Graphics2D g2 = (Graphics2D)g;


			for(int i = 0; i < tl.numNodes; i++){
				g2.setColor(Color.BLACK);

				sensorCover[i] =  new Ellipse2D.Float(tl.sensors[i].getX() - (range/2), (500) - tl.sensors[i].getY() - (range/2), range, range);

				sensor[i] = new Ellipse2D.Float(tl.sensors[i].getX()-5, (500) - tl.sensors[i].getY()-5, 10, 10);

				angle1 = (Math.toRadians(tl.sensors[i].getCurrAngle() + (tl.sensors[i].getBeamWidth()/2)));
				angle2 = (Math.toRadians(tl.sensors[i].getCurrAngle()));
				angle3 = (Math.toRadians(tl.sensors[i].getCurrAngle() - (tl.sensors[i].getBeamWidth()/2)));

				g2.drawLine(tl.sensors[i].getX(), 500 - tl.sensors[i].getY(), (int) (tl.sensors[i].getX() + range/2 * Math.cos(angle1)), 500 - (int) (tl.sensors[i].getY() + range/2 * Math.sin(angle1)));
				g2.drawLine(tl.sensors[i].getX(), 500 - tl.sensors[i].getY(), (int) (tl.sensors[i].getX() + range/2 * Math.cos(angle2)), 500 - (int) (tl.sensors[i].getY() + range/2 * Math.sin(angle2)));
				g2.drawLine(tl.sensors[i].getX(), 500 - tl.sensors[i].getY(), (int) (tl.sensors[i].getX() + range/2 * Math.cos(angle3)), 500 - (int) (tl.sensors[i].getY() + range/2 * Math.sin(angle3)));

				g2.setColor(Color.BLACK);
				g2.draw(sensorCover[i]);

				g2.setColor(Color.BLACK);
				if (tl.getAlg() == 1){ //changes here
					if (tl.sensors[i].getColour() == 0) {
						g2.setColor(Color.BLUE);
					} else if (tl.sensors[i].getColour() == 1) {
						g2.setColor(Color.CYAN);
					} else if (tl.sensors[i].getColour() == 2) {
						g2.setColor(Color.ORANGE);
					} else if (tl.sensors[i].getColour() == 3) {
						g2.setColor(Color.PINK);
					} else if (tl.sensors[i].getColour() == 4) {
						g2.setColor(Color.YELLOW);
					} else {
						g2.setColor(Color.RED);
					}
				}
				if(tl.sensors[i].connected) {
					g2.setColor(Color.GREEN);
				}

				g2.fill(sensor[i]);

				for(int j = 0; j < tl.numNodes; j++){

					if(tl.sensors[i].connections.containsKey(j)){
						g2.setColor(tl.sensors[i].connections.get(j));
						g2.drawLine(tl.sensors[i].getX(), 500 - tl.sensors[i].getY() , tl.sensors[j].getX() , 500 - tl.sensors[j].getY());

					}
				}
			}
		}
	}

	public void simRunning(){
		if(sim1.isEnabled()){
			sim1.setEnabled(false);
			sim2.setEnabled(false);
			sim3.setEnabled(false);
			sigStopped = false;
		}else{
			sim1.setEnabled(true);
			sim2.setEnabled(true);
			sim3.setEnabled(true);
		}
	}
}
