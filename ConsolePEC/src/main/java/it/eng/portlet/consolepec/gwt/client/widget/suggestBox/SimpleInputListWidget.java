package it.eng.portlet.consolepec.gwt.client.widget.suggestBox;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * versione generica: simile all'inputlistwidget ma senza la validazione sulla mail e senza suggestbox
 */
public class SimpleInputListWidget extends Composite {

	List<String> itemsSelected = new ArrayList<String>();

	protected BulletList list;
	protected TextBox itemBox;
	private boolean abilitato;

	public boolean isAbilitato() {
		return abilitato;
	}

	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
		this.itemBox.setEnabled(abilitato);
		if (abilitato) {
			this.itemBox.removeStyleName("disabilitato");
		} else {
			this.itemBox.setStyleName("disabilitato");
		}
	}

	public List<String> getItemSelected() {
		return new ArrayList<String>(itemsSelected);
	}

	public void setRequired(boolean b) {
		String isReq = list.getElement().getAttribute("required");
		if (b) {
			if (isReq != null && isReq.trim().equals(""))
				list.getElement().setAttribute("required", "required");
		} else {
			if (isReq != null && !isReq.trim().equals(""))
				list.getElement().removeAttribute("required");
		}
	}


	public SimpleInputListWidget(List<String> elenco) {
		this(elenco, "token-input-list-facebook");

	}
	public SimpleInputListWidget(List<String> elenco, String style) {
		FlowPanel panel = new FlowPanel();
		initWidget(panel);
		// 2. Show the following element structure and set the last <div> to display: block
		/*
		 * <ul class="token-input-list-facebook"> <li class="token-input-input-token-facebook"> <input type="text"
		 * style="outline-color: -moz-use-text-color; outline-style: none; outline-width: medium;"/> </li> </ul> <div class="token-input-dropdown-facebook" style="display: none;"/>
		 */
		list = new BulletList();
		list.setStyleName(style);
		final ListItem item = new ListItem();
		item.setStyleName("token-input-input-token-facebook");
		itemBox = new TextBox();
		itemBox.setStyleName("testo");
		// itemBox.getElement().setAttribute("style", "outline-color: -moz-use-text-color; outline-style: none; outline-width: medium;");
		itemBox.getElement().setAttribute("data-role", "input");

		item.add(getWidgetInserimento(itemBox, elenco));
		list.add(item);

		itemBox.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER || event.getNativeKeyCode() == KeyCodes.KEY_TAB) {
					deselectItem(itemBox, list);
					itemBox.setFocus(true);
					setRequired(false);
				}
				// handle backspace
				if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
					if ("".equals(itemBox.getValue().trim()) && list.getWidgetCount() > 1) {
						ListItem li = (ListItem) list.getWidget(list.getWidgetCount() - 2);
						Paragraph p = (Paragraph) li.getWidget(0);
						if (itemsSelected.contains(p.getText())) {
							itemsSelected.remove(p.getText());
							GWT.log("Removing selected item '" + p.getText() + "'", null);
							GWT.log("Remaining: " + itemsSelected, null);
						}
						list.remove(li);
						itemBox.setFocus(true);
					}
				}
			}
		});

		panel.add(list);


	}

	protected Widget getWidgetInserimento(final TextBox itemBox, List<String> elenco) {
		// this needs to be on the itemBox rather than box, or backspace will get executed twice
		itemBox.addBlurHandler(new BlurHandler() {

				@Override
				public void onBlur(BlurEvent event) {
					deselectItem(itemBox, list);
					//itemBox.setFocus(true);
					setRequired(false);

				}
		});
		return itemBox;
	}

	public void addValueItem(String value) {
		TextBox itemBox = new TextBox();
		itemBox.setValue(value);
		deselectItem(itemBox, list);
		itemBox.setFocus(true);
		this.setRequired(false);
	}

	protected void deselectItem(final HasValue<String> itemBox, final BulletList list) {
		if (itemBox.getValue() != null && !"".equals(itemBox.getValue().trim())) {
			/**
			 * Change to the following structure: <li class="token-input-token-facebook">
			 * <p>
			 * What's New Scooby-Doo?
			 * </p>
			 * <span class="token-input-delete-token-facebook">x</span></li>
			 */

			final ListItem displayItem = new ListItem();
			displayItem.setStyleName("token-input-token-facebook");
			Paragraph p = new Paragraph(itemBox.getValue());

			displayItem.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent clickEvent) {
					displayItem.addStyleName("token-input-selected-token-facebook");
				}
			});

			/**
			 * TODO: Figure out how to select item and allow deleting with backspace key displayItem.addKeyDownHandler(new KeyDownHandler() { public void onKeyDown(KeyDownEvent event) { if
			 * (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) { removeListItem(displayItem, list); } } }); displayItem.addBlurHandler(new BlurHandler() { public void onBlur(BlurEvent blurEvent)
			 * { displayItem.removeStyleName("token-input-selected-token-facebook"); } });
			 */

			Span span = new Span("x");
			span.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent clickEvent) {
					if (!abilitato)
						removeListItem(displayItem, list);
				}
			});

			displayItem.add(p);
			displayItem.add(span);
			// hold the original value of the item selected

			GWT.log("Adding selected item '" + itemBox.getValue() + "'", null);
			itemsSelected.add(itemBox.getValue());
			GWT.log("Total: " + itemsSelected, null);

			list.insert(displayItem, list.getWidgetCount() - 1);
			itemBox.setValue("");
		}
	}

	private void removeListItem(ListItem displayItem, BulletList list) {
		GWT.log("Removing: " + displayItem.getWidget(0).getElement().getInnerHTML(), null);
		itemsSelected.remove(displayItem.getWidget(0).getElement().getInnerHTML());
		list.remove(displayItem);
	}

	public void reset(){
		itemsSelected.clear();
		if ("".equals(itemBox.getValue().trim()) && list.getWidgetCount() > 1) {
			ListItem li = (ListItem) list.getWidget(list.getWidgetCount() - 2);
			Paragraph p = (Paragraph) li.getWidget(0);
			if (itemsSelected.contains(p.getText())) {
				itemsSelected.remove(p.getText());
				GWT.log("Removing selected item '" + p.getText() + "'", null);
				GWT.log("Remaining: " + itemsSelected, null);
			}
			list.remove(li);
		}
	}


}
