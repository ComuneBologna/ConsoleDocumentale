package it.eng.consolepec.spagicclient.search;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import it.eng.cobo.consolepec.util.date.DateUtils;

public class SearchObjectBuilder {

	private static HashMap<String, String> regexSpecialCharacter = new HashMap<String, String>();

	static {
		regexSpecialCharacter.put("\\\\", "\\\\\\\\");
		regexSpecialCharacter.put("\\^", "\\\\^");
		regexSpecialCharacter.put("\\$", "\\\\$");
		regexSpecialCharacter.put("\\.", "\\\\.");
		regexSpecialCharacter.put("\\|", "\\\\|");
		regexSpecialCharacter.put("\\?", "\\\\?");
		regexSpecialCharacter.put("\\*", "\\\\*");
		regexSpecialCharacter.put("\\+", "\\\\+");
		regexSpecialCharacter.put("\\(", "\\\\(");
		regexSpecialCharacter.put("\\)", "\\\\)");
		regexSpecialCharacter.put("\\[", "\\\\[");
		regexSpecialCharacter.put("\\{", "\\\\{");
	}

	private class SingleQueryCondition {

		private String nameField;
		private Object value;

		public SingleQueryCondition(String nameField, Object value) {
			super();
			this.nameField = nameField;
			this.value = value;
		}
	}

	private LinkedHashMap<String, Object> _mapSearchObject = null;

	private boolean count;

	private SearchObjectBuilder(boolean count) {
		super();
		this._mapSearchObject = new LinkedHashMap<String, Object>();
		this.count = count;
	}

	public static SearchObjectBuilder start() {
		return new SearchObjectBuilder(false);
	}

	public SearchObjectBuilder elemMatch(String fieldName, SearchObjectBuilder... searchObjectBuilders) {
		addOperand("\"" + fieldName + "\" : { \"" + SearchObjectOperator.ELEM_MATCH, searchObjectBuilders);
		return this;
	}

	public SearchObjectBuilder and(SearchObjectBuilder... searchObjectBuilders) {
		addOperand(SearchObjectOperator.AND, searchObjectBuilders);
		return this;
	}

	public SearchObjectBuilder or(SearchObjectBuilder... searchObjectBuilders) {
		addOperand(SearchObjectOperator.OR, searchObjectBuilders);
		return this;
	}

	public SearchObjectBuilder greaterThan(String nameField, Object value) {
		addOperand(SearchObjectOperator.GT, new SingleQueryCondition(nameField, value));
		return this;
	}

	public SearchObjectBuilder greaterThanEquals(String nameField, Object value) {
		addOperand(SearchObjectOperator.GTE, new SingleQueryCondition(nameField, value));
		return this;
	}

	public SearchObjectBuilder lessThan(String nameField, Object value) {
		addOperand(SearchObjectOperator.LT, new SingleQueryCondition(nameField, value));
		return this;
	}

	public SearchObjectBuilder lessThanEquals(String nameField, Object value) {
		addOperand(SearchObjectOperator.LTE, new SingleQueryCondition(nameField, value));
		return this;
	}

	public SearchObjectBuilder in(String nameField, Object... value) {
		addOperand(SearchObjectOperator.IN, new SingleQueryCondition(nameField, Arrays.asList(value)));
		return this;
	}

	public SearchObjectBuilder notIn(String nameField, Object... value) {
		addOperand(SearchObjectOperator.NIN, new SingleQueryCondition(nameField, Arrays.asList(value)));
		return this;
	}

	public SearchObjectBuilder exists(String nameField, Object value) {
		addOperand(SearchObjectOperator.EXISTS, new SingleQueryCondition(nameField, value));
		return this;
	}

	public SearchObjectBuilder eq(String nameField, Object value) {
		addOperand(SearchObjectOperator.EQ, new SingleQueryCondition(nameField, value));
		return this;
	}

	public SearchObjectBuilder size(String nameField, Integer size) {
		addOperand(SearchObjectOperator.SIZE, new SingleQueryCondition(nameField, size));
		return this;
	}

	public SearchObjectBuilder ne(String nameField, Object value) {
		addOperand(SearchObjectOperator.NE, new SingleQueryCondition(nameField, value));
		return this;
	}

	public SearchObjectBuilder like(String nameField, Object value) {
		addOperand(SearchObjectOperator.LIKE, new SingleQueryCondition(nameField, value));
		return this;
	}

	public SearchObjectBuilder likeIngnoreCase(String nameField, Object value) {
		addOperand(SearchObjectOperator.LIKE_IGNORE_CASE, new SingleQueryCondition(nameField, value));
		return this;
	}

	public boolean isCount() {
		return count;
	}

	private void addOperand(String operator, Object value) {
		_mapSearchObject.put(operator + "_" + _mapSearchObject.size(), value);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String operator : _mapSearchObject.keySet()) {
			serializeMultiple(operator.split("_")[0], _mapSearchObject.get(operator), sb);
			serializeSingle(operator.split("_")[0], _mapSearchObject.get(operator), sb);
		}
		sb.setLength(sb.length() - 3);
		return sb.toString();
	}

	private void serializeSingle(String operator, Object object, StringBuilder sb) {

		if (!operator.equalsIgnoreCase(SearchObjectOperator.AND) && !operator.equalsIgnoreCase(SearchObjectOperator.OR) && !operator.contains(SearchObjectOperator.ELEM_MATCH)) {

			if (object == null || ((object instanceof String) && (((String) object).equals(""))))
				return;

			SingleQueryCondition singleQueryCondition = (SingleQueryCondition) object;

			String value = getValue(singleQueryCondition.value, operator);

			if (!operator.equalsIgnoreCase(SearchObjectOperator.LIKE) && !operator.equalsIgnoreCase(SearchObjectOperator.LIKE_IGNORE_CASE))
				sb.append("{ \"").append(singleQueryCondition.nameField).append("\" : { \"").append(operator).append("\" : ").append(value).append(" }} ");
			else {
				sb.append("{ \"").append(singleQueryCondition.nameField).append("\" : { \"$regex\" :  \"^.*").append(replaceRegexSpecialCharacters(value)).append(".*$\"");

				if (operator.equalsIgnoreCase(SearchObjectOperator.LIKE_IGNORE_CASE)) {
					sb.append(", \"$options\" : \"i\"");
				}

				sb.append("}}");
			}
			sb.append(" , ");
		}

	}

	private void serializeMultiple(String operator, Object value, StringBuilder sb) {

		if (operator.equalsIgnoreCase(SearchObjectOperator.AND) || operator.equalsIgnoreCase(SearchObjectOperator.OR)) {

			sb.append("{ \"").append(operator).append("\" : [ ");

			List<SearchObjectBuilder> searchObjectBuilders = Arrays.asList((SearchObjectBuilder[]) value);
			String comma = "";
			for (SearchObjectBuilder searchObjectBuilder : searchObjectBuilders) {
				sb.append(comma);
				comma = " , ";
				sb.append(searchObjectBuilder.toString());
			}
			sb.append(" ]} , ");
		} else if (operator.contains(SearchObjectOperator.ELEM_MATCH)) {
			sb.append("{").append(operator).append("\" : {");

			List<SearchObjectBuilder> searchObjectBuilders = Arrays.asList((SearchObjectBuilder[]) value);
			String comma = "";
			for (SearchObjectBuilder searchObjectBuilder : searchObjectBuilders) {
				sb.append(comma);
				comma = " , ";
				sb.append(new StringBuilder(searchObjectBuilder.toString().trim()).deleteCharAt(searchObjectBuilder.toString().trim().length()-1).deleteCharAt(0));
			}
			sb.append(" }}} , ");
		}
	}

	private String getValue(Object value, String operator) {

		if (operator.equals(SearchObjectOperator.IN) || operator.equals(SearchObjectOperator.NIN)) {
			StringBuilder sb = new StringBuilder(" [");

			@SuppressWarnings("unchecked")
			List<Object> listObjects = (List<Object>) value;

			String comma = "";
			for (Object obj : listObjects) {
				sb.append(comma);
				comma = ",";
				sb.append(" ").append(toStringValue(obj)).append(" ");
			}

			sb.append("] ");
			return sb.toString();

		}

		if (operator.equalsIgnoreCase(SearchObjectOperator.LIKE) || operator.equalsIgnoreCase(SearchObjectOperator.LIKE_IGNORE_CASE))
			return value.toString();

		return toStringValue(value);
	}

	private String toStringValue(Object value) {

		if (value instanceof Date)
			return new StringBuilder(" \"").append(DateUtils.DATEFORMAT_ISO8601.format(value)).append("\" ").toString();
		if (value instanceof String)
			return new StringBuilder(" \"").append(value.toString()).append("\" ").toString();
		if (value instanceof Boolean)
			return new StringBuilder(" ").append(value.toString()).append(" ").toString();
		if (value instanceof Number)
			return new StringBuilder(" ").append(value.toString()).append(" ").toString();
		if (value == null)
			return new StringBuilder(" ").append("null").append(" ").toString();
		
		throw new IllegalStateException("type not allowed");
	}

	private String replaceRegexSpecialCharacters(String value) {
		for (Entry<String, String> specialChar : regexSpecialCharacter.entrySet()) {
			value = value.replaceAll(specialChar.getKey(), specialChar.getValue());
		}
		return value;
	}

}
