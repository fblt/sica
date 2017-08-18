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

public class PlaceholderDouble extends Placeholder {
	
	public PlaceholderDouble(String id, Range r) {
		super(id, Placeholder.Type.DOUBLE);
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
		return "PlaceholderDouble:<" + "id:" + id + " range:" + range + ">";
	}

	public class Value extends Placeholder.Value {
		private final double val;

		public Value(double val) {
			this.val = val;
		}

		public double getVal() {
			return val;
		}

		@Override
		public PlaceholderDouble outer() {
			return PlaceholderDouble.this;
		}

		@Override
		public int compareTo(Placeholder.Value o) {
			double otherval;
			if (o instanceof PlaceholderInteger.Value) {
				otherval = ((PlaceholderInteger.Value) o).getVal();
			} else {
				otherval = ((PlaceholderDouble.Value) o).getVal();
			}

			return Double.compare(getVal(), otherval);
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
			long temp;
			temp = Double.doubleToLongBits(getVal());
			result = prime * result + (int) (temp ^ (temp >>> 32));
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
			if (Double.doubleToLongBits(getVal()) != Double.doubleToLongBits(other.getVal()))
				return false;
			return true;
		}
	}
}
