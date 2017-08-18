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
package uni.stuttgart.rss.fachstudie.sica.report;

import java.lang.reflect.Type;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import uni.stuttgart.rss.fachstudie.sica.data.MonitorResult;

public class ResultSerializer implements JsonSerializer<MonitorResult> {

	@Override
	public JsonElement serialize(MonitorResult src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jo = new JsonObject();
		for (Entry<String, Double> entry : src.getValues().entrySet()) {
			jo.addProperty(entry.getKey(), entry.getValue());
		}
		return jo;
	}

}
