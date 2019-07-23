package me.mrletsplay.webinterfaceapi.js;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class JavaScriptScript {
	
	private List<JavaScriptFunction> functions;
	private Supplier<String> code;
	
	public JavaScriptScript() {
		this.functions = new ArrayList<>();
	}
	
	public void addFunction(JavaScriptFunction function) {
		functions.add(function);
	}
	
	public void removeFunction(JavaScriptFunction function) {
		functions.remove(function);
	}
	
	public List<JavaScriptFunction> getFunctions() {
		return functions;
	}
	
	public void setCode(Supplier<String> code) {
		this.code = code;
	}
	
	public void setCode(String code) {
		setCode(() -> code);
	}
	
	public void appendCode(Supplier<String> code) {
		Supplier<String> oldCode = this.code;
		this.code = () -> oldCode.get() + code.get();
	}
	
	public void appendCode(String code) {
		appendCode(() -> code);
	}
	
	public Supplier<String> getCode() {
		return code;
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for(JavaScriptFunction f : getFunctions()) {
			b.append(f.toString());
		}
		if(getCode() != null) b.append(getCode().get());
		return b.toString();
	}

}
