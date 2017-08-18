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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Scanner;

public class IO {
	private IO() {
		throw new IllegalAccessError("Utility class");
	}

	public static String readWholeStream(InputStream is) {
		Objects.requireNonNull(is, "tried to read null stream");
		try (Scanner sc = new Scanner(is)) {
			sc.useDelimiter("\\A");
			return sc.next();
		}
	}

	public static InputStream openResource(Class<?> from, String resourcePath)
			throws FileNotFoundException, IOException {
		InputStream is = from.getClassLoader().getResourceAsStream(resourcePath);
		if (is == null) {
			throw new FileNotFoundException(
					String.format("Could not find resource '%s' for class %s", resourcePath, from));
		}
		return is;
	}

	public static BufferedReader bufferedResource(Class<?> from, String resourcePath)
			throws FileNotFoundException, IOException {
		InputStream is = openResource(from, resourcePath);
		InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-8"));
		return new BufferedReader(isr);
	}

	/** @see java.nio.file.Files#newBufferedReader(Path, Charset) */
	public static PrintWriter newPrintWriter(Path path, Charset cs, OpenOption... options) throws IOException {
		Writer writer = new OutputStreamWriter(Files.newOutputStream(path, options), cs);
		return new PrintWriter(writer);
	}
}
