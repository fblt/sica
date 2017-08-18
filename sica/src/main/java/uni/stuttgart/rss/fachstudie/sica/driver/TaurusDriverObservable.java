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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class TaurusDriverObservable extends TestDriverObservable {

	Process taurusProcess;
	TaurusProcessMonitorThread monitorThread;
	
	public TaurusDriverObservable(Process taurusProcess) {

		this.taurusProcess = taurusProcess;
		this.monitorThread = new TaurusProcessMonitorThread();
		this.monitorThread.setDaemon(true);
		this.monitorThread.start();
	}

	public class TaurusProcessMonitorThread extends Thread {
		@Override
		public void run() {
			BufferedReader in = new BufferedReader(new InputStreamReader(taurusProcess.getInputStream()));
			String line = null;

			StringBuilder responseData = new StringBuilder();
			try {
				while ((line = in.readLine()) != null) {
					responseData.append(line);

					System.out.println(line);
					
					HashMap<String, Double> dataset = new HashMap<>();
					
					// get number of virtual users
					int numberOfUsersPos = line.indexOf("INFO: Current: ");
					if (numberOfUsersPos >= 0) {
						int numberOfUsers = Integer
								.parseInt(line.substring(numberOfUsersPos + "INFO: Current: ".length(),
										line.indexOf(' ', numberOfUsersPos + "INFO: Current: ".length())));
						dataset.put("vu", (double)numberOfUsers);
					}
					
					// get number of requests that succeeded in the last sec
					int numberOfSuccPos = line.indexOf("vu\t");
					if (numberOfSuccPos >= 0) {
						int numberOfSucc = Integer
								.parseInt(line.substring(numberOfSuccPos + "vu\t".length(),
										line.indexOf(' ', numberOfSuccPos + "vu\t".length())));
						dataset.put("succ", (double)numberOfSucc);
					}
					
					// get number of requests that failed in the last sec
					int numberOfFailPos = line.indexOf("succ\t");
					if (numberOfFailPos >= 0) {
						int numberOfFail = Integer
								.parseInt(line.substring(numberOfFailPos + "succ\t".length(),
										line.indexOf(' ', numberOfFailPos + "succ\t".length())));
						dataset.put("fail", (double)numberOfFail);
					}
					
					// get average response time in the last sec
					int avgResponsePos = line.indexOf("fail\t");
					if (avgResponsePos >= 0) {
						double avgResponse = Double
								.parseDouble(line.substring(avgResponsePos + "fail\t".length(),
										line.indexOf(' ', avgResponsePos + "fail\t".length())));
						dataset.put("avg", avgResponse);
					}
					
					// if a dataset was found notify observers
					if(dataset.size()>0){
						addNewTestDriverData(dataset);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
