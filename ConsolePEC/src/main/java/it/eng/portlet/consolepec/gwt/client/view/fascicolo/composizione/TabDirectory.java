package it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione;

import static it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.DirectoryComposizione.SEPARATOR;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.AllegatoComposizione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.DirectoryComposizione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.ElementoComposizione;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.interna.WidgetDirectory;

/**
 * @author GiacomoFM
 * @since 2019-03-22
 */
public class TabDirectory extends TabComposizione<DirectoryComposizione> {

	public static final String NAME = "Cartelle";

	@Override
	String getTabName() {
		return NAME;
	}

	public TabDirectory(EventBus eventBus) {
		super(eventBus);
	}

	@Override
	protected List<DirectoryComposizione> getList() {
		List<DirectoryComposizione> list = new ArrayList<>();
		DirectoryComposizione root = new DirectoryComposizione(SEPARATOR, null);
		for (AllegatoComposizione allegato : dto.getComposizioneAllegati()) {
			String path = allegato.getFolderOriginPath() == null ? SEPARATOR : allegato.getFolderOriginPath();
			getPath(root, getOrderedPath(path)).getElementi().add(allegato);
		}
		list.add(root);
		return list;
	}

	private static LinkedList<String> getOrderedPath(String path) {
		LinkedList<String> list = new LinkedList<>();
		for (String p : path.split(SEPARATOR)) {
			if (!p.isEmpty()) {
				list.add(p);
			}
		}
		return list;
	}

	private static DirectoryComposizione getPath(DirectoryComposizione root, LinkedList<String> orderedPath) {
		if (orderedPath.isEmpty())
			return root;
		String subdirPath = orderedPath.poll();
		for (DirectoryComposizione subDir : root.getSubDirectories()) {
			if (subDir.getPath().endsWith(SEPARATOR + subdirPath)) {
				return getPath(subDir, orderedPath);
			}
		}
		DirectoryComposizione subdir = //
				new DirectoryComposizione((root.getPath().endsWith(SEPARATOR) ? root.getPath() + subdirPath : root.getPath() + SEPARATOR + subdirPath), root);
		root.getSubDirectories().add(subdir);
		return getPath(subdir, orderedPath);
	}

	@Override
	protected Widget drawWidget(DirectoryComposizione elemento, Set<ElementoComposizione> elementiSelezionati) {
		return new WidgetDirectory(elemento, elementiSelezionati, eventBus, dto, this);
	}

	@Override
	public void filtraRicerca(Set<?> search) {
		// TODO ?
	}

	@Override
	protected void drawPaging(HTMLPanel panel, List<DirectoryComposizione> data) {
		// ~ non in questo tab
	}

}
