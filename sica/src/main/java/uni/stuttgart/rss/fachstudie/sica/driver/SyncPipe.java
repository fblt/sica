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
package uni.stuttgart.rss.fachstudie.sica.driver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class SyncPipe implements Runnable {
	private final int BUFSIZE = 4096;

	private final InputStream in;
	private final OutputStream out;

	public SyncPipe(InputStream in, OutputStream out) {
		this.in = in;
		this.out = out;
	}

	@Override
	public void run() {
		try {
			byte[] inbuf = new byte[BUFSIZE];
			int numBytes = in.read(inbuf);
			while (numBytes != -1) {
				out.write(inbuf, 0, numBytes);
				numBytes = in.read(inbuf);
			}
		} catch (IOException e) {
			throw new RuntimeException("could not sync streams");
		}
	}
}
