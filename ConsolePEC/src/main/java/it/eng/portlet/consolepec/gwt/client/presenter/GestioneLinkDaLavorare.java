package it.eng.portlet.consolepec.gwt.client.presenter;

import it.eng.portlet.consolepec.gwt.client.widget.TitoloLink;

import java.util.HashMap;

public class GestioneLinkDaLavorare {
	private String titoloVoce;
	private String idLink;
	private Integer counter;
	private Integer total;
	private String nomeWorklist;
	private String customTitoloLink;
	private HashMap<String, String> aliasEmailMap = new HashMap<String, String>();
	private Integer limit;

	public String getNomeWorklist() {
		return nomeWorklist;
	}

	public GestioneLinkDaLavorare(String titoloVoce, String idLink, Integer counter, Integer total, Integer limit, String nomeWorklist, String customTitoloLink, HashMap<String, String> aliasEmailMap) {
		super();
		this.titoloVoce = titoloVoce;
		this.idLink = idLink;
		this.counter = counter;
		this.total = total;
		this.nomeWorklist = nomeWorklist;
		this.customTitoloLink = customTitoloLink;
		this.aliasEmailMap = aliasEmailMap;
		this.limit = limit;
	}

	public TitoloLink getTitoloLink() {
		String titoloLink = (customTitoloLink == null) ? titoloVoce : customTitoloLink;
		if ((counter == null || counter < 0) && (total == null || total < 0))
			return new TitoloLink(titoloLink);
		else
			return new TitoloLink(titoloLink, counter, total, limit);
	}

	public void setContatore(Integer val) {
		this.counter = val;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public String getIdLink() {
		if (customTitoloLink == null) {
			return idLink;
		} else {
			for (String key : aliasEmailMap.keySet())
				if (customTitoloLink.equalsIgnoreCase(aliasEmailMap.get(key)))
					return key;
			return customTitoloLink;
		}
	}

	public TitoloLink dec() {
		counter--;
		TitoloLink newTitolo = getTitoloLink();
		return newTitolo;
	}

	public void setCustomTitoloLink(String customTitoloLink) {
		if (aliasEmailMap.get(customTitoloLink) == null)
			this.customTitoloLink = customTitoloLink;
		else
			this.customTitoloLink = aliasEmailMap.get(customTitoloLink);
	}
}
