package com.tkmcnally.comp3203.node;


import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Sensor {

	private final int coorX, coorY;
	private int rotSpeed;
	private int currAngle;
	private final int beamWidth;
	private final int numSec;
	private final String name;
	public int counter;
	public int state;
	public boolean connected;

	public Color color = Color.MAGENTA;
	public Map<Integer, Double> inRange = new HashMap<Integer, Double>(100);
	public Map<Integer, Color> connections = new HashMap<Integer, Color>(100);

	private int numRot, colour;
	private boolean noStopRot;
	Random rand = new Random();
	private boolean checked;


	public Sensor(String s, Random rand, int rs, int r, int x, int y, int sectorSpawn){

		rotSpeed = rs;
		name = s;
		coorX = x;
		coorY = y;
		numSec = rand.nextInt(9) + 3;
		beamWidth = 360/numSec;
		//currAngle = (beamWidth/2) * (rand.nextInt(numSec)); //random start angle of beam (sector)
		currAngle = (beamWidth/2) * sectorSpawn;
		counter = 0;

	}

	public boolean checkRotFinish(){
		if (noStopRot && (numRot > numSec*numSec)) {
			return true;
		} else if (!noStopRot && (numRot > numSec)) {
			return true;
		}

		return false;
	}

	public boolean checkRotFinish2(){
		if (noStopRot && (numRot > numSec*12)) {
			return true;
		} else if (!noStopRot && (numRot > numSec)) {
			return true;
		}

		return false;
	}

	public void coinFlip(){
		noStopRot = rand.nextBoolean();

		if (noStopRot) {
			rotSpeed = 1;
		} else {
			rotSpeed = numSec;
		}
	}

	public void coinFlip2(){
		noStopRot = rand.nextBoolean();

		if (noStopRot) {
			rotSpeed = 1;
		} else {
			rotSpeed = 12;
		}
	}
	public double getAngleOrig(){

		double angle;
		double top = (coorX * coorX);
		double bottom = (int) (coorX * Math.sqrt((coorX*coorX) + (coorY*coorY)));
		double result = top/bottom;
		angle = Math.acos(result) * 180/Math.PI;

		return angle;

	}
	public int getBeamWidth(){
		return beamWidth;
	}

	public boolean getChecked(){
		return checked;
	}

	public int getColour(){
		return colour;
	}
	public double getCurrAngle(){
		return currAngle;
	}
	public String getName(){
		return name;
	}

	public int getNumRot(){
		return numRot;
	}

	public int getNumSect(){

		return numSec;

	}

	public int getRotSpeed(){
		return rotSpeed;
	}

	public int getX(){
		return coorX;
	}

	public int getY(){
		return coorY;
	}
	public void incColour(){
		colour++;
	}
	public void incNumRot(){
		numRot++;
	}

	public void rotateBeam(){

		if(counter != 2){
			counter += rotSpeed;
		}
		if(counter == 2){
			if((currAngle + beamWidth) > 360){
				currAngle += beamWidth;
				currAngle -= 360;
			} else {
				currAngle +=beamWidth;
			}
			counter = 0;
		}
	}
	public void rotateBeam2(){

		if((currAngle + beamWidth) > 360){
			currAngle += beamWidth;
			currAngle -= 360;
		} else {
			currAngle +=beamWidth;
		}

	}
	//for log file
	public void setChecked(){
		checked = true;
	}
	public void setCount(int c){
	}
	//for thread
	public void setCurrAngle(int val){
		currAngle = val;
	}

	public void setNoStopRot(boolean val){
		noStopRot = val;
	}
	public void setRotSpeed(int val){
		rotSpeed = val;
	}
}