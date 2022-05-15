package me.mrletsplay.webinterfaceapi.webinterface.page;

import me.mrletsplay.webinterfaceapi.webinterface.page.action.Action;

public class PeriodicAction {
	
	private Action action;
	private long periodMillis;
	private boolean runImmediately;
	
	public PeriodicAction(Action action, long periodMillis, boolean runImmediately) {
		this.action = action;
		this.periodMillis = periodMillis;
		this.runImmediately = runImmediately;
	}

	public Action getAction() {
		return action;
	}

	public long getPeriodMillis() {
		return periodMillis;
	}
	
	public boolean isRunImmediately() {
		return runImmediately;
	}
	
}
