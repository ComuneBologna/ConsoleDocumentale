package it.eng.cobo.consolepec.util.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Predicate;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Gson Factory adattata a partire da quella ufficiale: https://github.com/google/gson/blob/master/extras/src/main/java/com/google/gson/typeadapters/RuntimeTypeAdapterFactory.java
 *
 * @author biagiot
 *
 * @param <T>
 */
public final class RuntimeTypeAdapterFactory<T> implements TypeAdapterFactory {

	private final Class<?> baseType;
	private final String typeFieldName;
	private final Map<String, Class<?>> labelToSubtype = new LinkedHashMap<String, Class<?>>();
	private final Map<Class<?>, String> subtypeToLabel = new LinkedHashMap<Class<?>, String>();
	private final boolean maintainType;
	private final Map<Class<? extends T>, ElementConverter> predicates = new LinkedHashMap<Class<? extends T>, ElementConverter>();
	private List<Class<?>> subtypes = new ArrayList<Class<?>>();

	private RuntimeTypeAdapterFactory(Class<?> baseType, String typeFieldName, boolean maintainType) {
		if (baseType == null) {
			throw new NullPointerException("Classe non specificata");
		}
		this.baseType = baseType;
		this.typeFieldName = typeFieldName;
		this.maintainType = maintainType;
	}

	public static <T> RuntimeTypeAdapterFactory<T> of(Class<T> baseType, String typeFieldName, boolean maintainType) {
		return new RuntimeTypeAdapterFactory<T>(baseType, typeFieldName, maintainType);
	}

	public static <T> RuntimeTypeAdapterFactory<T> of(Class<T> baseType) {
		return new RuntimeTypeAdapterFactory<T>(baseType, null, false);
	}

	public static <T> RuntimeTypeAdapterFactory<T> of(Class<T> baseType, String typeFieldName) {
		return new RuntimeTypeAdapterFactory<T>(baseType, typeFieldName, false);
	}

	public static <T> RuntimeTypeAdapterFactory<T> of(Class<T> baseType, String typeFieldName, String label, boolean maintainType) {
		RuntimeTypeAdapterFactory<T> factory = new RuntimeTypeAdapterFactory<T>(baseType, typeFieldName, maintainType);
		return factory.registerType(baseType, label, false);
	}

	public static <T> RuntimeTypeAdapterFactory<T> of(Class<T> baseType, String typeFieldName, String label) {
		RuntimeTypeAdapterFactory<T> factory = new RuntimeTypeAdapterFactory<T>(baseType, typeFieldName, false);
		return factory.registerType(baseType, label, false);
	}

	public RuntimeTypeAdapterFactory<T> registerPredicate(Class<? extends T> type, ElementConverter elementConverter) {
		if (baseType == null) {
			throw new NullPointerException("Classe non specificata");
		}

		if (type == null) {
			throw new NullPointerException("Sottoclasse non specificata");
		}

		if (elementConverter == null) {
			throw new NullPointerException("Predicato non definito");
		}

		if (subtypeToLabel.containsKey(type) || predicates.containsKey(type)) {
			throw new IllegalArgumentException("Sottotipo già registrato");
		}

		predicates.put(type, elementConverter);
		return this;
	}

	public RuntimeTypeAdapterFactory<T> registerType(Class<? extends T> type, String label, boolean registerSubtype) {
		return registerSubtype(type, label, registerSubtype);
	}

	public RuntimeTypeAdapterFactory<T> registerSubtype(Class<? extends T> type, String label, boolean registerSubtype) {
		if (typeFieldName == null) {
			throw new NullPointerException("Valore del campo non specificato");
		}

		if (baseType == null) {
			throw new NullPointerException("Classe non specificata");
		}

		if (type == null) {
			throw new NullPointerException("Sottotipo non specificato");
		}

		if (subtypeToLabel.containsKey(type) || labelToSubtype.containsKey(label) || predicates.containsKey(type)) {
			throw new IllegalArgumentException("Sottotipo già registrato");
		}

		if (registerSubtype && !subtypes.contains(type)) {
			subtypes.add(type);
		}

		labelToSubtype.put(label, type);
		subtypeToLabel.put(type, label);
		return this;
	}

	public RuntimeTypeAdapterFactory<T> registerSubtype(Class<? extends T> type, boolean registerSubtype) {
		return registerSubtype(type, type.getSimpleName(), registerSubtype);
	}

	@Override
	public <R> TypeAdapter<R> create(Gson gson, final TypeToken<R> type) {

		if (type.getRawType() != baseType && (subtypes.isEmpty() || !baseType.isAssignableFrom(type.getRawType()))) {
			return null;
		}

		final Map<String, TypeAdapter<?>> labelToDelegate = new LinkedHashMap<String, TypeAdapter<?>>();
		final Map<Class<?>, TypeAdapter<?>> subtypeToDelegate = new LinkedHashMap<Class<?>, TypeAdapter<?>>();

		for (Entry<Class<? extends T>, ElementConverter> entry : predicates.entrySet()) {
			TypeAdapter<?> delegate = gson.getDelegateAdapter(this, TypeToken.get(entry.getKey()));
			subtypeToDelegate.put(entry.getKey(), delegate);
		}

		for (Map.Entry<String, Class<?>> entry : labelToSubtype.entrySet()) {
			TypeAdapter<?> delegate = gson.getDelegateAdapter(this, TypeToken.get(entry.getValue()));
			labelToDelegate.put(entry.getKey(), delegate);

			if (!subtypeToDelegate.containsKey(entry.getValue()))
				subtypeToDelegate.put(entry.getValue(), delegate);
		}

		return new TypeAdapter<R>() {
			@Override
			public R read(JsonReader in) throws IOException {
				JsonElement jsonElement = Streams.parse(in);

				for (Entry<Class<? extends T>, ElementConverter> entry : predicates.entrySet()) {
					if (entry.getValue().apply(jsonElement.getAsJsonObject())) {
						@SuppressWarnings("unchecked") TypeAdapter<R> delegate = (TypeAdapter<R>) subtypeToDelegate.get(entry.getKey());
						return delegate.fromJsonTree(jsonElement);
					}
				}

				if (typeFieldName == null) {
					throw new JsonParseException("Impossibile deserializzare " + baseType + " ; Specificare un campo:" + typeFieldName);

				}

				JsonElement labelJsonElement;
				if (maintainType) {
					labelJsonElement = jsonElement.getAsJsonObject().get(typeFieldName);
				} else {
					labelJsonElement = jsonElement.getAsJsonObject().remove(typeFieldName);
				}

				if (labelJsonElement == null) {
					throw new JsonParseException("Impossibile deserializzare " + baseType);
				}
				String label = labelJsonElement.getAsString();
				@SuppressWarnings("unchecked") TypeAdapter<R> delegate = (TypeAdapter<R>) labelToDelegate.get(label);
				if (delegate == null) {
					throw new JsonParseException("Impossibile deserializzare " + baseType + " - sottoclasse: " + label);
				}
				return delegate.fromJsonTree(jsonElement);
			}

			@Override
			public void write(JsonWriter out, R value) throws IOException {
				Class<?> srcType = value.getClass();

				@SuppressWarnings("unchecked") TypeAdapter<R> delegate = (TypeAdapter<R>) subtypeToDelegate.get(srcType);
				if (delegate == null) {
					throw new JsonParseException("Impossibile serializzare " + srcType.getName());
				}

				JsonObject jsonObject = delegate.toJsonTree(value).getAsJsonObject();

				if (maintainType || predicates.containsKey(srcType)) {
					Streams.write(jsonObject, out);
					return;
				}

				String label = subtypeToLabel.get(srcType);
				JsonObject clone = new JsonObject();

				if (jsonObject.has(typeFieldName)) {
					throw new JsonParseException("Impossibile serializzare " + srcType.getName() + "; Duplicato campo: " + typeFieldName);
				}
				clone.add(typeFieldName, new JsonPrimitive(label));

				for (Map.Entry<String, JsonElement> e : jsonObject.entrySet()) {
					clone.add(e.getKey(), e.getValue());
				}
				Streams.write(clone, out);
			}
		}.nullSafe();
	}

	public static abstract class ElementConverter implements Predicate<JsonObject> {
		// ~
	}

	public static ElementConverter fieldEquals(final String field, final String... fieldValues) {
		return new ElementConverter() {
			@Override
			public boolean apply(JsonObject json) {
				if (json != null && !json.isJsonNull() && json.has(field) && !json.get(field).isJsonNull() && json.get(field).isJsonPrimitive()) {
					for (String value : fieldValues) {
						if (json.get(field).getAsString().equals(value)) {
							return true;
						}
					}
				}
				return false;
			}
		};
	}

	public static ElementConverter deserDefault() {
		return new ElementConverter() {
			@Override
			public boolean apply(JsonObject json) {
				return true;
			}
		};
	}

	public static ElementConverter hasFields(final String... fields) {
		return new ElementConverter() {
			@Override
			public boolean apply(JsonObject json) {
				for (String field : fields) {
					if (json != null && !json.isJsonNull() && json.has(field))
						return true;
				}
				return false;
			}
		};
	}

}
