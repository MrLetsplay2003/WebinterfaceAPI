package me.mrletsplay.webinterfaceapi.webinterface.page.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import me.mrletsplay.mrcore.http.HttpUtils;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccount;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccountConnection;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAuthMethod;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePageSection;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.ConfirmAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.MultiAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.RedirectAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.ReloadPageAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.SendJSAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.StringValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceButton;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceElementGroup;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceTitleText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceVerticalSpacer;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutOption;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.GridLayout;
import me.mrletsplay.webinterfaceapi.webinterface.session.WebinterfaceSession;

public class WebinterfaceAccountPage extends WebinterfacePage {
	
	public static final String URL = "/wiapi/account";
	
	public WebinterfaceAccountPage() {
		super("Account", URL, true);
		
		WebinterfacePageSection sc = new WebinterfacePageSection();
		sc.setSlimLayout(true);
		
		sc.addHeading("Account connections", 2);
		sc.getStyle().setProperty("grid-template-columns", "1fr");
		sc.getMobileStyle().setProperty("grid-template-columns", "1fr");
		
		sc.dynamic(els -> {
			WebinterfaceAccount account = WebinterfaceSession.getCurrentSession().getAccount();
			
			Map<WebinterfaceAccountConnection, WebinterfaceAuthMethod> loginCons = new LinkedHashMap<>();
			for(WebinterfaceAccountConnection con : account.getConnections()) {
				WebinterfaceAuthMethod mth = Webinterface.getAuthMethods().stream()
						.filter(m -> m.getID().equals(con.getConnectionName()))
						.findFirst().orElse(null);
				if(mth != null) loginCons.put(con, mth);
			}
			
			for(WebinterfaceAccountConnection con : account.getConnections()) {
				WebinterfaceAuthMethod mth = loginCons.get(con);
				
				WebinterfaceElementGroup grp = new WebinterfaceElementGroup();
				grp.addLayoutOptions(new GridLayout("min-content", "auto"));
				
//				grp.addTitle("Auth method: " + mth.getName());
				WebinterfaceTitleText title = WebinterfaceTitleText.builder()
						.text(con.getUserName())
						.fullWidth()
						.leftboundText()
						.create();
				title.getStyle().setProperty("font-size", "24px");
				grp.addElement(title);
				
				if(mth != null) {
					grp.addElement(WebinterfaceTitleText.builder()
							.text("Auth method")
							.noLineBreaks()
							.leftboundText()
							.create());
					grp.addElement(WebinterfaceText.builder()
							.text(mth.getName())
							.leftboundText()
							.create());
				}
				
				grp.addElement(WebinterfaceTitleText.builder()
						.text("Email")
						.leftboundText()
						.withLayoutOptions(DefaultLayoutOption.NEW_LINE)
						.create());
				grp.addElement(WebinterfaceText.builder()
						.text(con.getUserEmail() == null ? "-" : con.getUserEmail())
						.leftboundText()
						.create());
				
				grp.addElement(WebinterfaceTitleText.builder()
						.text("Is Temporary")
						.noLineBreaks()
						.leftboundText()
						.withLayoutOptions(DefaultLayoutOption.NEW_LINE)
						.create());
				grp.addElement(WebinterfaceText.builder()
						.text(con.isTemporary() ? "yes" : "no")
						.leftboundText()
						.create());
				
				if(loginCons.size() > 1) {
					grp.addElement(WebinterfaceButton.builder()
							.text("Remove connection")
							.fullWidth()
							.onClick(ConfirmAction.of(MultiAction.of(new SendJSAction("webinterface", "removeAccountConnection", new StringValue(con.getConnectionName())), ReloadPageAction.delayed(100))))
							.width("auto")
							.create());
				}
				
				grp.addElement(new WebinterfaceVerticalSpacer("30px"));
				
				els.add(grp);
			}
			
			els.add(WebinterfaceButton.builder()
					.text("Connect another auth method")
					.withLayoutOptions(DefaultLayoutOption.FULL_WIDTH)
					.onClick(RedirectAction.to("/login?from=" + HttpUtils.urlEncode(URL) + "&connect=true"))
					.create());
		});
		
		addSection(sc);
	}

}
