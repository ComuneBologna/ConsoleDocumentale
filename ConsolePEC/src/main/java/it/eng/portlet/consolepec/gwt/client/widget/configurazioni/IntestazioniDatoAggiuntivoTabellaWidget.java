package it.eng.portlet.consolepec.gwt.client.widget.configurazioni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo.DatoAggiuntivoVisitor;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoAnagrafica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoTabella;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreMultiplo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreSingolo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.TipoDato;
import it.eng.portlet.consolepec.gwt.client.widget.ListaCampiWidget;
import it.eng.portlet.consolepec.gwt.shared.dto.Campo.TipoWidget;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author biagiot
 *
 */
public class IntestazioniDatoAggiuntivoTabellaWidget extends Composite {

	interface IntestazioniDatoAggiuntivoTabellaWidgetBinder extends UiBinder<Widget, IntestazioniDatoAggiuntivoTabellaWidget> {/**/}

	private static IntestazioniDatoAggiuntivoTabellaWidgetBinder uiBinder = GWT.create(IntestazioniDatoAggiuntivoTabellaWidgetBinder.class);

	@UiField
	HTMLPanel intestazioniDatoTabellaPanel;

	private CampiDatiAggiuntiviTabellaWidget campiWidget;
	private List<String> idDatiAggiuntivi = new ArrayList<String>();
	private DatiAggiuntiviWidget parentWidget;

	public IntestazioniDatoAggiuntivoTabellaWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void render(List<String> idDatiAggiuntivi, Map<String, List<DatoAggiuntivo>> intestazioni, DatiAggiuntiviWidget parentWidget) {
		if (idDatiAggiuntivi != null)
			this.idDatiAggiuntivi = idDatiAggiuntivi;

		this.parentWidget = parentWidget;
		reload(intestazioni);
	}

	private void reload(Map<String, List<DatoAggiuntivo>> intestazioniMap) {
		this.intestazioniDatoTabellaPanel.clear();
		this.intestazioniDatoTabellaPanel.getElement().removeAllChildren();

		this.campiWidget = new CampiDatiAggiuntiviTabellaWidget(intestazioniMap != null ? intestazioniMap.size() + 20 : 100);
		this.campiWidget.render(true, true);

		if (intestazioniMap != null && !intestazioniMap.isEmpty()) {

			for (Entry<String, List<DatoAggiuntivo>> entry : intestazioniMap.entrySet()) {
				if (idDatiAggiuntivi.contains(entry.getKey())) {
					for (DatoAggiuntivo dag : entry.getValue()) {
						this.campiWidget.add(toCampo(dag, entry.getKey()));
					}
				}
			}

		}
		this.intestazioniDatoTabellaPanel.add(campiWidget);

		this.campiWidget.setAggiungiCommand(new AggiungiCampoCommand());
		this.campiWidget.setEliminaCommand(new EliminaCampoCommand());
	}

	public Map<String, List<DatoAggiuntivo>> getIntestazioni() {
		return toMap(campiWidget.getCampiDatiAggiuntivi());
	}

	public List<DatoAggiuntivo> getIntestazioni(String idDatoTabella) {
		return getIntestazioni().get(idDatoTabella);
	}

	public class CampiDatiAggiuntiviTabellaWidget extends ListaCampiWidget<CampoDatoAggiuntivoTabella> {

		public CampiDatiAggiuntiviTabellaWidget(Integer limit) {
			super(limit);
		}

		public List<CampoDatoAggiuntivoTabella> getCampiDatiAggiuntivi() {
			return getValori();
		}

		@Override
		protected void definisciCampi() {
			String[] tipiArr = new String[TipoDato.values().length];
			for (int i = 0; i < TipoDato.values().length; i++) {
				tipiArr[i] = TipoDato.values()[i].toString();
			}

			String[] datiArr = new String[idDatiAggiuntivi.size()];
			datiArr = idDatiAggiuntivi.toArray(datiArr);

			creaCampo("idDatoAggiuntivoTabella", "Identificativo dato aggiuntivo tabella", TipoWidget.LISTBOX, 0).obbligatorio(true).lista(datiArr);
			creaCampo("idDatoAggiuntivoT", "Identificativo intestazione", TipoWidget.TEXTBOX, 1).obbligatorio(true);
			creaCampo("nomeDatoAggiuntivoT", "Nome", TipoWidget.TEXTBOX, 2).obbligatorio(true);
			creaCampo("editabileT", "Editabile", TipoWidget.YESNORADIOBUTTON, 3).obbligatorio(false);
			creaCampo("visibileT", "Visibile", TipoWidget.YESNORADIOBUTTON, 4).obbligatorio(true);
			creaCampo("obbligatorioT", "Obbligatorio", TipoWidget.YESNORADIOBUTTON, 5).obbligatorio(false);
			creaCampo("posizioneT", "Posizione", TipoWidget.INTEGERBOX, 6).obbligatorio(true);
			creaCampo("tipoDatoT", "Tipo", TipoWidget.LISTBOX, 7).obbligatorio(true).lista(tipiArr);
		}

		@Override
		protected CampoDatoAggiuntivoTabella converti(Object[] riga) {
			CampoDatoAggiuntivoTabella campoDatoAggiuntivo = new CampoDatoAggiuntivoTabella();
			campoDatoAggiuntivo.setIdentificativoTabella((String) riga[0]);
			campoDatoAggiuntivo.setIdentificativo((String) riga[1]);
			campoDatoAggiuntivo.setNome((String) riga[2]);
			campoDatoAggiuntivo.setEditabile((Boolean) riga[3]);
			campoDatoAggiuntivo.setVisibile((boolean) riga[4]);
			campoDatoAggiuntivo.setObbligatorio((Boolean) riga[5]);
			campoDatoAggiuntivo.setPosizione((Integer) riga[6]);
			campoDatoAggiuntivo.setTipo(TipoDato.valueOf((String) riga[7]));
			return campoDatoAggiuntivo;
		}

		@Override
		protected Object[] converti(CampoDatoAggiuntivoTabella riga) {
			return new Object[] { riga.getIdentificativoTabella(), //
					riga.getIdentificativo(), //
					riga.getNome(), //
					riga.getEditabile(), //
					riga.isVisibile(), //
					riga.getObbligatorio(), //
					riga.getPosizione(), //
					riga.getTipo().toString() };
		}

		@Override
		protected boolean validaInserimento(CampoDatoAggiuntivoTabella riga, List<String> errori) {
			if (riga == null) {
				errori.add("Dato non valido");
				return false;
			}
			if (riga.getIdentificativo() == null || riga.getIdentificativo().trim().isEmpty() || riga.getNome() == null || riga.getNome().trim().isEmpty() || riga.getTipo() == null) {
				errori.add("Dato non valido");
				return false;
			}
			if (!idDatiAggiuntivi.contains(riga.getIdentificativoTabella())) {
				errori.add("Il dato aggiuntivo tabella non esiste");
				return false;
			}
			if (getValori().contains(riga)) {
				errori.add("Il dato esiste già");
				return false;
			}
			if (TipoDato.Tabella.equals(riga.getTipo())) {
				errori.add("Tipo dato non configurabile");
				return false;
			} else {
				if (riga.getObbligatorio() == null || riga.getEditabile() == null) {
					errori.add("Dato non valido");
					return false;
				}
			}
			return true;
		}
	}

	public class AggiungiCampoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CampoDatoAggiuntivoTabella> {

		@Override
		public Void exe(CampoDatoAggiuntivoTabella t) {
			campiWidget.add(t);
			parentWidget.reload(parentWidget.getDatiAggiuntivi());
			return null;
		}
	}

	public class EliminaCampoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CampoDatoAggiuntivoTabella> {

		@Override
		public Void exe(CampoDatoAggiuntivoTabella t) {
			campiWidget.remove(t);
			parentWidget.reload(parentWidget.getDatiAggiuntivi());
			return null;
		}
	}

	public void clear() {
		if (campiWidget != null) {
			campiWidget.getValori().clear();
		}
	}

	@Data
	@EqualsAndHashCode(of = "identificativo")
	private static class CampoDatoAggiuntivoTabella {
		String identificativoTabella;
		String identificativo;
		String nome;
		Boolean obbligatorio;
		Boolean editabile;
		boolean visibile;
		Integer posizione;
		TipoDato tipo;
	}

	private static DatoAggiuntivo toDatoAggiuntivo(final CampoDatoAggiuntivoTabella campo, final List<String> valoriPredefiniti) {
		DatoAggiuntivo res = campo.getTipo().createDato();
		res.setNome(campo.getIdentificativo());
		res.setDescrizione(campo.getNome());
		res.setPosizione(campo.getPosizione());
		res.setVisibile(campo.isVisibile());
		res.setTipo(campo.getTipo());

		res.accept(new DatoAggiuntivoVisitor() {
			@Override
			public void visit(DatoAggiuntivoTabella datoAggiuntivoTabella) {
				//
			}

			@Override
			public void visit(DatoAggiuntivoAnagrafica datoAggiuntivoAnagrafica) {
				datoAggiuntivoAnagrafica.setObbligatorio(campo.getObbligatorio());
				datoAggiuntivoAnagrafica.setEditabile(campo.getEditabile());
			}

			@Override
			public void visit(DatoAggiuntivoValoreMultiplo datoAggiuntivoValoreMultiplo) {
				datoAggiuntivoValoreMultiplo.setObbligatorio(campo.getObbligatorio());
				datoAggiuntivoValoreMultiplo.setEditabile(campo.getEditabile());
				if (valoriPredefiniti != null) {
					datoAggiuntivoValoreMultiplo.getValoriPredefiniti().addAll(valoriPredefiniti);
				}
			}

			@Override
			public void visit(DatoAggiuntivoValoreSingolo datoAggiuntivoValoreSingolo) {
				datoAggiuntivoValoreSingolo.setObbligatorio(campo.getObbligatorio());
				datoAggiuntivoValoreSingolo.setEditabile(campo.getEditabile());
				if (valoriPredefiniti != null) {
					datoAggiuntivoValoreSingolo.getValoriPredefiniti().addAll(valoriPredefiniti);
				}
			}
		});
		return res;
	}

	private static CampoDatoAggiuntivoTabella toCampo(DatoAggiuntivo dag, String identificativoDatoTabella) {
		final CampoDatoAggiuntivoTabella c = new CampoDatoAggiuntivoTabella();
		c.setIdentificativoTabella(identificativoDatoTabella);
		c.setIdentificativo(dag.getNome());
		c.setNome(dag.getDescrizione());
		c.setTipo(dag.getTipo());
		c.setVisibile(dag.isVisibile());
		c.setPosizione(dag.getPosizione());

		dag.accept(new DatoAggiuntivoVisitor() {

			@Override
			public void visit(DatoAggiuntivoTabella datoAggiuntivoTabella) {
				//
			}

			@Override
			public void visit(DatoAggiuntivoAnagrafica datoAggiuntivoAnagrafica) {
				c.setObbligatorio(datoAggiuntivoAnagrafica.isObbligatorio());
				c.setEditabile(datoAggiuntivoAnagrafica.isVisibile());
			}

			@Override
			public void visit(DatoAggiuntivoValoreMultiplo datoAggiuntivoValoreMultiplo) {
				c.setObbligatorio(datoAggiuntivoValoreMultiplo.isObbligatorio());
				c.setEditabile(datoAggiuntivoValoreMultiplo.isVisibile());
			}

			@Override
			public void visit(DatoAggiuntivoValoreSingolo datoAggiuntivoValoreSingolo) {
				c.setObbligatorio(datoAggiuntivoValoreSingolo.isObbligatorio());
				c.setEditabile(datoAggiuntivoValoreSingolo.isVisibile());
			}
		});
		return c;
	}

	private Map<String, List<DatoAggiuntivo>> toMap(List<CampoDatoAggiuntivoTabella> list) {
		Map<String, List<DatoAggiuntivo>> res = new HashMap<String, List<DatoAggiuntivo>>();
		for (CampoDatoAggiuntivoTabella c : list) {
			if (res.get(c.getIdentificativoTabella()) != null) {
				res.get(c.getIdentificativoTabella()).add(toDatoAggiuntivo(c, parentWidget.getValoriDatiAggiuntiviWidget().getValoriPredefiniti(c.getIdentificativo())));
			} else {
				List<DatoAggiuntivo> l = new ArrayList<DatoAggiuntivo>();
				l.add(toDatoAggiuntivo(c, parentWidget.getValoriDatiAggiuntiviWidget().getValoriPredefiniti(c.getIdentificativo())));
				res.put(c.getIdentificativoTabella(), l);
			}
		}
		return res;
	}
}
