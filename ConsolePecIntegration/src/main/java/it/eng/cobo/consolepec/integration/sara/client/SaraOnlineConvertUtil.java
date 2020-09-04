package it.eng.cobo.consolepec.integration.sara.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.Comodato;
import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.Contratto;
import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.DatiResidenti;
import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.Leasing;
import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.Noleggio;
import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.ProprietaAltroMembro;
import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.ProprietaPersonale;
import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.TitolareGenerico;
import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.TitolareTestoLibero;
import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.TitoloPossesso;
import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.TrascrizioneLibretto;
import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.Veicolo;
import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.VeicoloDettaglio;
import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.VeicoloGenerico;
import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.Titolare;
import it.eng.cobo.consolepec.integration.sara.client.emissionecontrassegno.EmissioneContrassegnoRequest;
import it.eng.cobo.consolepec.integration.sara.client.emissionecontrassegno.EmissioneContrassegnoResponse;
import it.eng.cobo.consolepec.integration.sara.client.emissionepermesso.EmissionePermessoRequest;
import it.eng.cobo.consolepec.integration.sara.client.emissionepermesso.EmissionePermessoResponse;
import it.eng.cobo.consolepec.integration.sara.generated.contrassegni.File;

public class SaraOnlineConvertUtil {

	public static XMLGregorianCalendar convertDateToXMLGregorianCalendar(Date date) {
		if (date != null) {
			GregorianCalendar c = new GregorianCalendar();
			c.setTime(date);
			XMLGregorianCalendar dataXMLGregorianCalendar;
			try {
				dataXMLGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
				return dataXMLGregorianCalendar;
			} catch (DatatypeConfigurationException e) {
				throw new IllegalArgumentException();
			}
		}
		return null;
	}

	public static Date convertXMLGregorianCalendarToDate(XMLGregorianCalendar xmlGregorianCalendar) {
		if (xmlGregorianCalendar == null) {
			return null;
		}
		return xmlGregorianCalendar.toGregorianCalendar().getTime();
	}

	// -- INIZIO METODI CONVERSIONE EMISSIONE PERMESSO -- //

	public static it.eng.cobo.consolepec.integration.sara.generated.contrassegni.VeicoloGenerico convertVeicoloGenericoToJAXB(VeicoloGenerico veicoloGenerico) {

		if (veicoloGenerico == null) {
			return null;
		}

		it.eng.cobo.consolepec.integration.sara.generated.contrassegni.VeicoloGenerico veicoloJAXB = new it.eng.cobo.consolepec.integration.sara.generated.contrassegni.VeicoloGenerico();
		veicoloJAXB.setVeicolo(convertVeicoloToJAXB(veicoloGenerico.getVeicolo()));
		veicoloJAXB.setVeicoloDettaglio(convertVeicoloDettaglioToJAXB(veicoloGenerico.getVeicoloDettaglio()));

		return veicoloJAXB;
	}

	public static it.eng.cobo.consolepec.integration.sara.generated.contrassegni.VeicoloDettaglio convertVeicoloDettaglioToJAXB(VeicoloDettaglio veicoloDettaglio) {

		if (veicoloDettaglio == null) {
			return null;
		}

		it.eng.cobo.consolepec.integration.sara.generated.contrassegni.VeicoloDettaglio veicoloDettaglioJAXB = new it.eng.cobo.consolepec.integration.sara.generated.contrassegni.VeicoloDettaglio();

		veicoloDettaglioJAXB.setAlimentazioneVeicolo(veicoloDettaglio.getAlimentazioneVeicolo());
		veicoloDettaglioJAXB.setCompatibilitaAmbientale(veicoloDettaglio.getCompatibilitaAmbientale());
		veicoloDettaglioJAXB.setDataInizioValidita(convertDateToXMLGregorianCalendar(veicoloDettaglio.getDataInizioValidita()));
		veicoloDettaglioJAXB.setDataFineValidita(convertDateToXMLGregorianCalendar(veicoloDettaglio.getDataFineValidita()));
		veicoloDettaglioJAXB.setDirettiva(veicoloDettaglio.getDirettiva());
		veicoloDettaglioJAXB.setDirettivaDesc(veicoloDettaglio.getDirettivaDesc());
		veicoloDettaglioJAXB.setDirettivaMctc(veicoloDettaglio.getDirettivaMctc());

		if (veicoloDettaglio.getEcoNeco() == it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.FlagEcoNeco.E) {
			veicoloDettaglioJAXB.setEcoNeco(it.eng.cobo.consolepec.integration.sara.generated.contrassegni.FlagEcoNeco.E);
		} else if (veicoloDettaglio.getEcoNeco() == it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.FlagEcoNeco.NE) {
			veicoloDettaglioJAXB.setEcoNeco(it.eng.cobo.consolepec.integration.sara.generated.contrassegni.FlagEcoNeco.NE);
		}

		if (veicoloDettaglio.getFlagCessato() == it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.FlagCessato.A) {
			veicoloDettaglioJAXB.setFlagCessato(it.eng.cobo.consolepec.integration.sara.generated.contrassegni.FlagCessato.A);
		} else if (veicoloDettaglio.getFlagCessato() == it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.FlagCessato.C) {
			veicoloDettaglioJAXB.setFlagCessato(it.eng.cobo.consolepec.integration.sara.generated.contrassegni.FlagCessato.C);
		}

		if (veicoloDettaglio.getFlagItalianoEstero() == it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.FlagItalianoEstero.I) {
			veicoloDettaglioJAXB.setFlagItalianoEstero(it.eng.cobo.consolepec.integration.sara.generated.contrassegni.FlagItalianoEstero.I);
		} else if (veicoloDettaglio.getFlagItalianoEstero() == it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.FlagItalianoEstero.E) {
			veicoloDettaglioJAXB.setFlagItalianoEstero(it.eng.cobo.consolepec.integration.sara.generated.contrassegni.FlagItalianoEstero.E);
		}

		if (veicoloDettaglio.getFlagPermanenteAggiuntivo() == it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.FlagPermanenteAggiuntivo.P) {
			veicoloDettaglioJAXB.setFlagPermanenteAggiuntivo(it.eng.cobo.consolepec.integration.sara.generated.contrassegni.FlagPermanenteAggiuntivo.P);
		} else if (veicoloDettaglio.getFlagPermanenteAggiuntivo() == it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.FlagPermanenteAggiuntivo.T) {
			veicoloDettaglioJAXB.setFlagPermanenteAggiuntivo(it.eng.cobo.consolepec.integration.sara.generated.contrassegni.FlagPermanenteAggiuntivo.P);
		}

		veicoloDettaglioJAXB.setIdPermessoVeicolo(veicoloDettaglio.getIdPermessoVeicolo());
		veicoloDettaglioJAXB.setIndEuro(veicoloDettaglio.getIndEuro());
		veicoloDettaglioJAXB.setTarga(veicoloDettaglio.getTarga());
		veicoloDettaglioJAXB.setTipoVeicolo(veicoloDettaglio.getTipoVeicolo());
		veicoloDettaglioJAXB.setTipoVeicoloDesc(veicoloDettaglio.getTipoVeicoloDesc());

		return veicoloDettaglioJAXB;

	}

	public static it.eng.cobo.consolepec.integration.sara.generated.contrassegni.Veicolo convertVeicoloToJAXB(Veicolo veicolo) {

		if (veicolo == null) {
			return null;
		}

		it.eng.cobo.consolepec.integration.sara.generated.contrassegni.Veicolo veicoloJAXB = new it.eng.cobo.consolepec.integration.sara.generated.contrassegni.Veicolo();

		if (veicolo.getFlagItalianoEstero() == it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.FlagItalianoEstero.E) {
			veicoloJAXB.setFlagItalianoEstero(it.eng.cobo.consolepec.integration.sara.generated.contrassegni.FlagItalianoEstero.E);
		} else if (veicolo.getFlagItalianoEstero() == it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.FlagItalianoEstero.I) {
			veicoloJAXB.setFlagItalianoEstero(it.eng.cobo.consolepec.integration.sara.generated.contrassegni.FlagItalianoEstero.I);
		}
		if (veicolo.getFlagPermanenteAggiuntivo() == it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.FlagPermanenteAggiuntivo.P) {
			veicoloJAXB.setFlagPermanenteAggiuntivo(it.eng.cobo.consolepec.integration.sara.generated.contrassegni.FlagPermanenteAggiuntivo.P);
		} else if (veicolo.getFlagPermanenteAggiuntivo() == it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.FlagPermanenteAggiuntivo.T) {
			veicoloJAXB.setFlagPermanenteAggiuntivo(it.eng.cobo.consolepec.integration.sara.generated.contrassegni.FlagPermanenteAggiuntivo.T);
		}

		veicoloJAXB.setIdPermessoVeicolo(veicolo.getIdPermessoVeicolo());
		veicoloJAXB.setTarga(veicolo.getTarga());
		veicoloJAXB.setTipoVeicolo(veicolo.getTipoVeicolo());

		return veicoloJAXB;
	}

	public static it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitolareGenerico convertTitolareGenericoToJAXB(TitolareGenerico titolareGenerico) {
		if (titolareGenerico == null) {
			return null;
		}

		it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitolareGenerico titolareGenericoJAXB = new it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitolareGenerico();

		titolareGenericoJAXB.setTitolare(convertTitolareToJAXB(titolareGenerico.getTitolare()));
		titolareGenericoJAXB.setTitolareTestoLibero(convertTitolareTestoLiberoToJAXB(titolareGenerico.getTitolareTestoLibero()));

		return titolareGenericoJAXB;
	}

	public static it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitolareTestoLibero convertTitolareTestoLiberoToJAXB(TitolareTestoLibero titolareTestoLibero) {
		if (titolareTestoLibero == null) {
			return null;
		}

		it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitolareTestoLibero titolareTestoLiberoJAXB = new it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitolareTestoLibero();

		titolareTestoLiberoJAXB.setComune(titolareTestoLibero.getComune());
		titolareTestoLiberoJAXB.setNomeCognome(titolareTestoLibero.getNomeCognome());
		titolareTestoLiberoJAXB.setProvincia(titolareTestoLibero.getProvincia());
		titolareTestoLiberoJAXB.setStato(titolareTestoLibero.getStato());

		return titolareTestoLiberoJAXB;
	}

	public static it.eng.cobo.consolepec.integration.sara.generated.contrassegni.Titolare convertTitolareToJAXB(Titolare titolare) {
		if (titolare == null) {
			return null;
		}

		it.eng.cobo.consolepec.integration.sara.generated.contrassegni.Titolare titolareJAXB = new it.eng.cobo.consolepec.integration.sara.generated.contrassegni.Titolare();

		titolareJAXB.setCodiceFiscale(titolare.getCodiceFiscale());
		titolareJAXB.setCognome(titolare.getCognome());
		titolareJAXB.setDataNascita(convertDateToXMLGregorianCalendar(titolare.getDataNascita()));
		titolareJAXB.setIdTitolare(titolare.getIdTitolare());
		titolareJAXB.setNome(titolare.getNome());

		if (titolare.getSesso() == it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.Sesso.M) {
			titolareJAXB.setSesso(it.eng.cobo.consolepec.integration.sara.generated.contrassegni.Sesso.M);
		} else if (titolare.getSesso() == it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.Sesso.F) {
			titolareJAXB.setSesso(it.eng.cobo.consolepec.integration.sara.generated.contrassegni.Sesso.F);
		}

		titolareJAXB.setTelefonoCellulare(titolare.getTelefonoCellulare());
		titolareJAXB.setTelefonoFisso(titolare.getTelefonoFisso());

		return titolareJAXB;
	}

	// request permesso
	public static it.eng.cobo.consolepec.integration.sara.generated.contrassegni.EmissionePermessoRequest convertEmissionePermessoRequestToJAXB(EmissionePermessoRequest request) {

		if (request == null) {
			return null;
		}

		it.eng.cobo.consolepec.integration.sara.generated.contrassegni.EmissionePermessoRequest requestJAXB = new it.eng.cobo.consolepec.integration.sara.generated.contrassegni.EmissionePermessoRequest();

		requestJAXB.setDataIniVal(convertDateToXMLGregorianCalendar(request.getDataIniVal()));
		requestJAXB.setDataFineVal(convertDateToXMLGregorianCalendar(request.getDataFineVal()));

		if (request.getTipoPermesso() == it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.TipoPermesso.XH) {
			requestJAXB.setTipoPermesso(it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TipoPermesso.XH);
		}

		requestJAXB.setTitolare(convertTitolareGenericoToJAXB(request.getTitolare()));
		requestJAXB.setVeicolo(convertVeicoloGenericoToJAXB(request.getVeicolo()));

		return requestJAXB;
	}

	// response permesso
	public static EmissionePermessoResponse convertJAXBToEmissionePermessoResponse(it.eng.cobo.consolepec.integration.sara.generated.contrassegni.EmissionePermessoResponse responseJAXB) {

		if (responseJAXB == null) {
			return null;
		}

		EmissionePermessoResponse response = new EmissionePermessoResponse();
		response.setEsito(convertJAXBToEsito(responseJAXB.getEsito()));
		response.setFile(convertJAXBToFile(responseJAXB.getFile()));
		return response;
	}

	// -- FINE METODI CONVERSIONE EMISSIONE PERMESSO -- //

	// -- INIZIO METODI CONVERSIONE EMISSIONE CONTRASSEGNO -- //

	public static it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.Titolare convertJAXBToTitolare(
			it.eng.cobo.consolepec.integration.sara.generated.contrassegni.Titolare titolareJAXB) {

		if (titolareJAXB == null) {
			return null;
		}

		it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.Titolare titolare = new it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.Titolare();

		titolare.setCodiceFiscale(titolareJAXB.getCodiceFiscale());
		titolare.setCognome(titolareJAXB.getCognome());

		titolare.setDataNascita(convertXMLGregorianCalendarToDate(titolareJAXB.getDataNascita()));

		titolare.setIdTitolare(titolareJAXB.getIdTitolare());
		titolare.setNome(titolareJAXB.getNome());

		if (titolareJAXB.getSesso() == it.eng.cobo.consolepec.integration.sara.generated.contrassegni.Sesso.M) {
			titolare.setSesso(it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.Sesso.M);
		} else if (titolareJAXB.getSesso() == it.eng.cobo.consolepec.integration.sara.generated.contrassegni.Sesso.F) {
			titolare.setSesso(it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.Sesso.F);
		}

		titolare.setTelefonoCellulare(titolareJAXB.getTelefonoCellulare());
		titolare.setTelefonoFisso(titolareJAXB.getTelefonoFisso());

		return titolare;
	}

	public static VeicoloDettaglio convertJAXBToVeicoloDettaglio(it.eng.cobo.consolepec.integration.sara.generated.contrassegni.VeicoloDettaglio veicoloDettaglioJAXB) {
		if (veicoloDettaglioJAXB == null) {
			return null;
		}

		VeicoloDettaglio veicoloDettaglio = new VeicoloDettaglio();

		veicoloDettaglio.setAlimentazioneVeicolo(veicoloDettaglioJAXB.getAlimentazioneVeicolo());
		veicoloDettaglio.setCompatibilitaAmbientale(veicoloDettaglioJAXB.getCompatibilitaAmbientale());
		veicoloDettaglio.setDataInizioValidita(convertXMLGregorianCalendarToDate(veicoloDettaglioJAXB.getDataInizioValidita()));
		veicoloDettaglio.setDataFineValidita(convertXMLGregorianCalendarToDate(veicoloDettaglioJAXB.getDataFineValidita()));
		veicoloDettaglio.setDirettiva(veicoloDettaglioJAXB.getDirettiva());
		veicoloDettaglio.setDirettivaDesc(veicoloDettaglioJAXB.getDirettivaDesc());
		veicoloDettaglio.setDirettivaMctc(veicoloDettaglioJAXB.getDirettivaMctc());

		if (veicoloDettaglioJAXB.getEcoNeco() == it.eng.cobo.consolepec.integration.sara.generated.contrassegni.FlagEcoNeco.E) {
			veicoloDettaglio.setEcoNeco(it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.FlagEcoNeco.E);
		} else if (veicoloDettaglioJAXB.getEcoNeco() == it.eng.cobo.consolepec.integration.sara.generated.contrassegni.FlagEcoNeco.NE) {
			veicoloDettaglio.setEcoNeco(it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.FlagEcoNeco.NE);
		}

		if (veicoloDettaglioJAXB.getFlagCessato() == it.eng.cobo.consolepec.integration.sara.generated.contrassegni.FlagCessato.A) {
			veicoloDettaglio.setFlagCessato(it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.FlagCessato.A);
		} else if (veicoloDettaglioJAXB.getFlagCessato() == it.eng.cobo.consolepec.integration.sara.generated.contrassegni.FlagCessato.C) {
			veicoloDettaglio.setFlagCessato(it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.FlagCessato.A);
		}

		if (veicoloDettaglioJAXB.getFlagItalianoEstero() == it.eng.cobo.consolepec.integration.sara.generated.contrassegni.FlagItalianoEstero.I) {
			veicoloDettaglio.setFlagItalianoEstero(it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.FlagItalianoEstero.I);
		} else if (veicoloDettaglioJAXB.getFlagItalianoEstero() == it.eng.cobo.consolepec.integration.sara.generated.contrassegni.FlagItalianoEstero.E) {
			veicoloDettaglio.setFlagItalianoEstero(it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.FlagItalianoEstero.E);
		}

		if (veicoloDettaglioJAXB.getFlagPermanenteAggiuntivo() == it.eng.cobo.consolepec.integration.sara.generated.contrassegni.FlagPermanenteAggiuntivo.P) {
			veicoloDettaglio.setFlagPermanenteAggiuntivo(it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.FlagPermanenteAggiuntivo.P);
		} else if (veicoloDettaglioJAXB.getFlagPermanenteAggiuntivo() == it.eng.cobo.consolepec.integration.sara.generated.contrassegni.FlagPermanenteAggiuntivo.T) {
			veicoloDettaglio.setFlagPermanenteAggiuntivo(it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.FlagPermanenteAggiuntivo.T);
		}

		veicoloDettaglio.setIdPermessoVeicolo(veicoloDettaglioJAXB.getIdPermessoVeicolo());
		veicoloDettaglio.setIndEuro(veicoloDettaglioJAXB.getIndEuro());
		veicoloDettaglio.setTarga(veicoloDettaglioJAXB.getTarga());
		veicoloDettaglio.setTipoVeicolo(veicoloDettaglioJAXB.getTipoVeicolo());
		veicoloDettaglio.setTipoVeicoloDesc(veicoloDettaglioJAXB.getTipoVeicoloDesc());

		return veicoloDettaglio;

	}

	public static it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.ArrayOfVeicoloDettaglio convertJAXBToArrayOfVeicoloDettaglio(
			it.eng.cobo.consolepec.integration.sara.generated.contrassegni.ArrayOfVeicoloDettaglio arrayOfVeicoloDettaglioJAXB) {

		if (arrayOfVeicoloDettaglioJAXB == null) {
			return null;
		}

		it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.ArrayOfVeicoloDettaglio arrayOfVeicoloDettaglio = new it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.ArrayOfVeicoloDettaglio();

		List<VeicoloDettaglio> list = new ArrayList<VeicoloDettaglio>();

		for (it.eng.cobo.consolepec.integration.sara.generated.contrassegni.VeicoloDettaglio veicoloDettaglioJAXB : arrayOfVeicoloDettaglioJAXB.getItem()) {
			list.add(convertJAXBToVeicoloDettaglio(veicoloDettaglioJAXB));
		}

		arrayOfVeicoloDettaglio.setItem(list);

		return arrayOfVeicoloDettaglio;
	}

	public static it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.Contrassegno convertJAXBToContrassegno(
			it.eng.cobo.consolepec.integration.sara.generated.contrassegni.Contrassegno contrassegnoJAXB) {
		if (contrassegnoJAXB == null) {
			return null;
		}

		it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.Contrassegno contrassegno = new it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.Contrassegno();

		contrassegno.setDataInizioValidita(convertXMLGregorianCalendarToDate(contrassegnoJAXB.getDataInizioValidita()));
		contrassegno.setDataFineValidita(convertXMLGregorianCalendarToDate(contrassegnoJAXB.getDataFineValidita()));

		if (contrassegnoJAXB.getEcoNeco() == it.eng.cobo.consolepec.integration.sara.generated.contrassegni.FlagEcoNeco.E) {
			contrassegno.setEcoNeco(it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.FlagEcoNeco.E);
		} else if (contrassegnoJAXB.getEcoNeco() == it.eng.cobo.consolepec.integration.sara.generated.contrassegni.FlagEcoNeco.NE) {
			contrassegno.setEcoNeco(it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.FlagEcoNeco.NE);
		}

		contrassegno.setFlagTargaEstera(contrassegnoJAXB.isFlagTargaEstera());
		contrassegno.setIndirizzoRiferimento(contrassegnoJAXB.getIndirizzoRiferimento());
		contrassegno.setNumCambiTargaAggiuntiviResidui(contrassegnoJAXB.getNumCambiTargaAggiuntiviResidui());
		contrassegno.setNumCambiTargaPermanentiResidui(contrassegnoJAXB.getNumCambiTargaPermanentiResidui());
		contrassegno.setNumeroContrassegno(contrassegnoJAXB.getNumeroContrassegno());
		contrassegno.setSecondoSettore(contrassegnoJAXB.getSecondoSettore());
		contrassegno.setSettore(contrassegnoJAXB.getSettore());
		contrassegno.setSostaPagamento(contrassegnoJAXB.getSostaPagamento());
		contrassegno.setTipoContrassegno(contrassegnoJAXB.getTipoContrassegno());
		contrassegno.setTitolare(convertJAXBToTitolare(contrassegnoJAXB.getTitolare()));
		contrassegno.setVeicoli(convertJAXBToArrayOfVeicoloDettaglio(contrassegnoJAXB.getVeicoli()));
		contrassegno.setVeicoliAggiuntivi(convertJAXBToArrayOfVeicoloDettaglio(contrassegnoJAXB.getVeicoliAggiuntivi()));
		contrassegno.setZoneSirio(contrassegnoJAXB.getZoneSirio());
		return contrassegno;
	}

	public static it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.File convertJAXBToFile(File fileJAXB) {
		if (fileJAXB == null) {
			return null;
		}
		it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.File file = new it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.File();

		file.setContent(fileJAXB.getContent());
		file.setFileName(fileJAXB.getFileName());

		return file;

	}

	public static it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.Esito convertJAXBToEsito(it.eng.cobo.consolepec.integration.sara.generated.contrassegni.Esito esitoJAXB) {
		if (esitoJAXB == null) {
			return null;
		}

		it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.Esito esito = new it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.Esito();

		if (esitoJAXB.getCodEsito() == it.eng.cobo.consolepec.integration.sara.generated.contrassegni.CodEsito.ERROR) {
			esito.setCodEsito(it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.CodEsito.ERROR);
			for (it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.ErrorCode e : it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.ErrorCode.values()) {
				if (esitoJAXB.getErrorCode().name().equals(e.name())) {
					esito.setErrorCode(e);
				}
			}
		} else if (esitoJAXB.getCodEsito() == it.eng.cobo.consolepec.integration.sara.generated.contrassegni.CodEsito.OK) {
			esito.setCodEsito(it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.CodEsito.OK);
		}

		esito.setDescrizione(esitoJAXB.getDescrizione());

		return esito;
	}

	public static it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.ArrayOfXsdString convertJAXBToArrayOfXsdString(
			it.eng.cobo.consolepec.integration.sara.generated.contrassegni.ArrayOfXsdString arrayOfXsdStringJAXB) {

		if (arrayOfXsdStringJAXB == null) {
			return null;
		}

		it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.ArrayOfXsdString arrayOfXsdString = new it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.ArrayOfXsdString();

		arrayOfXsdString.setItem(arrayOfXsdStringJAXB.getItem());

		return arrayOfXsdString;
	}

	public static it.eng.cobo.consolepec.integration.sara.generated.contrassegni.DatiResidenti convertDatiResidentiToJAXB(DatiResidenti datiResidenti) {

		if (datiResidenti == null) {
			return null;
		}

		it.eng.cobo.consolepec.integration.sara.generated.contrassegni.DatiResidenti datiResidentiJAXB = new it.eng.cobo.consolepec.integration.sara.generated.contrassegni.DatiResidenti();

		datiResidentiJAXB.setEnteEmissionePatente(datiResidenti.getEnteEmissionePatente());
		datiResidentiJAXB.setFlagPatente(datiResidenti.isFlagPatente());
		datiResidentiJAXB.setGratuito(datiResidenti.isGratuito());
		datiResidentiJAXB.setSecondoSettore(datiResidenti.getSecondoSettore());
		datiResidentiJAXB.setSettore(datiResidenti.getSettore());

		return datiResidentiJAXB;
	}

	public static it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitoloPossesso.Contratto.Comodato convertComodatoToJAXB(Comodato comodato) {

		if (comodato == null) {
			return null;
		}

		it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitoloPossesso.Contratto.Comodato comodatoJAXB = new it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitoloPossesso.Contratto.Comodato();

		comodatoJAXB.setLuogoRegistrazione(comodato.getLuogoRegistrazione());
		comodatoJAXB.setNumeroRegistrazione(comodato.getNumeroRegistrazione());

		return comodatoJAXB;

	}

	public static it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitoloPossesso.Contratto.Leasing convertLeasingToJAXB(Leasing leasing) {

		if (leasing == null) {
			return null;
		}

		it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitoloPossesso.Contratto.Leasing leasingJAXB = new it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitoloPossesso.Contratto.Leasing();

		leasingJAXB.setCodiceFiscaleAzienda(leasing.getCodiceFiscaleAzienda());
		leasingJAXB.setDenominazioneAzienda(leasing.getDenominazioneAzienda());

		return leasingJAXB;

	}

	public static it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitoloPossesso.Contratto.Noleggio convertNoleggioToJAXB(Noleggio noleggio) {

		if (noleggio == null) {
			return null;
		}

		it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitoloPossesso.Contratto.Noleggio noleggioJAXB = new it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitoloPossesso.Contratto.Noleggio();

		noleggioJAXB.setCodiceFiscaleAzienda(noleggio.getCodiceFiscaleAzienda());
		noleggioJAXB.setDenominazioneAzienda(noleggio.getDenominazioneAzienda());

		return noleggioJAXB;

	}

	public static it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitoloPossesso.Contratto convertContrattoToJAXB(Contratto contratto) {

		if (contratto == null) {
			return null;
		}

		it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitoloPossesso.Contratto contrattoJAXB = new it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitoloPossesso.Contratto();

		contrattoJAXB.setComodato(convertComodatoToJAXB(contratto.getComodato()));
		contrattoJAXB.setLeasing(convertLeasingToJAXB(contratto.getLeasing()));
		contrattoJAXB.setNoleggio(convertNoleggioToJAXB(contratto.getNoleggio()));

		return contrattoJAXB;
	}

	public static it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitoloPossesso.ProprietaAltroMembro convertProprietaAltroMembroToJAXB(ProprietaAltroMembro proprietaAltroMembro) {

		if (proprietaAltroMembro == null) {
			return null;
		}

		it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitoloPossesso.ProprietaAltroMembro proprietaAltroMembroJAXB = new it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitoloPossesso.ProprietaAltroMembro();
		proprietaAltroMembroJAXB.setCodiceFiscale(proprietaAltroMembro.getCodiceFiscale());

		return proprietaAltroMembroJAXB;
	}

	public static it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitoloPossesso.ProprietaPersonale convertProprietaPersonaleToJAXB(ProprietaPersonale proprietaPersonale) {

		if (proprietaPersonale == null) {
			return null;
		}

		it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitoloPossesso.ProprietaPersonale proprietaPersonaleJAXB = new it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitoloPossesso.ProprietaPersonale();
		proprietaPersonaleJAXB.setCodiceFiscale(proprietaPersonale.getCodiceFiscale());

		return proprietaPersonaleJAXB;
	}

	public static it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitoloPossesso.TrascrizioneLibretto convertTrascrizioneLibrettoToJAXB(TrascrizioneLibretto trascrizioneLibretto) {

		if (trascrizioneLibretto == null) {
			return null;
		}

		it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitoloPossesso.TrascrizioneLibretto trascrizioneLibrettoJAXB = new it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitoloPossesso.TrascrizioneLibretto();

		trascrizioneLibrettoJAXB.setDataRegistrazione(convertDateToXMLGregorianCalendar(trascrizioneLibretto.getDataRegistrazione()));
		trascrizioneLibrettoJAXB.setLuogoRegistrazione(trascrizioneLibretto.getLuogoRegistrazione());
		trascrizioneLibrettoJAXB.setProtocollo(trascrizioneLibretto.getProtocollo());

		return trascrizioneLibrettoJAXB;
	}

	public static it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitoloPossesso convertTitoloPossessoToJAXB(TitoloPossesso titoloPossesso) {

		if (titoloPossesso == null) {
			return null;
		}

		it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitoloPossesso titoloPossessoJAXB = new it.eng.cobo.consolepec.integration.sara.generated.contrassegni.TitoloPossesso();

		titoloPossessoJAXB.setContratto(convertContrattoToJAXB(titoloPossesso.getContratto()));
		titoloPossessoJAXB.setProprietaAltroMembro(convertProprietaAltroMembroToJAXB(titoloPossesso.getProprietaAltroMembro()));
		titoloPossessoJAXB.setProprietaPersonale(convertProprietaPersonaleToJAXB(titoloPossesso.getProprietaPersonale()));
		titoloPossessoJAXB.setTrascrizioneLibretto(convertTrascrizioneLibrettoToJAXB(titoloPossesso.getTrascrizioneLibretto()));

		return titoloPossessoJAXB;
	}

	// request Contrassegno
	public static it.eng.cobo.consolepec.integration.sara.generated.contrassegni.EmissioneContrassegnoRequest convertEmissioneContrassegnoRequestToJAXB(EmissioneContrassegnoRequest request) {

		if (request == null) {
			return null;
		}

		it.eng.cobo.consolepec.integration.sara.generated.contrassegni.EmissioneContrassegnoRequest requestJAXB = new it.eng.cobo.consolepec.integration.sara.generated.contrassegni.EmissioneContrassegnoRequest();

		if (request.getCategoriaContrassegno() == it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.CategoriaContrassegno.RESIDENTI) {
			requestJAXB.setCategoriaContrassegno(it.eng.cobo.consolepec.integration.sara.generated.contrassegni.CategoriaContrassegno.RESIDENTI);
		} else if (request.getCategoriaContrassegno() == it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.CategoriaContrassegno.VETROFANIE) {
			requestJAXB.setCategoriaContrassegno(it.eng.cobo.consolepec.integration.sara.generated.contrassegni.CategoriaContrassegno.VETROFANIE);
		}
		requestJAXB.setDatiResidenti(convertDatiResidentiToJAXB(request.getDatiResidenti()));
		requestJAXB.setNumeroMatricola(request.getNumeroMatricola());
		requestJAXB.setTitoloPossesso(convertTitoloPossessoToJAXB(request.getTitoloPossesso()));
		requestJAXB.setVeicolo(convertVeicoloToJAXB(request.getVeicolo()));

		return requestJAXB;
	}

	// response Contrassegno
	public static EmissioneContrassegnoResponse convertJAXBToEmissioneContrassegnoResponse(it.eng.cobo.consolepec.integration.sara.generated.contrassegni.EmissioneContrassegnoResponse responseJAXB) {

		if (responseJAXB == null) {
			return null;
		}

		EmissioneContrassegnoResponse response = new EmissioneContrassegnoResponse();
		response.setContrassegno(convertJAXBToContrassegno(responseJAXB.getContrassegno()));
		response.setEsito(convertJAXBToEsito(responseJAXB.getEsito()));
		response.setListaCodMessaggio(convertJAXBToArrayOfXsdString(responseJAXB.getListaCodMessaggio()));
		return response;
	}

	// -- FINE METODI CONVERSIONE EMISSIONE CONTRASSEGNO -- //

}
