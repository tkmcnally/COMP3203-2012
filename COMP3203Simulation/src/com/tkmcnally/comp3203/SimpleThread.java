package com.tkmcnally.comp3203;

import com.tkmcnally.comp3203.gui.GUI;
import com.tkmcnally.comp3203.node.Sensor;

public class SimpleThread extends Thread {

	private final GUI gui;
	private final Sensor sens;
	private final TimeLoop time;
	private final int alg;

	public SimpleThread(GUI g, Sensor s, TimeLoop t, int a) {
		gui = g;
		sens = s;
		time = t;
		alg = a;
	}

	@Override
	public void run() {
		while (true) {
			time.checkConnections();

			if((sens.getCurrAngle() + sens.getBeamWidth()) > 360){
				sens.setCurrAngle((int) (sens.getCurrAngle() + sens.getBeamWidth()));
				sens.setCurrAngle((int) (sens.getCurrAngle() - 360));
			} else {
				sens.setCurrAngle((int) (sens.getCurrAngle() + sens.getBeamWidth()));
			}

			sens.incNumRot();

			if (alg == 2){
				if (sens.checkRotFinish()){
					gui.repaint();
					break;
				}
			}
			if (alg == 3){
				if (sens.checkRotFinish2()){
					gui.repaint();
					break;
				}
			}

			if(gui.sigStopped){
				break;
			}
			gui.repaint();
			try {
				sleep(100*sens.getRotSpeed());
			}
			catch (InterruptedException e) {}
		}
	}
}