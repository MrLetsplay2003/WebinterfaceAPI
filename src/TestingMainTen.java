import java.io.File;

import me.mrletsplay.webinterfaceapi.css.CssElement;
import me.mrletsplay.webinterfaceapi.css.StyleSheet;
import me.mrletsplay.webinterfaceapi.css.selector.CssSelector;
import me.mrletsplay.webinterfaceapi.html.HtmlDocument;
import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.html.element.HtmlButton;
import me.mrletsplay.webinterfaceapi.html.element.HtmlSelect;
import me.mrletsplay.webinterfaceapi.http.HttpServer;
import me.mrletsplay.webinterfaceapi.http.document.FileDocument;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.js.JavaScriptFunction;
import me.mrletsplay.webinterfaceapi.js.JavaScriptScript;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;

public class TestingMainTen {

	public static void main(String[] args) throws Exception {
		HtmlDocument doc = new HtmlDocument();
		doc.setTitle("Hello there!");
		doc.setLanguage("en");
		doc.setDescription("Test document");
		HtmlElement myP = HtmlElement.p();
		myP.setID("thisisanelement");
		myP.setText(() -> "Your user agent is:\n" + HttpRequestContext.getCurrentContext().getClientHeader().getFields().getFieldValue("User-Agent"));
		doc.getBodyNode().appendChild(myP);
		
		HtmlSelect mySel = new HtmlSelect();
		mySel.addOption("Option 1", "\"'a");
		mySel.addOption("Option 2", "b");
		doc.getBodyNode().appendChild(mySel);
		
		StyleSheet st = new StyleSheet();
		CssElement el = new CssElement(CssSelector.selectElement(myP));
		el.setProperty("color", "red");
		el.setProperty("font-size", "30px");
		el.setProperty("font-family", "Arial");
		el.setProperty("font-weight", "bold");
		st.addElement(el);
		
		CssElement el2 = new CssElement(CssSelector.selectBody());
		el2.setProperty("background-color", "white");
		st.addElement(el2);
		
		doc.addStyleSheet("/style.css");
		doc.includeScript("https://code.jquery.com/jquery-3.4.1.min.js", true);
		doc.includeScript("/_internal/include.js", true);
		doc.addStyleSheet("/_internal/include.css");
		
		JavaScriptScript sc = new JavaScriptScript();
		JavaScriptFunction f = new JavaScriptFunction("test()");
		f.setCode("console.log(\"hello world\");");
		sc.addFunction(f);
		doc.getHeadNode().appendChild(HtmlElement.script(sc));
		
		HtmlButton b = new HtmlButton();
		b.setText("Hello world");
		b.setOnClick("alert(\"hi\");");
		doc.getBodyNode().appendChild(b);
		
		HttpServer s = Webinterface.getServer();
		s.getDocumentProvider().registerDocument("/test", new FileDocument(new File("/home/mr/Desktop/testing/mrcore/test.php")));
		s.getDocumentProvider().registerDocument("/favicon.ico", new FileDocument(new File("/home/mr/Desktop/testing/mrcore/test.jpg")));
		s.getDocumentProvider().registerDocument("/", doc);
		s.getDocumentProvider().registerDocument("/style.css", st);
		Webinterface.start();
	}

}
