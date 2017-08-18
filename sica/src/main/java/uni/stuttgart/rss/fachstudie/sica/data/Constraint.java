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
import java.util.HashMap;
import java.util.Map;

/** Represents a constraining relationship between two {@link Placeholder}s */
public class Constraint {

	private final Placeholder param1;
	private final Placeholder param2;
	private final ConstraintType constType;

	public static enum ConstraintType {
		/** = */
		EQUALS("=", "equal", "equals"),
		/** != */
		UNEQUALS("!=", "unequal", "unequals"),
		/** < */
		SMALLER("<", "smaller", "smallerthan", "less", "lessthan"),
		/** > */
		GREATER(">", "greater", "greaterthan", "more", "morethan"),
		/** <= */
		SMALLEREQUALS("<=", "smallerequals", "smallerthanorequal", "smallerthanorequalto", "lessequals",
				"lessthanorequal", "lessthanorequalto"),
		/** >= */
		GREATEREQUALS(">=", "greaterequals", "greaterthanorequal", "greaterthanorequalto", "moreequals",
				"morethanorequal", "morethanorequalto");

		private String[] names;

		public static final Map<String, ConstraintType> nameMap;

		ConstraintType(String... names) {
			this.names = names;
		}

		static {
			Map<String, ConstraintType> map = new HashMap<>();
			nameMap = Collections.unmodifiableMap(map);

			for (ConstraintType ct : ConstraintType.values())
				for (String name : ct.names)
					map.put(name, ct);
		}
	}

	public Constraint(Placeholder p1, Placeholder p2, ConstraintType t) {
		this.param1 = p1;
		this.param2 = p2;
		this.constType = t;
	}

	public Placeholder getParam1() {
		return param1;
	}

	public Placeholder getParam2() {
		return param2;
	}

	public ConstraintType getConstType() {
		return constType;
	}

	public boolean checkConstraint(Solution sol) {
		Placeholder.Value val1 = sol.getSolutionMap().get(param1);
		Placeholder.Value val2 = sol.getSolutionMap().get(param2);

		switch (constType) {
		case EQUALS:
			return val1.equals(val2);
		case UNEQUALS:
			return !val1.equals(val2);
		case SMALLER:
			return val1.compareTo(val2) < 0;
		case GREATER:
			return val1.compareTo(val2) > 0;
		case SMALLEREQUALS:
			return val1.compareTo(val2) <= 0;
		case GREATEREQUALS:
			return val1.compareTo(val2) >= 0;
		default:
			return false;
		}
	}

	@Override
	public String toString() {
		return "Constraint:<" + param1.id + "[" + constType.names[0] + "]" + param2.id + ">";
	}
}
