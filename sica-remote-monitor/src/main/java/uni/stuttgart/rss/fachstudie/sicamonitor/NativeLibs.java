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
package uni.stuttgart.rss.fachstudie.sicamonitor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

public class NativeLibs {

	private static String[] libs = { "libsigar-amd64-freebsd-6.so", "libsigar-amd64-linux.so",
			"libsigar-amd64-solaris.so", "libsigar-ia64-hpux-11.sl", "libsigar-ia64-linux.so", "libsigar-pa-hpux-11.sl",
			"libsigar-ppc64-aix-5.so", "libsigar-ppc64-linux.so", "libsigar-ppc-aix-5.so", "libsigar-ppc-linux.so",
			"libsigar-s390x-linux.so", "libsigar-sparc64-solaris.so", "libsigar-sparc-solaris.so",
			"libsigar-universal64-macosx.dylib", "libsigar-universal-macosx.dylib", "libsigar-x86-freebsd-5.so",
			"libsigar-x86-freebsd-6.so", "libsigar-x86-linux.so", "libsigar-x86-solaris.so", "sigar-amd64-winnt.dll",
			"sigar-x86-winnt.dll", "sigar-x86-winnt.lib" };

	public static void prepareLibraries() throws IOException {
		Path tdir = Files.createTempDirectory("sigar-tmp-libs");

		for (String l : libs) {
			Path dest = tdir.resolve(l);
			String libpath = "sigar-lib/" + l;
			try (InputStream is = NativeLibs.class.getClassLoader().getResourceAsStream(libpath)) {
				Objects.requireNonNull(is, "Could not open library " + libpath + " from classpath");
				Files.copy(is, dest);
			}
		}
		System.setProperty("java.library.path", tdir.toString());

		Runtime.getRuntime().addShutdownHook(new CleanupLibs(tdir));
	}

	private static final class CleanupLibs extends Thread {
		private final Path tdir;

		private CleanupLibs(Path tdir) {
			super();
			this.tdir = tdir;
		}

		@Override
		public void run() {
			try {
				Files.walkFileTree(tdir, new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						Files.delete(file);
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
						if (exc != null) {
							return FileVisitResult.TERMINATE;
						}
						Files.delete(dir);
						return FileVisitResult.CONTINUE;
					}
				});
			} catch (IOException e) {
				// we're shutting the VM down. there's nothing we can do at this
				// point.
				e.printStackTrace();
			}
		}
	}
}
