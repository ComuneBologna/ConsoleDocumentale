package it.eng.portlet.consolepec.gwt.client.view.template;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Strings;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.presenter.template.CompilaCampiTemplatePresenter;
import it.eng.portlet.consolepec.gwt.client.widget.YesNoRadioButton;
import it.eng.portlet.consolepec.gwt.shared.model.CampoTemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.CampoTemplateDTO.TipoCampoTemplateDTO;

public class CompilaCampiTemplateView extends ViewImpl implements CompilaCampiTemplatePresenter.MyView {

	private static DateTimeFormat dateFormat = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATA);
	private static final String CAMPI_OBBLIGATORI = "- Compilare i campi obbligatori";

	private final Widget widget;

	private Command indietroCommand;
	private Command avantiCommand;
	private Command annullaCommand;

	@UiField
	Button avantiButton;
	@UiField
	Button annullaButton;
	@UiField
	Button indietroButton;

	@UiField
	HTMLPanel campiPanel;
	@UiField
	HTMLPanel alertPanel;

	@UiField
	HTMLPanel nomeFilePanel;
	@UiField
	TextBox nomeFileTextBox;

	private Set<String> errori = new HashSet<String>();

	private List<ControlloForm> controlli = new ArrayList<ControlloForm>();
	private Map<CampoTemplateDTO, Object> valori = new LinkedHashMap<CampoTemplateDTO, Object>();
	private boolean nomeFilePanelVisible;

	public interface Binder extends UiBinder<Widget, CompilaCampiTemplateView> {}

	@Inject
	public CompilaCampiTemplateView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		avantiButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				avantiCommand.execute();
			}
		});

		annullaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				annullaCommand.execute();
			}
		});

		indietroButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				indietroCommand.execute();
			}
		});
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void mostraCampi(List<CampoTemplateDTO> campi) {

		campiPanel.clear();
		controlli.clear();

		nomeFileTextBox.setText(null);
		nomeFilePanel.setVisible(nomeFilePanelVisible);

		boolean campoVisibile = false;

		for (CampoTemplateDTO campo : campi) {

			if (TipoCampoTemplateDTO.METADATA.equals(campo.getTipo())) {
				continue;
			}

			campoVisibile = true;
			HTMLPanel panel = new HTMLPanel("");
			panel.setStyleName("cell acapo");

			SpanElement label = SpanElement.as(DOM.createSpan());
			label.setClassName("label");
			label.setInnerText(campo.getNome());
			panel.getElement().appendChild(label);

			Widget field = buildCampo(campo);

			HTMLPanel inputPanel = new HTMLPanel("");
			inputPanel.setStyleName("abstract");
			inputPanel.add(field);

			panel.add(inputPanel);

			campiPanel.add(panel);
		}

		if (!nomeFilePanelVisible && !campoVisibile) {
			String messHtml = "Nessun campo valorizzabile. Clicca su Continua per proseguire con la generazione del modello";
			alertPanel.setStyleName("alert-box notice");
			alertPanel.getElement().setInnerHTML(messHtml);
			alertPanel.setVisible(true);
		}

	}

	private Widget buildCampo(final CampoTemplateDTO campo) {

		switch (campo.getTipo()) {
		case DATE: {
			final DateBox input = new DateBox();
			Format format = new DateBox.DefaultFormat(dateFormat);
			input.setFormat(format);
			input.getDatePicker().setYearArrowsVisible(true);

			controlli.add(new ControlloForm() {

				@Override
				public boolean controllo() {
					input.getElement().getStyle().setBorderColor("#FF0000");

					// obbligatorio
					if (input.getValue() == null) {
						errori.add(CAMPI_OBBLIGATORI);
						return false;
					}

					input.getElement().getStyle().clearBorderColor();

					DateTimeFormat df;
					if (!Strings.isNullOrEmpty(campo.getFormato())) {
						df = DateTimeFormat.getFormat(campo.getFormato());
					} else {
						df = dateFormat;
					}

					valori.put(campo, df.format(input.getValue()));
					return true;
				}
			});
			return input;
		}

		case DOUBLE: {

			final DoubleBox input = new DoubleBox();

			controlli.add(new ControlloForm() {

				@Override
				public boolean controllo() {
					input.getElement().getStyle().setBorderColor("#FF0000");

					String value = input.getText();

					// obbligatorio
					if (Strings.isNullOrEmpty(value)) {
						errori.add(CAMPI_OBBLIGATORI);
						return false;
					}

					// formato
					if (input.getValue() == null) {
						errori.add("- Il campo " + campo.getNome() + " non ha un formato valido, inserire un numero decimale");
						return false;

					}

					// lunghezza massima
					if (campo.getLunghezzaMassima() != null) {
						if (value.length() > campo.getLunghezzaMassima()) {
							errori.add("- Il campo " + campo.getNome() + " ha superato la lunghezza massima di " + campo.getLunghezzaMassima());
							return false;
						}

					}

					input.getElement().getStyle().clearBorderColor();

					valori.put(campo, input.getValue());
					return true;
				}
			});

			return input;
		}
		case INTEGER: {

			final IntegerBox input = new IntegerBox();

			controlli.add(new ControlloForm() {

				@Override
				public boolean controllo() {
					input.getElement().getStyle().setBorderColor("#FF0000");

					String value = input.getText();

					// obbligatorio
					if (Strings.isNullOrEmpty(value)) {
						errori.add(CAMPI_OBBLIGATORI);
						return false;
					}

					// formato
					if (input.getValue() == null) {
						errori.add("- Il campo " + campo.getNome() + " non ha un formato valido, inserire un numero intero");
						return false;

					}

					// lunghezza massima
					if (campo.getLunghezzaMassima() != null) {
						if (value.length() > campo.getLunghezzaMassima()) {
							errori.add("- Il campo " + campo.getNome() + " ha superato la lunghezza massima di " + campo.getLunghezzaMassima());
							return false;
						}

					}

					input.getElement().getStyle().clearBorderColor();

					valori.put(campo, input.getValue());
					return true;
				}
			});

			return input;

		}
		case LIST: {
			final ListBox input = new ListBox();
			input.addItem("");
			for (String v : campo.getValoriLista()) {
				input.addItem(v);
			}
			controlli.add(new ControlloForm() {

				@Override
				public boolean controllo() {
					input.getElement().getStyle().setBorderColor("#FF0000");

					if (input.getSelectedIndex() == -1 || input.getSelectedIndex() == 0) {
						errori.add(CAMPI_OBBLIGATORI);
						return false;
					}

					input.getElement().getStyle().clearBorderColor();

					valori.put(campo, input.getValue(input.getSelectedIndex()));
					return true;
				}
			});
			return input;
		}

		case TEXT: {
			final TextBox input = new TextBox();

			controlli.add(new ControlloForm() {

				@Override
				public boolean controllo() {
					input.getElement().getStyle().setBorderColor("#FF0000");

					String value = input.getText();

					// obbligatorio
					if (Strings.isNullOrEmpty(value)) {
						errori.add(CAMPI_OBBLIGATORI);
						return false;
					}

					// regex
					if (!Strings.isNullOrEmpty(campo.getRegexValidazione())) {
						if (!value.matches(campo.getRegexValidazione())) {
							errori.add("- Il campo " + campo.getNome() + " non ha un formato valido");
							return false;
						}

					}

					// lunghezza massima
					// regex
					if (campo.getLunghezzaMassima() != null) {
						if (value.length() > campo.getLunghezzaMassima()) {
							errori.add("- Il campo " + campo.getNome() + " ha superato la lunghezza massima di " + campo.getLunghezzaMassima());
							return false;
						}

					}

					input.getElement().getStyle().clearBorderColor();

					valori.put(campo, input.getValue());
					return true;
				}
			});

			return input;

		}
		case TEXTAREA: {
			final TextArea input = new TextArea();

			controlli.add(new ControlloForm() {

				@Override
				public boolean controllo() {
					input.getElement().getStyle().setBorderColor("#FF0000");

					String value = input.getText();

					// obbligatorio
					if (Strings.isNullOrEmpty(value)) {
						errori.add(CAMPI_OBBLIGATORI);
						return false;
					}

					// lunghezza massima
					// regex
					if (campo.getLunghezzaMassima() != null) {
						if (value.length() > campo.getLunghezzaMassima()) {
							errori.add("- Il campo " + campo.getNome() + " ha superato la lunghezza massima di " + campo.getLunghezzaMassima());
							return false;
						}

					}

					input.getElement().getStyle().clearBorderColor();

					valori.put(campo, input.getValue());
					return true;
				}
			});

			return input;
		}
		case YESNO: {
			final YesNoRadioButton input = new YesNoRadioButton("");
			controlli.add(new ControlloForm() {

				@Override
				public boolean controllo() {
					valori.put(campo, input.getStringValueSiNo());
					return true;

				}
			});
			return input;

		}
		default:
			throw new IllegalArgumentException("Tipo Dato non gestito: " + campo.getTipo());

		}
	}

	@Override
	public void setIndietroCommand(Command indietroCommand) {
		this.indietroCommand = indietroCommand;
	}

	@Override
	public void setAvantiCommand(Command avantiCommand) {
		this.avantiCommand = avantiCommand;
	}

	@Override
	public void setAnnullaCommand(Command annullaCommand) {
		this.annullaCommand = annullaCommand;
	}

	@Override
	public boolean controllaCampi() {
		boolean valid = true;

		alertPanel.clear();
		alertPanel.setVisible(false);
		errori.clear();

		valori.clear();

		for (ControlloForm cf : controlli) {
			boolean controllo = cf.controllo();
			valid &= controllo;
		}

		if (nomeFilePanelVisible) {
			if (Strings.isNullOrEmpty(nomeFileTextBox.getValue())) {
				nomeFileTextBox.getElement().setAttribute("required", "required");
				errori.add("- Specificare un nome per il file da generare");
				valid = false;

			} else {
				nomeFileTextBox.getElement().removeAttribute("required");
			}
		}

		if (!valid && errori.isEmpty() == false) {
			String message = "<br/>";
			for (String current : errori) {
				message = message + current + "<br/>";
			}

			alertPanel.setStyleName("alert-box error");

			String htmlSpan = errori.size() <= 1 ? "<span>Errore: </span>" : "<span>Errori: </span>";

			String messHtml = htmlSpan + message;
			alertPanel.getElement().setInnerHTML(messHtml);
			alertPanel.setVisible(true);

		}
		return valid;
	}

	@Override
	public Map<CampoTemplateDTO, Object> getValori() {
		return valori;
	}

	private interface ControlloForm {
		boolean controllo();
	}

	@Override
	public void mostraCampoFileName(boolean b) {
		nomeFilePanelVisible = b;
	}

	@Override
	public boolean hasFileName() {
		return nomeFilePanelVisible;
	}

	@Override
	public String getFileName() {
		return nomeFileTextBox.getValue();
	}

	@Override
	public void clear() {
		campiPanel.clear();
		controlli.clear();
		alertPanel.clear();
		alertPanel.setVisible(false);
		errori.clear();
		valori.clear();
		nomeFileTextBox.getElement().removeAttribute("required");
	}
}
