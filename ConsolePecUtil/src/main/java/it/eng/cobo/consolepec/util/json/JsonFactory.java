package it.eng.cobo.consolepec.util.json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/*
 * 
 */
public class JsonFactory {

	private final Gson gson;
	private static Map<String, JsonFactory> map = new HashMap<>();
	
	public static synchronized JsonFactory defaultFactory() {
		return jsonFactory(ConsolePecJsonConfiguration.class);
	}
	
	public static synchronized JsonFactory jsonFactory(Class<? extends JsonConfiguration> clazz) {
		
		if (map.get(clazz.getSimpleName()) == null) {
			
			try {
				map.put(clazz.getSimpleName(), new JsonFactory(clazz.newInstance()));
				
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		
		return map.get(clazz.getSimpleName());
	}

	private JsonFactory(JsonConfiguration configuration) {
		GsonBuilder builder = new GsonBuilder();
		configuration.configure(builder);
		gson = builder.setPrettyPrinting().create();
	}

	public synchronized String serialize(Object src) {
		return gson.toJson(src);
	}

	public synchronized <T> T deserialize(String json, Class<T> classOfType) {
		return gson.fromJson(json, classOfType);
	}

	public synchronized <T> List<T> deserializeList(String json, Class<T> classOfType) {
		return gson.fromJson(json, new ListOfJson<>(classOfType));
	}
}
