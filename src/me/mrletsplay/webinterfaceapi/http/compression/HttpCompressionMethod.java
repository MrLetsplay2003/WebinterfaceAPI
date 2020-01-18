package me.mrletsplay.webinterfaceapi.http.compression;

public interface HttpCompressionMethod {

	public String getName();
	
	public byte[] compress(byte[] uncompressed);
	
}
