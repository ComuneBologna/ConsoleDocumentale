package it.eng.portlet.consolepec.gwt.client.widget.configurazioni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ConsoleDisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo.DatoAggiuntivoVisitor;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoAnagrafica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoTabella;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreMultiplo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreSingolo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.TipoDato;
import it.eng.portlet.consolepec.gwt.client.view.OpenCloseImagePanel;
import it.eng.portlet.consolepec.gwt.client.widget.ListaCampiWidget;
import it.eng.portlet.consolepec.gwt.shared.dto.Campo.TipoWidget;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 *
 * @author biagiot
 *
 */
public class DatiAggiuntiviWidget extends Composite {

	interface DatiAggiuntiviWidgetBinder extends UiBinder<Widget, DatiAggiuntiviWidget> {/**/}

	private static DatiAggiuntiviWidgetBinder uiBinder = GWT.create(DatiAggiuntiviWidgetBinder.class);

	@UiField
	HTMLPanel datiAggPanel;

	@UiField(provided = true)
	ConsoleDisclosurePanel valoriPredefinitiDisclosure = //
			new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Valori predefiniti");

	@UiField(provided = true)
	ConsoleDisclosurePanel datiAggTabellaDisclosure = //
			new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Intestazioni tabelle");

	@UiField
	HTMLPanel valoriPredefinitiPanel;

	@UiField
	HTMLPanel datiAggTabellaPanel;

	private CampiDatiAggiuntiviWidget campiWidget;

	@Getter
	private ValoriDatiAggiuntiviWidget valoriDatiAggiuntiviWidget;

	private IntestazioniDatoAggiuntivoTabellaWidget intestazioniDatoAggiuntivoTabellaWidget;

	public DatiAggiuntiviWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void render(List<DatoAggiuntivo> datiAggiuntivi) {
		reload(datiAggiuntivi);
	}

	void reload(List<DatoAggiuntivo> datiAggiuntivi) {
		this.valoriPredefinitiDisclosure.setOpen(false);
		this.valoriPredefinitiPanel.clear();
		this.valoriPredefinitiPanel.getElement().removeAllChildren();

		this.datiAggTabellaDisclosure.setOpen(false);
		this.datiAggTabellaPanel.clear();
		this.datiAggTabellaPanel.getElement().removeAllChildren();

		this.datiAggPanel.clear();
		this.datiAggPanel.getElement().removeAllChildren();

		this.campiWidget = new CampiDatiAggiuntiviWidget(datiAggiuntivi != null ? datiAggiuntivi.size() + 30 : 100);
		this.campiWidget.render(true, true);
		Map<String, List<String>> datiAggMultipli = new HashMap<String, List<String>>();
		Map<String, List<DatoAggiuntivo>> datiAggiuntiviTabella = new HashMap<String, List<DatoAggiuntivo>>();
		if (datiAggiuntivi != null && !datiAggiuntivi.isEmpty()) {
			datiAggMultipli = getDatiAggiuntiviMultipli(datiAggiuntivi);
			datiAggiuntiviTabella = getDatiAggiuntiviTabella(datiAggiuntivi);
			for (DatoAggiuntivo da : datiAggiuntivi) {
				this.campiWidget.add(toCampo(da));
			}
		}
		this.datiAggPanel.add(campiWidget);

		this.valoriDatiAggiuntiviWidget = new ValoriDatiAggiuntiviWidget();
		this.valoriDatiAggiuntiviWidget.render(new ArrayList<String>(datiAggMultipli.keySet()), datiAggMultipli);
		this.valoriPredefinitiPanel.add(valoriDatiAggiuntiviWidget);
		this.valoriPredefinitiDisclosure.setOpen(true);

		this.intestazioniDatoAggiuntivoTabellaWidget = new IntestazioniDatoAggiuntivoTabellaWidget();
		this.intestazioniDatoAggiuntivoTabellaWidget.render(new ArrayList<String>(datiAggiuntiviTabella.keySet()), datiAggiuntiviTabella, this);
		this.datiAggTabellaPanel.add(intestazioniDatoAggiuntivoTabellaWidget);
		this.datiAggTabellaDisclosure.setOpen(true);

		this.campiWidget.setAggiungiCommand(new AggiungiCampoCommand());
		this.campiWidget.setEliminaCommand(new EliminaCampoCommand());
	}

	public List<DatoAggiuntivo> getDatiAggiuntivi() {
		List<DatoAggiuntivo> res = new ArrayList<DatoAggiuntivo>();
		for (CampoDatoAggiuntivo cda : campiWidget.getCampiDatiAggiuntivi()) {
			List<DatoAggiuntivo> intestazioniTabella = intestazioniDatoAggiuntivoTabellaWidget.getIntestazioni(cda.getIdentificativo());
			res.add(toDatoAggiuntivo(cda, valoriDatiAggiuntiviWidget.getValoriPredefiniti(cda.getIdentificativo()), intestazioniTabella));
		}
		return res;
	}

	public class CampiDatiAggiuntiviWidget extends ListaCampiWidget<CampoDatoAggiuntivo> {
		public CampiDatiAggiuntiviWidget(Integer limit) {
			super(limit);
		}

		public List<CampoDatoAggiuntivo> getCampiDatiAggiuntivi() {
			return getValori();
		}

		@Override
		protected void definisciCampi() {
			String[] tipiArr = new String[TipoDato.values().length];
			for (int i = 0; i < TipoDato.values().length; i++) {
				tipiArr[i] = TipoDato.values()[i].toString();
			}
			creaCampo("idDatoAggiuntivo", "Identificativo dato aggiuntivo", TipoWidget.TEXTBOX, 0).obbligatorio(true);
			creaCampo("nomeDatoAggiuntivo", "Nome", TipoWidget.TEXTBOX, 1).obbligatorio(true);
			creaCampo("editabile", "Editabile", TipoWidget.YESNORADIOBUTTON, 2).obbligatorio(true);
			creaCampo("visibile", "Visibile", TipoWidget.YESNORADIOBUTTON, 3).obbligatorio(true);
			creaCampo("obbligatorio", "Obbligatorio", TipoWidget.YESNORADIOBUTTON, 4).obbligatorio(true);
			creaCampo("posizione", "Posizione", TipoWidget.INTEGERBOX, 5).obbligatorio(true);
			creaCampo("tipoDato", "Tipo", TipoWidget.LISTBOX, 6).obbligatorio(true).lista(tipiArr);
		}

		@Override
		protected CampoDatoAggiuntivo converti(Object[] riga) {
			if (riga[4] == null) { // imposto un default per l'obbligatorieta, per evitare problemi con i tipi tabella
				riga[4] = false;
			}
			if (riga != null && riga[0] != null && riga[1] != null && riga[2] != null && riga[3] != null && riga[4] != null && riga[5] != null && riga[6] != null) {
				CampoDatoAggiuntivo campoDatoAggiuntivo = new CampoDatoAggiuntivo();
				campoDatoAggiuntivo.setIdentificativo((String) riga[0]);
				campoDatoAggiuntivo.setNome((String) riga[1]);
				campoDatoAggiuntivo.setEditabile((boolean) riga[2]);
				campoDatoAggiuntivo.setVisibile((boolean) riga[3]);
				campoDatoAggiuntivo.setObbligatorio((boolean) riga[4]);
				campoDatoAggiuntivo.setPosizione((Integer) riga[5]);
				campoDatoAggiuntivo.setTipo(TipoDato.valueOf((String) riga[6]));
				return campoDatoAggiuntivo;
			}
			return null;
		}

		@Override
		protected Object[] converti(CampoDatoAggiuntivo riga) {
			if (riga != null) {
				return new Object[] { riga.getIdentificativo(), //
						riga.getNome(), //
						riga.getEditabile(), //
						riga.isVisibile(), //
						riga.getObbligatorio(), //
						riga.getPosizione(), //
						riga.getTipo().toString() };
			}
			return null;
		}

		@Override
		protected boolean validaInserimento(CampoDatoAggiuntivo riga, List<String> errori) {
			if (riga == null) {
				errori.add("Dato non valido");
				return false;
			}
			if (riga.getIdentificativo() == null || riga.getIdentificativo().trim().isEmpty() || riga.getNome() == null || riga.getNome().trim().isEmpty() || riga.getTipo() == null) {
				errori.add("Dato non valido");
				return false;
			}

			if (getValori().contains(riga)) {
				errori.add("Il dato esiste già");
				return false;
			}
			if (!TipoDato.Tabella.equals(riga.getTipo()) && (riga.getObbligatorio() == null || riga.getEditabile() == null)) {
				errori.add("Dato non valido");
				return false;
			}
			return true;

		}
	}

	public class AggiungiCampoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CampoDatoAggiuntivo> {

		@Override
		public Void exe(CampoDatoAggiuntivo t) {
			campiWidget.add(t);
			reload(getDatiAggiuntivi());
			return null;
		}
	}

	public class EliminaCampoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CampoDatoAggiuntivo> {

		@Override
		public Void exe(CampoDatoAggiuntivo t) {
			campiWidget.remove(t);
			reload(getDatiAggiuntivi());
			return null;
		}
	}

	public void clear() {
		if (campiWidget != null) {
			campiWidget.getValori().clear();
		}
		if (valoriDatiAggiuntiviWidget != null) {
			valoriDatiAggiuntiviWidget.clear();
		}
		if (intestazioniDatoAggiuntivoTabellaWidget != null) {
			intestazioniDatoAggiuntivoTabellaWidget.clear();
		}
	}

	@Data
	@EqualsAndHashCode(of = "identificativo")
	private static class CampoDatoAggiuntivo {
		String identificativo;
		String nome;
		Boolean obbligatorio;
		Boolean editabile;
		boolean visibile;
		Integer posizione;
		TipoDato tipo;
	}

	private static DatoAggiuntivo toDatoAggiuntivo(final CampoDatoAggiuntivo campo, final List<String> valoriPredefiniti, final List<DatoAggiuntivo> intestazioni) {
		DatoAggiuntivo res = campo.getTipo().createDato();
		res.setNome(campo.getIdentificativo());
		res.setDescrizione(campo.getNome());
		res.setPosizione(campo.getPosizione());
		res.setVisibile(campo.isVisibile());
		res.setTipo(campo.getTipo());

		res.accept(new DatoAggiuntivoVisitor() {

			@Override
			public void visit(DatoAggiuntivoTabella datoAggiuntivoTabella) {
				datoAggiuntivoTabella.setEditabile(campo.getEditabile());
				if (intestazioni != null) {
					datoAggiuntivoTabella.getIntestazioni().addAll(intestazioni);
				}
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

	private static CampoDatoAggiuntivo toCampo(DatoAggiuntivo dag) {
		final CampoDatoAggiuntivo c = new CampoDatoAggiuntivo();
		c.setIdentificativo(dag.getNome());
		c.setNome(dag.getDescrizione());
		c.setTipo(dag.getTipo());
		c.setVisibile(dag.isVisibile());
		c.setPosizione(dag.getPosizione());

		dag.accept(new DatoAggiuntivoVisitor() {

			@Override
			public void visit(DatoAggiuntivoTabella datoAggiuntivoTabella) {
				c.setEditabile(datoAggiuntivoTabella.isEditabile());
			}

			@Override
			public void visit(DatoAggiuntivoAnagrafica datoAggiuntivoAnagrafica) {
				c.setObbligatorio(datoAggiuntivoAnagrafica.isObbligatorio());
				c.setEditabile(datoAggiuntivoAnagrafica.isEditabile());
			}

			@Override
			public void visit(DatoAggiuntivoValoreMultiplo datoAggiuntivoValoreMultiplo) {
				c.setObbligatorio(datoAggiuntivoValoreMultiplo.isObbligatorio());
				c.setEditabile(datoAggiuntivoValoreMultiplo.isEditabile());
			}

			@Override
			public void visit(DatoAggiuntivoValoreSingolo datoAggiuntivoValoreSingolo) {
				c.setObbligatorio(datoAggiuntivoValoreSingolo.isObbligatorio());
				c.setEditabile(datoAggiuntivoValoreSingolo.isEditabile());
			}
		});
		return c;
	}

	private static Map<String, List<String>> getDatiAggiuntiviMultipli(List<DatoAggiuntivo> datiAggiuntivi) {
		final Map<String, List<String>> res = new HashMap<String, List<String>>();

		for (DatoAggiuntivo dag : datiAggiuntivi) {

			dag.accept(new DatoAggiuntivo.DatoAggiuntivoVisitorAdapter() {

				@Override
				public void visit(DatoAggiuntivoValoreMultiplo datoAggiuntivoValoreMultiplo) {
					res.put(datoAggiuntivoValoreMultiplo.getNome(), datoAggiuntivoValoreMultiplo.getValoriPredefiniti());
				}

				@Override
				public void visit(DatoAggiuntivoValoreSingolo datoAggiuntivoValoreSingolo) {
					if (datoAggiuntivoValoreSingolo.getTipo().isValoriPredefiniti()) {
						res.put(datoAggiuntivoValoreSingolo.getNome(), datoAggiuntivoValoreSingolo.getValoriPredefiniti());
					}
				}

				@Override
				public void visit(DatoAggiuntivoTabella datoAggiuntivoTabella) {
					// ?_?
					for (DatoAggiuntivo dat : datoAggiuntivoTabella.getIntestazioni()) {
						dat.accept(new DatoAggiuntivo.DatoAggiuntivoVisitorAdapter() {
							@Override
							public void visit(DatoAggiuntivoValoreMultiplo datoAggiuntivoValoreMultiplo) {
								res.put(datoAggiuntivoValoreMultiplo.getNome(), datoAggiuntivoValoreMultiplo.getValoriPredefiniti());
							}

							@Override
							public void visit(DatoAggiuntivoValoreSingolo datoAggiuntivoValoreSingolo) {
								if (datoAggiuntivoValoreSingolo.getTipo().isValoriPredefiniti()) {
									res.put(datoAggiuntivoValoreSingolo.getNome(), datoAggiuntivoValoreSingolo.getValoriPredefiniti());
								}
							}
						});
					}
				}
			});
		}
		return res;
	}

	private static Map<String, List<DatoAggiuntivo>> getDatiAggiuntiviTabella(List<DatoAggiuntivo> datiAggiuntivi) {
		final Map<String, List<DatoAggiuntivo>> res = new HashMap<String, List<DatoAggiuntivo>>();

		for (DatoAggiuntivo dag : datiAggiuntivi) {
			dag.accept(new DatoAggiuntivo.DatoAggiuntivoVisitorAdapter() {
				@Override
				public void visit(DatoAggiuntivoTabella datoAggiuntivoTabella) {
					res.put(datoAggiuntivoTabella.getNome(), datoAggiuntivoTabella.getIntestazioni());
				}
			});
		}
		return res;
	}

}
