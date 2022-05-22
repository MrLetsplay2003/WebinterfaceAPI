package me.mrletsplay.webinterfaceapi.page.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import me.mrletsplay.mrcore.http.HttpUtils;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.auth.Account;
import me.mrletsplay.webinterfaceapi.auth.AccountConnection;
import me.mrletsplay.webinterfaceapi.auth.AuthMethod;
import me.mrletsplay.webinterfaceapi.page.Page;
import me.mrletsplay.webinterfaceapi.page.PageSection;
import me.mrletsplay.webinterfaceapi.page.action.ConfirmAction;
import me.mrletsplay.webinterfaceapi.page.action.RedirectAction;
import me.mrletsplay.webinterfaceapi.page.action.ReloadPageAction;
import me.mrletsplay.webinterfaceapi.page.action.SendJSAction;
import me.mrletsplay.webinterfaceapi.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.page.element.Button;
import me.mrletsplay.webinterfaceapi.page.element.Group;
import me.mrletsplay.webinterfaceapi.page.element.Text;
import me.mrletsplay.webinterfaceapi.page.element.TitleText;
import me.mrletsplay.webinterfaceapi.page.element.VerticalSpacer;
import me.mrletsplay.webinterfaceapi.page.element.layout.DefaultLayoutOption;
import me.mrletsplay.webinterfaceapi.page.element.layout.GridLayout;
import me.mrletsplay.webinterfaceapi.session.Session;

public class AccountPage extends Page {

	public static final String URL = "/wiapi/account";

	public AccountPage() {
		super("Account", URL, true);

		PageSection sc = new PageSection();
		sc.setSlimLayout(true);

		sc.addHeading("Account connections", 2);
		sc.addLayoutOptions(new GridLayout("1fr"));

		sc.dynamic(els -> {
			Account account = Session.getCurrentSession().getAccount();

			Map<AccountConnection, AuthMethod> loginCons = new LinkedHashMap<>();
			for(AccountConnection con : account.getConnections()) {
				AuthMethod mth = Webinterface.getAuthMethods().stream()
					.filter(m -> m.getID().equals(con.getConnectionName()))
					.findFirst().orElse(null);
				if(mth != null) loginCons.put(con, mth);
			}

			for(AccountConnection con : account.getConnections()) {
				AuthMethod mth = loginCons.get(con);

				Group grp = new Group();
				grp.addLayoutOptions(new GridLayout("min-content", "auto"));

				TitleText title = TitleText.builder()
					.text(con.getUserName())
					.fullWidth()
					.leftboundText()
					.create();
				title.getStyle().setProperty("font-size", "24px");
				grp.addElement(title);

				if(mth != null) {
					grp.addElement(TitleText.builder()
						.text("Auth method")
						.noLineBreaks()
						.leftboundText()
						.create());
					grp.addElement(Text.builder()
						.text(mth.getName())
						.leftboundText()
						.create());
				}

				grp.addElement(TitleText.builder()
					.text("Email")
					.leftboundText()
					.withLayoutOptions(DefaultLayoutOption.NEW_LINE)
					.create());
				grp.addElement(Text.builder()
					.text(con.getUserEmail() == null ? "-" : con.getUserEmail())
					.leftboundText()
					.create());

				grp.addElement(TitleText.builder()
					.text("Is Temporary")
					.noLineBreaks()
					.leftboundText()
					.withLayoutOptions(DefaultLayoutOption.NEW_LINE)
					.create());
				grp.addElement(Text.builder()
					.text(con.isTemporary() ? "yes" : "no")
					.leftboundText()
					.create());

				if(loginCons.size() > 1) {
					grp.addElement(Button.builder()
						.text("Remove connection")
						.fullWidth()
						.onClick(ConfirmAction.of(SendJSAction.of("webinterface", "removeAccountConnection", ActionValue.object().put("connection", ActionValue.string(con.getConnectionName()))).onSuccess(ReloadPageAction.delayed(100))))
						.width("auto")
						.create());
				}

				grp.addElement(new VerticalSpacer("30px"));

				els.add(grp);
			}

			els.add(Button.builder()
				.text("Connect another auth method")
				.withLayoutOptions(DefaultLayoutOption.FULL_WIDTH)
				.onClick(RedirectAction.to("/login?from=" + HttpUtils.urlEncode(URL) + "&connect=true"))
				.create());
		});

		addSection(sc);
	}

}
