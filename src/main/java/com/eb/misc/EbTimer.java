package com.eb.misc;

import java.util.Timer;
import java.util.TimerTask;

public class EbTimer {
	
	private Timer timer;

	public void schedule(Runnable action, int n, int m)
	{
		if (timer!=null)
			timer.cancel();
		timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				action.run();				
			}
		}, n, m);
	}
	
	public void stop()
	{
		if (timer!=null)
		{
			timer.cancel();
			timer=null;
		}
	}

}
