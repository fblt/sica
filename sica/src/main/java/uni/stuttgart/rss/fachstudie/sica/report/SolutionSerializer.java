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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import uni.stuttgart.rss.fachstudie.sica.data.PlaceholderDouble;
import uni.stuttgart.rss.fachstudie.sica.data.PlaceholderEnumeration;
import uni.stuttgart.rss.fachstudie.sica.data.PlaceholderInteger;
import uni.stuttgart.rss.fachstudie.sica.data.Solution;

public class SolutionSerializer implements JsonSerializer<Solution> {

	@Override
	public JsonElement serialize(Solution src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jo = new JsonObject();
		jo.addProperty("goalFunctionScore", src.getGoalFunctionScore());
		src.getSolutionMap().forEach((placeholder, value) -> {
			if (value instanceof PlaceholderDouble.Value) {
				jo.addProperty(placeholder.getId(), ((PlaceholderDouble.Value) value).getVal());
			} else if (value instanceof PlaceholderInteger.Value) {
				jo.addProperty(placeholder.getId(), ((PlaceholderInteger.Value) value).getVal());
			} else if (value instanceof PlaceholderEnumeration.Value) {
				jo.addProperty(placeholder.getId(), ((PlaceholderEnumeration.Value) value).getVal());
			}
		});
		return jo;
	}

}
