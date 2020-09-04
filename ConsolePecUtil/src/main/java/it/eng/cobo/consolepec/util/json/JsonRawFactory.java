package it.eng.cobo.consolepec.util.json;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import lombok.NonNull;

/*
 * factory che serializza e deserializza senza bisogno di configurazioni. Il JSON è molto meno leggibile per questo non va utilizzatoper la serializzazione sul database, ma solo nella parte servizi
 */
public final class JsonRawFactory {

	private static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat(ConsoleConstants.FORMATO_ISO8601);
		}
	};

	public JsonRawFactory() {
		super();
	}

	public String serialize(@NonNull final Object src) {
		try (StringWriter writer = new StringWriter(); JsonWriter jsonWriter = new JsonWriter(writer)) {
			internalWrite(jsonWriter, src);
			return writer.toString();
		} catch (IOException e) {
			throw new JsonParseException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T deserialize(@NonNull final String json) {
		try (JsonReader reader = new JsonReader(new StringReader(json))) {
			Object token = calculateToken(reader);
			return (T) internalRead(token, null);
		} catch (IOException e) {
			throw new JsonParseException(e);
		}
	}

	private void internalWrite(JsonWriter out, Object value) {
		try {
			if (value == null) {
				out.nullValue();
				return;
			}
			if (is(value, String.class)) {
				out.value((String) value);
				return;
			}
			if (is(value, boolean.class, Boolean.class)) {
				out.value((boolean) value);
				return;
			}
			if (is(value, Integer.class, int.class)) {
				out.value("i:" + value.toString());
				return;
			}
			if (is(value, Long.class, long.class)) {
				out.value("l:" + value.toString());
				return;
			}
			if (is(value, Float.class, float.class)) {
				out.value("f:" + value.toString());
				return;
			}
			if (is(value, Double.class, double.class)) {
				out.value("d:" + value.toString());
				return;
			}
			if (is(value, Date.class)) {
				out.value("date:" + df.get().format((Date) value));
				return;
			}
			if (is(value, BigInteger.class)) {
				out.value("bi:" + value.toString());
				return;
			}

			if (value.getClass().isEnum()) {
				out.beginObject();
				out.name("clazz").value(value.getClass().getName());
				out.name("value").value(((Enum<?>) value).name());
				out.endObject();
				return;
			}

			// Gestione ENUM complessi
			if (value.getClass().getSuperclass() != null && value.getClass().getSuperclass().getSuperclass() == Enum.class) {
				out.beginObject();
				out.name("clazz").value(value.getClass().getSuperclass().getName());
				out.name("value").value(((Enum<?>) value).name());
				out.endObject();
				return;
			}

			if (value.getClass().isArray()) {
				// TODO da fixare: se l'array è di tipo primitivo (es. int[] il cast Object[]) value non funziona
				out.beginObject();
				out.name("arraySerialization").value(true);
				out.name("clazz").value(value.getClass().getComponentType().getName());
				out.name("values");
				out.beginArray();
				for (Object obj : (Object[]) value) {
					internalWrite(out, obj);
				}

				out.endArray();
				out.endObject();
				return;
			}

			if (isSubclass(value.getClass(), Iterable.class)) {

				String className = resolveCollectionType(value.getClass());

				out.beginObject();
				out.name("iterableSerialization").value(true);
				out.name("clazz").value(className);
				out.name("values");
				out.beginArray();
				for (Object obj : ((Iterable<?>) value)) {
					internalWrite(out, obj);
				}

				out.endArray();
				out.endObject();

				return;
			}
			if (isSubclass(value.getClass(), Map.class)) {
				String className = resolveCollectionType(value.getClass());

				out.beginObject();
				out.name("mapSerialization").value(true);
				out.name("clazz").value(className);
				out.name("entries");
				out.beginArray();

				for (Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
					out.beginObject();

					Object entryKey = entry.getKey();
					out.name("key");
					internalWrite(out, entryKey);

					Object entryValue = entry.getValue();
					out.name("value");
					internalWrite(out, entryValue);

					out.endObject();

				}
				out.endArray();
				out.endObject();
				return;
			}

			out.beginObject();
			out.name("clazz").value(value.getClass().getName());

			Map<String, Member> properties = createFieldMap(value.getClass());

			for (Method getter : value.getClass().getDeclaredMethods()) {
				if (getter.getName().startsWith("get") && getter.isAccessible()) { // TODO: BOOLEAN IS
					properties.put(Character.toLowerCase(getter.getName().charAt(3)) + getter.getName().substring(4), getter);
				}
			}

			for (Entry<String, Member> e : properties.entrySet()) {
				Object o = null;
				if (e.getValue() instanceof Field) {
					((Field) e.getValue()).setAccessible(true);
					o = ((Field) e.getValue()).get(value);
				}
				if (e.getValue() instanceof Method) {
					o = ((Method) e.getValue()).invoke(value);
				}

				// key
				out.name(e.getKey());
				// value
				internalWrite(out, o);

			}
			out.endObject();

		} catch (Exception e) {
			throw new JsonParseException(e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object internalRead(Object token, Class<?> type) {
		try {

			if (token == null) {
				return null;
			}

			if (token instanceof Map) {
				Map<String, Object> jsonObject = (Map<String, Object>) token;

				// simple object
				if (jsonObject.containsKey("clazz")) {
					String className = (String) jsonObject.get("clazz");
					type = Class.forName(className);
				}

				if (jsonObject.containsKey("arraySerialization")) {
					type = Array.class;
				}
			}

			if (type == null) {// proviamo ad identificare il tipo per primo livello di deserializzazione
				if (token instanceof String) {
					String sToken = (String) token;

					if (sToken.startsWith("date:")) {
						type = Date.class;
					} else if (sToken.startsWith("i:")) {
						type = Integer.class;
					} else if (sToken.startsWith("l:")) {
						type = Long.class;
					} else if (sToken.startsWith("f:")) {
						type = Float.class;
					} else if (sToken.startsWith("d:")) {
						type = Double.class;
					} else if (sToken.startsWith("bi:")) {
						type = BigInteger.class;
					} else {
						type = String.class;
					}

				}
				if (token instanceof Boolean) type = boolean.class;
				if (token instanceof Double) type = double.class;

				if (type == null) {
					throw new IllegalArgumentException("Tipo non gestibile sul primo livello: " + token.getClass().getSimpleName());
				}

			}

			if (is(type, String.class)) {
				return token;
			}
			if (is(type, Date.class)) {
				return df.get().parse(((String) token).substring(5));
			}
			if (is(type, boolean.class, Boolean.class)) {
				return (boolean) token;
			}
			if (is(type, Integer.class, int.class)) {
				return Integer.parseInt(((String) token).substring(2));
			}
			if (is(type, Long.class, long.class)) {
				return Long.parseLong(((String) token).substring(2));
			}
			if (is(type, Float.class, float.class)) {
				return Float.parseFloat(((String) token).substring(2));
			}
			if (is(type, Double.class, double.class)) {
				return Double.parseDouble(((String) token).substring(2));
			}
			if (is(type, BigInteger.class)) {
				return new BigInteger(((String) token).substring(3));
			}

			if (isSubclass(type, Enum.class)) {
				Map<String, Object> jsonObject = (Map<String, Object>) token;
				String className = (String) jsonObject.get("clazz");
				Class<Enum> enumType = (Class<Enum>) Class.forName(className);
				return Enum.valueOf(enumType, (String) jsonObject.get("value"));
			}

			if (is(type, Array.class)) {

				Map<String, Object> jsonObject = (Map<String, Object>) token;
				String className = (String) jsonObject.get("clazz");
				Class<?> arrayType = Class.forName(className);

				List<Object> values = (List<Object>) jsonObject.get("values");

				Object[] array = (Object[]) Array.newInstance(arrayType, values.size());

				for (int i = 0; i < values.size(); i++) {
					array[i] = internalRead(values.get(i), null);
				}
				return array;
			}

			// json iterable
			if (isSubclass(type, Iterable.class)) {
				Map<String, Object> jsonObject = (Map<String, Object>) token;
				List<Object> values = (List<Object>) jsonObject.get("values");

				if (isSubclass(type, List.class)) {
					List<Object> l = createCollectionInstance(type);
					for (Object o : values) {
						l.add(internalRead(o, null));
					}

					return l;
				}
				if (isSubclass(type, Set.class)) {
					Set<Object> s = createCollectionInstance(type);
					for (Object o : values) {
						s.add(internalRead(o, null));
					}
					return s;
				}
			}

			// json jsonMap
			if (isSubclass(type, Map.class)) {
				Map<String, Object> jsonObject = (Map<String, Object>) token;
				List<Object> entries = (List<Object>) jsonObject.get("entries");

				Map<Object, Object> map = createCollectionInstance(type);

				for (Object entry : entries) {
					Object key = ((Map<?, ?>) entry).get("key");
					Object value = ((Map<?, ?>) entry).get("value");

					map.put(internalRead(key, null), internalRead(value, null));
				}
				return map;
			}

			// json object
			Map<String, Object> jsonObject = (Map<String, Object>) token;

			Constructor<?> c = null;
			try {
				c = type.getDeclaredConstructor(new Class[] {});
				c.setAccessible(true);
			} catch (NoSuchMethodException ex) {
				throw new IllegalArgumentException("Costruttore vuoto non definito: " + type.getSimpleName());
			}
			Object instance = c.newInstance();

			// da mettere in cache
			Map<String, Member> properties = createFieldMap(type);

			for (Method setter : type.getDeclaredMethods()) {
				if (setter.getName().startsWith("set") && setter.isAccessible()) {
					properties.put(Character.toLowerCase(setter.getName().charAt(3)) + setter.getName().substring(4), setter);
				}
			}

			for (Entry<String, Member> e : properties.entrySet()) {

				if (e.getValue() instanceof Field) {
					Field field = ((Field) e.getValue());
					Object value = internalRead(jsonObject.get(e.getKey()), field.getType());
					field.setAccessible(true);
					field.set(instance, value);
				}
				if (e.getValue() instanceof Method) {
					Method method = ((Method) e.getValue());
					Object value = internalRead(jsonObject.get(e.getKey()), method.getReturnType());
					method.invoke(instance, value);
				}
			}

			return instance;
		} catch (Exception e) {
			throw new JsonParseException(e);
		}
	}

	private Object calculateToken(JsonReader in) {
		try {
			while (in.hasNext()) {
				JsonToken nextToken = in.peek();

				if (JsonToken.NULL.equals(nextToken)) {
					in.nextNull();
					return null;
				}
				if (JsonToken.STRING.equals(nextToken)) {
					return in.nextString();
				}
				if (JsonToken.BOOLEAN.equals(nextToken)) {
					return in.nextBoolean();
				}
				if (JsonToken.NUMBER.equals(nextToken)) {
					return in.nextDouble();
				}
				if (JsonToken.BEGIN_ARRAY.equals(nextToken)) {
					List<Object> objects = new ArrayList<>();
					in.beginArray();
					nextToken = in.peek();
					while (!JsonToken.END_ARRAY.equals(nextToken)) {
						Object object = calculateToken(in);
						objects.add(object);
						nextToken = in.peek();
					}

					in.endArray();
					return objects;
				}

				if (JsonToken.BEGIN_OBJECT.equals(nextToken)) {
					Map<String, Object> jsonObject = new HashMap<String, Object>();

					in.beginObject();
					nextToken = in.peek();
					while (JsonToken.NAME.equals(nextToken)) {
						String name = in.nextName();
						Object value = calculateToken(in);
						jsonObject.put(name, value);
						nextToken = in.peek();
					}

					in.endObject();

					return jsonObject;
				}
			}

			throw new IllegalStateException("Cannot parse" + in.getPath());

		} catch (Exception e) {
			throw new JsonParseException(e);
		}
	}

	private static Map<String, Member> createFieldMap(Class<?> clazz) {
		Map<String, Member> fieldMap = new LinkedHashMap<>();

		while (clazz != null) {
			for (Field f : clazz.getDeclaredFields()) {
				if (Modifier.isStatic(f.getModifiers()) || Modifier.isTransient(f.getModifiers())) continue;
				fieldMap.put(f.getName(), f);
			}
			clazz = clazz.getSuperclass();
		}

		return fieldMap;
	}

	private static boolean is(Object o, Class<?>... cz) {
		for (Class<?> c : cz) {
			if (c.equals(o.getClass()) || c.equals(o)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isSubclass(Class<?> subclass, Class<?> superClass) {
		return superClass.isAssignableFrom(subclass);
	}

	private static String resolveCollectionType(Class<? extends Object> c) {

		// list
		if (is(c, ArrayList.class) || is(c, LinkedList.class)) return c.getName();
		if (isSubclass(c, List.class)) return ArrayList.class.getName(); // caso List generico

		// set
		if (is(c, HashSet.class) || is(c, TreeSet.class) || is(c, LinkedHashSet.class)) return c.getName();
		if (isSubclass(c, Set.class)) return LinkedHashSet.class.getName(); // caso Set generico

		// map
		if (is(c, HashMap.class) || is(c, LinkedHashMap.class) || is(c, TreeMap.class)) return c.getName();
		if (isSubclass(c, Map.class)) return LinkedHashMap.class.getName(); // caso Map generico

		throw new IllegalArgumentException("Tipo non supportato: " + c.getName());
	}

	@SuppressWarnings("unchecked")
	private static <T> T createCollectionInstance(Class<? extends Object> c) {

		// list
		if (is(c, ArrayList.class)) return (T) new ArrayList<>();
		if (is(c, LinkedList.class)) return (T) new LinkedList<>();

		// set
		if (is(c, HashSet.class)) return (T) new HashSet<>();
		if (is(c, TreeSet.class)) return (T) new TreeSet<>();
		if (is(c, LinkedHashSet.class)) return (T) new LinkedHashSet<>();
		if (isSubclass(c, Set.class)) return (T) new LinkedHashSet<>(); // caso Set generico

		// map
		if (is(c, HashMap.class)) return (T) new HashMap<>();
		if (is(c, LinkedHashMap.class)) return (T) new LinkedHashMap<>();
		if (is(c, TreeMap.class)) return (T) new TreeMap<>();
		if (isSubclass(c, Map.class)) return (T) new LinkedHashMap<>();

		throw new IllegalArgumentException("Tipo non supportato: " + c.getName());
	}

}
