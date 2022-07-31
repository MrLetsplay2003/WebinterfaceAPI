package me.mrletsplay.webinterfaceapi.setup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.setup.impl.AccountSetupStep;
import me.mrletsplay.webinterfaceapi.setup.impl.AuthSetupStep;
import me.mrletsplay.webinterfaceapi.setup.impl.DatabaseSetupStep;
import me.mrletsplay.webinterfaceapi.setup.impl.HTTPSetupStep;

public class Setup {

	public static final String OVERRIDE_SETUP_STEPS_DONE = "setup.steps-done";

	private List<SetupStep> steps;

	public Setup() {
		this.steps = new ArrayList<>();
		steps.add(new DatabaseSetupStep());
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

	public boolean isStepDone(String id) {
		return getCompletedSteps().contains(id);
	}

	public boolean isDone() {
		List<String> steps = getCompletedSteps();
		return this.steps.stream().allMatch(s -> steps.contains(s.getID()));
	}

	private List<String> getCompletedSteps() {
		List<String> steps = Complex.castList(Webinterface.getConfig().getOverride(OVERRIDE_SETUP_STEPS_DONE, List.class), String.class).get();
		if(steps == null) return Collections.emptyList();
		return steps;
	}

	public SetupStep getNextStep() {
		List<String> steps = getCompletedSteps();
		return this.steps.stream()
			.filter(s -> steps == null || !steps.contains(s.getID()))
			.findFirst().orElse(null);
	}

	public void addCompletedStep(String id) {
		List<String> steps = new ArrayList<>(getCompletedSteps());
		steps.add(id);
		Webinterface.getConfig().setOverride(OVERRIDE_SETUP_STEPS_DONE, steps);
	}

}
