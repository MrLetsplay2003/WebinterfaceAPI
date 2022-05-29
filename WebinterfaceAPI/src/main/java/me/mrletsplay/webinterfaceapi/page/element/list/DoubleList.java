package me.mrletsplay.webinterfaceapi.page.element.list;

import me.mrletsplay.webinterfaceapi.page.element.Text;

public class DoubleList extends ElementList<Double> {

	public DoubleList() {
		super(Text.builder().template(true).text("${this}").leftboundText().create());
	}

	@SuppressWarnings("unchecked")
	public static Builder builder() {
		return new Builder(new DoubleList());
	}

	public static class Builder extends ElementList.Builder<Double, Builder> {

		public Builder(DoubleList list) {
			super(list);
		}

		@Override
		public DoubleList create() {
			return (DoubleList) super.create();
		}

	}

}
