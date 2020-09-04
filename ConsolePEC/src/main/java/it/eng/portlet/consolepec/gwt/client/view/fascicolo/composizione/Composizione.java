package it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter.DownloadAllegatoCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter.MostraDettaglioBozzaCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter.MostraDettaglioEmailCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.GoToDettaglioModuloCommand;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.DettaglioFascicoloGenericoView;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElencoVisitor;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.web.bindery.event.shared.EventBus;

public abstract class Composizione implements ElementoElencoVisitor{
	
	
	protected final PecInPraticheDB pecInDb;
	protected final SitemapMenu sitemapMenu;
	protected final EventBus eventBus;
	protected final DettaglioFascicoloGenericoView dettaglioFascicoloGenericoViw;
	
	protected HTMLPanel panel;
	protected Button button;
	protected FascicoloDTO pratica;
	
	protected MostraDettaglioEmailCommand mostraDettaglioEmailCommand;
	protected MostraDettaglioBozzaCommand mostraDettaglioBozzaCommand;
	protected GoToDettaglioModuloCommand goToDettaglioModuloCommand;
	protected it.eng.portlet.consolepec.gwt.client.presenter.BiCommand<Object, String, AllegatoDTO> mostraDettaglioAllegatoCommand;
	protected DownloadAllegatoCommand downloadAllegatoCommand;
	
	
	public Composizione(DettaglioFascicoloGenericoView dettaglioFascicoloGenericoViw, PecInPraticheDB pecInDb, SitemapMenu sitemapMenu , EventBus eventBus) {
		this.dettaglioFascicoloGenericoViw = dettaglioFascicoloGenericoViw;
		this.eventBus = eventBus;
		this.pecInDb = pecInDb;
		this.sitemapMenu = sitemapMenu;
	}

	public void init(HTMLPanel panel, FascicoloDTO pratica, Button button){
		this.panel = panel;
		this.pratica = pratica;
		this.button = button;
	}
	
	public void setMostraDettaglioEmailCommand(MostraDettaglioEmailCommand mostraDettaglioEmailCommand) {
		this.mostraDettaglioEmailCommand = mostraDettaglioEmailCommand;
	}
	public void setMostraDettaglioBozzaCommand(MostraDettaglioBozzaCommand mostraDettaglioBozzaCommand) {
		this.mostraDettaglioBozzaCommand = mostraDettaglioBozzaCommand;
	}
	public void setGoToDettaglioModuloCommand(GoToDettaglioModuloCommand goToDettaglioModuloCommand) {
		this.goToDettaglioModuloCommand = goToDettaglioModuloCommand;
	}

	public void setMostraDettaglioAllegatoCommand(it.eng.portlet.consolepec.gwt.client.presenter.BiCommand<Object, String, AllegatoDTO> mostraDettaglioAllegatoCommand) {
		this.mostraDettaglioAllegatoCommand = mostraDettaglioAllegatoCommand;
	}

	public void setDownloadAllegatoCommand(DownloadAllegatoCommand downloadAllegatoCommand) {
		this.downloadAllegatoCommand = downloadAllegatoCommand;
	}

	public abstract void render();
	
	public abstract String getTitolo();

	public void hide() {
		this.panel.setVisible(false);
		this.button.setEnabled(true);
	}
	public void show() {
		this.panel.setVisible(true);
		this.button.setEnabled(false);
	}
	
}
