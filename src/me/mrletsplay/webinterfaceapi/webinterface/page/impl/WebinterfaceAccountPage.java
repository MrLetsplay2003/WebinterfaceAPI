package me.mrletsplay.webinterfaceapi.webinterface.page.impl;

import java.util.ArrayList;
import java.util.List;

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
import me.mrletsplay.webinterfaceapi.webinterface.page.action.ReloadPageAfterAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.SendJSAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.StringValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceButton;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceElementGroup;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfacePageElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceTitleText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceVerticalSpacer;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutProperty;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.GridLayout;
import me.mrletsplay.webinterfaceapi.webinterface.session.WebinterfaceSession;

public class WebinterfaceAccountPage extends WebinterfacePage {
	
	public static final String URL = "/wiapi/account";
	
	public WebinterfaceAccountPage() {
		super("Account", URL, true);
		getContainerStyle().setProperty("max-width", "900px");
		
		WebinterfacePageSection sc = new WebinterfacePageSection();
		
		sc.addHeading("Account connections", 2);
		sc.getStyle().setProperty("grid-template-columns", "1fr");
		sc.getMobileStyle().setProperty("grid-template-columns", "1fr");
		
		sc.addDynamicElements(() -> {
			List<WebinterfacePageElement> els = new ArrayList<>();
			
			WebinterfaceAccount account = WebinterfaceSession.getCurrentSession().getAccount();
			
			for(WebinterfaceAccountConnection con : account.getConnections()) {
				WebinterfaceAuthMethod mth = Webinterface.getAuthMethods().stream()
						.filter(m -> m.getID().equals(con.getAuthMethod()))
						.findFirst().orElse(null);
				
				if(mth == null) continue;
				
				WebinterfaceElementGroup grp = new WebinterfaceElementGroup();
				grp.addInnerLayoutProperties(new GridLayout("min-content", "auto"));
				
//				grp.addTitle("Auth method: " + mth.getName());
				WebinterfaceTitleText title = new WebinterfaceTitleText(con.getUserName());
				title.getStyle().setProperty("font-size", "24px");
				title.addLayoutProperties(DefaultLayoutProperty.LEFTBOUND, DefaultLayoutProperty.FULL_WIDTH);
				grp.addElement(title);
				
				WebinterfaceTitleText tt = new WebinterfaceTitleText("Auth method");
				tt.getStyle().setProperty("white-space", "nowrap");
				tt.addLayoutProperties(DefaultLayoutProperty.LEFTBOUND);
				grp.addElement(tt);
				WebinterfaceText un = new WebinterfaceText(mth.getName());
				un.addLayoutProperties(DefaultLayoutProperty.LEFTBOUND);
				grp.addElement(un);
				
				WebinterfaceTitleText tx2 = new WebinterfaceTitleText("Email");
				tx2.addLayoutProperties(DefaultLayoutProperty.NEW_LINE, DefaultLayoutProperty.LEFTBOUND);
				grp.addElement(tx2);
				WebinterfaceText em = new WebinterfaceText(con.getUserEmail() == null ? "-" : con.getUserEmail());
				em.addLayoutProperties(DefaultLayoutProperty.LEFTBOUND);
				grp.addElement(em);
				
				WebinterfaceTitleText tx4 = new WebinterfaceTitleText("Is Temporary");
				tx4.getStyle().setProperty("white-space", "nowrap");
				tx4.addLayoutProperties(DefaultLayoutProperty.NEW_LINE, DefaultLayoutProperty.LEFTBOUND);
				grp.addElement(tx4);
				WebinterfaceText temp = new WebinterfaceText(con.isTemporary() ? "yes" : "no");
				temp.addLayoutProperties(DefaultLayoutProperty.LEFTBOUND);
				grp.addElement(temp);
				
				if(account.getConnections().size() > 1) {
					WebinterfaceButton delBtn = new WebinterfaceButton("Remove connection");
					delBtn.setWidth("auto");
					delBtn.addLayoutProperties(DefaultLayoutProperty.FULL_WIDTH);
					delBtn.setOnClickAction(new ConfirmAction(new MultiAction(new SendJSAction("webinterface", "removeAccountConnection", new StringValue(con.getAuthMethod())), new ReloadPageAfterAction(100))));
					
					grp.addElement(delBtn);
				}
				
				grp.addElement(new WebinterfaceVerticalSpacer("30px"));
				
				els.add(grp);
			}
			
			WebinterfaceButton btnConnect = new WebinterfaceButton("Connect another auth method");
			btnConnect.addLayoutProperties(DefaultLayoutProperty.FULL_WIDTH);
			btnConnect.setOnClickAction(new RedirectAction("/login?from=" + HttpUtils.urlEncode(URL) + "&connect=true"));
			els.add(btnConnect);
			
			return els;
		});
		
		addSection(sc);
	}

}
