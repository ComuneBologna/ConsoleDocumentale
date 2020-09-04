package it.eng.portlet.consolepec.gwt.client.view;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmaiInlLoaded;
import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoAllegatoElencoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoAllegatoElencoWidget.SelezioneAllegato;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.action.pec.TipoRiferimentoPEC;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoAllegato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoComunicazioneRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElenco;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppo;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollatoCapofila;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGrupppoNonProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPECRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPraticaModulisticaRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * 
 * @author pluttero
 * 
 */
public class AllegatiBozzaFascicoloTreeBuilder implements FascicoloDTO.ElementoElencoVisitor {

	private final FascicoloDTO dto;
	private final Tree tree;
	private TreeItem root;
	private final PecInPraticheDB db;
	private final SitemapMenu sitemapMenu;
	private final List<ElementoAllegatoElencoWidget> elementoAllegatoElencoWidget = new ArrayList<ElementoAllegatoElencoWidget>();
	/**
	 * mappa dei selezionati: key-clientID pratica di origine, value:List di allegati pratica
	 */
	private final Map<String, List<AllegatoDTO>> selected = new HashMap<String, List<AllegatoDTO>>();

	public AllegatiBozzaFascicoloTreeBuilder(FascicoloDTO dto, Tree tree, PecInPraticheDB db, SitemapMenu sitemapMenu) {
		this.dto = dto;
		this.tree = tree;
		this.db = db;
		this.sitemapMenu = sitemapMenu;
	}

	public void start() {

		tree.removeItems();
		// tree.setStyleName("box-documenti");
		HTML span = new HTML("<span style='cursor:pointer' class='tree_level_0'>" + dto.getTitolo() + "</span>");
		root = tree.addItem(span);

		for (ElementoElenco elem : dto.getElenco()) {
			elem.accept(this);
		}
		root.setState(true);
	}

	@Override
	public void visit(ElementoAllegato allegato) {
		addAllegatoFascicolo(allegato);
	}

	@Override
	public void visit(ElementoPECRiferimento pec) {
		if (pec.getTipo().equals(TipoRiferimentoPEC.IN)) {
			db.getPecInByPath(pec.getRiferimento(), sitemapMenu.containsLink(pec.getRiferimento()), new PraticaEmaiInlLoaded() {

				@Override
				public void onPraticaLoaded(PecInDTO pec) {
					AllegatiBozzaFascicoloTreeBuilder thiz = AllegatiBozzaFascicoloTreeBuilder.this;
					thiz.addMailSection(pec);
				}

				@Override
				public void onPraticaError(String error) {
					// NOP
				}
			});
		}
	}
	@Override
	public void visit(ElementoPraticaModulisticaRiferimento pm) {
		// NOP
	}
	@Override
	public void visit(ElementoGruppo gruppo) {
		for (ElementoElenco elem : gruppo.getElementi())
			elem.accept(this);
	}

	@Override
	public void visit(ElementoGruppoProtocollatoCapofila capofila) {
		for (ElementoElenco elem : capofila.getElementi())
			elem.accept(this);
	}

	@Override
	public void visit(ElementoGruppoProtocollato subProt) {
		for (ElementoElenco elem : subProt.getElementi())
			elem.accept(this);
	}

	@Override
	public void visit(ElementoGrupppoNonProtocollato nonProt) {
		for (ElementoElenco elem : nonProt.getElementi())
			elem.accept(this);
	}

	private void addAllegatoFascicolo(AllegatoDTO allegato) {

		addToggleButtonAllegato(root, allegato, dto);
	}

	private void addMailSection(final PecInDTO pec) {
		if (pec.getAllegati().size() > 0) {
			HTML span = new HTML("<span style='cursor:pointer' class='tree_level_1' >" + pec.getMittente() + "    " + pec.getTitolo() + "</span>");
			TreeItem currentItem = root.addItem(span);
			currentItem.setStyleName("box-mail");

			for (final AllegatoDTO allegato : pec.getAllegati()) {
				addToggleButtonAllegato(currentItem, allegato, pec);
			}
		}
	}

	private void addToggleButtonAllegato(TreeItem item, final AllegatoDTO allegato, final PraticaDTO pratica) {
		// Image iconaFirma = new
		// Image(allegato.getIconaStato(ConsolePECIcons._instance));

		/*
		 * final HTMLPanel leafPanel = new HTMLPanel(""); leafPanel.setStyleName("documenti-mail");
		 * 
		 * aggiunta checkbox di selezione final CheckBox check = new CheckBox(); leafPanel.add( check ); Image iconaFirma = new Image(allegato.getIconaStato(ConsolePECIcons._instance)); leafPanel.add(
		 * iconaFirma ); Anchor a = new Anchor(allegato.getNome()); leafPanel.add(a); check.addClickHandler(new ClickHandler() {
		 * 
		 * @Override public void onClick(ClickEvent event) { if (check.getValue()){ if (!selected.containsKey(pratica.getClientID())) selected.put(pratica.getClientID(), new ArrayList<AllegatoDTO>());
		 * selected.get(pratica.getClientID()).add(allegato); }else{ selected.remove(pratica.getClientID()); if (selected.get(pratica.getClientID()).size() == 0)
		 * selected.remove(pratica.getClientID()); } } } );
		 * 
		 * 
		 * 
		 * checkBoxes.add(check);
		 */

		ElementoAllegatoElencoWidget allgwidget = new ElementoAllegatoElencoWidget();
		allgwidget.setSelezionaAllegatoCommand(new Command<Void, ElementoAllegatoElencoWidget.SelezioneAllegato>() {

			@Override
			public Void exe(SelezioneAllegato allegatoSelezionato) {
				if (allegatoSelezionato.isChecked()) {
					if (!selected.containsKey(pratica.getClientID()))
						selected.put(pratica.getClientID(), new ArrayList<AllegatoDTO>());
					selected.get(pratica.getClientID()).add(allegato);
				} else {
					selected.remove(pratica.getClientID());
					if (selected.get(pratica.getClientID()).size() == 0)
						selected.remove(pratica.getClientID());
				}
				return null;
			}
		});
		allgwidget.mostraDettaglio(allegato);
		elementoAllegatoElencoWidget.add(allgwidget);
		TreeItem i = item.addItem(allgwidget);

		i.setStyleName("tree_level_2");

	}

	/*
	 * mappa dei correntemente selezionati
	 */
	public Map<String, List<AllegatoDTO>> getButtonsAllegati() {
		return selected;
	}

	public int getNumeroAllegati() {
		return elementoAllegatoElencoWidget.size();
	}

	@Override
	public void visit(ElementoComunicazioneRiferimento elementoComunicazioneRiferimento) {
		// 	NOP
	}

	

}
