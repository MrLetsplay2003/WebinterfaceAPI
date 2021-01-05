package me.mrletsplay.webinterfaceapi.js;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class JavaScriptFunction {

	private Supplier<String>
		signature,
		code;
	
	private List<String> modifiers = new ArrayList<>();
	
	public JavaScriptFunction(Supplier<String> signature) {
		this.signature = signature;
	}
	
	public JavaScriptFunction(String signature) {
		this(() -> signature);
	}
	
	public void setSignature(Supplier<String> signature) {
		this.signature = signature;
	}
	
	public void setSignature(String signature) {
		setSignature(() -> signature);
	}
	
	public Supplier<String> getSignature() {
		return signature;
	}
	
	public void setCode(Supplier<String> code) {
		this.code = code;
	}
	
	public void setCode(String code) {
		setCode(() -> code);
	}
	
	public void appendCode(Supplier<String> code) {
		if(this.code == null) {
			setCode(code);
			return;
		}
		Supplier<String> oldCode = this.code;
		this.code = () -> oldCode.get() + code.get();
	}
	
	public void appendCode(String code) {
		appendCode(() -> code);
	}
	
	public Supplier<String> getCode() {
		return code;
	}
	
	public void addModifier(String modifier) {
		modifiers.add(modifier);
	}
	
	public List<String> getModifiers() {
		return modifiers;
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for(String m : modifiers) b.append(m).append(" ");
		b.append("function ")
				.append(getSignature().get())
				.append("{");
		if(getCode() != null) b.append(getCode().get());
		b.append("}");
		return b.toString();
	}
	
}
