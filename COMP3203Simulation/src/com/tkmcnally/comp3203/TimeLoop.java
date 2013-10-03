package com.tkmcnally.comp3203;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import javax.swing.Timer;

import com.tkmcnally.comp3203.gui.GUI;
import com.tkmcnally.comp3203.node.Sensor;

public class TimeLoop implements ActionListener {

	private final Random rand = new Random();
	public int numNodes = 2;
	public Sensor[] sensors;
	public final int range = 100;
	private double angleBtw;
	private final GUI gui;
	private int state = 0;
	public Timer timer;
	public SimpleThread simpleThread;

	public int part = 0;

	private int alg = 0;

	//for log calculations
	private int time, numCycles, numConnection, numRange; //time completed, number of cycles, number of nodes, number of connections made, number of nodes in range of one another (respectively)


	public TimeLoop(GUI g){
		gui = g;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	public void calForLog(StopWatch s){
		time = (int) s.elapsedTime();

		for(int i = 0; i < numNodes; i++){
			numCycles += sensors[i].getNumRot();
			if (sensors[i].connected) {
				numConnection++;
			}

			for(int j = 0; j < numNodes; j++){
				if(sensors[i].inRange.containsKey(j) && !(sensors[j].getChecked())){
					sensors[j].setChecked();
					numRange++;
				}
			}
		}

	}

	public void checkConnections(){

		for(int i = 0; i < numNodes; i++){
			for(int j = 0; j < numNodes; j++){
				if(j != i){
					if(sensors[i].inRange.containsKey(j)){
						initialCheck(sensors[i], sensors[j]);
						if(checkSensor1(sensors[i] , sensors[j]) && checkSensor2(sensors[i], sensors[j])){
							sensors[i].connections.put(j, sensors[i].color);
							sensors[i].connected = true;
							sensors[j].connected = true;
						}
					}
				}
			}
		}
	}


	public boolean checkSensor1(Sensor s1, Sensor s2){

		double delta_y = s2.getY() - s1.getY();
		double delta_x = s2.getX() - s1.getX();
		double angle = Math.atan(delta_y / delta_x) * 180/Math.PI;



		if(state == 1){
			if(angle < 0) {
				angle = 180 + angle;
			}
			angle = angle + 180;
		}else if(state == 2){
			if(angle < 0) {
				angle = 360 + angle;
			}
			angle = 180 + angle;
		}else if(state == 3){
			if(angle < 0) {
				angle = angle * -1;
			}
			angle = 180 - angle;
		}else if(state == 4){
			if(angle < 0) {
				angle = 360 + angle;
			}

		}
		else if(state == 5){
			if(angle < 0) {
				angle = 360 + angle;
			}
		}

		else if(state == 6){
			if(angle < 0) {
				angle = 360 + angle;
			}
			angle = 180;

		}

		else if(state == 7){
			if(angle < 0) {
				angle = 360 + angle;
			}

		}
		else if(state == 8){
			if(angle < 0) {
				angle = 360 + angle;
			}
		}


		//   System.out.println("ANGLE 1 - > 2: " + angle + " cur" + s1.getCurrAngle());
		if(angle <= (s1.getCurrAngle() + s1.getBeamWidth()/2) && angle >= (s1.getCurrAngle() - s1.getBeamWidth()/2)){
			//System.out.println("TRUE 1 ");
			return true;
		}


		return false;
	}

	public boolean checkSensor2(Sensor s1, Sensor s2){

		double delta_y = s1.getY() - s2.getY();
		double delta_x = s1.getX() - s2.getX();
		double angle = Math.atan(delta_y / delta_x) * 180/Math.PI;

		double b_minus = s2.getCurrAngle() - (s2.getBeamWidth()/2);
		double b_plus = s2.getCurrAngle() + (s2.getBeamWidth()/2);



		if(state == 1){
			if(angle < 0) {
				angle = 180 + angle;
			}
		}else if(state == 2){
			if(angle < 0) {
				angle = 360 + angle;
			}
		}else if(state == 3){

			if(angle < 0) {
				angle = angle * -1;
			}
			angle = 180 - angle;
			angle = angle + 180;
		}else if(state == 4){
			if(angle < 0) {
				angle = 360 + angle;
			}
			angle = angle + 180;
		}else if(state == 5){

		}else if(state == 6){
			angle = 0;

			if(b_plus >= 360){
				b_plus = b_plus - 360;
				b_minus = b_minus - 360;
				//System.out.println("cur: " + s2.getCurrAngle() + " bplus: " + b_plus + " b_minus: " + b_minus);
			}

		}else if(state == 7){
			angle = 0;
		}else if(state == 8){

		}

		// System.out.println("ANGLE 2 -> 1: " + angle + " cur" + s2.getCurrAngle());
		if(angle <= (b_plus) && angle >= (b_minus)){
			//System.out.println("TRUE");
			return true;
		}

		return false;
	}

	public void clearLog(){
		time = 0;
		numCycles = 0;
		numConnection = 0;
		numRange = 0;
	}

	public void coinFlip(){
		for(int i = 0; i < numNodes; i++){
			sensors[i].coinFlip();
		}
	}

	public void coinFlip2(){
		for(int i = 0; i < numNodes; i++){
			sensors[i].coinFlip2();
		}
	}

	public void colourGraph(){
		for(int i = 0; i < numNodes; i++){
			for(int j = 0; j < numNodes; j++){
				if(j != i){
					if(sensors[i].inRange.containsKey(j)){
						if (sensors[i].getColour() == sensors[j].getColour()){
							sensors[j].incColour();
							sensors[j].setRotSpeed(sensors[j].getNumSect() ^ sensors[j].getColour());
						}
					}
				}
			}
		}
	}

	public int getAlg(){
		return alg;
	}

	public void initialCheck(Sensor s1, Sensor s2){

		//	angleBtw = Math.acos(result) * 180/Math.PI;
		//state number given for each case
		if(s1.getY() > s2.getY() && s1.getX() < s2.getX()){        //top left
			//	System.out.println("TOP LEFT");
			state = 1;
		}else if(s1.getY() > s2.getY() && s1.getX() > s2.getX()){  //top right
			//	System.out.println("TOP RIGHT");
			state = 2;
		}else if(s1.getY() < s2.getY() && s1.getX() > s2.getX()){  //bottom right
			//System.out.println("BOTTOM RIGHT");
			if(angleBtw < 0) {
				angleBtw = angleBtw * -1;
			}
			state = 3;
			//s1Diff = -180;
		}else if(s1.getY() < s2.getY() && s1.getX() < s2.getX()){  //bottom left
			//	System.out.println("BOTTOM LEFT");
			state = 4;
		}else if(s1.getY() == s2.getY() && s1.getX() < s2.getX()){  // left
			//	System.out.println("LEFT SAME Y");
			state = 5;
		}else if(s1.getY() == s2.getY() && s1.getX() > s2.getX()){  //bottom left
			//	System.out.println("RIGHT SAME Y");
			state = 6;
		}else if(s1.getY() >= s2.getY() && s1.getX() == s2.getX()){  // left
			//	System.out.println(" ABOVE SAME x");
			state = 7;
		}else if(s1.getY() <= s2.getY() && s1.getX() == s2.getX()){  //bottom left
			//System.out.println("BELOW SAME Y");
			state = 8;
		}
	}
	public void initializeList(){

		double delta_y;
		double delta_x;
		double angle;

		for(int i = 0; i < numNodes; i++){
			for(int j = 0; j < numNodes; j++){
				if(j != i){

					if(((sensors[i].getX() - sensors[j].getX()) * (sensors[i].getX() - sensors[j].getX())) + ((sensors[i].getY() - sensors[j].getY()) * (sensors[i].getY() - sensors[j].getY())) <= ((range / 2) * (range / 2))){

						delta_y = sensors[i].getY() - sensors[j].getY();
						delta_x = sensors[i].getX() - sensors[j].getX();
						angle = Math.atan(delta_y / delta_x) * 180/Math.PI;

						sensors[i].inRange.put(j, angle);
						//	System.out.println(j + " " + angle);

					}
				}
			}
		}
	}

	public int runSim(){
		spawnSensors();

		initializeList();
		colourGraph();
		alg = 1;
		gui.simRunning();

		gui.repaint();
		checkConnections();

		for(int i = 0; i < numNodes; i++){
			checkConnections();
			gui.repaint();
			simpleThread = new SimpleThread(gui, sensors[i], this, alg);
			simpleThread.start();
			gui.repaint();
		}
		gui.repaint();

		return 0;
	}

	public int runSim2(){
		spawnSensors();

		initializeList();
		coinFlip();
		alg = 2;
		gui.simRunning();

		gui.repaint();
		checkConnections();

		for(int i = 0; i < numNodes; i++){
			checkConnections();
			gui.repaint();
			simpleThread = new SimpleThread(gui, sensors[i], this, alg);
			simpleThread.start();
			gui.repaint();
		}
		gui.repaint();

		return 0;
	}

	public int runSim3(){
		spawnSensors();

		initializeList();
		coinFlip2();
		alg = 3;
		gui.simRunning();

		gui.repaint();
		checkConnections();

		for(int i = 0; i < numNodes; i++){
			checkConnections();
			gui.repaint();
			simpleThread = new SimpleThread(gui, sensors[i], this, alg);
			simpleThread.start();
			gui.repaint();
		}
		gui.repaint();

		return 0;
	}

	public void spawnSensors(){
		sensors = new Sensor[numNodes];
		int coorX1 = rand.nextInt(450 - 100) + 100;
		int coorY1 = rand.nextInt(450 - 100) + 100;

		if(part == 0){


			int a;
			int b;
			while(true){
				if (rand.nextBoolean()) {
					a = coorX1 + rand.nextInt(range/2) - (0);
				} else {
					a = coorX1 - rand.nextInt(range/2) - (0);
				}
				if (rand.nextBoolean()) {
					b = coorY1 + rand.nextInt(range/2) - (0);
				} else {
					b = coorY1 - rand.nextInt(range/2) - (0);
				}
				if(((a-coorX1)*(a-coorX1)) + ((b-coorY1)*(b-coorY1)) <= ((range/2)*(range/2))){

					break;
				}
			}
			int uOrAng = 2;
			int vOrAng = 2;

			while ((uOrAng % 2) == 0) {
				uOrAng = rand.nextInt(8);
			}
			while ((vOrAng % 2) == 0) {
				vOrAng = rand.nextInt(8);
			}


			sensors[0] = new Sensor("U", rand, 1, range, coorX1, coorY1, uOrAng);
			sensors[1] = new Sensor("U", rand, 1, range, a, b, vOrAng);

		}else if(part == 1){
			for(int i = 0; i < numNodes; i++)
			{

				coorX1 = rand.nextInt(450 - 100) + 100;
				coorY1 = rand.nextInt(450 - 100) + 100;


				int uOrAng = 2;

				while ((uOrAng % 2) == 0) {
					uOrAng = rand.nextInt(8);
				}


				sensors[i] = new Sensor("U", rand, 1, range, coorX1, coorY1, uOrAng);
			}
		}
	}

	//CREATES THE LOG FILE
	public void writeFile() throws IOException{
		BufferedWriter buf = new BufferedWriter(new FileWriter("log.txt"));
		buf.write("LOG FILE");
		buf.newLine();
		buf.write("------------------------------------------------");
		buf.newLine();
		buf.write("Completion Time: " + time + " seconds");
		buf.newLine();
		buf.write("Number of Cycles: " + numCycles);
		buf.newLine();
		buf.write("Number of Nodes: " + numNodes);
		buf.newLine();
		buf.write("Number of Connected Node: " + numConnection);
		buf.newLine();
		buf.write("Number of Nodes in Range of each other: " + numRange);

		buf.close();

	}
}
