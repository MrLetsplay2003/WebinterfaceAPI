package me.mrletsplay.webinterfaceapi.http.compression;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import me.mrletsplay.mrcore.misc.FriendlyException;

public class GZIPCompression implements HttpCompressionMethod {

	@Override
	public String getName() {
		return "gzip";
	}

	@Override
	public byte[] compress(byte[] uncompressed) {
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		try {
			GZIPOutputStream gOut = new GZIPOutputStream(bOut);
			gOut.write(uncompressed);
			gOut.close();
		} catch (IOException e) {
			throw new FriendlyException("Failed to compress", e);
		}
		return bOut.toByteArray();
	}

}
