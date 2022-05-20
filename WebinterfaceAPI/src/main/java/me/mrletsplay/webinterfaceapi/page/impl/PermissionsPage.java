package me.mrletsplay.webinterfaceapi.page.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import me.mrletsplay.webinterfaceapi.DefaultPermissions;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.auth.Account;
import me.mrletsplay.webinterfaceapi.context.WebinterfaceContext;
import me.mrletsplay.webinterfaceapi.page.Page;
import me.mrletsplay.webinterfaceapi.page.PageSection;
import me.mrletsplay.webinterfaceapi.page.action.MultiAction;
import me.mrletsplay.webinterfaceapi.page.action.RedirectAction;
import me.mrletsplay.webinterfaceapi.page.action.ReloadPageAction;
import me.mrletsplay.webinterfaceapi.page.action.SendJSAction;
import me.mrletsplay.webinterfaceapi.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.page.element.CheckBox;
import me.mrletsplay.webinterfaceapi.page.element.Group;
import me.mrletsplay.webinterfaceapi.page.element.Select;
import me.mrletsplay.webinterfaceapi.page.element.Text;
import me.mrletsplay.webinterfaceapi.page.element.TitleText;
import me.mrletsplay.webinterfaceapi.page.element.VerticalSpacer;
import me.mrletsplay.webinterfaceapi.page.element.builder.Align;
import me.mrletsplay.webinterfaceapi.page.element.layout.DefaultLayoutOption;
import me.mrletsplay.webinterfaceapi.page.element.layout.GridLayout;
import me.mrletsplay.webinterfaceapi.session.Session;

public class PermissionsPage extends Page {

	public PermissionsPage() {
		super("Permissions", "/wiapi/permissions", DefaultPermissions.MODIFY_USERS);
		setIcon("mdi:account-lock");

		PageSection sc = new PageSection();
		sc.setSlimLayout(true);

		sc.addHeading("Permissions", 2);
		sc.getStyle().setProperty("grid-template-columns", "1fr");
		sc.getMobileStyle().setProperty("grid-template-columns", "1fr");

		sc.dynamic(els -> {
			WebinterfaceContext ctx = WebinterfaceContext.getCurrentContext();
			String accID = ctx.getRequestedPath().getQuery().getFirst("acc");
			Account account = accID == null ? null : Webinterface.getAccountStorage().getAccountByID(accID);

			Group group = new Group();
			group.addLayoutOptions(new GridLayout("auto", "75fr"));
			group.addLayoutOptions(DefaultLayoutOption.NO_PADDING);
			group.addElement(Text.builder()
				.text("Select an account")
				.create());

			Select.Builder b = Select.builder();
			b.addOption("Select an account", null, account == null, false);
			for(Account acc : Webinterface.getAccountStorage().getAccounts()) {
				boolean self = acc.getID().equals(Session.getCurrentSession().getAccountID());
				b.addOption(acc.getName() + " (" + acc.getID() + ")" + (self ? " - Can't change own permissions" : ""), acc.getID(), acc.getID().equals(accID), !self);
			}
			b.onChange(s -> RedirectAction.to(ActionValue.string("/wiapi/permissions?acc=").plus(s.selectedValue())));
			group.addElement(b.create());
			els.add(group);

			if(account != null) {
				Group permsGrp = new Group();
				permsGrp.addLayoutOptions(new GridLayout("max-content", "auto"));
				Set<String> permsSet = new HashSet<>(Webinterface.getActionHandlers().stream()
					.map(h -> h.getPermissions())
					.flatMap(s -> s.stream())
					.collect(Collectors.toSet()));

				permsSet.add("*");

				Set<String> perms2 = new HashSet<>();
				for(String perm : permsSet) {
					String[] parts = perm.split("\\.");
					for(int i = 1; i < parts.length; i++) {
						perms2.add(Arrays.stream(parts).limit(i).collect(Collectors.joining(".")) + ".*");
					}
				}
				permsSet.addAll(perms2);

				permsSet.addAll(account.getRawPermissions());

				List<String> perms = new ArrayList<>(permsSet);
				Collections.sort(perms);

				Map<String, List<String>> permGroups = new LinkedHashMap<>();
				for(String perm : perms) {
					String grp = perm.split("\\.", 2)[0];
					List<String> ps = permGroups.getOrDefault(grp, new ArrayList<>());
					ps.add(perm);
					permGroups.put(grp, ps);
				}

				permGroups.forEach((grp, ps) -> {
					permsGrp.addElement(new VerticalSpacer("30px"));
					permsGrp.addElement(TitleText.builder()
						.text(grp)
						.leftboundText()
						.fullWidth()
						.create());
					for(String perm : ps) {
						permsGrp.addElement(Text.builder()
							.text(perm)
							.leftboundText()
							.create());
						permsGrp.addElement(CheckBox.builder()
							.initialState(account.hasPermission(perm))
							.align(Align.LEFT_CENTER)
							.disabled(!account.hasPermissionExactly(perm) && account.hasPermission(perm))
							.onChange(cb -> MultiAction.of(SendJSAction.of("webinterface", "setPermission",
									ActionValue.object()
										.put("account", ActionValue.string(accID))
										.put("permission", ActionValue.string(perm))
										.put("value", cb.checkedValue()))
								.onSuccess(ReloadPageAction.forceReload())))
							.create());
					}
				});
				els.add(permsGrp);
			}
		});

		addSection(sc);
	}

}
