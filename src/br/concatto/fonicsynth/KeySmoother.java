package br.concatto.fonicsynth;

public class KeySmoother extends Thread {
	private long time;
	private Runnable action;
	private volatile boolean free = true;
	
	@Override
	public void run() {
		while (true) {
			while (free) {
				synchronized (this) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			action.run();
			free = true;
		}
	}
	
	public void delay(long time, Runnable action) {
		if (!free) {
			free = false;
			synchronized (this) {
				notify();
			}
		}
		
		while (!free);
		
		this.time = time;
		this.action = action;
		
		free = false;
		System.out.println("notifying");
		synchronized (this) {
			notify();
		}
	}
}
