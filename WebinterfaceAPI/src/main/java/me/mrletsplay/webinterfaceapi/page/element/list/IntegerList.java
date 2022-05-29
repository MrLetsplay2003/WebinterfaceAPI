package me.mrletsplay.webinterfaceapi.page.element.list;

import me.mrletsplay.webinterfaceapi.page.element.Text;

public class IntegerList extends ElementList<Integer> {

	public IntegerList() {
		super(Text.builder().template(true).text("${this}").leftboundText().create());
	}

	@SuppressWarnings("unchecked")
	public static Builder builder() {
		return new Builder(new IntegerList());
	}

	public static class Builder extends ElementList.Builder<Integer, Builder> {

		public Builder(IntegerList list) {
			super(list);
		}

		@Override
		public IntegerList create() {
			return (IntegerList) super.create();
		}

	}

}
