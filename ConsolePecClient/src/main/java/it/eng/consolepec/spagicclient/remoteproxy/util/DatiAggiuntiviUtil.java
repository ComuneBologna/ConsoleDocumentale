package it.eng.consolepec.spagicclient.remoteproxy.util;

import java.math.BigInteger;
import java.util.List;

import it.bologna.comune.base.Cella;
import it.bologna.comune.base.CellaDatoAnagrafica;
import it.bologna.comune.base.CellaDatoValoreMultiplo;
import it.bologna.comune.base.CellaDatoValoreSingolo;
import it.bologna.comune.base.Riga;
import it.bologna.comune.base.Tabella;
import it.bologna.comune.spagic.aggiuntadatiaggiuntivi.Request.Valori.Valore;
import it.bologna.comune.spagic.gestione.datiaggiuntivi.DatoAgg;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo.DatoAggiuntivoVisitor;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoAnagrafica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoTabella;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreMultiplo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreSingolo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.RigaDatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.ValoreCellaDatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.ValoreCellaDatoAggiuntivo.ValoreCellaDatoAggiuntivoAnagrafica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.ValoreCellaDatoAggiuntivo.ValoreCellaDatoAggiuntivoMultiplo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.ValoreCellaDatoAggiuntivo.ValoreCellaDatoAggiuntivoSingolo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.ValoreCellaDatoAggiuntivo.ValoreCellaVisitor;
import it.eng.consolepec.spagicclient.bean.request.datiaggiuntivi.DatoAggiuntivo;
import it.eng.consolepec.spagicclient.bean.request.datiaggiuntivi.DatoAggiuntivo.TabellaDatoAggiuntivo;
import it.eng.consolepec.spagicclient.bean.request.datiaggiuntivi.DatoAggiuntivo.TipoDato;

public class DatiAggiuntiviUtil {

	public static void copyListFromBeanToXML(List<DatoAggiuntivo> src, List<it.bologna.comune.base.DatoAggiuntivo> dest) {
		for (DatoAggiuntivo dato : src) {
			dest.add(datoSpagicToDatoBase(dato));
		}
	}

	public static void copyListFromXMLToBean(List<it.bologna.comune.base.DatoAggiuntivo> src, List<DatoAggiuntivo> dest) {
		for (it.bologna.comune.base.DatoAggiuntivo dato : src) {
			dest.add(datoBaseToDatoSpagic(dato));
		}
	}

	/*
	 * DATO AGGIUNTIVO COMMON TO DATO AGGIUNTIVO BASE REQUEST
	 */

	public static it.bologna.comune.base.DatoAggiuntivo datoCommonToDatoBase(it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo da) {
		final it.bologna.comune.base.DatoAggiuntivo datoAggiuntivo = new it.bologna.comune.base.DatoAggiuntivo();
		datoAggiuntivo.setNome(da.getNome());
		datoAggiuntivo.setTipo(da.getTipo().name());
		datoAggiuntivo.setDescrizione(da.getDescrizione());
		datoAggiuntivo.setVisibile(da.isVisibile());

		if (da.getPosizione() != null)
			datoAggiuntivo.setPosizione(new BigInteger(da.getPosizione().toString()));

		da.accept(new DatoAggiuntivoVisitor() {

			@Override
			public void visit(DatoAggiuntivoTabella datoAggiuntivoTabella) {
				datoAggiuntivo.setTabella(new Tabella());
				datoAggiuntivo.setEditabile(datoAggiuntivoTabella.isEditabile());
				datoAggiuntivo.getTabella().setEditabile(datoAggiuntivoTabella.isEditabile());

				for (it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo d : datoAggiuntivoTabella.getIntestazioni())
					datoAggiuntivo.getTabella().getIntestazioni().add(datoCommonToDatoBase(d));

				for (RigaDatoAggiuntivo riga : datoAggiuntivoTabella.getRighe()) {

					final Riga rigaRq = new Riga();

					for (ValoreCellaDatoAggiuntivo cella : riga.getCelle()) {

						if (cella != null) {

							cella.accept(new ValoreCellaVisitor() {

								@Override
								public void visit(ValoreCellaDatoAggiuntivoSingolo cella) {
									CellaDatoValoreSingolo c = new CellaDatoValoreSingolo();
									c.setValore(cella.getValore());
									rigaRq.getCella().add(c);
								}

								@Override
								public void visit(ValoreCellaDatoAggiuntivoMultiplo cella) {
									CellaDatoValoreMultiplo c = new CellaDatoValoreMultiplo();
									c.getValori().addAll(cella.getValori());
									rigaRq.getCella().add(c);
								}

								@Override
								public void visit(ValoreCellaDatoAggiuntivoAnagrafica cella) {
									CellaDatoAnagrafica c = new CellaDatoAnagrafica();
									c.setIdAnagrafica(cella.getIdAnagrafica());
									c.setValore(cella.getValore());
									rigaRq.getCella().add(c);
								}
							});

						} else {
							rigaRq.getCella().add(null);
						}
					}

					datoAggiuntivo.getTabella().getRiga().add(rigaRq);
				}
			}

			@Override
			public void visit(DatoAggiuntivoAnagrafica datoAggiuntivoAnagrafica) {
				if (datoAggiuntivoAnagrafica.getIdAnagrafica() != null) {
					datoAggiuntivo.setIdAnagrafica(datoAggiuntivoAnagrafica.getIdAnagrafica());
					datoAggiuntivo.setValore(datoAggiuntivoAnagrafica.getValore());
				}

				datoAggiuntivo.setEditabile(datoAggiuntivoAnagrafica.isEditabile());
				datoAggiuntivo.setObbligatorio(datoAggiuntivoAnagrafica.isObbligatorio());

			}

			@Override
			public void visit(DatoAggiuntivoValoreMultiplo datoAggiuntivoValoreMultiplo) {
				datoAggiuntivo.getValori().addAll(datoAggiuntivoValoreMultiplo.getValori());
				datoAggiuntivo.setEditabile(datoAggiuntivoValoreMultiplo.isEditabile());
				datoAggiuntivo.setObbligatorio(datoAggiuntivoValoreMultiplo.isObbligatorio());
			}

			@Override
			public void visit(DatoAggiuntivoValoreSingolo datoAggiuntivoValoreSingolo) {
				datoAggiuntivo.setValore(datoAggiuntivoValoreSingolo.getValore());
				datoAggiuntivo.setEditabile(datoAggiuntivoValoreSingolo.isEditabile());
				datoAggiuntivo.setObbligatorio(datoAggiuntivoValoreSingolo.isObbligatorio());
			}
		});

		return datoAggiuntivo;
	}

	/*
	 * DATO AGGIUNTIVO SPAGIC TO REQUEST*
	 */

	/**
	 * Metodo privato per utilizzato per la conversione delle intestazioni del d. aggiuntivo tabella
	 */
	private static it.bologna.comune.base.DatoAggiuntivo datoSpagicToDatoBase(DatoAggiuntivo dato) {
		it.bologna.comune.base.DatoAggiuntivo da = new it.bologna.comune.base.DatoAggiuntivo();
		da.setNome(dato.getNome());
		da.setValore(dato.getValore());
		da.getValori().addAll(dato.getValori());
		da.setDescrizione(dato.getDescrizione());
		da.setTipo(dato.getTipo().toString());

		if (dato.getObbligatorio() != null)
			da.setObbligatorio(dato.getObbligatorio());

		if (dato.getEditabile() != null)
			da.setEditabile(dato.getEditabile());

		if (dato.getVisibile() != null)
			da.setVisibile(dato.getVisibile());

		if (dato.getPosizione() != null)
			da.setPosizione(BigInteger.valueOf(dato.getPosizione()));

		if (dato.getIdAnagrafica() != null)
			da.setIdAnagrafica(dato.getIdAnagrafica());

		return da;
	}

	public static Valore datoSpagicToDatoAggiunta(DatoAggiuntivo datoAggiuntivo) {

		Valore valore = new Valore();
		valore.setNome(datoAggiuntivo.getNome());
		valore.setDescrizione(datoAggiuntivo.getDescrizione());
		valore.setTipo(datoAggiuntivo.getTipo().name());

		if (datoAggiuntivo.getPosizione() != null)
			valore.setPosizione(new BigInteger(datoAggiuntivo.getPosizione().toString()));

		if (datoAggiuntivo.getEditabile() != null)
			valore.setEditabile(datoAggiuntivo.getEditabile());

		if (datoAggiuntivo.getObbligatorio() != null)
			valore.setObbligatorio(datoAggiuntivo.getObbligatorio());

		if (datoAggiuntivo.getVisibile() != null)
			valore.setVisibile(datoAggiuntivo.getVisibile());

		switch (datoAggiuntivo.getTipo()) {
		case Data:
		case IndirizzoCivico:
		case IndirizzoEsponente:
		case IndirizzoVia:
		case Lista:
		case Numerico:
		case Testo:
		case Suggest:
			valore.setValore(datoAggiuntivo.getValore());
			break;

		case MultiploTesto:
			valore.getValori().addAll(datoAggiuntivo.getValori());
			break;

		case Anagrafica:
			valore.setValore(datoAggiuntivo.getValore());
			valore.setIdAnagrafica(datoAggiuntivo.getIdAnagrafica());
			break;

		case Tabella:
			if (datoAggiuntivo.getTabella() != null) {
				Tabella tabella = new Tabella();
				tabella.setEditabile(datoAggiuntivo.getTabella().isEditabile());

				for (DatoAggiuntivo intestazione : datoAggiuntivo.getTabella().getIntestazioni()) {
					tabella.getIntestazioni().add(datoSpagicToDatoBase(intestazione));
				}

				for (RigaDatoAggiuntivo riga : datoAggiuntivo.getTabella().getRighe()) {

					final Riga rigaRq = new Riga();

					for (ValoreCellaDatoAggiuntivo cella : riga.getCelle()) {

						if (cella != null) {

							cella.accept(new ValoreCellaVisitor() {

								@Override
								public void visit(ValoreCellaDatoAggiuntivoAnagrafica cella) {
									CellaDatoAnagrafica c = new CellaDatoAnagrafica();
									c.setIdAnagrafica(cella.getIdAnagrafica());
									c.setValore(cella.getValore());
									rigaRq.getCella().add(c);
								}

								@Override
								public void visit(ValoreCellaDatoAggiuntivoMultiplo cella) {
									CellaDatoValoreMultiplo c = new CellaDatoValoreMultiplo();
									c.getValori().addAll(cella.getValori());
									rigaRq.getCella().add(c);
								}

								@Override
								public void visit(ValoreCellaDatoAggiuntivoSingolo cella) {
									CellaDatoValoreSingolo c = new CellaDatoValoreSingolo();
									c.setValore(cella.getValore());
									rigaRq.getCella().add(c);
								}
							});

						} else {
							rigaRq.getCella().add(null);
						}
					}

					tabella.getRiga().add(rigaRq);
				}

				valore.setTabella(tabella);
			}

			break;

		default:
			throw new UnsupportedOperationException();
		}

		return valore;
	}

	public static DatoAgg datoSpagicToDatoGestione(DatoAggiuntivo datoAggiuntivo) {

		DatoAgg valore = new DatoAgg();
		valore.setNome(datoAggiuntivo.getNome());
		valore.setDescrizione(datoAggiuntivo.getDescrizione());
		valore.setTipo(datoAggiuntivo.getTipo().name());

		if (datoAggiuntivo.getPosizione() != null)
			valore.setPosizione(datoAggiuntivo.getPosizione());

		if (datoAggiuntivo.getEditabile() != null)
			valore.setEditabile(datoAggiuntivo.getEditabile());

		if (datoAggiuntivo.getObbligatorio() != null)
			valore.setObbligatorio(datoAggiuntivo.getObbligatorio());

		if (datoAggiuntivo.getVisibile() != null)
			valore.setVisibile(datoAggiuntivo.getVisibile());

		switch (datoAggiuntivo.getTipo()) {
		case Data:
		case IndirizzoCivico:
		case IndirizzoEsponente:
		case IndirizzoVia:
		case Lista:
		case Numerico:
		case Testo:
		case Suggest:
			valore.setValore(datoAggiuntivo.getValore());
			break;

		case MultiploTesto:
			valore.getValori().addAll(datoAggiuntivo.getValori());
			break;

		case Anagrafica:
			valore.setValore(datoAggiuntivo.getValore());
			valore.setIdAnagrafica(datoAggiuntivo.getIdAnagrafica());
			break;

		case Tabella:
			if (datoAggiuntivo.getTabella() != null) {
				Tabella tabella = new Tabella();
				tabella.setEditabile(datoAggiuntivo.getTabella().isEditabile());

				for (DatoAggiuntivo intestazione : datoAggiuntivo.getTabella().getIntestazioni()) {
					tabella.getIntestazioni().add(datoSpagicToDatoBase(intestazione));
				}

				for (RigaDatoAggiuntivo riga : datoAggiuntivo.getTabella().getRighe()) {

					final Riga rigaRq = new Riga();

					for (ValoreCellaDatoAggiuntivo cella : riga.getCelle()) {

						if (cella != null) {
							cella.accept(new ValoreCellaVisitor() {

								@Override
								public void visit(ValoreCellaDatoAggiuntivoAnagrafica cella) {
									CellaDatoAnagrafica c = new CellaDatoAnagrafica();
									c.setIdAnagrafica(cella.getIdAnagrafica());
									c.setValore(cella.getValore());
									rigaRq.getCella().add(c);
								}

								@Override
								public void visit(ValoreCellaDatoAggiuntivoMultiplo cella) {
									CellaDatoValoreMultiplo c = new CellaDatoValoreMultiplo();
									c.getValori().addAll(cella.getValori());
									rigaRq.getCella().add(c);
								}

								@Override
								public void visit(ValoreCellaDatoAggiuntivoSingolo cella) {
									CellaDatoValoreSingolo c = new CellaDatoValoreSingolo();
									c.setValore(cella.getValore());
									rigaRq.getCella().add(c);
								}
							});

						} else {
							rigaRq.getCella().add(null);
						}
					}

					tabella.getRiga().add(rigaRq);
				}

				valore.setTabella(tabella);
			}

			break;

		default:
			throw new UnsupportedOperationException();
		}

		return valore;

	}

	public static it.bologna.comune.alfresco.creazione.fascicolo.Request.DatoAggiuntivo datoSpagicToDatoCreazione(DatoAggiuntivo datoAggiuntivo) {

		it.bologna.comune.alfresco.creazione.fascicolo.Request.DatoAggiuntivo valore = new it.bologna.comune.alfresco.creazione.fascicolo.Request.DatoAggiuntivo();
		valore.setNome(datoAggiuntivo.getNome());
		valore.setDescrizione(datoAggiuntivo.getDescrizione());
		valore.setTipo(datoAggiuntivo.getTipo().name());

		if (datoAggiuntivo.getPosizione() != null)
			valore.setPosizione(new BigInteger(datoAggiuntivo.getPosizione().toString()));

		if (datoAggiuntivo.getEditabile() != null)
			valore.setEditabile(datoAggiuntivo.getEditabile());

		if (datoAggiuntivo.getObbligatorio() != null)
			valore.setObbligatorio(datoAggiuntivo.getObbligatorio());

		if (datoAggiuntivo.getVisibile() != null)
			valore.setVisibile(datoAggiuntivo.getVisibile());

		switch (datoAggiuntivo.getTipo()) {
		case Data:
		case IndirizzoCivico:
		case IndirizzoEsponente:
		case IndirizzoVia:
		case Lista:
		case Numerico:
		case Testo:
		case Suggest:
			valore.setValore(datoAggiuntivo.getValore());
			break;

		case MultiploTesto:
			valore.getValori().addAll(datoAggiuntivo.getValori());
			break;

		case Anagrafica:
			valore.setValore(datoAggiuntivo.getValore());
			valore.setIdAnagrafica(datoAggiuntivo.getIdAnagrafica());
			break;

		case Tabella:
			if (datoAggiuntivo.getTabella() != null) {
				Tabella tabella = new Tabella();
				tabella.setEditabile(datoAggiuntivo.getTabella().isEditabile());

				for (DatoAggiuntivo intestazione : datoAggiuntivo.getTabella().getIntestazioni()) {
					tabella.getIntestazioni().add(datoSpagicToDatoBase(intestazione));
				}

				for (RigaDatoAggiuntivo riga : datoAggiuntivo.getTabella().getRighe()) {

					final Riga rigaRq = new Riga();

					for (ValoreCellaDatoAggiuntivo cella : riga.getCelle()) {

						if (cella != null) {

							cella.accept(new ValoreCellaVisitor() {

								@Override
								public void visit(ValoreCellaDatoAggiuntivoAnagrafica cella) {
									CellaDatoAnagrafica c = new CellaDatoAnagrafica();
									c.setIdAnagrafica(cella.getIdAnagrafica());
									c.setValore(cella.getValore());
									rigaRq.getCella().add(c);
								}

								@Override
								public void visit(ValoreCellaDatoAggiuntivoMultiplo cella) {
									CellaDatoValoreMultiplo c = new CellaDatoValoreMultiplo();
									c.getValori().addAll(cella.getValori());
									rigaRq.getCella().add(c);
								}

								@Override
								public void visit(ValoreCellaDatoAggiuntivoSingolo cella) {
									CellaDatoValoreSingolo c = new CellaDatoValoreSingolo();
									c.setValore(cella.getValore());
									rigaRq.getCella().add(c);
								}
							});

						} else {
							rigaRq.getCella().add(null);
						}
					}

					tabella.getRiga().add(rigaRq);
				}

				valore.setTabella(tabella);
			}

			break;

		default:
			throw new UnsupportedOperationException();
		}

		return valore;
	}

	/*
	 * REQUEST* TO DATO AGGIUNTIVO SPAGIC CLIENT
	 */
	private static DatoAggiuntivo datoBaseToDatoSpagic(it.bologna.comune.base.DatoAggiuntivo dato) {
		DatoAggiuntivo da = new DatoAggiuntivo();
		da.setNome(dato.getNome());
		da.setValore(dato.getValore());
		da.getValori().addAll(dato.getValori());
		da.setDescrizione(dato.getDescrizione());
		da.setTipo(TipoDato.valueOf(dato.getTipo()));
		da.setObbligatorio(dato.isObbligatorio());
		da.setEditabile(dato.isEditabile());
		da.setVisibile(dato.isVisibile());
		if (dato.getPosizione() != null)
			da.setPosizione(dato.getPosizione().intValue());
		da.setIdAnagrafica(dato.getIdAnagrafica());
		return da;
	}

	public static DatoAggiuntivo datoCreazioneToDatoSpagic(final it.bologna.comune.alfresco.creazione.fascicolo.Request.DatoAggiuntivo request) {
		DatoAggiuntivo dato = new DatoAggiuntivo();
		dato.setNome(request.getNome());
		dato.setDescrizione(request.getDescrizione());
		dato.setTipo(TipoDato.valueOf(request.getTipo()));
		dato.setVisibile(request.isVisibile());
		dato.setValore(request.getValore());
		dato.setEditabile(request.isEditabile());
		dato.setObbligatorio(request.isObbligatorio());
		dato.setValore(request.getValore());
		dato.getValori().addAll(request.getValori());
		dato.setIdAnagrafica(request.getIdAnagrafica());

		if (request.getPosizione() != null)
			dato.setPosizione(request.getPosizione().intValue());

		if (request.getTabella() != null) {

			TabellaDatoAggiuntivo tab = new TabellaDatoAggiuntivo();
			tab.setEditabile(request.getTabella().isEditabile());

			for (it.bologna.comune.base.DatoAggiuntivo intestazione : request.getTabella().getIntestazioni()) {
				tab.getIntestazioni().add(datoBaseToDatoSpagic(intestazione));
			}

			for (Riga rigaRequest : request.getTabella().getRiga()) {

				RigaDatoAggiuntivo riga = new RigaDatoAggiuntivo();

				for (Cella cellaRequest : rigaRequest.getCella()) {

					if (cellaRequest == null)
						riga.getCelle().add(null);

					else {
						if (cellaRequest instanceof CellaDatoValoreSingolo) {
							CellaDatoValoreSingolo c = (CellaDatoValoreSingolo) cellaRequest;
							ValoreCellaDatoAggiuntivoSingolo val = new ValoreCellaDatoAggiuntivoSingolo(c.getValore());
							riga.getCelle().add(val);

						} else if (cellaRequest instanceof CellaDatoValoreMultiplo) {
							CellaDatoValoreMultiplo c = (CellaDatoValoreMultiplo) cellaRequest;
							ValoreCellaDatoAggiuntivoMultiplo val = new ValoreCellaDatoAggiuntivoMultiplo(c.getValori());
							riga.getCelle().add(val);

						} else if (cellaRequest instanceof CellaDatoAnagrafica) {
							CellaDatoAnagrafica c = (CellaDatoAnagrafica) cellaRequest;
							ValoreCellaDatoAggiuntivoAnagrafica val = new ValoreCellaDatoAggiuntivoAnagrafica(c.getIdAnagrafica(), c.getValore());
							riga.getCelle().add(val);
						}
					}
				}

				tab.getRighe().add(riga);
			}

			dato.setTabella(tab);
		}

		return dato;
	}

	public static DatoAggiuntivo datoGestioneToDatoSpagic(final DatoAgg request) {
		DatoAggiuntivo dato = new DatoAggiuntivo();
		dato.setNome(request.getNome());
		dato.setDescrizione(request.getDescrizione());
		dato.setTipo(TipoDato.valueOf(request.getTipo()));
		dato.setPosizione(request.getPosizione());
		dato.setVisibile(request.isVisibile());
		dato.setValore(request.getValore());
		dato.setEditabile(request.isEditabile());
		dato.setObbligatorio(request.isObbligatorio());
		dato.setValore(request.getValore());
		dato.getValori().addAll(request.getValori());
		dato.setIdAnagrafica(request.getIdAnagrafica());

		if (request.getTabella() != null) {

			TabellaDatoAggiuntivo tab = new TabellaDatoAggiuntivo();
			tab.setEditabile(request.getTabella().isEditabile());
			for (it.bologna.comune.base.DatoAggiuntivo intestazione : request.getTabella().getIntestazioni()) {
				tab.getIntestazioni().add(datoBaseToDatoSpagic(intestazione));
			}

			for (Riga rigaRequest : request.getTabella().getRiga()) {

				RigaDatoAggiuntivo riga = new RigaDatoAggiuntivo();

				for (Cella cellaRequest : rigaRequest.getCella()) {

					if (cellaRequest == null)
						riga.getCelle().add(null);

					else {

						if (cellaRequest instanceof CellaDatoValoreSingolo) {
							CellaDatoValoreSingolo c = (CellaDatoValoreSingolo) cellaRequest;
							ValoreCellaDatoAggiuntivoSingolo val = new ValoreCellaDatoAggiuntivoSingolo(c.getValore());
							riga.getCelle().add(val);

						} else if (cellaRequest instanceof CellaDatoValoreMultiplo) {
							CellaDatoValoreMultiplo c = (CellaDatoValoreMultiplo) cellaRequest;
							ValoreCellaDatoAggiuntivoMultiplo val = new ValoreCellaDatoAggiuntivoMultiplo(c.getValori());
							riga.getCelle().add(val);

						} else if (cellaRequest instanceof CellaDatoAnagrafica) {
							CellaDatoAnagrafica c = (CellaDatoAnagrafica) cellaRequest;
							ValoreCellaDatoAggiuntivoAnagrafica val = new ValoreCellaDatoAggiuntivoAnagrafica(c.getIdAnagrafica(), c.getValore());
							riga.getCelle().add(val);
						}
					}
				}

				tab.getRighe().add(riga);
			}

			dato.setTabella(tab);
		}

		return dato;
	}

	/*
	 * DATO AGGIUNTIVO COMMON TO DATO AGGIUNTIVO SPAGIC CLIENT
	 */

	public static DatoAggiuntivo datoCommonToDatoSpagic(it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo da) {
		final DatoAggiuntivo datoAggiuntivo = new DatoAggiuntivo();
		datoAggiuntivo.setNome(da.getNome());
		datoAggiuntivo.setTipo(TipoDato.valueOf(da.getTipo().name()));
		datoAggiuntivo.setDescrizione(da.getDescrizione());
		datoAggiuntivo.setPosizione(da.getPosizione());
		datoAggiuntivo.setVisibile(da.isVisibile());

		da.accept(new DatoAggiuntivoVisitor() {

			@Override
			public void visit(DatoAggiuntivoTabella datoAggiuntivoTabella) {
				TabellaDatoAggiuntivo tabella = new TabellaDatoAggiuntivo();

				for (it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo d : datoAggiuntivoTabella.getIntestazioni())
					tabella.getIntestazioni().add(datoCommonToDatoSpagic(d));

				for (RigaDatoAggiuntivo riga : datoAggiuntivoTabella.getRighe()) {
					tabella.getRighe().add(riga);
				}

				datoAggiuntivo.setEditabile(datoAggiuntivoTabella.isEditabile());
				datoAggiuntivo.setTabella(tabella);
			}

			@Override
			public void visit(DatoAggiuntivoAnagrafica datoAggiuntivoAnagrafica) {
				if (datoAggiuntivoAnagrafica.getIdAnagrafica() != null) {
					datoAggiuntivo.setIdAnagrafica(datoAggiuntivoAnagrafica.getIdAnagrafica());
					datoAggiuntivo.setValore(datoAggiuntivoAnagrafica.getValore());
				}

				datoAggiuntivo.setEditabile(datoAggiuntivoAnagrafica.isEditabile());
				datoAggiuntivo.setObbligatorio(datoAggiuntivoAnagrafica.isObbligatorio());
			}

			@Override
			public void visit(DatoAggiuntivoValoreMultiplo datoAggiuntivoValoreMultiplo) {
				datoAggiuntivo.getValori().addAll(datoAggiuntivoValoreMultiplo.getValori());
				datoAggiuntivo.setEditabile(datoAggiuntivoValoreMultiplo.isEditabile());
				datoAggiuntivo.setObbligatorio(datoAggiuntivoValoreMultiplo.isObbligatorio());
			}

			@Override
			public void visit(DatoAggiuntivoValoreSingolo datoAggiuntivoValoreSingolo) {
				datoAggiuntivo.setValore(datoAggiuntivoValoreSingolo.getValore());
				datoAggiuntivo.setEditabile(datoAggiuntivoValoreSingolo.isEditabile());
				datoAggiuntivo.setObbligatorio(datoAggiuntivoValoreSingolo.isObbligatorio());
			}
		});

		return datoAggiuntivo;
	}

	/*
	 * DATO AGG. SP. CLIENT TO DATO AGG. COMMON
	 */

	public static it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo datoSpagicToDatoCommon(final DatoAggiuntivo request) {

		it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo dato = it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.TipoDato.valueOf(request.getTipo().name()).createDato();
		dato.setNome(request.getNome());
		dato.setDescrizione(request.getDescrizione());
		dato.setTipo(it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.TipoDato.valueOf(request.getTipo().name()));
		dato.setPosizione(request.getPosizione().intValue());
		dato.setVisibile(request.getVisibile());

		dato.accept(new DatoAggiuntivoVisitor() {

			@Override
			public void visit(DatoAggiuntivoTabella datoAggiuntivoTabella) {
				if (request.getTabella() != null) {

					datoAggiuntivoTabella.getIntestazioni().clear();

					for (DatoAggiuntivo d : request.getTabella().getIntestazioni()) {
						datoAggiuntivoTabella.getIntestazioni().add(datoSpagicToDatoCommon(d));
					}

					datoAggiuntivoTabella.getRighe().addAll(request.getTabella().getRighe());
					datoAggiuntivoTabella.setEditabile(request.getTabella().isEditabile());
				}
			}

			@Override
			public void visit(DatoAggiuntivoAnagrafica datoAggiuntivoAnagrafica) {
				datoAggiuntivoAnagrafica.setIdAnagrafica(request.getIdAnagrafica());
				datoAggiuntivoAnagrafica.setValore(request.getValore());
				datoAggiuntivoAnagrafica.setObbligatorio(request.getObbligatorio());
				datoAggiuntivoAnagrafica.setEditabile(request.getEditabile());
			}

			@Override
			public void visit(DatoAggiuntivoValoreMultiplo datoAggiuntivoValoreMultiplo) {
				datoAggiuntivoValoreMultiplo.getValori().clear();
				datoAggiuntivoValoreMultiplo.getValori().addAll(request.getValori());
				datoAggiuntivoValoreMultiplo.setEditabile(request.getEditabile());
				datoAggiuntivoValoreMultiplo.setObbligatorio(request.getObbligatorio());
			}

			@Override
			public void visit(DatoAggiuntivoValoreSingolo datoAggiuntivoValoreSingolo) {
				datoAggiuntivoValoreSingolo.setValore(request.getValore());
				datoAggiuntivoValoreSingolo.setEditabile(request.getEditabile());
				datoAggiuntivoValoreSingolo.setObbligatorio(request.getObbligatorio());
			}
		});

		return dato;
	}
}
