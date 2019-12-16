package me.mrletsplay.webinterfaceapi.webinterface.page.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.permission.Permission;
import me.mrletsplay.webinterfaceapi.webinterface.DefaultPermissions;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccount;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePageSection;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.ConfirmAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.MultiAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.ReloadPageAfterAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.SendJSAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.CheckboxValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ObjectValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.StringValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceCheckBox;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceElementGroup;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfacePageElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceTitleText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceVerticalSpacer;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutProperty;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.GridLayout;

public class WebinterfaceAccountsPage extends WebinterfacePage {
	
	public WebinterfaceAccountsPage() {
		super("Account Management", "/accounts", DefaultPermissions.MODIFY_USERS);
		
		WebinterfacePageSection sc = new WebinterfacePageSection();
		
		sc.addHeading("Accounts", 2);
		sc.addInnerLayoutProperties(new GridLayout("1fr", "1fr"));
		
		sc.addDynamicElements(() -> {
			List<WebinterfacePageElement> els = new ArrayList<>();
			
			for(WebinterfaceAccount acc : Webinterface.getAccountStorage().getAccounts()) {
				WebinterfaceElementGroup grp = new WebinterfaceElementGroup();
				grp.addElement(new WebinterfaceTitleText("Username"));
				grp.addElement(new WebinterfaceText(acc.getName()));
				
				WebinterfaceTitleText tx2 = new WebinterfaceTitleText("Primary Email");
				tx2.addLayoutProperties(DefaultLayoutProperty.NEW_LINE);
				grp.addElement(tx2);
				grp.addElement(new WebinterfaceText(acc.getPrimaryEmail() == null ? "-" : acc.getPrimaryEmail()));
				
				WebinterfaceTitleText tx3 = new WebinterfaceTitleText("Permissions");
				tx3.addLayoutProperties(DefaultLayoutProperty.NEW_LINE);
				grp.addElement(tx3);
				grp.addElement(new WebinterfaceText(acc.getPermissions().isEmpty() ? "-" : acc.getPermissions().stream().map(Permission::getPermission).collect(Collectors.joining(", "))));
				
				WebinterfaceElementGroup grp2 = new WebinterfaceElementGroup();
				grp2.addInnerLayoutProperties(new GridLayout("1fr", "1fr"));
				grp2.addTitle("Account Actions");
				grp2.addLayoutProperties(DefaultLayoutProperty.FULL_WIDTH);
				
				WebinterfaceText tx = new WebinterfaceText("Is OP (has * permission)");
				grp2.addElement(tx);
				
				WebinterfaceCheckBox cb = new WebinterfaceCheckBox(acc.hasPermission("*"));
				
				ObjectValue val = new ObjectValue();
				val.putValue("account_id", new StringValue(acc.getID()));
				val.putValue("value", new CheckboxValue(cb));
				
				cb.setOnChangeAction(new MultiAction(new ConfirmAction(new SendJSAction("webinterface", "setOP", val)), new ReloadPageAfterAction(100)));
				grp2.addElement(cb);
				
				grp.addElement(grp2);
				grp.addElement(new WebinterfaceVerticalSpacer("50px"));
				
				els.add(grp);
			}
			
			return els;
		});
		
		addSection(sc);
	}

}
