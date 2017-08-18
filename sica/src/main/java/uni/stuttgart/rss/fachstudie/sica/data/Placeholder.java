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

/** Configuration file placeholder, will be textually replaced with a value */
public abstract class Placeholder {
	protected final String id;
	private final Type t;

	/**
	 * the type of a placeholder
	 */
	public enum Type {
		INTEGER, DOUBLE, ENUMERATION
	}

	/**
	 * @param id
	 *            The string to be replaced in the tool template configuration file
	 */
	/* package-private */ Placeholder(String id, Type t) {
		this.id = id;
		this.t = t;
	}

	public String getId() {
		return this.id;
	}

	public Type getType() {
		return t;
	}

	/** Convert everything (enums too) to a double range */
	public Range getEffectiveRange() {
		switch (t) {
		case DOUBLE:
			return ((PlaceholderDouble) this).getRange();
		case INTEGER:
			return ((PlaceholderInteger) this).getRange();
		case ENUMERATION:
			double max = Math.nextDown(((PlaceholderEnumeration) this).getValues().size());
			return new Range(0, max);
		default:
			throw new IllegalStateException("invalid placeholder type");
		}
	}

	/** Value for a concrete {@link Placeholder} instance */
	public abstract class Value implements Comparable<Value> {
		public Placeholder outer() {
			return Placeholder.this;
		}

		@Override
		public abstract int compareTo(Value o);

		@Override
		public abstract boolean equals(Object obj);

		@Override
		public abstract int hashCode();
	}
}
