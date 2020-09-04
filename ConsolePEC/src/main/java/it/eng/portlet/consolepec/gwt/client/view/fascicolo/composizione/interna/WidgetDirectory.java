package it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.interna;

import static it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.DirectoryComposizione.SEPARATOR;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.AllegatoComposizione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.DirectoryComposizione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.ElementoComposizione;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.TabComposizione;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.interna.SelezioneElementoEvent.SelezioneElementoEventHandler;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author GiacomoFM
 * @since 2019-03-26
 */
public class WidgetDirectory extends Composite implements SelezioneElementoEventHandler {

	private static final Integer MAX_DIRECTORY_NAME_LENGTH = 12;

	private static final String LABEL_STYLE = "path-label";
	private static final String DIRECTORY_STYLE = "directory-btn";
	private static final String SELECTION_STYLE = "selected-directory";
	private static final String SELECTABLE_STYLE = "selectable-directory";

	private static final String IMG_SRC = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUA"
			+ "AAAJcEhZcwAADsMAAA7DAcdvqGQAAAD6SURBVGhD7ZhLCsJAEAVzD3WnXtP7+Dma4ucA+norDTMVeE7ALqjlGyqGLJypKIqiKBI28iyf8g09yKGs5VVmcb0OfYiTzKKowx7iJb"
			+ "OgOQ55iCxkpPEdXuROdpEdsgTju4zvs0k2XopH2SQbLsWHbPI9Gg3uwQMzuAcPzOAePDCDe/DADO7BAzO4Bw/M4B48MIN78MAM7sEDM7gHD8zgHjwwg3vwwAzuwQMzuAcPzOAe"
			+ "PDCDe/DADO7BAzO4Bw/M4B48MIN75tyH/squP/VxiZSNl2BcODfZy5vMDhhpNG1lFysZl0jxyrLDfuldxi/fHV8URVH8C9P0Ack5F7etsu5ZAAAAAElFTkSuQmCC";

	@UiField
	HTMLPanel pathPanel;
	@UiField
	InlineLabel pathLabel;
	@UiField
	HTMLPanel gruppoDirectories;
	@UiField
	HTMLPanel gruppoElementi;

	private PositionHolder holder;

	private static Binder binder = GWT.create(Binder.class);

	public interface Binder extends UiBinder<Widget, WidgetDirectory> {/**/}

	private Map<String, HTMLPanel> sorting = new TreeMap<>(new Comparator<String>() {
		@Override
		public int compare(String o1, String o2) {
			if (o1 == null || o2 == null) {
				return 0;
			}
			return o1.toLowerCase().compareTo(o2.toLowerCase());
		}
	});

	private Comparator<ElementoComposizione> comparator = new Comparator<ElementoComposizione>() {
		@Override
		public int compare(ElementoComposizione o1, ElementoComposizione o2) {
			if (o1 == null || o2 == null) {
				return 0;
			}
			if (o1.getNome() != null && o2.getNome() != null) {
				return o1.getNome().toLowerCase().compareTo(o2.getNome().toLowerCase());
			}
			return 0;
		}
	};

	public WidgetDirectory(DirectoryComposizione root, Set<ElementoComposizione> elementiSelezionati, final EventBus eventBus, final FascicoloDTO dto, final TabComposizione<?> tab) {
		initWidget(binder.createAndBindUi(this));
		gruppoDirectories(root, elementiSelezionati, eventBus, dto, tab);
		gruppoElementi(root, elementiSelezionati, eventBus, dto, tab);

		eventBus.addHandler(SelezioneElementoEvent.TYPE, this);
	}

	private void gruppoDirectories(DirectoryComposizione root, Set<ElementoComposizione> elementiSelezionati, final EventBus eventBus, final FascicoloDTO dto, final TabComposizione<?> tab) {
		sorting.clear();
		gruppoDirectories.clear();
		pathPanel.setStylePrimaryName(LABEL_STYLE);
		pathLabel.setText(root.getPath());
		if (root.getParent() != null) {
			Button parentBtn = createButton("..");
			parentBtn.addDoubleClickHandler(new ApriCartellaHandler(root.getParent(), elementiSelezionati, eventBus, dto, tab, this));
			gruppoDirectories.add(parentBtn);
		}
		for (final DirectoryComposizione subDirectory : root.getSubDirectories()) {
			HTMLPanel selectableFolder = new HTMLPanel("");
			selectableFolder.setStylePrimaryName(SELECTABLE_STYLE);

			String folderName = subDirectory.getPath().substring(subDirectory.getPath().lastIndexOf(SEPARATOR)).replace(SEPARATOR, "");
			Button folderBtn = null;
			if (folderName.length() < MAX_DIRECTORY_NAME_LENGTH) {
				folderBtn = createButton(folderName);
			} else {
				String folderShortName = folderName.substring(0, (MAX_DIRECTORY_NAME_LENGTH - 2)).concat("..");
				folderBtn = createButton(folderShortName);
				folderBtn.addMouseOverHandler(new MostraNomeCompletoHandler(folderBtn, folderName));
				folderBtn.addMouseOutHandler(new NascondiNomeCompletoHandler(folderBtn, folderShortName));
			}
			folderBtn.addDoubleClickHandler(new ApriCartellaHandler(subDirectory, elementiSelezionati, eventBus, dto, tab, this));

			CheckBox folderCheck = new CheckBox();
			folderCheck.addValueChangeHandler(new SelezionaCartellaHandler(eventBus, subDirectory, elementiSelezionati, folderBtn, this));

			controllaCartellaSelezionata(subDirectory, elementiSelezionati, folderCheck, folderBtn);

			if (!subDirectory.getElementi().isEmpty()) {
				selectableFolder.add(folderCheck);
			}
			selectableFolder.add(folderBtn);
			sorting.put(folderName, selectableFolder);
		}

		for (HTMLPanel p : sorting.values()) {
			gruppoDirectories.add(p);
		}

		this.holder = new PositionHolder(root, elementiSelezionati, eventBus, dto, tab);
	}

	private static Button createButton(String text) {
		Button btn = new Button();
		composeButton(btn, text);
		return btn;
	}

	private static void composeButton(final Button btn, String text) {
		btn.setStylePrimaryName(DIRECTORY_STYLE);
		Image folderImg = new Image(IMG_SRC);
		InlineLabel folderName = new InlineLabel(text);
		btn.getElement().appendChild(folderImg.getElement());
		btn.getElement().appendChild(Document.get().createBRElement());
		btn.getElement().appendChild(folderName.getElement());
	}

	private static void controllaCartellaSelezionata(DirectoryComposizione subDirectory, Set<ElementoComposizione> elementiSelezionati, CheckBox folderCheck, Button folderBtn) {
		if (!subDirectory.getElementi().isEmpty() && elementiSelezionati.containsAll(subDirectory.getElementi())) {
			folderCheck.setValue(true);
			folderBtn.addStyleName(SELECTION_STYLE);
		} else {
			folderCheck.setValue(false);
			folderBtn.removeStyleName(SELECTION_STYLE);
		}
	}

	private void gruppoElementi(DirectoryComposizione root, Set<ElementoComposizione> elementiSelezionati, final EventBus eventBus, final FascicoloDTO dto, final TabComposizione<?> tab) {
		gruppoElementi.clear();
		Collections.sort(root.getElementi(), comparator);
		for (AllegatoComposizione elemento : root.getElementi()) {
			gruppoElementi.add(new WidgetAllegatoComposizione(elemento, elementiSelezionati, eventBus, dto, tab));
		}
	}

	@Override
	public void onSelezionaElemento(ElementoComposizione elementoSelezionato) {
		if (this.holder != null) {
			gruppoDirectories(holder.getRoot(), holder.getElementiSelezionati(), holder.getEventBus(), holder.getDto(), holder.getTab());
		}
	}

	@Override
	public void onDeselezionaElemento(ElementoComposizione elementoDeselezionato) {
		if (this.holder != null) {
			gruppoDirectories(holder.getRoot(), holder.getElementiSelezionati(), holder.getEventBus(), holder.getDto(), holder.getTab());
		}
	}

	@AllArgsConstructor
	private static class MostraNomeCompletoHandler implements MouseOverHandler {

		private final Button folderBtn;
		private final String folderName;

		@Override
		public void onMouseOver(MouseOverEvent event) {
			folderBtn.getElement().removeAllChildren();
			composeButton(folderBtn, folderName);
		}
	}

	@AllArgsConstructor
	private static class NascondiNomeCompletoHandler implements MouseOutHandler {

		private final Button folderBtn;
		private final String folderName;

		@Override
		public void onMouseOut(MouseOutEvent event) {
			folderBtn.getElement().removeAllChildren();
			composeButton(folderBtn, folderName);
		}
	}

	@AllArgsConstructor
	private static class ApriCartellaHandler implements DoubleClickHandler {

		private final DirectoryComposizione root;
		private final Set<ElementoComposizione> elementiSelezionati;
		private final EventBus eventBus;
		private final FascicoloDTO dto;
		private final TabComposizione<?> tab;
		private final WidgetDirectory widget;

		@Override
		public void onDoubleClick(DoubleClickEvent event) {
			widget.gruppoDirectories(root, elementiSelezionati, eventBus, dto, tab);
			widget.gruppoElementi(root, elementiSelezionati, eventBus, dto, tab);
		}
	}

	@AllArgsConstructor
	private static class SelezionaCartellaHandler implements ValueChangeHandler<Boolean> {

		private final EventBus eventBus;
		private final DirectoryComposizione root;
		private final Set<ElementoComposizione> elementiSelezionati;
		private final Button folderBtn;
		private final WidgetDirectory widget;

		@Override
		public void onValueChange(ValueChangeEvent<Boolean> event) {
			widget.holder = null;

			for (AllegatoComposizione elemento : root.getElementi()) {
				if (event.getValue()) {
					elementiSelezionati.add(elemento);
				} else {
					elementiSelezionati.remove(elemento);
				}
				eventBus.fireEvent(new SelezioneElementoEvent(event.getValue(), elemento));
			}
			eventBus.fireEvent(new AggiornaPostSelezioneEvent());

			if (event.getValue()) {
				folderBtn.addStyleName(SELECTION_STYLE);
			} else {
				folderBtn.removeStyleName(SELECTION_STYLE);
			}
		}
	}

	@Getter
	@AllArgsConstructor
	private static class PositionHolder {
		private final DirectoryComposizione root;
		private final Set<ElementoComposizione> elementiSelezionati;
		private final EventBus eventBus;
		private final FascicoloDTO dto;
		private final TabComposizione<?> tab;
	}

}
