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
import me.mrletsplay.webinterfaceapi.webinterface.page.action.ReloadPageAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.SendJSAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.SetValueAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.CheckboxValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ElementValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ObjectValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.StringValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceButton;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceCheckBox;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceElementGroup;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceInputField;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfacePageElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceTitleText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceVerticalSpacer;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.builder.Align;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutOption;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.GridLayout;

public class WebinterfaceAccountsPage extends WebinterfacePage {
	
	public WebinterfaceAccountsPage() {
		super("Accounts", "/wiapi/accounts", DefaultPermissions.MODIFY_USERS);
		setIcon("mdi:account");
		getContainerStyle().setProperty("max-width", "900px");
		
		WebinterfacePageSection sc = new WebinterfacePageSection();
		
		sc.addHeading("Accounts", 2);
		sc.getStyle().setProperty("grid-template-columns", "1fr");
		sc.getMobileStyle().setProperty("grid-template-columns", "1fr");
		
		sc.addDynamicElements(() -> {
			List<WebinterfacePageElement> els = new ArrayList<>();
			
			for(WebinterfaceAccount acc : Webinterface.getAccountStorage().getAccounts()) {
				WebinterfaceElementGroup grp = new WebinterfaceElementGroup();
				grp.addLayoutOptions(new GridLayout("min-content", "auto"));
				
				WebinterfaceTitleText tt = WebinterfaceTitleText.builder()
						.text(acc.getName())
						.leftboundText()
						.noLineBreaks()
						.create();
				tt.getStyle().setProperty("font-size", "24px");
				grp.addElement(tt);
				
				grp.addElement(WebinterfaceTitleText.builder()
						.text("Primary Email")
						.noLineBreaks()
						.leftboundText()
						.withLayoutOptions(DefaultLayoutOption.NEW_LINE)
						.create());
				grp.addElement(WebinterfaceText.builder()
						.text(acc.getPrimaryEmail() == null ? "-" : acc.getPrimaryEmail())
						.leftboundText()
						.create());
				
				grp.addElement(WebinterfaceTitleText.builder()
						.text("Permissions")
						.leftboundText()
						.withLayoutOptions(DefaultLayoutOption.NEW_LINE)
						.create());
				grp.addElement(WebinterfaceText.builder()
						.text(acc.getPermissions().isEmpty() ? "-" : acc.getPermissions().stream().map(Permission::getPermission).collect(Collectors.joining(", ")))
						.leftboundText()
						.create());
				
				grp.addElement(WebinterfaceTitleText.builder()
						.text("Is Temporary")
						.noLineBreaks()
						.leftboundText()
						.withLayoutOptions(DefaultLayoutOption.NEW_LINE)
						.create());
				grp.addElement(WebinterfaceText.builder()
						.text(acc.isTemporary() ? "yes" : "no")
						.leftboundText()
						.create());
				
				grp.addElement(WebinterfaceTitleText.builder()
						.text("Is Admin")
						.leftboundText()
						.withLayoutOptions(DefaultLayoutOption.NEW_LINE)
						.create());
				
				WebinterfaceCheckBox cb = WebinterfaceCheckBox.builder()
						.align(Align.LEFT_CENTER)
						.initialState(acc.hasPermission("*"))
						.create();
				
				ObjectValue opVal = new ObjectValue();
				opVal.put("account_id", new StringValue(acc.getID()));
				opVal.put("value", new CheckboxValue(cb));
				
				cb.setOnChangeAction(new MultiAction(new ConfirmAction(new SendJSAction("webinterface", "setOP", opVal)), new ReloadPageAction(true, 100)));
				grp.addElement(cb);
				
				WebinterfaceElementGroup grp2 = new WebinterfaceElementGroup();
				grp2.addLayoutOptions(new GridLayout("1fr", "1fr"));
				grp2.addTitle("Account Actions");
				grp2.addLayoutOptions(DefaultLayoutOption.FULL_WIDTH);
				
				WebinterfaceInputField addP = new WebinterfaceInputField("Add permission");
				
				ObjectValue addPVal = new ObjectValue();
				addPVal.put("account_id", new StringValue(acc.getID()));
				addPVal.put("permission", new ElementValue(addP));
				
				addP.setOnChangeAction(new MultiAction(new SendJSAction("webinterface", "addPermission", addPVal), new SetValueAction(addP, new StringValue("")), new ReloadPageAction(false, 100)));
				
				grp2.addElement(addP);
				
				WebinterfaceInputField remP = new WebinterfaceInputField("Remove permission");
				
				ObjectValue remPVal = new ObjectValue();
				remPVal.put("account_id", new StringValue(acc.getID()));
				remPVal.put("permission", new ElementValue(remP));
				
				remP.setOnChangeAction(new MultiAction(new SendJSAction("webinterface", "removePermission", remPVal), new SetValueAction(remP, new StringValue("")), new ReloadPageAction(false, 100)));
				
				grp2.addElement(remP);
				
				WebinterfaceButton delBtn = new WebinterfaceButton("Delete account");
				delBtn.setWidth("auto");
				delBtn.addLayoutOptions(DefaultLayoutOption.FULL_WIDTH);
				
				delBtn.setOnClickAction(new ConfirmAction(new MultiAction(new SendJSAction("webinterface", "deleteAccount", new StringValue(acc.getID())), new ReloadPageAction(false, 100))));
				
				grp2.addElement(delBtn);
				
				grp.addElement(grp2);
				grp.addElement(new WebinterfaceVerticalSpacer("30px"));
				
				els.add(grp);
			}
			
			return els;
		});
		
		addSection(sc);
	}

}
