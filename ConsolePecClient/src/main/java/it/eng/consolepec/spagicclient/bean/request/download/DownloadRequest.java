package it.eng.consolepec.spagicclient.bean.request.download;

import it.eng.consolepec.spagicclient.bean.request.Request;
import lombok.Getter;
import lombok.Setter;

public class DownloadRequest implements Request {
	@Getter
	@Setter
	private String path, uuid;
}
