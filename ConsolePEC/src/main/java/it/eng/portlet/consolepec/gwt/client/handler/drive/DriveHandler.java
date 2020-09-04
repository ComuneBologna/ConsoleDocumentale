package it.eng.portlet.consolepec.gwt.client.handler.drive;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import it.eng.cobo.consolepec.commons.drive.Dizionario;
import it.eng.cobo.consolepec.commons.drive.Nomenclatura;
import it.eng.portlet.consolepec.gwt.client.PostLoadingAction;

public class DriveHandler {

	private DizionariHandler dizionariHandler;
	private NomenclatureHandler nomenclatureHandler;

	@Inject
	public DriveHandler(DizionariHandler dizionariHandler, NomenclatureHandler nomenclatureHandler) {
		this.dizionariHandler = dizionariHandler;
		this.nomenclatureHandler = nomenclatureHandler;
	}

	public void init(final PostLoadingAction postLoadingAction) {
		DriveCallback callback = new DriveCallback() {

			int loading = 0;

			@Override
			public void onSuccess() {
				loading++;
				if (loading == 2 && postLoadingAction != null) {
					postLoadingAction.onComplete();
				}
			}

			@Override
			public void onFailure(String error) {
				throw new IllegalArgumentException(error);
			}
		};

		caricaDizionari(callback);
		caricaNomenclature(callback);
	}

	// ////////////////// DIZIONARI ////////////////////
	private void caricaDizionari(DriveCallback callback) {
		dizionariHandler.caricaDizionari(callback);
	}

	public List<Dizionario> getDizionari(final boolean perCartella) {
		return Lists.newArrayList(Collections2.filter(dizionariHandler.getDizionari(), new Predicate<Dizionario>() {
			@Override
			public boolean apply(Dizionario input) {
				return input.isPerCartella() == perCartella;
			}
		}));
	}

	public Dizionario getDizionario(String nomeDizionario) {
		for (Dizionario dizionario : dizionariHandler.getDizionari()) {
			if (dizionario.getNome().equals(nomeDizionario)) {
				return dizionario;
			}
		}
		return null;
	}

	// ////////////////// NOMENCLATURE ////////////////////
	private void caricaNomenclature(DriveCallback callback) {
		nomenclatureHandler.caricaNomenclature(callback);
	}

	public List<Nomenclatura> getNomenclature() {
		return new ArrayList<>(nomenclatureHandler.getNomenclature().values());
	}

	public Nomenclatura getNomenclatura(final String tipo) {
		if (tipo == null)
			return null;
		return nomenclatureHandler.getNomenclature().get(tipo);
	}

	public static abstract class DriveCallback {
		public abstract void onSuccess();

		public abstract void onFailure(String error);
	}
}
