package it.eng.portlet.consolepec.gwt.client.widget;


public class YesNoNullRadioButton extends YesNoRadioButton {
	
//	private YesNoRadioButtonCommand yesNoNullRadioButtonCommand;
	
//	public final class RadioButtonClickHandler implements ClickHandler {
//		private Boolean value;
//
//		public RadioButtonClickHandler(Boolean value) {
//			this.value = value;
//		}
//
//		@Override
//		public void onClick(ClickEvent event) {
//			backingValue = value;
//			if(yesNoNullRadioButtonCommand != null) {
//				yesNoNullRadioButtonCommand.execute(backingValue);
//			}
//		}
//	}
	
//	private static YesNoRadioButtonUiBinder uiBinder = GWT.create(YesNoRadioButtonUiBinder.class);
	
	public YesNoNullRadioButton() {
		super();
//		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public YesNoNullRadioButton(String label) {
		super(label);
//		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public Boolean getValue(){
		if(yes.getValue() == false &&
		   no.getValue() == false){
			return null;
		}
		return yes.getValue() == true;
	}
}
