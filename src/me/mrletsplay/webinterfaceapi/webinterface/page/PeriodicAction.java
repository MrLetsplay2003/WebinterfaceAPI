package me.mrletsplay.webinterfaceapi.webinterface.page;

import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceAction;

public class PeriodicAction {
	
	private WebinterfaceAction action;
	private long periodMillis;
	private boolean runImmediately;
	
	public PeriodicAction(WebinterfaceAction action, long periodMillis, boolean runImmediately) {
		this.action = action;
		this.periodMillis = periodMillis;
		this.runImmediately = runImmediately;
	}

	public WebinterfaceAction getAction() {
		return action;
	}

	public long getPeriodMillis() {
		return periodMillis;
	}
	
	public boolean isRunImmediately() {
		return runImmediately;
	}
	
}
