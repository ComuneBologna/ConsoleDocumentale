package it.eng.cobo.consolepec.util.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreMultiplo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.TipoDato;
import it.eng.cobo.consolepec.util.date.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author GiacomoFM
 * @since 14/set/2017
 */
public class JsonRawFactoryTest {

	private static final Integer NUMBER = 156;
	private static final String STRING = "Mock";
	private static final Double D1 = 12.56d;
	private static final Double D2 = 13.58d;
	private static final Date DATE = new Date(768065101000L);
	private static final Long L1 = (long) 100;
	private static final long L2 = 120;
	private static final BigInteger B1 = new BigInteger("300");

	private static final BeanMockup bean = new BeanMockup(NUMBER, D1, D2, L1, L2, STRING, DATE, B1);

	@Test
	public void testLong() {
		long l = 100;

		JsonRawFactory j = new JsonRawFactory();
		String s = j.serialize(l);

		long res = j.deserialize(s);

		Assert.assertEquals(res, 100);
	}

	@Test(expected = NullPointerException.class)
	public void serializationNull() {
		JsonRawFactory js = new JsonRawFactory();
		js.serialize(null);
	}

	@Test(expected = NullPointerException.class)
	public void deserializationNull() {
		JsonRawFactory js = new JsonRawFactory();
		js.deserialize(null);
	}

	@Test
	public void serialization() {
		JsonRawFactory js = new JsonRawFactory();
		assertNotNull(js);

		js.serialize(bean);
	}

	@Test
	public void serializeBoolean() {
		JsonRawFactory js = new JsonRawFactory();
		String s = js.serialize(true);
		Boolean b = js.deserialize(s);
		assertTrue(b);
	}

	@Test
	public void serializeString() {
		JsonRawFactory js = new JsonRawFactory();
		String s = js.serialize("ciao");
		String ciao = js.deserialize(s);

		assertEquals("ciao", ciao);
	}

	@Test
	public void deserializzaDataSpecifica() {
		// 2017-12-21T16:54:36.000+0100
		DateFormat df = DateUtils.DATEFORMAT_ISO8601_3; // MEH.. Non che vada proprio bene eh..
		Date dataSpecifica = null;
		try {
			dataSpecifica = df.parse("2017-12-21T15:54:36.000Z");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JsonRawFactory js = new JsonRawFactory();
		String json = "{\"clazz\": \"it.eng.cobo.consolepec.util.json.JsonRawFactoryTest$OggettoDataSpecifica\",\"date\": \"date:2017-12-21T16:54:36.000+0100\"}";
		OggettoDataSpecifica res = js.deserialize(json);

		assertEquals(dataSpecifica, res.getDate());
	}

	@Data
	@NoArgsConstructor
	public static class OggettoDataSpecifica {
		private Date date;
	}

	@Test
	public void serializeMap() {
		JsonRawFactory js = new JsonRawFactory();
		Map<B, A> map = new HashMap<>();

		map.put(new B(), new C());
		map.put(new B(), new B());
		String s = js.serialize(map);

		Map<B, A> map2 = js.deserialize(s);
		assertEquals(map, map2);

		Map<String, Double> map3 = new HashMap<>();
		map3.put("ciao", 1d);
		map3.put("ciao2", 2d);

		s = js.serialize(map3);

		Map<String, Double> map4 = js.deserialize(s);
		assertEquals(map3, map4);
	}

	@Test
	public void serializeList() {
		JsonRawFactory js = new JsonRawFactory();
		List<A> as = new ArrayList<A>();

		as.add(new B());
		as.add(new C());

		String s = js.serialize(as);

		List<A> as2 = js.deserialize(s);
		assertEquals(as, as2);
	}

	@Test
	public void serializeDouble() {
		JsonRawFactory js = new JsonRawFactory();
		String s = js.serialize(1.5d);
		double d = js.deserialize(s);
		assertTrue(d == 1.5d);
	}

	@Test
	public void serializeArray() {
		JsonRawFactory js = new JsonRawFactory();

		A[] as = new A[] { new B(), new C() };
		String s = js.serialize(as);
		A[] as2 = js.deserialize(s);
		assertEquals(as[0], as2[0]);
		assertEquals(as[1], as2[1]);
	}

	@Test
	public void deserialization() {
		JsonRawFactory js = new JsonRawFactory();
		assertNotNull(js);

		String json = js.serialize(bean);

		BeanMockup b = js.deserialize(json);
		assertNotNull(b);
		assertEquals(b.num, NUMBER);
		assertEquals(b.d1, D1);
		assertTrue(b.d2 == D2);
		assertEquals(b.num, NUMBER);
		assertEquals(b.str, STRING);
		assertEquals(b.date, DATE);
		assertEquals(b.l1, L1);
		assertEquals(b.l2, L2);
		assertEquals(b.b1, B1);
		assertEquals(b, bean);
	}

	@Test
	public void testPolymorhpic() {
		JsonRawFactory js = new JsonRawFactory();
		A a = new B();
		String s = js.serialize(a);

		B b = js.deserialize(s);
		assertEquals(b.name(), "b");
	}

	@Test
	public void testHashMapEmptyArray() {
		String json = "{\"arraySerialization\":true,\"clazz\":\"java.lang.Object\"," + "\"values\":[{\"mapSerialization\":true,\"clazz\":\"java.util.HashMap\",\"entries\":[]},\"i:0\",\"i:0\"]}";
		JsonRawFactory js = new JsonRawFactory();
		assertNotNull(js.deserialize(json));
	}

	@Test
	public void testSerializationSerializableObject() {
		SerializableMockup mock = new SerializableMockup("testo");
		JsonRawFactory js = new JsonRawFactory();
		String json = js.serialize(mock);
		assertNotNull(json);

		SerializableMockup demock = js.deserialize(json);
		assertNotNull(demock);
		assertEquals("testo", demock.getTesto());
	}

	@Test
	public void testEnum() {
		EnumClass ec = new EnumClass();
		ec.setEnumz(EnumClass.EnumTest.TEST);

		JsonRawFactory js = new JsonRawFactory();
		String json = js.serialize(ec);
		assertEquals(EnumClass.EnumTest.TEST, ((EnumClass) js.deserialize(json)).getEnumz());
	}

	@Test
	public void testDatoAggiuntivoValoreMultiplo() {
		List<String> testList = new ArrayList<String>();
		testList.add("primo");
		testList.add("secondo");
		DatoAggiuntivoValoreMultiplo datoAggiuntivoValoreMultiplo = new DatoAggiuntivoValoreMultiplo("nomeM", "descrizione M", TipoDato.MultiploTesto, 100, true, false, false, testList, testList);
		JsonRawFactory js = new JsonRawFactory();
		String json = js.serialize(datoAggiuntivoValoreMultiplo);
		Assert.assertNotNull(json);

		datoAggiuntivoValoreMultiplo = js.deserialize(json);
		Assert.assertNotNull(datoAggiuntivoValoreMultiplo);
	}

	@Test
	public void testAnagraficaFascicolo() {
		AnagraficaFascicolo a = new AnagraficaFascicolo();
		a.setNomeTipologia("TEST_ANAGRAFICA");
		a.setEtichettaTipologia("TEST ANAGRAFICA SERIALIZZAZIONE");

		JsonRawFactory js = new JsonRawFactory();
		String json = js.serialize(a);
		Assert.assertNotNull(json);
		a = js.deserialize(json);
		Assert.assertNotNull(a);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testComplexEnum() {
		List<EnumClass> list = new ArrayList<JsonRawFactoryTest.EnumClass>();
		EnumClass e = new EnumComplexA();
		e.setEnumz(EnumClass.EnumTest.TEST);
		list.add(e);

		e = new EnumComplexA();
		e.setEnumz(EnumClass.EnumTest.CIAO);
		list.add(e);

		JsonRawFactory js = new JsonRawFactory();
		String json = js.serialize(list);
		assertEquals(EnumClass.EnumTest.CIAO, ((List<EnumClass>) js.deserialize(json)).get(1).getEnumz());
	}

	@NoArgsConstructor
	public static class EnumComplexA extends EnumClass {
		@Getter
		@Setter
		private String uffa;
	}

	@Test
	public void testComplexObject() {
		JsonRawFactory js = new JsonRawFactory();
		ComplexObject object = new ComplexObject();
		object.setA(new B("propB"));
		object.getAs().addAll(Arrays.asList(new B("listB"), new C("listC")));
		object.getSet().addAll(Arrays.asList("A1", "B2"));
		object.getListSet().add(Arrays.asList("a", "b"));

		String s = js.serialize(object);

		ComplexObject object2 = js.deserialize(s);
		assertEquals(object, object2);

		assertEquals(object2.getA().name(), "b");
		assertEquals(object2.getA().getProp(), "propB");
		assertEquals(object2.getAs().get(0).getProp(), "listB");
		assertEquals(object2.getAs().get(1).getProp(), "listC");
		assertEquals(object2.getSet(), new HashSet<String>(Arrays.asList("A1", "B2")));
		assertEquals(object2.getListSet().iterator().next().get(1), "b");
	}

	@Test
	public void testOnNull() {
		JsonRawFactory js = new JsonRawFactory();
		BeanMockup o = new BeanMockup();
		String json = js.serialize(o);

		BeanMockup res = js.deserialize(json);
		assertEquals(o, res);
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@EqualsAndHashCode
	public static class BeanMockup {
		private Integer num;
		private Double d1;
		private double d2;
		private Long l1;
		private long l2;
		private String str;
		private Date date;
		private BigInteger b1;
	}

	public static interface A {
		public String getProp();

		public String name();
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class B implements A {
		private String prop;

		@Override
		public String name() {
			return "b";
		}

	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class C implements A {
		private String prop;

		@Override
		public String name() {
			return "c";
		}
	}

	@EqualsAndHashCode
	@Data
	public static class ComplexObject {
		private A a;
		private List<A> as = new ArrayList<>();
		private Set<String> set = new HashSet<>();
		private Set<List<String>> listSet = new HashSet<>();
	}

	@Data
	@NoArgsConstructor
	public static class EnumClass {
		public enum EnumTest {
			TEST, CIAO
		}

		private EnumTest enumz;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SerializableMockup implements Serializable {
		private static final long serialVersionUID = -404520154538498780L;
		private String testo;
	}

}
