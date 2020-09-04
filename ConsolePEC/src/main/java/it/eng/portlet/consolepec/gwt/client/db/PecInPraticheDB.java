package it.eng.portlet.consolepec.gwt.client.db;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.shared.action.CaricaPraticaAction;
import it.eng.portlet.consolepec.gwt.shared.action.CaricaPraticaActionResult;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CaricaPraticaFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CaricaPraticaFascicoloActionResult;
import it.eng.portlet.consolepec.gwt.shared.action.modulistica.CaricaPraticaModulisticaAction;
import it.eng.portlet.consolepec.gwt.shared.action.modulistica.CaricaPraticaModulisticaActionResult;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CaricaPraticaEmailInAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CaricaPraticaEmailInActionResult;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CaricaPraticaEmailOutAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CaricaPraticaEmailOutActionResult;
import it.eng.portlet.consolepec.gwt.shared.action.template.CaricaModelloAction;
import it.eng.portlet.consolepec.gwt.shared.action.template.CaricaModelloResult;
import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ErrorCode;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
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
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;

/**
 * Persistenza lato client, dei bean pratica
 * 
 * @author pluttero
 * 
 */
public class PecInPraticheDB {
	private final Map<Integer, CachedPratica> db = new HashMap<Integer, CachedPratica>();
	private final DispatchAsync dispatcher;

	@Inject
	public PecInPraticheDB(final DispatchAsync dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void getPraticaByPathETipo(final String clientID, TipologiaPratica tipologiaPratica, final boolean reload, final PraticaLoaded callback) {
		
		if (PraticaUtil.isIngresso(tipologiaPratica)) {
			getPecInByPath(clientID, reload, new PraticaEmaiInlLoaded() {

				@Override
				public void onPraticaLoaded(PecInDTO pec) {
					if (pec == null)
						callback.onPraticaError(ErrorCode.PRATICA_NOT_FOUND.name());
					else
						callback.onPraticaLoaded(pec);
				}

				@Override
				public void onPraticaError(String error) {
					callback.onPraticaError(error);
				}
			});
		}

		else if (PraticaUtil.isEmailOut(tipologiaPratica)) {
			getPecOutByPath(clientID, reload, new PraticaEmailOutLoaded() {

				@Override
				public void onPraticaLoaded(PecOutDTO pec) {
					if (pec == null)
						callback.onPraticaError(ErrorCode.PRATICA_NOT_FOUND.name());
					else
						callback.onPraticaLoaded(pec);
				}

				@Override
				public void onPraticaError(String error) {
					callback.onPraticaError(error);
				}

			});
		}

		else if (PraticaUtil.isPraticaModulistica(tipologiaPratica)) {
			getPraticaModulisticaByPath(clientID, reload, new PraticaModulisticaLoaded() {

				@Override
				public void onPraticaModulisticaLoaded(PraticaModulisticaDTO pratica) {
					if (pratica == null)
						callback.onPraticaError(ErrorCode.PRATICA_NOT_FOUND.name());
					else
						callback.onPraticaLoaded(pratica);
				}

				@Override
				public void onPraticaModulisticaError(String error) {
					callback.onPraticaError(error);
				}
			});
			
		} else if (PraticaUtil.isModello(tipologiaPratica)) {
			
			getModelloByPath(clientID, reload, new ModelloLoaded() {
				
				@Override
				public <T extends BaseTemplateDTO> void onPraticaLoaded(T template) {
					if (template == null)
						callback.onPraticaError(ErrorCode.PRATICA_NOT_FOUND.name());
					else
						callback.onPraticaLoaded(template);
				}
				
				@Override
				public void onPraticaError(String error) {
					callback.onPraticaError(error);
				}
			});
			
		} else if (PraticaUtil.isComunicazione(tipologiaPratica)) {
			getComunicazioneByPath(clientID, reload, new PraticaComunicazioneLoaded() {
				
				@Override
				public void onPraticaLoaded(ComunicazioneDTO pratica) {
					if (pratica == null)
						callback.onPraticaError(ErrorCode.PRATICA_NOT_FOUND.name());
					else
						callback.onPraticaLoaded(pratica);
				}
				
				@Override
				public void onPraticaError(String error) {
					callback.onPraticaError(error);
				}
			});
		} else if (PraticaUtil.isFascicolo(tipologiaPratica)) {
			getFascicoloByPath(clientID, reload, new PraticaFascicoloLoaded() {

				@Override
				public void onPraticaLoaded(FascicoloDTO pec) {
					if (pec == null)
						callback.onPraticaError(ErrorCode.PRATICA_NOT_FOUND.name());
					else
						callback.onPraticaLoaded(pec);
				}

				@Override
				public void onPraticaError(String error) {
					callback.onPraticaError(error);
				}
			});
		} else
			throw new IllegalArgumentException();

	}

	/**
	 * Carica una pratica generica dato il clientId. Se non presente localmente, la richiesta è inviata al server
	 * 
	 * @param alfrescoPath
	 * @param callback
	 */
	public void getPraticaByPath(final String clientID, final boolean dontReload, final PraticaLoaded callback) {
		PraticaDTO dto = null;
		if (db.get(clientID.hashCode()) != null)
			dto = db.get(clientID.hashCode()).getPratica();

		CaricaPraticaAction action = new CaricaPraticaAction(clientID, TipologiaCaricamento.getTipologiaCaricamento(dontReload));
		if (dto == null) {
			dispatcher.execute(action, new AsyncCallback<CaricaPraticaActionResult>() {
				@Override
				public void onFailure(Throwable arg0) {
					callback.onPraticaError(arg0 != null ? arg0.getLocalizedMessage() : "");
				}

				@Override
				public void onSuccess(CaricaPraticaActionResult res) {
					if (res.isError()) {
						callback.onPraticaError(res.getErrorMsg());
					} else {
						insertOrUpdate(clientID, res.getPraticaDTO(), dontReload);
						callback.onPraticaLoaded(res.getPraticaDTO());
					}
				}
			});
		} else {
			callback.onPraticaLoaded(dto);
		}
	}

	/**
	 * Carica una PecIn dato il clientId. Se non presente localmente, la richiesta è inviata al server
	 * 
	 * @param alfrescoPath
	 * @param callback
	 */
	public void getPecInByPath(final String clientID, final boolean dontReload, final PraticaEmaiInlLoaded callback) {
		PraticaDTO dto = null;
		if (db.get(clientID.hashCode()) != null)
			dto = db.get(clientID.hashCode()).getPratica();

		CaricaPraticaEmailInAction action = new CaricaPraticaEmailInAction(clientID, TipologiaCaricamento.getTipologiaCaricamento(dontReload));
		if (dto == null) {
			dispatcher.execute(action, new AsyncCallback<CaricaPraticaEmailInActionResult>() {
				@Override
				public void onFailure(Throwable arg0) {
					callback.onPraticaError(arg0 != null ? arg0.getLocalizedMessage() : "");
				}

				@Override
				public void onSuccess(CaricaPraticaEmailInActionResult res) {
					if (res.isError()) {
						callback.onPraticaError(res.getErrorCode());
					} else {
						insertOrUpdate(clientID, res.getDettaglio(), dontReload);
						callback.onPraticaLoaded(res.getDettaglio());
					}
				}
			});
		} else {
			if (!(dto instanceof PecInDTO))
				throw new IllegalArgumentException("La pratica con clientID: " + clientID + " non è di tipo PecInDTO");
			callback.onPraticaLoaded((PecInDTO) dto);
		}
	}

	/**
	 * Carica una PecOut dato il clientId.
	 * 
	 * @param id
	 * @param callback
	 */
	public void getPecOutByPath(final String clientID, final boolean reload, final PraticaEmailOutLoaded callback) {
		PraticaDTO dto = null;
		if (db.get(clientID.hashCode()) != null)
			dto = db.get(clientID.hashCode()).getPratica();

		CaricaPraticaEmailOutAction action = new CaricaPraticaEmailOutAction(clientID, TipologiaCaricamento.getTipologiaCaricamento(reload));
		if (dto == null) {

			dispatcher.execute(action, new AsyncCallback<CaricaPraticaEmailOutActionResult>() {

				@Override
				public void onFailure(Throwable arg0) {
					callback.onPraticaError(arg0 != null ? arg0.getLocalizedMessage() : "");
				}

				@Override
				public void onSuccess(CaricaPraticaEmailOutActionResult res) {
					if (res.isError()) {
						callback.onPraticaError(res.getErrorCode());
					} else {
						insertOrUpdate(clientID, res.getDettaglio(), reload);
						callback.onPraticaLoaded(res.getDettaglio());
					}

				}

			});
		} else {
			if (!(dto instanceof PecOutDTO))
				throw new IllegalArgumentException("La pratica con id: " + clientID + " non è di tipo PecOutDTO");
			callback.onPraticaLoaded((PecOutDTO) dto);
		}
	}

	/**
	 * Carica un fascicolo, dato il clientID.
	 * 
	 * @param clientID
	 * @param callback
	 */
	public void getFascicoloByPath(final String clientID, final boolean reload, final PraticaFascicoloLoaded callback) {
		PraticaDTO dto = null;
		if (db.get(clientID.hashCode()) != null)
			dto = db.get(clientID.hashCode()).getPratica();

		CaricaPraticaFascicoloAction action = new CaricaPraticaFascicoloAction(clientID, TipologiaCaricamento.getTipologiaCaricamento(reload));
		// se il dettaglio non è aperto ricarico la pratica stessa
		if (dto == null) {
			dispatcher.execute(action, new AsyncCallback<CaricaPraticaFascicoloActionResult>() {

				@Override
				public void onFailure(Throwable caught) {
					callback.onPraticaError(caught != null ? caught.getLocalizedMessage() : "");
				}

				@Override
				public void onSuccess(CaricaPraticaFascicoloActionResult result) {
					if (result.getError()) {
						callback.onPraticaError(result.getErrorCode());
					} else {
						insertOrUpdate(clientID, result.getPratica(), reload);
						callback.onPraticaLoaded(result.getPratica());
					}
				}
			});
		} else {
			if (!(dto instanceof FascicoloDTO))
				throw new IllegalArgumentException("La pratica con path: " + clientID + " non è di tipo FascicoloDTO");
			callback.onPraticaLoaded((FascicoloDTO) dto);
		}
	}

	public void getPraticaModulisticaByPath(final String clientID, final boolean reload, final PraticaModulisticaLoaded callback) {
		PraticaDTO dto = null;
		if (db.get(clientID.hashCode()) != null)
			dto = db.get(clientID.hashCode()).getPratica();
		CaricaPraticaModulisticaAction action = new CaricaPraticaModulisticaAction(clientID, TipologiaCaricamento.getTipologiaCaricamento(reload));
		if (dto == null) {
			dispatcher.execute(action, new AsyncCallback<CaricaPraticaModulisticaActionResult>() {
				@Override
				public void onFailure(Throwable arg0) {
					callback.onPraticaModulisticaError(arg0 != null ? arg0.getLocalizedMessage() : "");
				}

				@Override
				public void onSuccess(CaricaPraticaModulisticaActionResult res) {
					if (res.getError()) {
						callback.onPraticaModulisticaError(res.getErrorCode());
					} else {
						insertOrUpdate(clientID, res.getPratica(), reload);
						callback.onPraticaModulisticaLoaded(res.getPratica());
					}
				}
			});
		} else {
			if (!(dto instanceof PraticaModulisticaDTO))
				throw new IllegalArgumentException("La pratica con id: " + clientID + " non è di tipo PraticaModulisticaDTO");
			callback.onPraticaModulisticaLoaded((PraticaModulisticaDTO) dto);
		}
	}
	
	public void getModelloByPath(final String clientID, final boolean reload, final ModelloLoaded callback) {
		PraticaDTO dto = null;
		if (db.get(clientID.hashCode()) != null)
			dto = db.get(clientID.hashCode()).getPratica();
		
		CaricaModelloAction action = new CaricaModelloAction(clientID, TipologiaCaricamento.getTipologiaCaricamento(reload));
		
		if (dto == null) {
			dispatcher.execute(action, new AsyncCallback<CaricaModelloResult>() {

				@Override
				public void onFailure(Throwable arg0) {
					callback.onPraticaError(arg0 != null ? arg0.getLocalizedMessage() : "");
				}

				@Override
				public void onSuccess(CaricaModelloResult res) {
					if (res.isError()) {
						callback.onPraticaError(res.getErrorMessage());
						
					} else {
						insertOrUpdate(clientID, res.getModello(), reload);
						callback.onPraticaLoaded(res.getModello());
					}
				}
			});
		} else {
			if (!(dto instanceof BaseTemplateDTO))
				throw new IllegalArgumentException("La pratica con id: " + clientID + " non è di tipo Modello");
			
			callback.onPraticaLoaded((BaseTemplateDTO) dto);
		}
	}

	public void getComunicazioneByPath(final String clientID, final boolean reload, final PraticaComunicazioneLoaded callback) {
		PraticaDTO dto = null;
		if (db.get(clientID.hashCode()) != null)
			dto = db.get(clientID.hashCode()).getPratica();
		CaricaPraticaAction action = new CaricaPraticaAction(clientID, TipologiaCaricamento.getTipologiaCaricamento(reload));
		if (dto == null) {
			dispatcher.execute(action, new AsyncCallback<CaricaPraticaActionResult>() {
				@Override
				public void onFailure(Throwable arg0) {
					callback.onPraticaError(arg0 != null ? arg0.getLocalizedMessage() : "");
				}

				@Override
				public void onSuccess(CaricaPraticaActionResult res) {
					if (res.isError()) {
						callback.onPraticaError(res.getErrorMsg());
					} else {
						insertOrUpdate(clientID, res.getPraticaDTO(), reload);
						callback.onPraticaLoaded((ComunicazioneDTO) res.getPraticaDTO());
					}
				}
			});
		} else {
			if (!(dto instanceof ComunicazioneDTO))
				throw new IllegalArgumentException("La pratica con id: " + clientID + " non è di tipo ComunicazioneDTO");
			callback.onPraticaLoaded((ComunicazioneDTO) dto);
		}
	}

	
	public void flush() {
		db.clear();
	}

	public void insertOrUpdate(String id, PraticaDTO dd, boolean isEditable) {
		db.put(id.hashCode(), new CachedPratica(dd, isEditable));
	}

	public void insert(String id, PraticaDTO dd, boolean isEditable) {
		if (db.containsKey(id.hashCode()) || dd == null)
			throw new IllegalArgumentException("Il db contiene già una entry con id: " + id + " oppure pratica è null: " + dd);
		insertOrUpdate(id, dd, isEditable);
	}

	public void update(String id, PraticaDTO dd, boolean isEditable) {
		if (!db.containsKey(id.hashCode()) || dd == null)
			throw new IllegalArgumentException("Il db NON contiene una entry con id: " + id + " oppure pratica è null: " + dd);
		db.put(id.hashCode(), new CachedPratica(dd, isEditable));
	}

	public void remove(String id) {
		CachedPratica cachedPratica = db.get(id.hashCode());
		if (cachedPratica != null && cachedPratica.getPratica() != null && cachedPratica.getPratica() instanceof FascicoloDTO) {
			FascicoloDTO fascicoloDTO = (FascicoloDTO) cachedPratica.getPratica();
			List<ElementoElenco> elenco = fascicoloDTO.getElenco();
			SimpleElencoVisitor simpleElencoVisitor = new SimpleElencoVisitor();
			for (ElementoElenco elem : elenco) {
				elem.accept(simpleElencoVisitor);
			}
		}
		db.remove(id.hashCode());
	}

	public boolean contains(String id) {
		return db.containsKey(id.hashCode());
	}

	/* interfacce */

	public interface PraticaLoaded {
		public void onPraticaLoaded(PraticaDTO pratica);

		public void onPraticaError(String error);
	}

	public interface PraticaEmaiInlLoaded {
		public void onPraticaLoaded(PecInDTO pec);

		public void onPraticaError(String error);
	}

	public interface PraticaEmailOutLoaded {
		public void onPraticaLoaded(PecOutDTO pec);

		public void onPraticaError(String error);
	}

	public interface PraticaModulisticaLoaded {
		public void onPraticaModulisticaLoaded(PraticaModulisticaDTO pratica);

		public void onPraticaModulisticaError(String error);
	}

	public interface PraticaFascicoloLoaded {
		public void onPraticaLoaded(FascicoloDTO pec);

		public void onPraticaError(String error);
	}

	public interface PraticaFascicoloGenericoLoaded extends PraticaFascicoloLoaded {
		public void onPraticaLoaded(FascicoloDTO pec);

		public void onPraticaError(String error);
	}

	public interface ModelloLoaded {
		public <T extends BaseTemplateDTO> void onPraticaLoaded(T template);
		public void onPraticaError(String error);
	}

	public interface PraticaComunicazioneLoaded {
		public void onPraticaLoaded(ComunicazioneDTO comunicazione);

		public void onPraticaError(String error);
	}

	/* private class */
	private class CachedPratica {

		private final PraticaDTO pratica;
		private final boolean isEditable;

		public CachedPratica(PraticaDTO pratica, boolean isEditable) {
			super();
			this.pratica = pratica;
			this.isEditable = isEditable;
		}

		public PraticaDTO getPratica() {
			return pratica;
		}

		public boolean isEditable() {
			return isEditable;
		}

		@Override
		public String toString() {
			if (pratica != null)
				return pratica.toString();
			return super.toString();
		}

	}

	private class SimpleElencoVisitor implements ElementoElencoVisitor {

		@Override
		public void visit(ElementoGrupppoNonProtocollato nonProt) {
			for (ElementoElenco ele : nonProt.getElementi()) {
				ele.accept(this);
			}
		}

		@Override
		public void visit(ElementoGruppoProtocollato subProt) {
			for (ElementoElenco ele : subProt.getElementi()) {
				ele.accept(this);
			}
		}

		@Override
		public void visit(ElementoGruppoProtocollatoCapofila capofila) {
			for (ElementoElenco ele : capofila.getElementi()) {
				ele.accept(this);
			}
		}

		@Override
		public void visit(ElementoGruppo gruppo) {
			for (ElementoElenco ele : gruppo.getElementi()) {
				ele.accept(this);
			}
		}

		@Override
		public void visit(ElementoPECRiferimento pec) {
			CachedPratica cachedPratica = db.get(pec.getRiferimento().hashCode());
			if (cachedPratica != null && !cachedPratica.isEditable())
				db.remove(pec.getRiferimento().hashCode());
		}

		@Override
		public void visit(ElementoAllegato allegato) {
		}

		@Override
		public void visit(ElementoPraticaModulisticaRiferimento pm) {
			CachedPratica cachedPratica = db.get(pm.getRiferimento().hashCode());
			if (cachedPratica != null && !cachedPratica.isEditable())
				db.remove(pm.getRiferimento().hashCode());
		}

		@Override
		public void visit(ElementoComunicazioneRiferimento c) {
			CachedPratica cachedPratica = db.get(c.getRiferimento().hashCode());
			if (cachedPratica != null && !cachedPratica.isEditable())
				db.remove(c.getRiferimento().hashCode());
		}

	}

}
