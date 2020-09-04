package it.eng.portlet.consolepec.gwt.client.widget.configurazioni;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.ui.Composite;

import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.Abilitazione;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import lombok.Getter;

public abstract class AbilitazioneWidget<T extends Abilitazione> extends Composite {

	@Getter
	protected List<Azione> azioni = new ArrayList<Azione>();

	@Getter
	protected List<T> abilitazioniDaAggiungere = new ArrayList<>();

	@Getter
	protected List<T> abilitazioniDaRimuovere = new ArrayList<>();

	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	protected void render(List<T> abilitazioni, ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;

		this.abilitazioniDaRimuovere.clear();
		this.abilitazioniDaAggiungere.clear();
		this.azioni.clear();

		if (abilitazioni != null)
			this.abilitazioniDaAggiungere.addAll(abilitazioni);
	}

	public abstract class AggiungiAbilitazioneCommand implements Command<Void, T> {

		public abstract void execute(T t);

		@Override
		public Void exe(T t) {

			if (t != null) {
				execute(t);

				if (!abilitazioniDaAggiungere.contains(t)) {
					t.setDataCreazione(new Date());
					t.setUsernameCreazione(profilazioneUtenteHandler.getDatiUtente().getUsername());
					abilitazioniDaAggiungere.add(t);
				}
			}

			return null;
		}
	}

	public abstract class EliminaAbilitazioneCommand implements Command<Void, T> {

		public abstract void execute(T t);

		@Override
		public Void exe(T t) {

			if (t != null) {
				execute(t);

				if (abilitazioniDaAggiungere.contains(t)) {
					abilitazioniDaAggiungere.remove(t);
				}

				if (!abilitazioniDaRimuovere.contains(t)) {
					abilitazioniDaRimuovere.add(t);
				}
			}

			return null;
		}
	}
}
