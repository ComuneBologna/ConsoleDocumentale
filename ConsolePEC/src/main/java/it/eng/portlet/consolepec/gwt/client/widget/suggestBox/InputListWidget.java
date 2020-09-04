package it.eng.portlet.consolepec.gwt.client.widget.suggestBox;


import it.eng.cobo.consolepec.util.validation.ValidationUtilities;

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
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Facebook Style Autocompleter. CSS and DIV structure from http://loopj.com/tokeninput/demo.html:
 */
public class InputListWidget extends Composite {
	List<String> itemsSelected = new ArrayList<String>();
	private String id;
	private BulletList list;
	private TextBox itemBox;
	private boolean abilitato;
	private Command itemCommand = new Command() {

		@Override
		public void execute() {

		}
	};

	public String getText() {
		return itemBox.getText();
	}

	public boolean isAbilitato() {
		return abilitato;
	}

	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
		this.itemBox.setEnabled(!abilitato);
		if (abilitato) {
			this.itemBox.setStyleName("disabilitato");
		} else {
			this.itemBox.removeStyleName("disabilitato");
		}
	}

	public void addItemCommand(Command itemCommand) {
		this.itemCommand = itemCommand;
	}

	public List<String> getItemSelected() {
		return new ArrayList<String>(itemsSelected);
	}

	public void setRequired(boolean b) {
		String isReq = itemBox.getElement().getAttribute("required");
		if (b) {
			if (isReq != null && isReq.trim().equals(""))
				itemBox.getElement().setAttribute("required", "required");
		} else {
			if (isReq != null && !isReq.trim().equals(""))
				itemBox.getElement().removeAttribute("required");
		}
	}

	private void init(SuggestOracle suggestOracle, String id) {
		FlowPanel panel = new FlowPanel();
		initWidget(panel);
		// 2. Show the following element structure and set the last <div> to display: block
		/*
		 * <ul class="token-input-list-facebook"> <li class="token-input-input-token-facebook"> <input type="text"
		 * style="outline-color: -moz-use-text-color; outline-style: none; outline-width: medium;"/> </li> </ul> <div class="token-input-dropdown-facebook" style="display: none;"/>
		 */
		this.id = "suggestion_box_" + id;
		list = new BulletList();
		list.setStyleName("token-input-list-facebook");
		final ListItem item = new ListItem();
		item.setStyleName("token-input-input-token-facebook");
		itemBox = new TextBox() {
			@Override
			public String getValue() {
				String val = super.getValue();
				if (val != null) {
					val = val.trim();
				}
				return val;
			}
		};
		itemBox.setStyleName("testo");
		// itemBox.getElement().setAttribute("style", "outline-color: -moz-use-text-color; outline-style: none; outline-width: medium;");
		itemBox.getElement().setAttribute("data-role", "input");
		final SuggestBox box = new SuggestBox(suggestOracle, itemBox);
		box.setStyleName("testo");
		box.getElement().setId(this.id);

		item.add(box);
		list.add(item);

		// this needs to be on the itemBox rather than box, or backspace will get executed twice
		itemBox.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				String mailValue = itemBox.getValue().trim();
				itemBox.setValue(mailValue);

				if (emailValidation) {
					if (ValidationUtilities.validateEmailAddress(itemBox.getValue())) {
						deselectItem(itemBox, list);
						setRequired(false);
						itemCommand.execute();
					}

				} else {
					deselectItem(itemBox, list);
					setRequired(false);
					itemCommand.execute();
				}
			}
		});

		itemBox.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER || event.getNativeKeyCode() == KeyCodes.KEY_TAB) {
					// only allow manual entries with @ signs (assumed email addresses)

					if (emailValidation) {
						if (ValidationUtilities.validateEmailAddress(itemBox.getValue())) {
							deselectItem(itemBox, list);
							setRequired(false);
							itemCommand.execute();
						}
					} else {
						deselectItem(itemBox, list);
						setRequired(false);
						itemCommand.execute();
					}

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
						itemCommand.execute();
					}
				}
			}
		});

		box.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				deselectItem(itemBox, list);
				itemCommand.execute();
			}
		});

		panel.add(list);

		// panel.getElement().setAttribute("onclick", "document.getElementById('" + this.id + "').focus()");
		box.setFocus(true);
		/*
		 * Div structure after a few elements have been added: <ul class="token-input-list-facebook"> <li class="token-input-token-facebook"> <p>What's New Scooby-Doo?</p> <span
		 * class="token-input-delete-token-facebook">x</span> </li> <li class="token-input-token-facebook"> <p>Fear Factor</p> <span class="token-input-delete-token-facebook">x</span> </li> <li
		 * class="token-input-input-token-facebook"> <input type="text" style="outline-color: -moz-use-text-color; outline-style: none; outline-width: medium;"/> </li> </ul>
		 */
	}

	private boolean emailValidation;

	public InputListWidget(SuggestOracle suggestOracle, String id, boolean emailValidation) {
		this.emailValidation = emailValidation;
		init(suggestOracle, id);
	}

	public InputListWidget(SuggestOracle suggestOracle, String id) {
		this.emailValidation = true;
		init(suggestOracle, id);
	}

	public void addValueItem(String value) {
		TextBox itemBox = new TextBox();
		itemBox.setValue(value);
		deselectItem(itemBox, list);
		this.setRequired(false);
	}

	private void deselectItem(final TextBox itemBox, final BulletList list) {
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
					itemCommand.execute();
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
			itemBox.setFocus(true);
		}
	}

	private void removeListItem(ListItem displayItem, BulletList list) {
		GWT.log("Removing: " + displayItem.getWidget(0).getElement().getInnerHTML(), null);
		itemsSelected.remove(displayItem.getWidget(0).getElement().getInnerHTML());
		list.remove(displayItem);
	}

	public void reset() {
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

	public void clear() {
		itemsSelected.clear();
		if (list.getWidgetCount() > 1) { // l'ultimo è la textbox
			while(list.getWidgetCount() > 1) {
				ListItem li = (ListItem) list.getWidget(0);
				removeListItem(li, list);
			}
		}
	}

}
