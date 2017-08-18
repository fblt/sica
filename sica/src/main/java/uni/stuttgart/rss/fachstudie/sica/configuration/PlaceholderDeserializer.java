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
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import uni.stuttgart.rss.fachstudie.sica.data.Placeholder;
import uni.stuttgart.rss.fachstudie.sica.data.PlaceholderDouble;
import uni.stuttgart.rss.fachstudie.sica.data.PlaceholderEnumeration;
import uni.stuttgart.rss.fachstudie.sica.data.PlaceholderInteger;
import uni.stuttgart.rss.fachstudie.sica.data.Range;

public class PlaceholderDeserializer implements JsonDeserializer<Placeholder> {

	@Override
	public Placeholder deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		Placeholder ret;
		JsonObject jsonObject = json.getAsJsonObject();
		String type = jsonObject.get("type").getAsString();

		String id = jsonObject.get("id").getAsString();

		JsonArray rangeArray = jsonObject.get("range").getAsJsonArray();

		switch (type) {
		case "integer": {
			ret = new PlaceholderInteger(id, new Range(rangeArray.get(0).getAsInt(), rangeArray.get(1).getAsInt()));
			break;
		}
		case "double": {
			ret = new PlaceholderDouble(id, new Range(rangeArray.get(0).getAsInt(), rangeArray.get(1).getAsInt()));
			break;
		}
		case "enum": {
			List<String> values = new ArrayList<>(rangeArray.size());
			rangeArray.forEach(x -> values.add(x.getAsString()));
			ret = new PlaceholderEnumeration(id, values);
			break;
		}
		default: {
			throw new InvalidConfigurationException("got invalid placeholder type " + type);
		}
		}

		return ret;
	}

}
