package it.eng.portlet.consolepec.gwt.client.widget;

import it.eng.portlet.consolepec.gwt.client.presenter.GestioneLinkDaLavorare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * 
 * 
 *
 */
public class SitemapMenu {

	public final String DEFAULT_ID_CLASS_MENU = "naviga";
	public final int DEFAULT_MAX_LUNGHEZZA_TITOLO = 30;
	Element sitemap;
	Element sitemapTitle = DOM.getElementById("naviga-title");
	private final HashMap<String, LIElement> rootLiMap = new HashMap<String, LIElement>();
	private final HashMap<String, LIElement> childLiMap = new HashMap<String, LIElement>();
	private final HashMap<String, UListElement> childUlMap = new HashMap<String, UListElement>();

	private final HashMap<String, Anchor> mapHyperlink = new HashMap<String, Anchor>();
	private final HashMap<String, ClickVoceHandler> mapClickHandlers = new HashMap<String, SitemapMenu.ClickVoceHandler>();
	private final HashMap<String, Integer> numeroMailNonLette = new HashMap<String, Integer>();
	private final HashMap<String, GestioneLinkDaLavorare> linkDaLavorare = new HashMap<String, GestioneLinkDaLavorare>();

	private final ArrayList<CaricamentoChildVoiceDifferito> caricamentoChildVoiceDifferito = new ArrayList<CaricamentoChildVoiceDifferito>();

	private final Stack<String> stackDettagli = new Stack<String>();
	/* default place utilizzato quando lo stack è vuoto */
	private String emptyStackToken = null;

	public SitemapMenu(String idMenu) {
		sitemap = DOM.getElementById(idMenu);
	}

	public SitemapMenu() {
		sitemap = DOM.getElementById(DEFAULT_ID_CLASS_MENU);
		sitemap.setInnerHTML("");
	}

	public void setTitle(String title) {
		if (sitemapTitle != null) sitemapTitle.setInnerText(title);
	}

	public void setEmptyStackToken(String token) {
		this.emptyStackToken = token;
	}

	public String addRootVoce(String titolo, final String token, boolean active, String idLink, final ClickHandler clickHandler, ImageResource imageResource) {
		if (mapHyperlink.containsKey(idLink)) throw new IllegalArgumentException("Link già presente");

		Anchor hyperlink = new Anchor(titolo);
		hyperlink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (clickHandler != null) clickHandler.onClick(event);
			}
		});

		hyperlink.setStyleName("close-btn");
		mapHyperlink.put(idLink, hyperlink);
		RootPanel.get().add(hyperlink);
		UListElement ul = Document.get().createULElement();
		ul.setId(idLink + "-ulroot");
		sitemap.appendChild(ul);
		LIElement li = Document.get().createLIElement();
		rootLiMap.put(idLink, li);
		if (active) {
			resetRootLi();
			resetChildrenLi();
			li.setClassName("is-active");
		}
		li.setId(idLink + "-liroot");
		li.appendChild(hyperlink.getElement());
		UListElement ulChild = Document.get().createULElement();
		ulChild.setId(idLink + "-ulchild");
		childUlMap.put(idLink, ulChild);
		li.appendChild(ulChild);
		ul.appendChild(li);
		return idLink;
	}

	public String addRootVoce(String titolo, String token, boolean active, String idLink, String... additionalRootCssClasses) {
		if (mapHyperlink.containsKey(idLink)) throw new IllegalArgumentException("Link già presente");

		ParagraphElement p = ParagraphElement.as(Document.get().createElement("p"));
		p.setInnerHTML(titolo);
		p.addClassName("close-btn");

		UListElement ul = Document.get().createULElement();
		ul.setId(idLink + "-ulroot");
		for (String cssClass : additionalRootCssClasses) {
			ul.addClassName(cssClass);
		}
		sitemap.appendChild(ul);
		LIElement li = Document.get().createLIElement();
		rootLiMap.put(idLink, li);
		if (active) {
			resetRootLi();
			resetChildrenLi();
			li.setClassName("is-active");
		}
		li.setId(idLink + "-liroot");
		li.appendChild(p);
		UListElement ulChild = Document.get().createULElement();
		ulChild.setId(idLink + "-ulchild");
		childUlMap.put(idLink, ulChild);
		li.appendChild(ulChild);
		ul.appendChild(li);
		return idLink;
	}

	/**
	 * Aggiunge una voce child, che entra nel breadcrumb di navigazione in fase di chiusura (si tratta quindi di una voce temporanea)
	 * 
	 * @param titolo
	 * @param token
	 * @param idRoot
	 * @param active
	 * @param idLink
	 * @param clickHandler
	 */
	public void addBreadCrumbsChildVoce(TitoloLink titolo, final String token, String idRoot, boolean active, String idLink) {
		this.stackDettagli.push(idLink);
		addChildVoce(titolo, token, idRoot, active, true, idLink, null);
	}

	public void doBackBreadCrumbs(String idLink) {
		String idTogo = null;
		while (!stackDettagli.isEmpty() && (idTogo == null || idTogo.equals(idLink))) {
			idTogo = stackDettagli.pop();
		}
		/* il link di provenienza va comunque rimosso */
		removeVoice(idLink);
		/* se idTogo==idLink lo stack è vuoto */
		if (idTogo == null || idTogo.equals(idLink)) {
			if (emptyStackToken != null) History.newItem(emptyStackToken);
		} else {
			/* in questo caso, il link più recente, non è quello di provenienza. Lo si reinserisce in stack e poi si esegue il suo click handler */
			stackDettagli.push(idTogo);
			ClickVoceHandler clickVoceHandler = mapClickHandlers.get(idTogo);
			clickVoceHandler.doClick();
		}
	}

	public void removeSiteMapOpenVoice(String idLink) {
		String idTogo = null;
		while (!stackDettagli.isEmpty() && (idTogo == null || idTogo.equals(idLink))) {
			idTogo = stackDettagli.pop();
		}
		/* il link di provenienza va comunque rimosso */
		removeVoice(idLink);
	}

	public void addChildVoce(TitoloLink titolo, final String token, String idRoot, boolean active, boolean enabled, String idLink, final ClickHandler clickHandler) {
		if (enabled) {

			// caricamento differito: F5 su dettaglio fascicolo
			if (!rootLiMap.containsKey(idRoot)) {
				// marco da carica ed esco
				caricamentoChildVoiceDifferito.add(new CaricamentoChildVoiceDifferito(titolo, token, idRoot, active, enabled, idLink, clickHandler));
				return;
			}

			String titoloNormalizzato = titolo.normalizzaTitolo(DEFAULT_MAX_LUNGHEZZA_TITOLO);
			if (mapHyperlink.containsKey(idLink)) throw new IllegalArgumentException("Link già presente");

			Anchor hyperlink = new Anchor("javascript:void(0)");
			if (clickHandler != null) hyperlink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if (clickHandler != null) clickHandler.onClick(event);
					if (token != null) History.newItem(token);
				}
			});

			hyperlink.setText(titoloNormalizzato);

			// InlineHyperlink hyperlink = new InlineHyperlink(titoloNormalizzato,
			// token);
			hyperlink.setStyleName("close-btn");
			mapHyperlink.put(idLink, hyperlink);
			RootPanel.get().add(hyperlink);

			final LIElement li = Document.get().createLIElement();
			final LIElement liParent = LIElement.as(rootLiMap.get(idRoot));
			UListElement childrenUl = UListElement.as(childUlMap.get(idRoot));

			ClickVoceHandler clickVoceHandler = new ClickVoceHandler(li, liParent, token);
			mapClickHandlers.put(idLink, clickVoceHandler);
			hyperlink.addClickHandler(clickVoceHandler);

			if (active) {
				resetChildrenLi();
				resetRootLi();
				li.setClassName("is-active");
				liParent.setClassName("is-active");
			}
			li.appendChild(hyperlink.getElement());
			childrenUl.appendChild(li);

			childLiMap.put(idLink, li);
		}
	}

	public void setTitoloPratica(String pathPratica, TitoloLink titolo) {
		Anchor hyperLink = mapHyperlink.get(pathPratica);
		if (hyperLink == null) return;
		String titoloNormalizzato = titolo.normalizzaTitolo(DEFAULT_MAX_LUNGHEZZA_TITOLO);
		hyperLink.setText(titoloNormalizzato);
	}

	/**
	 * Imposta gli stili per selezionare la voce con id passato
	 * 
	 * @param id
	 */
	public void setActiveVoce(String id) {
		Anchor hyperLink = mapHyperlink.get(id);
		if (hyperLink == null) return;
		LIElement liChild = childLiMap.get(id);
		LIElement liParent = rootLiMap.get(id);
		if (hyperLink == null || liChild == null && liParent == null) throw new IllegalArgumentException("Link inesistente");
		resetRootLi();
		resetChildrenLi();
		if (liChild != null && !liChild.getClassName().contains("is-active")) {
			liChild.setClassName("is-active");
			/* risalgo a ul e li parent */
			liParent = LIElement.as(liChild.getParentElement().getParentElement());
		}
		if (liParent != null && !liParent.getClassName().contains("is-active")) liParent.setClassName("is-active");
	}

	public void removeVoice(String id) {
		/* rimozione dallo stack dettagli, nel caso in cui non si sia passati dalla doBack() */
		stackDettagli.remove(id);
		Anchor hyperLink = mapHyperlink.get(id);
		if (hyperLink == null) return;
		numeroMailNonLette.remove(id);
		// listIdToken.remove(id);
		hyperLink.removeFromParent();
		mapHyperlink.remove(id);
		mapClickHandlers.remove(id);
		LIElement li = childLiMap.get(id);
		if (li != null) {
			li.removeFromParent();
			childLiMap.remove(id);
		}
		li = rootLiMap.get(id);
		if (li != null) {
			li.removeFromParent();
			rootLiMap.remove(id);
		}

	}

	public void removeAllVoices() {
		mapHyperlink.clear();
	}

	public void updateTitoloVoce(String id, TitoloLink newTitolo, ImageResource image) {
		if (mapHyperlink.get(id) != null) {
			mapHyperlink.get(id).setText(newTitolo.normalizzaTitolo(DEFAULT_MAX_LUNGHEZZA_TITOLO));
		}
	}

	public boolean containsLink(String id) {
		return mapHyperlink.containsKey(id);
	}

	private void resetRootLi() {
		/* rimuovo active da tutti i link padri */
		for (String liId : SitemapMenu.this.rootLiMap.keySet()) {
			LIElement l = SitemapMenu.this.rootLiMap.get(liId);
			if (l.getClassName().contains("is-active")) {
				l.removeClassName("is-active");
			}
		}
	}

	private void resetChildrenLi() {
		/* rimuovo active da tutti i link fratelli */
		for (String liId : SitemapMenu.this.childLiMap.keySet()) {
			LIElement l = SitemapMenu.this.childLiMap.get(liId);
			if (l.getClassName().contains("is-active")) {
				l.removeClassName("is-active");
			}
		}
	}

	class ClickVoceHandler implements ClickHandler {
		private final LIElement li, liParent;
		private final String userToken;

		public ClickVoceHandler(LIElement li, LIElement liParent, String userToken) {
			this.li = li;
			this.liParent = liParent;
			this.userToken = userToken;
		}

		public void doClick() {
			resetRootLi();
			resetChildrenLi();
			String className = li.getClassName();
			if (!className.contains("is-active")) {
				li.setClassName("is-active");
			}
			className = liParent.getClassName();
			if (!className.contains("is-active")) {
				liParent.setClassName("is-active");
			}
			/* esecuzione evento registrato */
			// if (userHandler != null)
			// userHandler.onClick(event);
			/* link a token */
			if (userToken != null) History.newItem(userToken);

		}

		@Override
		public void onClick(ClickEvent event) {
			event.preventDefault();
			doClick();
		}
	}

	public void addLinkGestione(GestioneLinkDaLavorare gestioneLinkDaLavorare) {
		linkDaLavorare.put(gestioneLinkDaLavorare.getNomeWorklist(), gestioneLinkDaLavorare);
	}

	// public GestioneLinkDaLavorare getLink(TipoFascicoloWorklist tipoFascicoloWorklist) {
	// return linkDaLavorare.get(tipoFascicoloWorklist.getTipoPratica());
	// }

	public void caricamentoChildVoiceDifferite() {
		for (CaricamentoChildVoiceDifferito caricamento : caricamentoChildVoiceDifferito) {
			caricamento.updateMenu();
		}
		caricamentoChildVoiceDifferito.clear();
	}

	private class CaricamentoChildVoiceDifferito {

		TitoloLink titolo;
		String token;
		String idRoot;
		boolean active;
		boolean enabled;
		String idLink;
		ClickHandler clickHandler;

		public CaricamentoChildVoiceDifferito(TitoloLink titolo, String token, String idRoot, boolean active, boolean enabled, String idLink, ClickHandler clickHandler) {
			super();
			this.titolo = titolo;
			this.token = token;
			this.idRoot = idRoot;
			this.active = active;
			this.enabled = enabled;
			this.idLink = idLink;
			this.clickHandler = clickHandler;
		}

		public void updateMenu() {
			addChildVoce(titolo, token, idRoot, active, enabled, idLink, clickHandler);
		}

	}

}
