package it.eng.consolepec.xmlplugin.factory;


public enum MetadatiPratica {
	pAssegnatoA,
	//
	pVisibileA,
	//
	pDataCreazione,
	//
	pInoltratoDa,
	//
	pIdDocumentale,
	//
	pProvenienza,
	//
	pStato,
	//
	pTipoPratica,
	//
	pTitolo,
	//
	pNumeroPG,
	//
	pAnnoPG,
	//
	pDestinatario,
	//
	pCc,
	//
	pTipoMail,
	//
	pRicevutaAccettazione,
	//
	pRicevutaConsegna,
	//
	pDataInvio,
	//
	pDataRicezione,
	//
	pLetto,
	//
	pDirezioneEmail,
	//
	pUtenteCreazione,
	//
	pIdMessaggioEmail,
	//
	pData,
	//
	pNumeroFascicolo,
	//
	pElencoProtocollazioni,
	//
	pIncaricoA,
	//
	pNumeroFattura,
	//
	pRagioneSociale,
	//
	pPartitaIva,
	//
	pCodicePartitaIva,
	//
	pCondivisoCon,
	//
	pDatiAggiuntivi,
	//
	pNomeModulo,
	//
	pValoriModulo,
	//
	pElencoProcedimenti,
	//
	pMailInteroperabileStato,
	//
	//pMailInteroperabileConfermata,
	//
	//pMailInteroperabileAggiornata,
	//
	//pMailInteroperabileAnnullata,
	//
	pMailInteroperabileOggettoMessaggio,
	//
	pMailInteroperabileProvenienzaMessaggio,
	//
	pMailInteroperabileCodiceAmministrazione,
	//
	pMailInteroperabileCodiceAOO,
	//
	pMailInteroperabileCodiceRegistro,
	//
	pMailInteroperabileNumeroRegistrazione,
	//
	pMailInteroperabileDataRegistrazione,
	//
	pMailInteroperabileNomeDocumento,
	//
	pMailInteroperabileOggettoDocumento,
	//
	pMailInteroperabileTitoloDocumento,
	//
	//pMailInteroperabileConfermaRicezione,
	//
	pMailInteroperabileDestinatari,
	//
	pMailInteroperabileCc,
	//
	pMailInteroperabile,
	//
	pDestinatariInoltro,
	//
	pProgressivoInoltro,
	//
	pNotificaRifiutoInoltro,
	//
	pNomeTemplate,
	//
	pDescrizioneTemplate,
	//
	pStatoTemplate,
	//
	pCodiceComunicazione,
	//
	pDescrizioneComunicazione,
	//
	pIdTemplateComunicazione,
	//
	pOperatore,

	pStepIter;

	public String getNome() {
		return toString();
	}

	public String getNomeQualified() {
		return getPrefisso() + ":" + getNome();
	}

	public static String getPrefisso() {
		return "cp";
	}

}
