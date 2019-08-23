package me.mrletsplay.webinterfaceapi.php;

public class PHP {

	private static boolean enabled;
	private static String cgiPath;
	
	static {
		enabled = false;
		cgiPath = "php-cgi";
	}
	
	public static boolean isEnabled() {
		return enabled;
	}
	
	public static void setEnabled(boolean enabled) {
		PHP.enabled = enabled;
	}
	
	public static String getCGIPath() {
		return cgiPath;
	}
	
	public static void setCGIPath(String cgiPath) {
		PHP.cgiPath = cgiPath;
	}
	
}
