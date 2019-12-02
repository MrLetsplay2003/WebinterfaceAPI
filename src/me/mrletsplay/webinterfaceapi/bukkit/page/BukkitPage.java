package me.mrletsplay.webinterfaceapi.bukkit.page;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.webinterfaceapi.bukkit.WebinterfacePlugin;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccount;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccountConnection;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePageSection;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.SendJSAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ElementValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.ElementLayout;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceInputField;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfacePageElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceText;
import me.mrletsplay.webinterfaceapi.webinterface.session.WebinterfaceSession;

public class BukkitPage extends WebinterfacePage {

	public BukkitPage() {
		super("Bukkit", "/bukkit");
		
		WebinterfacePageSection sets = new WebinterfacePageSection();
		sets.addTitle("Settings");
		
		sets.addDynamicElements(() -> {
			List<WebinterfacePageElement> els = new ArrayList<>();
			
			WebinterfaceAccount acc = WebinterfaceSession.getCurrentSession().getAccount();
			WebinterfaceAccountConnection mcAcc = acc.getConnection(WebinterfacePlugin.MINECRAFT_ACCOUNT_CONNECTION_NAME);
			
			if(mcAcc != null) {
				WebinterfaceText txt = new WebinterfaceText("Connected Minecraft account");
				txt.addLayouts(ElementLayout.CENTER_VERTICALLY);
				els.add(txt);
				
				WebinterfaceText txt2 = new WebinterfaceText(mcAcc.getUserName() + " (" + mcAcc.getUserID() + ")");
				txt2.addLayouts(ElementLayout.SECOND_TO_LAST_COLUMN);
				els.add(txt2);
			}else {
				WebinterfaceText txt = new WebinterfaceText("Connect to Minecraft account");
				txt.addLayouts(ElementLayout.CENTER_VERTICALLY);
				els.add(txt);
				
				WebinterfaceInputField f = new WebinterfaceInputField("MC name");
				f.setOnChangeAction(new SendJSAction("bukkit", "connectMCAccount", new ElementValue(f)));
				els.add(f);
			}
			return els;
		});
		
		addSection(sets);
	}

}