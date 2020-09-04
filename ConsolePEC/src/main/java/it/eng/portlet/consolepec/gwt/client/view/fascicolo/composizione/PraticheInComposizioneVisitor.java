package it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoAllegato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoComunicazioneRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElenco;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElencoVisitor;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppo;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollatoCapofila;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGrupppoNonProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPECRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPraticaModulisticaRiferimento;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Visitor che serve per ricavare le tutte le pratiche in composizione che fanno parte di un fascicolo.
 * A differenza di FascicoloVisitorComposizione, che serve per renderizzare il pannello "Composizione" di un fascicolo,
 * questo viene utilizzato per il pannello "Collegamenti" di un fascicolo e ricava solo ed esclusivamente gli elementi in composizione di un fascicolo.
 * 
 * Tipi pratiche possibili in composizione: EMAIL_IN, EMAIL_OUT, EMAIL_EPROTOCOLLO, COMUNICAZIONE, PRATICA_MODULISTICA
 * 
 * @author biagiot
 *
 */
public class PraticheInComposizioneVisitor implements ElementoElencoVisitor {

	private List<ElementoPECRiferimento> elencoPEC;
	private List<ElementoComunicazioneRiferimento> elencoComunicazioni;
	private List<ElementoPraticaModulisticaRiferimento> elencoPraticheModulistica;
	
	public PraticheInComposizioneVisitor(List<ElementoElenco> praticheInComposizione) {
		
		this.elencoPEC = new ArrayList<ElementoPECRiferimento>();
		this.elencoComunicazioni = new ArrayList<ElementoComunicazioneRiferimento>();
		this.elencoPraticheModulistica = new ArrayList<ElementoPraticaModulisticaRiferimento>();
		
		for (ElementoElenco pratica : praticheInComposizione) {
			pratica.accept(this);
		}
	}
	
	@Override
	public void visit(ElementoPECRiferimento pec) {
		getElencoPEC().add(pec);
		
	}
	
	@Override
	public void visit(ElementoComunicazioneRiferimento elementoComunicazioneRiferimento) {
		getElencoComunicazioni().add(elementoComunicazioneRiferimento);

	}
	
	@Override
	public void visit(ElementoPraticaModulisticaRiferimento elementoPraticaModulisticaRiferimento) {
		getElencoPraticheModulistica().add(elementoPraticaModulisticaRiferimento);
		
	}
	
	@Override
	public void visit(ElementoAllegato allegato) {
		// NOP
		
	}

	@Override
	public void visit(ElementoGruppo gruppo) {
		for (ElementoElenco e : gruppo.getElementi()) {
			e.accept(this);
		}
	}

	@Override
	public void visit(ElementoGruppoProtocollatoCapofila capofila) {
		for (ElementoElenco e : capofila.getElementi()) {
			e.accept(this);
		}
	}

	@Override
	public void visit(ElementoGruppoProtocollato subProt) {
		for (ElementoElenco e : subProt.getElementi()) {
			e.accept(this);
		}
	}

	@Override
	public void visit(ElementoGrupppoNonProtocollato nonProt) {
		for (ElementoElenco e : nonProt.getElementi()) {
			e.accept(this);
		}
	}

	public List<ElementoPECRiferimento> getElencoPEC() {
		return elencoPEC;
	}

	public List<ElementoComunicazioneRiferimento> getElencoComunicazioni() {
		return elencoComunicazioni;
	}

	public List<ElementoPraticaModulisticaRiferimento> getElencoPraticheModulistica() {
		return elencoPraticheModulistica;
	}
}
