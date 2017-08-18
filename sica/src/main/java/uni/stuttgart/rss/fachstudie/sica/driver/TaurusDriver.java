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

import java.io.File;
import java.io.IOException;

import uni.stuttgart.rss.fachstudie.sica.util.OS;

public class TaurusDriver implements ITestDriver {

	private TaurusDriverObservable observable;

	private Process taurusProcess;

	/**
	 * starts the load test and returns a LoadDriverObservable
	 */
	@Override
	public TaurusDriverObservable startTest(File taurusConfig) {
		if (!isTaurusAvailable()) {
			installTaurus();
		}

		if (taurusProcess != null && taurusProcess.isAlive()) {
			stopTest();
		}

		this.taurusProcess = null;
		try {
			String[] processCall = new String[2];
			processCall[0] = "bzt";
			processCall[1] = taurusConfig.getAbsolutePath();
			taurusProcess = Runtime.getRuntime().exec(processCall, null, getTestrunsFolder());

			this.observable = new TaurusDriverObservable(taurusProcess);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return new TaurusDriverObservable(taurusProcess);
	}

	/**
	 * stop the current load test
	 */
	@Override
	public void stopTest() {
		OS.requestProcessClose(OS.isWindows() ? "bzt.exe" : "bzt");

		// this.taurusProcess.destroy();
	}

	/**
	 * check whether Taurus is available on the system
	 * 
	 * @return
	 */
	public boolean isTaurusAvailable() {
		try {
			taurusProcess = Runtime.getRuntime().exec("bzt ", null, getTestrunsFolder());
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * install Taurus - blocking!
	 * 
	 * @return
	 */
	public boolean installTaurus() {
		Process installProcess;
		try {
			installProcess = Runtime.getRuntime().exec("pip install bzt");
			try {
				installProcess.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public boolean installLocust() {
		Process installProcess;
		try {
			installProcess = Runtime.getRuntime().exec("pip install locustio");
			new Thread(new SyncPipe(installProcess.getInputStream(), System.out)).start();
			new Thread(new SyncPipe(installProcess.getInputStream(), System.err)).start();
			try {
				installProcess.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} catch (IOException e) {
			return false;
		}

	}

	/**
	 * 
	 * @return
	 */
	public File getTestrunsFolder() {
		// create testresults folder
		File testrunsFolder = new File("testruns");
		if (testrunsFolder.mkdirs() || testrunsFolder.exists()) {
			return testrunsFolder;
		}
		return new File(".");
	}

}
