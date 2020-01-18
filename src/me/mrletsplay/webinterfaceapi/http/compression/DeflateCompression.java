package me.mrletsplay.webinterfaceapi.http.compression;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;

import me.mrletsplay.mrcore.misc.FriendlyException;

public class DeflateCompression implements HttpCompressionMethod {

	@Override
	public String getName() {
		return "deflate";
	}

	@Override
	public byte[] compress(byte[] uncompressed) {
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		try {
			DeflaterOutputStream dOut = new DeflaterOutputStream(bOut);
			dOut.write(uncompressed);
			dOut.close();
		} catch (IOException e) {
			throw new FriendlyException("Failed to compress", e);
		}
		return bOut.toByteArray();
	}

}
