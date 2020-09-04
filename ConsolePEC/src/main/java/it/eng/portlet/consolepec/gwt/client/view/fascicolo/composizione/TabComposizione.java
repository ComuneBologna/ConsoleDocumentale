package it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.ElementoComposizione;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

/**
 * @author GiacomoFM
 * @since 28/dic/2018
 */
public abstract class TabComposizione<T> extends Composite {

	public static final Integer PAGE_SIZE = 5;

	protected EventBus eventBus;

	protected FascicoloDTO dto;
	protected Set<ElementoComposizione> elementiSelezionati;

	abstract String getTabName();

	protected abstract List<T> getList();

	public abstract void filtraRicerca(final Set<?> search);

	protected abstract Widget drawWidget(T elemento, Set<ElementoComposizione> elementiSelezionati);

	protected HTMLPanel internalPanel = new HTMLPanel("");
	protected HTMLPanel pagerPanel = new HTMLPanel("");
	private final HTML pageLabel = new HTML();
	private Integer currentPage = 1;
	private Integer lastPage = 1;

	public TabComposizione(EventBus eventBus) {
		super();
		this.eventBus = eventBus;
	}

	public void initTab(FascicoloDTO fascicoloDTO, Set<ElementoComposizione> elementiSelezionati, TabLayoutPanel tabPanel) {
		this.dto = fascicoloDTO;
		this.elementiSelezionati = elementiSelezionati;
		redrawInternalPanel(getList());
		tabPanel.add(internalPanel, getTabName());
	}

	public void reset() {
		currentPage = 1;
		elementiSelezionati.clear();
		redrawInternalPanel(getList());
	}

	protected void redrawInternalPanel(final List<T> data) {
		pagerPanel.clear();
		internalPanel.clear();

		drawPaging(internalPanel, data);
		openPage(data, currentPage);

		internalPanel.add(pagerPanel);
	}

	public void openPage(final List<T> data, Integer page) {
		lastPage = (int) Math.ceil(((double) data.size()) / PAGE_SIZE);
		if (lastPage < 1)
			lastPage = 1;
		currentPage = page;
		openPage(data);
	}

	private void openPage(final List<T> data) {
		if (currentPage < 1)
			currentPage = 1;
		if (currentPage > lastPage)
			currentPage = lastPage;
		pagerPanel.clear();

		List<T> pagedList = new LinkedList<>();
		for (int i = currentPage * PAGE_SIZE - PAGE_SIZE; pagedList.size() < PAGE_SIZE && i < data.size(); i++) {
			pagedList.add(data.get(i));
		}
		for (T a : pagedList) {
			pagerPanel.add(drawWidget(a, elementiSelezionati));
		}
		pageLabel.setText(createText(data.size()));
	}

	private String createText(Integer dataSize) {
		StringBuilder sb = new StringBuilder("pagina ");
		if (dataSize > 0) {
			// Integer pageStart = currentPage * PAGE_SIZE - PAGE_SIZE + 1;
			// Integer endIndex = pageStart + pagedListSize - 1;
			// return pageStart + "-" + endIndex + " di " + dataSize;
			return sb.append(currentPage).append(" di ").append(lastPage).toString();
		}
		return sb.append("0 di 0").toString();
	}

	protected void drawPaging(HTMLPanel panel, final List<T> data) {
		Button prevPage = new Button(SafeHtmlUtils.fromString("<"));
		Button nextPage = new Button(SafeHtmlUtils.fromString(">"));

		prevPage.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				currentPage--;
				openPage(data);
			}
		});

		nextPage.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				currentPage++;
				openPage(data);
			}
		});

		HorizontalPanel layout = new HorizontalPanel();
		layout.addStyleName("composizione-paginazione");
		layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		layout.add(pageLabel);
		layout.add(prevPage);
		layout.add(nextPage);
		panel.add(layout);
	}

}
