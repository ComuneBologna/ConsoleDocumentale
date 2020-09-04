package it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle;

import it.eng.cobo.consolepec.util.console.ConsoleConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.common.base.Strings;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.SuggestOracle;

/**
 *
 * @author biagiot
 *
 */
public class TimePickerSuggestOracle extends SuggestOracle {

	private final long PARTIAL_INTERVAL = 60 * 1000L;
    private final long DAY = 24 * 60 * 60 * 1000L;
    private final int MAX_ELEMENTS = 6;
    private final static String TIME24HOURS_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]";

    private List<String> values1 = new ArrayList<String>();
    private List<String> values15 = new ArrayList<String>();
    private List<String> values60 = new ArrayList<String>();

    public TimePickerSuggestOracle() {
    	initalizeValues(1);
    	initalizeValues(15);
    	initalizeValues(60);
    }

    @Override
	public void requestSuggestions(Request request, Callback callback) {
		String query = request.getQuery().toUpperCase();
		ArrayList<Suggestion> suggestions = new ArrayList<SuggestOracle.Suggestion>();
		SuggestOracle.Response response = new SuggestOracle.Response();
		List<String> values = getValues(query);

		for (final String value : values) {

			if (checkForSuggestLike(query, value) && suggestions.size() < MAX_ELEMENTS) {

				Suggestion s = new Suggestion() {

					@Override
					public String getReplacementString() {
						return value;
					}

					@Override
					public String getDisplayString() {
						return value;
					}
				};

				suggestions.add(s);
			}
		}

		response.setSuggestions(suggestions);
		callback.onSuggestionsReady(request, response);
	}

    private boolean checkForSuggestLike(String query, String value) {
    	return " ".equalsIgnoreCase(query) ||
    			value.toLowerCase().startsWith(query.toLowerCase().trim());
    }

    private void initalizeValues(int intervalMinutes) {
    	List<String> values = new ArrayList<String>();

    	if (intervalMinutes == 1)
    		values = values1;
    	else if (intervalMinutes == 15)
    		values = values15;
    	else if(intervalMinutes == 60)
    		values = values60;

    	values.clear();

    	long interval = intervalMinutes * PARTIAL_INTERVAL;
    	int numOptions = (int) (DAY / interval);

		for (int i = 0; i < numOptions; i++) {
			long time = i * interval;
			DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_TIME);
			values.add(dateTimeFormat.format(new Date(time)));
        }
    }

    private List<String> getValues(String query) {
    	if (" ".equalsIgnoreCase(query))
			return values60;
		else if (query.length() <= 2)
			return values15;
		else
			return values1;
    }

    public static Integer getHour(String timeSuggestOracle) {
    	if (!Strings.isNullOrEmpty(timeSuggestOracle) && timeSuggestOracle.matches(TIME24HOURS_PATTERN))
    		return Integer.parseInt(timeSuggestOracle.split(":")[0]);

    	return null;
    }

    public static Integer getMinute(String timeSuggestOracle) {
    	if (!Strings.isNullOrEmpty(timeSuggestOracle) && timeSuggestOracle.matches(TIME24HOURS_PATTERN))
    		return Integer.parseInt(timeSuggestOracle.split(":")[1]);

    	return null;
    }
}
