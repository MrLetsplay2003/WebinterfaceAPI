package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebinterfaceHandler {

	public String requestTarget();

	public String[] requestTypes();

	public String permission() default "";

}
