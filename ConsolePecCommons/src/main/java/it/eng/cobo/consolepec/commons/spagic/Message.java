package it.eng.cobo.consolepec.commons.spagic;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Message {

	private String bodyText;
	private Map<String, Attachment> attachments = new HashMap<>();

	public void addAttachment(String id, InputStream stream) {
		this.attachments.put(id, new Attachment(stream));
	}

	public InputStream getAttachment(String id) {
		return this.attachments.get(id) != null ? this.attachments.get(id).getInputStream() : null;
	}

	public void removeAttachment(String id) {
		if (attachments.get(id) != null)
			attachments.remove(id);
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Attachment {
		private InputStream inputStream;

	}
}
