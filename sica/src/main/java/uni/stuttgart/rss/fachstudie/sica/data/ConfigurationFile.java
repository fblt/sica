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
package uni.stuttgart.rss.fachstudie.sica.data;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import uni.stuttgart.rss.fachstudie.sica.configuration.ConfigurationFileDeserializer;

/**
 * represents a configuration file, used to create {@link Problem}s
 * 
 * @see ConfigurationFileDeserializer
 */
public class ConfigurationFile {
	public static final String GOALFUNCTION = "goalFunction";
	public static final String EXECUTOR = "executor";
	public static final String ORIGINAL_TEST_FILE = "originalTestFile";
	public static final String SUT_ADDRESS = "sutAddress";
	public static final String COUNTER = "counter";
	public static final String DUE_TIME = "dueTime";
	public static final String STEADY_STATE_TIME = "steadyStateTime";
	public static final String MONITOR_SAMPLES = "monitorSamples";
	public static final String MONITOR_INTERVAL = "monitorInterval";
	public static final String MONITOR_ADDRESS = "monitorAddress";

	private final List<Placeholder> placeholders;
	private final List<Constraint> constraints;
	private final Map<String, String> configurations;

	public ConfigurationFile(List<Placeholder> placeholder, List<Constraint> constraints,
			Map<String, String> configurations) {
		this.placeholders = Collections.unmodifiableList(placeholder);
		this.constraints = Collections.unmodifiableList(constraints);
		this.configurations = Collections.unmodifiableMap(configurations);
	}

	public List<Placeholder> getPlaceholders() {
		return placeholders;
	}

	public List<Constraint> getConstraints() {
		return constraints;
	}

	public Map<String, String> getConfigurations() {
		return configurations;
	}

	@Override
	public String toString() {
		return "ConfigurationFile:<constraints:" + getConstraints() + " placeholders:" + getPlaceholders()
				+ " configurations:" + getConfigurations() + ">";
	}
}
