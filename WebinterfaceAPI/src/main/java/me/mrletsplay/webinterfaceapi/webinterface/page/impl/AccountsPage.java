package me.mrletsplay.webinterfaceapi.webinterface.page.impl;

import me.mrletsplay.webinterfaceapi.webinterface.DefaultPermissions;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.auth.Account;
import me.mrletsplay.webinterfaceapi.webinterface.page.Page;
import me.mrletsplay.webinterfaceapi.webinterface.page.PageSection;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.ConfirmAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.MultiAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.ReloadPageAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.SendJSAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.Button;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.Group;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.Text;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.TitleText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.VerticalSpacer;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.builder.Align;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutOption;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.GridLayout;

public class AccountsPage extends Page {

	public AccountsPage() {
		super("Accounts", "/wiapi/accounts", DefaultPermissions.MODIFY_USERS);
		setIcon("mdi:account");

		PageSection sc = new PageSection();
		sc.setSlimLayout(true);

		sc.addHeading("Accounts", 2);
		sc.getStyle().setProperty("grid-template-columns", "1fr");
		sc.getMobileStyle().setProperty("grid-template-columns", "1fr");

		sc.dynamic(els -> {
			for(Account acc : Webinterface.getAccountStorage().getAccounts()) {
				Group grp = new Group();
				grp.getStyle().setProperty("border", "1px solid var(--theme-color-content-border)");
				grp.getStyle().setProperty("border-radius", "5px");
				grp.getStyle().setProperty("margin", "5px 0px");
				grp.addLayoutOptions(new GridLayout("min-content", "auto"));

				TitleText tt = TitleText.builder()
					.text(acc.getName())
					.leftboundText()
					.noLineBreaks()
					.create();
				tt.getStyle().setProperty("font-size", "24px");
				grp.addElement(tt);

				grp.addElement(TitleText.builder()
					.text("Account ID")
					.noLineBreaks()
					.leftboundText()
					.withLayoutOptions(DefaultLayoutOption.NEW_LINE)
					.create());
				grp.addElement(Text.builder()
					.text(acc.getID())
					.leftboundText()
					.create());

				grp.addElement(TitleText.builder()
					.text("Primary Email")
					.noLineBreaks()
					.leftboundText()
					.withLayoutOptions(DefaultLayoutOption.NEW_LINE)
					.create());
				grp.addElement(Text.builder()
					.text(acc.getPrimaryEmail() == null ? "-" : acc.getPrimaryEmail())
					.leftboundText()
					.create());

				grp.addElement(TitleText.builder()
					.text("Is Temporary")
					.noLineBreaks()
					.leftboundText()
					.withLayoutOptions(DefaultLayoutOption.NEW_LINE)
					.create());
				grp.addElement(Text.builder()
					.text(acc.isTemporary() ? "yes" : "no")
					.leftboundText()
					.create());

				grp.addElement(Button.builder()
					.text("Delete account")
					.width("auto")
					.align(Align.LEFT_CENTER)
					.withLayoutOptions(DefaultLayoutOption.FULL_WIDTH)
					.onClick(ConfirmAction.of(MultiAction.of(SendJSAction.of("webinterface", "deleteAccount", ActionValue.string(acc.getID())), ReloadPageAction.delayed(100))))
					.create());
				grp.addElement(new VerticalSpacer("30px"));

				els.add(grp);
			}
		});

		addSection(sc);
	}

}
