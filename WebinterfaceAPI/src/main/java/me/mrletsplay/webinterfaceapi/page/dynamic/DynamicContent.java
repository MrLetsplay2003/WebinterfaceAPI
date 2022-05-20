package me.mrletsplay.webinterfaceapi.page.dynamic;

public interface DynamicContent<T> {
	
	public T create();
	
	/**
	 * Indicates whether this content is static, meaning it can safely be cached between multiple requests and users
	 * @return <code>true</code> if this content can be treated as static content, <code>false</code> otherwise
	 */
	public default boolean isStatic() {
		return false;
	}

}
