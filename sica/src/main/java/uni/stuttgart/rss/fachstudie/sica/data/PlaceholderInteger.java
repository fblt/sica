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

public class PlaceholderInteger extends Placeholder {
	public PlaceholderInteger(String id, Range r) {
		super(id, Placeholder.Type.INTEGER);
		range = r;
	}

	private Range range;

	public Range getRange() {
		return range;
	}

	public void setRange(Range range) {
		this.range = range;
	}

	@Override
	public String toString() {
		return "PlaceholderInteger:<" + "id:" + id + " range:" + range + ">";
	}

	public class Value extends Placeholder.Value {
		private final int val;

		public Value(int val) {
			this.val = val;
		}

		@Override
		public PlaceholderInteger outer() {
			return PlaceholderInteger.this;
		}

		public int getVal() {
			return val;
		}

		@Override
		public int compareTo(Placeholder.Value o) {
			if (o instanceof PlaceholderDouble.Value) {
				PlaceholderDouble.Value other = ((PlaceholderDouble.Value) o);
				return -other.compareTo(o);
			} else {
				int otherval = ((PlaceholderInteger.Value) o).getVal();
				return Integer.compare(getVal(), otherval);
			}
		}

		@Override
		public String toString() {
			return this.val+"";
		}
		
		@Override
		/* AUTO-GENERATED */
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getVal();
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
			if (getVal() != other.getVal())
				return false;
			return true;
		}
	}
}
