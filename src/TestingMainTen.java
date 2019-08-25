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
import me.mrletsplay.webinterfaceapi.webinterface.page.element.ElementLayout;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceButton;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceCheckBox;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceElementGroup;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceInputField;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceSelect;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceTitleText;

public class TestingMainTen {

	public static void main(String[] args) throws Exception {
		System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
		WebinterfacePage p = new WebinterfacePage("Test page", "/test");
		WebinterfacePageSection sc = new WebinterfacePageSection();
		
		WebinterfaceElementGroup g = new WebinterfaceElementGroup();
		g.addTitle("This is a section");
		
		WebinterfaceTitleText tt = new WebinterfaceTitleText("Hello world!");
		tt.addLayouts(ElementLayout.FULL_WIDTH, ElementLayout.LEFTBOUND, ElementLayout.CENTER_VERTICALLY);
		g.addElement(tt);
		
		WebinterfaceText tx = new WebinterfaceText("Lorem ipsum dolor sit amet etc etc etc text huiui wtf was tu ich hier");
		tx.addLayouts(ElementLayout.FULL_WIDTH, ElementLayout.LEFTBOUND);
		g.addElement(tx);
		
		WebinterfaceInputField ip = new WebinterfaceInputField();
		ip.addLayouts(ElementLayout.FULL_NOT_LAST_COLUMN);
		ip.setOnChangeAction(new SendJSAction("webinterface", "lol", new ElementValue(ip)));
		g.addElement(ip);
		
		g.addElement(new WebinterfaceButton("Button #1"));
		
		WebinterfaceButton b = new WebinterfaceButton("Button #2");
		g.addElement(b);
		
		WebinterfaceText txt = new WebinterfaceText("Enable some stuff?");
		txt.addLayouts(ElementLayout.CENTER_VERTICALLY, ElementLayout.NEW_LINE);
		g.addElement(txt);
		
		WebinterfaceCheckBox ip2 = new WebinterfaceCheckBox();
		ip2.setOnChangeAction(new SendJSAction("webinterface", "lol", new CheckboxValue(ip2)));
		ip2.addLayouts(ElementLayout.CENTER_VERTICALLY);
		g.addElement(ip2);
		
		WebinterfaceSelect sel = new WebinterfaceSelect();
		sel.addLayouts(ElementLayout.NEW_LINE);
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
		b2.addLayouts(ElementLayout.FULL_WIDTH);
		g.addElement(b2);

		sc.addElement(g);
		
		p.addSection(sc);
		
		Webinterface.registerPage(p);
		
		WebinterfacePage p2 = new WebinterfacePage("Test2 page", "/test2");
		p2.addSection(sc);
		p2.addDynamicSections(() -> {
			List<WebinterfacePageSection> ss = new ArrayList<>();
			for(int i = 0; i < Math.random() * 20; i++) {
				ss.add(sc);
			}
			return ss;
		});
		Webinterface.registerPage(p2);
		
		Webinterface.start();
		
		MrCoreServiceRegistry.awaitServiceRegistration("WebinterfaceAPI").thenRun(() -> {
			System.out.println("WIAPI is online");
		});
	}

}
