package it.eng.portlet.consolepec.gwt.client.widget.template;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;

import it.eng.portlet.consolepec.gwt.client.widget.ListaCampiWidget;
import it.eng.portlet.consolepec.gwt.shared.dto.Campo.TipoWidget;
import it.eng.portlet.consolepec.gwt.shared.model.CampoMetadatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.CampoTemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.CampoTemplateDTO.TipoCampoTemplateDTO;

public class ListaCampiTemplateWidget extends ListaCampiWidget<CampoTemplateDTO> {

	public ListaCampiTemplateWidget(Integer limit) {
		super(limit);
	}

	private Map<String, String> etichetteMetadatiMap = new HashMap<String, String>();
	private boolean metadatiEnabled = false;

	@Override
	protected void definisciCampi() {

		creaCampo("nome", "Nome", TipoWidget.TEXTBOX, 10).obbligatorio(true);

		if (metadatiEnabled) {
			String[] tipi = new String[TipoCampoTemplateDTO.getAllLabels().length + 1];
			tipi[0] = "";
			int i = 1;
			for (String tipo : TipoCampoTemplateDTO.getAllLabels()) {
				tipi[i] = tipo;
				i++;
			}

			creaCampo("tipo", "Tipo", TipoWidget.LISTBOX, 20).obbligatorio(true).lista(tipi);

		} else {
			String[] tipi = new String[TipoCampoTemplateDTO.getEnabledLabels().length + 1];
			tipi[0] = "";
			int i = 1;
			for (String tipo : TipoCampoTemplateDTO.getEnabledLabels()) {
				tipi[i] = tipo;
				i++;
			}

			creaCampo("tipo", "Tipo", TipoWidget.LISTBOX, 20).obbligatorio(true).lista(tipi);
		}

		creaCampo("formato", "Formato", TipoWidget.TEXTBOX, 30);
		creaCampo("regexValidazione", "Regex Validazione", TipoWidget.TEXTBOX, 40);
		creaCampo("lunghezzaMassima", "Lunghezza", TipoWidget.INTEGERBOX, 50);
		creaCampo("valoriLista", "Valori Lista", TipoWidget.VALORIBOX, 60);

		if (metadatiEnabled) {
			String[] etichette = new String[etichetteMetadatiMap.keySet().size() + 1];
			etichette[0] = "";
			int i = 1;
			for (String etichetta : etichetteMetadatiMap.keySet()) {
				etichette[i] = etichetta;
				i++;
			}
			creaCampo("metadato", "Metadato", TipoWidget.LISTBOX, 70).lista(etichette);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected CampoTemplateDTO converti(Object[] riga) {
		CampoTemplateDTO dto = new CampoTemplateDTO();
		if (riga[0] != null)
			dto.setNome(((String) riga[0]).trim());
		if (riga[1] != null)
			dto.setTipo(TipoCampoTemplateDTO.fromLabel((String) riga[1]));
		if (riga[2] != null)
			dto.setFormato((String) riga[2]);
		if (riga[3] != null)
			dto.setRegexValidazione((String) riga[3]);
		if (riga[4] != null)
			dto.setLunghezzaMassima((Integer) riga[4]);
		dto.getValoriLista().addAll((Collection<? extends String>) riga[5]);

		if (metadatiEnabled && !Strings.isNullOrEmpty((String) riga[6])) {
			String etichettaMetadato = (String) riga[6];
			String idMetadato = etichetteMetadatiMap.get(etichettaMetadato);
			CampoMetadatoDTO cm = new CampoMetadatoDTO(idMetadato, etichettaMetadato);
			dto.setCampoMetadato(cm);
		}

		return dto;
	}

	@Override
	protected Object[] converti(CampoTemplateDTO riga) {
		String nome = riga.getNome().trim();
		String tipo = riga.getTipo().getLabel();
		String formato = (riga.getFormato() != null ? riga.getFormato().trim() : null);
		String regex = (riga.getRegexValidazione() != null ? riga.getRegexValidazione().trim() : null);
		Integer lunghezzaMassima = riga.getLunghezzaMassima();
		List<String> valoriLista = riga.getValoriLista();
		if (metadatiEnabled && riga.getCampoMetadato() != null) {
			String etichetta = !Strings.isNullOrEmpty(riga.getCampoMetadato().getEtichettaMetadato()) ? riga.getCampoMetadato().getEtichettaMetadato() : null;
			return new Object[] { nome, tipo, formato, regex, lunghezzaMassima, valoriLista, etichetta };

		} else {
			return new Object[] { nome, tipo, formato, regex, lunghezzaMassima, valoriLista };
		}
	}

	@Override
	protected boolean validaInserimento(CampoTemplateDTO riga, List<String> errori) {

		if (riga.getValoriLista().size() > 0 && TipoCampoTemplateDTO.LIST.equals(riga.getTipo()) == false) {
			errori.add("- I valori lista possono essere inseriti solo per il tipo lista");
			return false;
		}
		if (riga.getValoriLista().size() == 0 && TipoCampoTemplateDTO.LIST.equals(riga.getTipo()) == true) {
			errori.add("- Per un campo di tipo lista devono essere obbligatoriamente specificati dei valori");
			return false;
		}
		if (riga.getCampoMetadato() != null && !Strings.isNullOrEmpty(riga.getCampoMetadato().getEtichettaMetadato()) && !TipoCampoTemplateDTO.METADATA.equals(riga.getTipo())) {
			errori.add("- I valori di tipo metadato possono essere inseriti solo per il tipo metadato");
			return false;
		}
		if ((riga.getCampoMetadato() != null && Strings.isNullOrEmpty(riga.getCampoMetadato().getEtichettaMetadato()) || riga.getCampoMetadato() == null)
				&& TipoCampoTemplateDTO.METADATA.equals(riga.getTipo())) {
			errori.add("- Per un campo di tipo metadato devono essere obbligatoriamente specificati dei valori di metadati");
			return false;
		}
		return true;
	}

	public void enableMetadati(Map<String, String> etichetteMetadatiMap) {
		this.etichetteMetadatiMap.clear();
		this.etichetteMetadatiMap.putAll(etichetteMetadatiMap);
		this.metadatiEnabled = true;
	}

	public void disableMetadati() {
		this.etichetteMetadatiMap.clear();
		this.metadatiEnabled = false;
	}
}
