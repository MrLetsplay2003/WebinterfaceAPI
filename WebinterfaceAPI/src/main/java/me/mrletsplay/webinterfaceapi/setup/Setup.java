package me.mrletsplay.webinterfaceapi.setup;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;
import me.mrletsplay.webinterfaceapi.setup.impl.AccountSetupStep;
import me.mrletsplay.webinterfaceapi.setup.impl.AuthSetupStep;
import me.mrletsplay.webinterfaceapi.setup.impl.HTTPSetupStep;

public class Setup {

	public static final String OVERRIDE_SETUP_STEPS_DONE = "setup.steps-done";

	private List<SetupStep> steps;

	public Setup() {
		this.steps = new ArrayList<>();
		steps.add(new AccountSetupStep());
		steps.add(new HTTPSetupStep());
		steps.add(new AuthSetupStep());
	}

	public void addStep(SetupStep step) {
		steps.add(step);
	}

	public List<SetupStep> getSteps() {
		return steps;
	}

	public SetupStep getNextStep() {
		if(!Webinterface.getConfig().getSetting(DefaultSettings.ENABLE_SETUP)) return null;
		List<String> steps = Complex.castList(Webinterface.getConfig().getOverride(OVERRIDE_SETUP_STEPS_DONE, List.class), String.class).get();
		return this.steps.stream()
			.filter(s -> steps == null || !steps.contains(s.getID()))
			.findFirst().orElse(null);
	}

	public void addCompletedStep(String id) {
		List<String> steps = Complex.castList(Webinterface.getConfig().getOverride(OVERRIDE_SETUP_STEPS_DONE, List.class), String.class).get();
		if(steps == null) steps = new ArrayList<>();
		steps.add(id);
		Webinterface.getConfig().setOverride(OVERRIDE_SETUP_STEPS_DONE, steps);
	}

}
