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
package uni.stuttgart.rss.fachstudie.sica.optimizer;

/**
 * Represents an exception that occurred while processing the goal function, in
 * sica this is likely to stem from the JavaScript goal function.
 * 
 * @author Nils Wenzler
 */
public class GoalFunctionException extends Exception {
	private static final long serialVersionUID = -166543428856735730L;

	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public GoalFunctionException(String message, Throwable e) {
		super(message, e);
	}

	/**
	 * @see Exception#Exception(String)
	 */
	public GoalFunctionException(String message) {
		super(message);
	}

}
