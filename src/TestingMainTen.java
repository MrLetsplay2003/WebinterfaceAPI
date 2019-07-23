import java.io.File;

import me.mrletsplay.webinterfaceapi.http.HttpServer;
import me.mrletsplay.webinterfaceapi.http.document.FileDocument;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;

public class TestingMainTen {

	public static void main(String[] args) throws Exception {
		HttpServer s = Webinterface.getServer();
		s.getDocumentProvider().registerDocument("/favicon.ico", new FileDocument(new File("/home/mr/Desktop/testing/mrcore/test.jpg")));
		Webinterface.start();
	}

}
