import java.io.File;

import me.mrletsplay.webinterfaceapi.http.HttpServer;
import me.mrletsplay.webinterfaceapi.http.document.FileDocument;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePageSection;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.SendJSAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ElementValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceButton;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceElementGroup;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceElementLayout;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceInputField;

public class TestingMainTen {

	public static void main(String[] args) throws Exception {
		HttpServer s = Webinterface.getServer();
		s.getDocumentProvider().registerDocument("/favicon.ico", new FileDocument(new File("/home/mr/Desktop/testing/mrcore/test.jpg")));
		
		
		WebinterfacePage p = new WebinterfacePage("Test page", "/test");
		WebinterfacePageSection sc = new WebinterfacePageSection();
		
		WebinterfaceElementGroup g = new WebinterfaceElementGroup();
		g.addElement(new WebinterfaceButton("Button #1"));
		
		WebinterfaceButton b = new WebinterfaceButton("Button #2");
		b.setLayout(WebinterfaceElementLayout.FULL_WIDTH);
		g.addElement(b);
		
		WebinterfaceInputField ip = new WebinterfaceInputField();
		ip.setID("hello-world");
		g.addElement(ip);
		
		WebinterfaceButton b2 = new WebinterfaceButton("Save");
		b2.setOnClickAction(new SendJSAction("webinterface", "lol", new ElementValue("hello-world")));
		b2.setWidth("150px");
		b2.setLayout(WebinterfaceElementLayout.FULL_WIDTH);
		g.addElement(b2);

		sc.addElement(g);
		
		p.addSection(sc);
		
		Webinterface.registerPage(p);
		
		WebinterfacePage p2 = new WebinterfacePage("Test2 page", "/test2");
		p2.addSection(sc);
		Webinterface.registerPage(p2);
		
		Webinterface.registerActionHandler(new TestHandler());
		Webinterface.start();
	}

}
