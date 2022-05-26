package me.mrletsplay.webinterfaceapi.page.element.list;

import me.mrletsplay.webinterfaceapi.page.element.Text;

public class StringList extends ElementList<String> {

	public StringList() {
		super(Text.builder().template(true).text("${this}").leftboundText().create(), s -> s);
	}

	@SuppressWarnings("unchecked")
	public static Builder builder() {
		return new Builder(new StringList());
	}

	public static class Builder extends ElementList.Builder<String, Builder> {

		public Builder(StringList list) {
			super(list);
		}

		@Override
		public StringList create() {
			return (StringList) super.create();
		}

	}

}
