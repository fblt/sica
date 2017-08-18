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

import java.util.List;

public class PlaceholderEnumeration extends Placeholder {
	private List<String> values;

	public PlaceholderEnumeration(String id, List<String> values) {
		super(id, Placeholder.Type.ENUMERATION);
		this.values = values;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "PlaceholderEnumeration:<" + "id:" + id + " values:" + values + ">";
	}

	public class Value extends Placeholder.Value {
		private final String val;

		public Value(String val) {
			this.val = val;
		}

		public String getVal() {
			return val;
		}

		@Override
		public String toString() {
			return this.val+"";
		}
		
		@Override
		public PlaceholderEnumeration outer() {
			return PlaceholderEnumeration.this;
		}

		@Override
		public int compareTo(Placeholder.Value o) {
			return getVal().compareTo(((PlaceholderEnumeration.Value) o).getVal());
		}

		@Override
		/* AUTO-GENERATED */
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((getVal() == null) ? 0 : getVal().hashCode());
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
			Value other = (Value) obj;
			if (getVal() == null) {
				if (other.getVal() != null)
					return false;
			} else if (!getVal().equals(other.getVal()))
				return false;
			return true;
		}
	}
}
