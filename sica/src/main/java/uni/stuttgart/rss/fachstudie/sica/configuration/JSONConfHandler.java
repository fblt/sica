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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import uni.stuttgart.rss.fachstudie.sica.data.ConfigurationFile;
import uni.stuttgart.rss.fachstudie.sica.data.Placeholder;

/** Holds utility methods for loading configuration files */
public final class JSONConfHandler {
	private JSONConfHandler() {
		throw new IllegalAccessError("Utility class");
	}

	/** loads a configuration file from a file on disk at the given {@link Path} */
	public static ConfigurationFile loadConfiguration(Path tempFile) throws IOException {
		BufferedReader bufferedReader = Files.newBufferedReader(tempFile);
		return readConfiguration(bufferedReader);
	}

	/** reads a configuration file from the given {@link BufferedReader} */
	public static ConfigurationFile readConfiguration(Reader reader) {
		Gson gson = createGson();
		ConfigurationFile cf = gson.fromJson(reader, ConfigurationFile.class);
		return cf;
	}

	/** parses a configuration file given as a string */
	public static ConfigurationFile parseConfiguration(String configJson) {
		Gson gson = createGson();
		ConfigurationFile cf = gson.fromJson(configJson, ConfigurationFile.class);
		return cf;
	}

	private static Gson createGson() {
		GsonBuilder gsonbuilder = new GsonBuilder();
		gsonbuilder.registerTypeAdapter(Placeholder.class, new PlaceholderDeserializer());
		gsonbuilder.registerTypeAdapter(ConfigurationFile.class, new ConfigurationFileDeserializer());
		Gson gson = gsonbuilder.create();
		return gson;
	}
}
