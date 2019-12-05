import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.main.MrCoreServiceRegistry;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePageSection;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.SendJSAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ArrayValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.CheckboxValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ElementAttributeValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ElementValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ObjectValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceButton;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceCheckBox;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceElementGroup;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceImage;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceInputField;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceSelect;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceTitleText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutProperty;

public class TestingMainTen {

	public static void main(String[] args) throws Exception {
		WebinterfacePage p = new WebinterfacePage("Test page", "/test");
		WebinterfacePageSection sc = new WebinterfacePageSection();
		
		WebinterfaceElementGroup g = new WebinterfaceElementGroup();
		g.addTitle("This is a section");
		
		WebinterfaceTitleText tt = new WebinterfaceTitleText("Hello world!");
		tt.addLayoutProperties(DefaultLayoutProperty.FULL_WIDTH, DefaultLayoutProperty.LEFTBOUND, DefaultLayoutProperty.CENTER_VERTICALLY);
		g.addElement(tt);
		
		WebinterfaceText tx = new WebinterfaceText("Lorem ipsum dolor sit amet etc etc etc text huiui wtf was tu ich hier");
		tx.addLayoutProperties(DefaultLayoutProperty.FULL_WIDTH, DefaultLayoutProperty.LEFTBOUND);
		g.addElement(tx);
		
		WebinterfaceInputField ip = new WebinterfaceInputField();
		ip.addLayoutProperties(DefaultLayoutProperty.FULL_NOT_LAST_COLUMN);
		ip.setOnChangeAction(new SendJSAction("webinterface", "lol", new ElementValue(ip)));
		g.addElement(ip);
		
		g.addElement(new WebinterfaceButton("Button #1"));
		
		WebinterfaceButton b = new WebinterfaceButton("Button #2");
		g.addElement(b);
		
		WebinterfaceText txt = new WebinterfaceText("Enable some stuff?");
		txt.addLayoutProperties(DefaultLayoutProperty.CENTER_VERTICALLY, DefaultLayoutProperty.NEW_LINE);
		g.addElement(txt);
		
		WebinterfaceCheckBox ip2 = new WebinterfaceCheckBox();
		ip2.setOnChangeAction(new SendJSAction("webinterface", "lol", new CheckboxValue(ip2)));
		ip2.addLayoutProperties(DefaultLayoutProperty.CENTER_VERTICALLY);
		g.addElement(ip2);
		
		WebinterfaceSelect sel = new WebinterfaceSelect();
		sel.addLayoutProperties(DefaultLayoutProperty.NEW_LINE);
		sel.addOption("This is an option", "option-1");
		sel.addOption("This is also an option", "option-2");
		sel.addOption("This is another option", "option-3");
		sel.addOption("This is the last option", "option-4");
		g.addElement(sel);
		
		WebinterfaceButton b2 = new WebinterfaceButton("Save");
		
		ObjectValue v = new ObjectValue();
		v.putValue("testing", new ElementValue(ip));
		v.putValue("äöü\"", new ArrayValue());
		
		b2.setOnClickAction(new SendJSAction("webinterface", "lol", new ArrayValue(new ElementValue(ip), new ElementAttributeValue(b2, "style"), v)));
		b2.setWidth("150px");
		b2.addLayoutProperties(DefaultLayoutProperty.FULL_WIDTH);
		g.addElement(b2);

		sc.addElement(g);
		
		p.addSection(sc);
		
		Webinterface.registerPage(p);
		
		WebinterfacePage p2 = new WebinterfacePage("Test2 page", "/test2");
		p2.addSection(sc);
		p2.addDynamicSections(() -> {
			List<WebinterfacePageSection> ss = new ArrayList<>();
			for(int i = 0; i < /*Math.random() **/ 20; i++) {
				ss.add(sc);
			}
			return ss;
		});
		Webinterface.registerPage(p2);
		
		WebinterfacePage pg = new WebinterfacePage("TV16", "/tv16");
		
		WebinterfacePageSection sc2 = new WebinterfacePageSection();
		
		sc2.addTitle("Test");
		
		WebinterfaceImage img = new WebinterfaceImage("https://www.mairie-francheville69.fr/wp-content/uploads/2017/11/image-test.jpeg");
		
		sc2.addElement(img);
		sc2.addElement(img);
		sc2.addElement(img);
		
		pg.addSection(sc2);
		
		Webinterface.registerPage(pg);
		
		Webinterface.start();
		
		MrCoreServiceRegistry.awaitServiceRegistration("WebinterfaceAPI").thenRun(() -> {
			System.out.println("WIAPI is online");
		});
	}

}
