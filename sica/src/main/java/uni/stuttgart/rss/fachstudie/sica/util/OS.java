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

import java.io.IOException;
import java.util.Scanner;

public class OS {
	private static String OS = System.getProperty("os.name").toLowerCase();

	/** @deprecated possible OS Command Injection vulnerability */
	@Deprecated
	public static String execCmdVulnerable(String cmd) {
		try (Scanner s = new Scanner(Runtime.getRuntime().exec(cmd).getInputStream())) {
			s.useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	public static boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
	}

	public static void requestProcessClose(String processName) {
		if (isWindows()) {
			String processList = execCmdVulnerable("tasklist").replace("\r", "");
			String[] lines = processList.split("\n");
			for (String line : lines) {
				if (line.contains(processName)) {
					while (line.contains("  ")) {
						line = line.replace("  ", " ");
					}
					String[] params = line.split(" ");
					System.out.println("executing: " + "taskkill -t -f /pid " + params[1]);
					System.out.println(execCmdVulnerable("taskkill -t -f /pid " + params[1]));
					return;
				}
			}
		} else {
			System.out.println("going to kill processes");
			try (Scanner scanParent = new Scanner(
					Runtime.getRuntime().exec(new String[] { "pgrep", "bzt" }).getInputStream())) {
				while (scanParent.hasNext()) {
					String parent = scanParent.next();
					System.out.println("Killing children of " + parent);
					try (Scanner scanChildren = new Scanner(
							Runtime.getRuntime().exec(new String[] { "pgrep", "-P", parent }).getInputStream())) {
						while (scanChildren.hasNext()) {
							String child = scanChildren.next();
							System.out.println("Killing " + parent + "->" + child);
							Runtime.getRuntime().exec(new String[] { "kill", "-9", child });
						}
						Runtime.getRuntime().exec(new String[] { "kill", "-9", parent });
					}
				}
			} catch (IOException e) {
				throw new RuntimeException("could not close process");
			}
		}
	}
}
