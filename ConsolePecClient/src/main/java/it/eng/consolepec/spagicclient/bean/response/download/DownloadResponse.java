package it.eng.consolepec.spagicclient.bean.response.download;

import it.eng.consolepec.spagicclient.bean.response.Response;
import lombok.Getter;
import lombok.Setter;

public class DownloadResponse implements Response {

	@Getter
	@Setter
	private String desc, code, ref_in_store, doc_name;
}
