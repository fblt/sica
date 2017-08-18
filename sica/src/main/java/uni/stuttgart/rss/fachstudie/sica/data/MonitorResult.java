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

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents a result from the monitor component running on the remote system.
 */
public class MonitorResult {
	Timestamp timestamp;
	HashMap<String, Double> values;

	public MonitorResult() {
		timestamp = new Timestamp(new Date().getTime());
		values = new HashMap<>();
	}

	/**
	 * Add a new test value
	 * 
	 * @param key
	 *            the key of the value
	 * @param value
	 *            the test value
	 * @return returns true if the values was correctly added
	 */
	public boolean addValue(String key, double value) {
		if (!values.containsKey(key)) {
			values.put(key, value);
			return true;
		}
		return false;
	}

	/**
	 * returns a value from the test results with the specified key
	 * 
	 * @param key
	 *            the key of the value
	 * @return the double value
	 */
	public double getValue(String key) {
		return values.get(key);
	}

	public Set<String> getKeySet() {
		return values.keySet();
	}

	/**
	 * @return timestamp of creation
	 */
	public Timestamp getCreationTime() {
		return timestamp;
	}

	public Map<String, Double> getValues() {
		return new HashMap<>(values);
	}

	@Override
	/* AUTO-GENERATED */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		result = prime * result + ((values == null) ? 0 : values.hashCode());
		return result;
	}

	@Override
	/* AUTO-GENERATED */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MonitorResult other = (MonitorResult) obj;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}

}
