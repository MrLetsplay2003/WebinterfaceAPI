package me.mrletsplay.webinterfaceapi.webinterface.page.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.webinterface.DefaultPermissions;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccount;
import me.mrletsplay.webinterfaceapi.webinterface.page.Page;
import me.mrletsplay.webinterfaceapi.webinterface.page.PageSection;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.MultiAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.RedirectAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.ReloadPageAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.SendJSAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.CheckBox;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.Group;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.Select;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.Text;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.TitleText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.VerticalSpacer;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.builder.Align;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutOption;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.GridLayout;

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
			HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
			String accID = ctx.getRequestedPath().getQuery().getFirst("acc");
			WebinterfaceAccount account = accID == null ? null : Webinterface.getAccountStorage().getAccountByID(accID);

			Group group = new Group();
			group.addLayoutOptions(new GridLayout("auto", "75fr"));
			group.addLayoutOptions(DefaultLayoutOption.NO_PADDING);
			group.addElement(Text.builder()
				.text("Select an account")
				.create());

			Select.Builder b = Select.builder();
			b.addOption("Select an account", null, account == null, false);
			for(WebinterfaceAccount acc : Webinterface.getAccountStorage().getAccounts()) {
				b.addOption(acc.getName() + " (" + acc.getID() + ")", acc.getID(), acc.getID().equals(accID));
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
