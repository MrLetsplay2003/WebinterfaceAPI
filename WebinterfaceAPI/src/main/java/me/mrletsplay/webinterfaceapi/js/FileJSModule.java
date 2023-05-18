package me.mrletsplay.webinterfaceapi.js;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.exception.InvalidModuleException;

public interface FileJSModule extends JSModule {

	@Override
	public default void createContent() {
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();

		File f = new File(Webinterface.getIncludePath().resolve("js/module/").toFile(), getFileName());
		if(!f.exists()) throw new InvalidModuleException(getIdentifier());

		if(f.isDirectory()) {
			byte[] total = new byte[0];
			List<File> files = Arrays.asList(f.listFiles());
			files.sort(Comparator.comparing(fl -> fl.getName()));
			for(File fl : files) {
				if(fl.isDirectory()) continue; // No nested directories
				try {
					byte[] bs = Files.readAllBytes(fl.toPath());
					byte[] total2 = new byte[total.length + bs.length];
					System.arraycopy(total, 0, total2, 0, total.length);
					System.arraycopy(bs, 0, total2, total.length, bs.length);
					total = total2;
				} catch (IOException e) {
					throw new InvalidModuleException(getIdentifier(), e);
				}
			}

			ctx.getServerHeader().setContent("text/javascript", total);
		}else {
			try {
				byte[] bs = Files.readAllBytes(f.toPath());
				ctx.getServerHeader().setContent("text/javascript", bs);
			} catch (IOException e) {
				throw new InvalidModuleException(getIdentifier(), e);
			}
		}
	}

	public String getFileName();

}
