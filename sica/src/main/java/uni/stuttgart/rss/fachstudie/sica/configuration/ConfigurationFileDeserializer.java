/*******************************************************************************
 * This file is part of SICA.
 * 
 * SICA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SICA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SICA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package uni.stuttgart.rss.fachstudie.sica.configuration;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import uni.stuttgart.rss.fachstudie.sica.data.ConfigurationFile;
import uni.stuttgart.rss.fachstudie.sica.data.Constraint;
import uni.stuttgart.rss.fachstudie.sica.data.Constraint.ConstraintType;
import uni.stuttgart.rss.fachstudie.sica.data.Placeholder;

/** Class to be passed to GSON for loading a {@link ConfigurationFile} */
public class ConfigurationFileDeserializer implements JsonDeserializer<ConfigurationFile> {

	@Override
	public ConfigurationFile deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();

		List<Placeholder> phs = context.deserialize(jsonObject.get("placeholders"), new TypeToken<List<Placeholder>>() {
			/* type token */}.getType());
		requireNonNull(phs, "The ConfigurationFile must contain a list of placeholders");
		Map<String, Placeholder> phMap = new HashMap<>(phs.size());
		phs.forEach(ph -> phMap.put(ph.getId(), ph));

		JsonArray constArray = jsonObject.get("constraints").getAsJsonArray();
		List<Constraint> constraints = new ArrayList<>(constArray.size());
		constArray.forEach(jsonConst -> {
			JsonObject jsonConstObj = jsonConst.getAsJsonObject();
			String val1 = jsonConstObj.get("value1").getAsString();
			String val2 = jsonConstObj.get("value2").getAsString();
			String type = jsonConstObj.get("type").getAsString();

			Placeholder ph1 = phMap.get(val1);
			Placeholder ph2 = phMap.get(val2);
			ConstraintType ct = ConstraintType.nameMap.get(type.toLowerCase());
			requireNonNull(ph1, "unknown placeholder specified as value1: %s", val1);
			requireNonNull(ph2, "unknown placeholder specified as value2: %s", val2);
			requireNonNull(ct, "unknown constraint type specified: %s", type);

			constraints.add(new Constraint(ph1, ph2, ct));
		});
		HashMap<String, String> map = new HashMap<>();

		for (String confKey : new String[] { //
				// required config values
				ConfigurationFile.ORIGINAL_TEST_FILE, //
				ConfigurationFile.SUT_ADDRESS, //
				ConfigurationFile.EXECUTOR, //
				ConfigurationFile.GOALFUNCTION, //
				ConfigurationFile.STEADY_STATE_TIME, //
				ConfigurationFile.MONITOR_ADDRESS, //
		}) {
			JsonElement element = jsonObject.get(confKey);
			requireNonNull(element, "%s not present in configuration file", confKey);
			map.put(confKey, element.getAsString());
		}

		for (String confKey : new String[] { //
				// optional config values
				ConfigurationFile.COUNTER, //
				ConfigurationFile.DUE_TIME, //
				ConfigurationFile.MONITOR_SAMPLES, //
				ConfigurationFile.MONITOR_INTERVAL, //
		}) {
			JsonElement element = jsonObject.get(confKey);
			if (element != null) {
				map.put(confKey, element.getAsString());
			}
		}

		return new ConfigurationFile(phs, constraints, map);
	}

	private static void requireNonNull(Object o, String message, Object... formatElements) {
		if (o == null) {
			throw new InvalidConfigurationException(String.format(message, formatElements));
		}
	}
}
