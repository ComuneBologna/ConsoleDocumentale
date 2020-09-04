package it.eng.cobo.consolepec.util.json;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.internal.LinkedTreeMap;

import it.eng.cobo.consolepec.commons.spagic.Message.Attachment;

public abstract class JsonConfiguration {

	public abstract void configure(GsonBuilder builder);

	public static class NotSerializableExclusionStrategy implements ExclusionStrategy {

		@Override
		public boolean shouldSkipField(FieldAttributes f) {
			return f.getAnnotation(NotSerializable.class) != null;
		}

		@Override
		public boolean shouldSkipClass(Class<?> clazz) {
			return false;
		}
	}

	public static class MapAttachmentDeserializer implements JsonDeserializer<Map<String, Attachment>> {

		@Override
		public Map<String, Attachment> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

			if (json.isJsonNull()) {
				return context.deserialize(json, typeOfT);
			}

			Map<String, Attachment> map = new LinkedHashMap<>();
			for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) {
				Attachment value = context.deserialize(entry.getValue(), Attachment.class);
				map.put(entry.getKey(), value);
			}

			return map;
		}
	}

	public static class MapDeserializer implements JsonDeserializer<Map<String, Object>> {

		@Override
		@SuppressWarnings("unchecked")
		public Map<String, Object> deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
			if (json.isJsonNull()) {
				return context.deserialize(json, type);
			}

			Map<String, Object> map = new LinkedHashMap<>();
			for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) {
				Object value = context.deserialize(entry.getValue(), Object.class);

				if (value instanceof LinkedTreeMap) {
					LinkedHashMap<String, Object> linkedhashmap = new LinkedHashMap<>();
					linkedhashmap.putAll((Map<? extends String, ? extends Object>) value);
					map.put(entry.getKey(), linkedhashmap);

				} else {
					map.put(entry.getKey(), value);
				}
			}

			return map;
		}
	}
}
