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
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceImage;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceInputField;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfacePageElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutProperty;
import me.mrletsplay.webinterfaceapi.webinterface.session.WebinterfaceSession;

public class BukkitPage extends WebinterfacePage {

	public BukkitPage() {
		super("Home", "/bukkit/home");
		
		addDynamicSections(() -> {
			List<WebinterfacePageSection> secs = new ArrayList<>();
			
			WebinterfaceAccount acc = WebinterfaceSession.getCurrentSession().getAccount();
			WebinterfaceAccountConnection mcAcc = acc.getConnection(WebinterfacePlugin.MINECRAFT_ACCOUNT_CONNECTION_NAME);
			
			if(mcAcc != null) {
				WebinterfacePageSection yourAccount = new WebinterfacePageSection();
				yourAccount.addInnerLayoutProperties(DefaultLayoutProperty.FULL_WIDTH);
				yourAccount.addTitle("Your Profile");
				
				yourAccount.addElement(WebinterfaceImage.builder()
						.src("https://minotar.net/avatar/" + mcAcc.getUserName())
						.alt("Avatar")
						.width("128px")
						.create());
				
				secs.add(yourAccount);
			}
			
			return secs;
		});
		
		WebinterfacePageSection sets = new WebinterfacePageSection();
		sets.addTitle("Settings");
		
		sets.addDynamicElements(() -> {
			List<WebinterfacePageElement> els = new ArrayList<>();
			
			WebinterfaceAccount acc = WebinterfaceSession.getCurrentSession().getAccount();
			WebinterfaceAccountConnection mcAcc = acc.getConnection(WebinterfacePlugin.MINECRAFT_ACCOUNT_CONNECTION_NAME);
			
			if(mcAcc != null) {
				WebinterfaceText txt = new WebinterfaceText("Connected Minecraft account");
				txt.addLayoutProperties(DefaultLayoutProperty.CENTER_VERTICALLY);
				els.add(txt);
				
				WebinterfaceText txt2 = new WebinterfaceText(mcAcc.getUserName() + " (" + mcAcc.getUserID() + ")");
				txt2.addLayoutProperties(DefaultLayoutProperty.SECOND_TO_LAST_COLUMN);
				els.add(txt2);
			}else {
				WebinterfaceText txt = new WebinterfaceText("Connect to Minecraft account");
				txt.addLayoutProperties(DefaultLayoutProperty.CENTER_VERTICALLY);
				els.add(txt);
				
				WebinterfaceInputField f = new WebinterfaceInputField("Minecraft name");
				f.setOnChangeAction(new SendJSAction("bukkit", "connectMinecraftAccount", new ElementValue(f)));
				els.add(f);
			}
			return els;
		});
		
		addSection(sets);
	}

}
