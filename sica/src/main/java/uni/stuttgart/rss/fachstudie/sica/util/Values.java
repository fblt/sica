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
package uni.stuttgart.rss.fachstudie.sica.util;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/** Utility class for value/input validation */
public final class Values {
	private Values() {
		throw new IllegalAccessError("Utility class");
	}

	/**
	 * Assures a value isn't null and returns that value
	 * 
	 * @throws NullPointerException
	 *             if the value is null
	 */
	public static <T> T notNull(T t) throws NullPointerException {
		return Objects.requireNonNull(t);
	}

	/**
	 * Assures a value isn't null and returns that value
	 * 
	 * @throws NullPointerException
	 *             with the given message if the value is null
	 */
	public static <T> T notNull(T t, String message) throws NullPointerException {
		return Objects.requireNonNull(t, message);
	}

	/**
	 * Assures a list is neither null nor empty and returns that list
	 * 
	 * @throws NullPointerException
	 *             if the list is null
	 * @throws IllegalArgumentException
	 *             if the list is empty
	 */
	public static <T extends List<U>, U> T notEmpty(T t) throws NullPointerException, IllegalArgumentException {
		return not(List::isEmpty, notNull(t), "list must not be empty");
	}

	/**
	 * Assures a string is neither null nor empty and returns that string
	 * 
	 * @throws NullPointerException
	 *             if the string is null
	 * @throws IllegalArgumentException
	 *             if the string is empty
	 */
	public static String notEmpty(String t) throws NullPointerException, IllegalArgumentException {
		return not(String::isEmpty, notNull(t), "string must not be empty");
	}

	/**
	 * Assures a predicate does not match a value, returns that value
	 * 
	 * @param predicate
	 *            the predicate to test
	 * @param t
	 *            value check
	 * @param message
	 *            error message if the predicte matches
	 * @return <code>t</code>, the checked value
	 * @throws IllegalArgumentException
	 *             if the predicate matches
	 */
	public static <T> T not(Predicate<T> predicate, T t, String message) throws IllegalArgumentException {
		if (predicate.test(t)) {
			throw new IllegalArgumentException(message);
		}
		return t;
	}

}
