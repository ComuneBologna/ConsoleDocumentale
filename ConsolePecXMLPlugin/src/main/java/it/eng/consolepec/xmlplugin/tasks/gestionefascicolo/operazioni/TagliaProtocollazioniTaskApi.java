package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Protocollazione;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 
 * @author biagiot
 *
 */
public interface TagliaProtocollazioniTaskApi extends ITaskApi {

	public TagliaProtocollazioniOutput tagliaProtocollazioni(List<Allegato> allegati, List<Pratica<?>> praticheCollegateProtocollate, Pratica<?> praticaDestinataria) throws Exception;

	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class TagliaProtocollazioniOutput {
		private List<ProtocollazioneCapofilaOutput> protocollazioniCapofilaOutput = new ArrayList<ProtocollazioneCapofilaOutput>();
		private List<ProtocollazioneCollegataOutput> protocollazioniCollegateOutput = new ArrayList<ProtocollazioneCollegataOutput>();
		private List<Pratica<?>> praticheDaCollegare = new ArrayList<>();

		public TagliaProtocollazioniOutput(List<ProtocollazioneCapofilaOutput> protocollazioniCapofilaOutput, List<ProtocollazioneCollegataOutput> protocollazioniCollegateOutput,
				List<Pratica<?>> praticheDaCollegare) {
			// il cloning è necessario per evitare che gli oggetti puntino alla pratica sorgente
			for (ProtocollazioneCapofilaOutput pcapo : protocollazioniCapofilaOutput) {
				this.protocollazioniCapofilaOutput.add(pcapo.clona());
			}

			for (ProtocollazioneCollegataOutput pc : protocollazioniCollegateOutput) {
				this.protocollazioniCollegateOutput.add(pc.clona());
			}

			this.praticheDaCollegare = praticheDaCollegare;
		}
	}

	@Getter
	@AllArgsConstructor
	public static class ProtocollazioneCollegataOutput {
		private Protocollazione protocollazioneCollegata;
		private ProtocollazioneCapofila protocollazioneCapofila;
		private ElementiProtocollazione elementiProtocollazione;

		public ProtocollazioneCollegataOutput clona() {
			List<Allegato> allegati = new ArrayList<Allegato>();
			List<PraticaCollegata> pr = new ArrayList<PraticaCollegata>();
			for (Allegato allegato : this.elementiProtocollazione.getAllegatiProtocollati())
				allegati.add(allegato.clona());

			for (PraticaCollegata pc : this.elementiProtocollazione.getPraticheProtocollate())
				pr.add(pc.clona());

			ElementiProtocollazione elProt = new ElementiProtocollazione();
			elProt.getAllegatiProtocollati().addAll(allegati);
			elProt.getPraticheProtocollate().addAll(pr);

			return new ProtocollazioneCollegataOutput(this.protocollazioneCollegata.clona(), this.protocollazioneCapofila.clona(), elProt);

		}
	}

	@Getter
	@AllArgsConstructor
	public static class ProtocollazioneCapofilaOutput {
		private ProtocollazioneCapofila protocollazioneCapofila;
		private Map<ProtocollazioneCollegataContainer, ElementiProtocollazione> elementiProtocollati = new HashMap<ProtocollazioneCollegataContainer, ElementiProtocollazione>();

		public ProtocollazioneCapofilaOutput clona() {
			Map<ProtocollazioneCollegataContainer, ElementiProtocollazione> elementiProtocollati = new HashMap<ProtocollazioneCollegataContainer, ElementiProtocollazione>();
			for (Entry<ProtocollazioneCollegataContainer, ElementiProtocollazione> entry : this.elementiProtocollati.entrySet()) {
				List<PraticaCollegata> pr = new ArrayList<PraticaCollegata>();
				List<Allegato> all = new ArrayList<Allegato>();

				for (Allegato allegato : entry.getValue().getAllegatiProtocollati())
					all.add(allegato.clona());

				for (PraticaCollegata pc : entry.getValue().getPraticheProtocollate())
					pr.add(pc.clona());

				ElementiProtocollazione el = new ElementiProtocollazione();
				el.getAllegatiProtocollati().addAll(all);
				el.getPraticheProtocollate().addAll(pr);

				Protocollazione proto = entry.getKey().getProtocollazioneCollegata() != null ? entry.getKey().getProtocollazioneCollegata().clona() : null;
				ProtocollazioneCollegataContainer container = new ProtocollazioneCollegataContainer(proto);
				elementiProtocollati.put(container, el);
			}

			return new ProtocollazioneCapofilaOutput(this.protocollazioneCapofila.clona(), elementiProtocollati);
		}

		@Getter
		public static class ProtocollazioneCollegataContainer {
			private boolean exists;
			private Integer annoPG;
			private String numeroPG;
			private Protocollazione protocollazioneCollegata;

			public ProtocollazioneCollegataContainer(Protocollazione protocollazioneCollegata) {
				this.protocollazioneCollegata = protocollazioneCollegata;

				if (protocollazioneCollegata != null) {
					this.numeroPG = protocollazioneCollegata.getNumeroPG();
					this.annoPG = protocollazioneCollegata.getAnnoPG();
					exists = true;

				} else {
					exists = false;
				}
			}
		}
	}

	@Getter
	public static class ElementiProtocollazione {
		private List<Allegato> allegatiProtocollati = new ArrayList<>();
		private List<PraticaCollegata> praticheProtocollate = new ArrayList<>();
	}
}
