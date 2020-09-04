package it.eng.portlet.consolepec.gwt.client.widget.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Contiene le icone utilizzate nell'applicativo
 * 
 * @author pluttero
 *
 */
public interface ConsolePECIcons extends ClientBundle {

	public ConsolePECIcons _instance = GWT.create(ConsolePECIcons.class);

	// nuova icona
	@Source("firmanonvalida.png")
	ImageResource firmanonvalida();

	@Source("firmato.png")
	ImageResource firmato();

	// nuova icona
	@Source("nonfirmato.png")
	ImageResource nonfirmato();

	@Source("mail.png") // Qua qualcuno si e' fumato qualcosa prima di scrivere questi nomi.
	ImageResource bustinaChiusaEmail();

	@Source("mail-in-uscita.png") // Qualcosa di pesante. TODO: commentare questa e la precedente
	ImageResource bustinaApertaEmail();

	@Source("mail.png")
	ImageResource mailAperta();

	@Source("mail-in-uscita.png")
	ImageResource mailInUscita();

	@Source("reinoltro.png")
	ImageResource mailDiRisposta();

	@Source("reinoltro.png")
	ImageResource reinoltro();

	@Source("fascicolo-19x23_old.png")
	ImageResource allegatoEsterno();

	// nuova icona
	@Source("allegato.png")
	ImageResource allegato();

	@Source("elimina.png")
	ImageResource elimina();

	@Source("help.png")
	ImageResource help();

	// nuova icona
	@Source("pratica-altrui-19x23.png")
	ImageResource praticaInCaricoAdAltri();

	// nuova icona
	@Source("pratica-mia-19x23.png")
	ImageResource praticaInCaricoAMe();

	@Source("imgVuota19x23.png")
	ImageResource imgVuota19x23();

	@Source("fascicolo-19x23.png")
	ImageResource fascicolo();

	@Source("fascicolo-riservato.png")
	ImageResource fascicoloRiservato();

	@Source("fascicolo_ba01.png")
	ImageResource fascicoloBa01();

	// nuova icona
	@Source("fascicolo-19x23.png")
	ImageResource fascicoloAlboPretorio();

	@Source("ricerca.png")
	ImageResource ricerca();

	@Source("gruppo.png")
	ImageResource gruppo();

	@Source("utente.png")
	ImageResource utente();

	@Source("praticamodulistica.png")
	ImageResource praticamodulistica();

	@Source("procedimento.png")
	ImageResource procedimento();

	@Source("triangolo-bianco.png")
	ImageResource triangolinoBianco();

}
