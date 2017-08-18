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
package uni.stuttgart.rss.fachstudie.sica.monitor;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import org.apache.commons.math3.stat.descriptive.rank.Median;

import com.google.gson.Gson;

import uni.stuttgart.rss.fachstudie.sica.data.MonitorResult;
import uni.stuttgart.rss.fachstudie.sica.data.Problem;
import uni.stuttgart.rss.fachstudie.sica.driver.TestDriverObservable;
import uni.stuttgart.rss.fachstudie.sica.util.IO;

public class DefaultMonitor extends IMonitor {

	private TestDriverObservable testDriverObservable;
	private DefaultMonitoringThread monitoringThread;
	private long steadyStateWaitTime = 5;
	private int monitorSamples = 5;
	private long monitorInterval = 5000;
	private HashMap<String, ArrayList<Double>> latestData;
	private boolean testFinished = false;
	private final String monitorAddress;

	public DefaultMonitor(Problem problem) {
		this.latestData = new HashMap<>();
		this.monitorAddress = problem.getMonitorAddress();
		if (problem.getMonitorInterval() != -1) {
			this.monitorInterval = problem.getMonitorInterval();
		}if (problem.getMonitorSamples() != -1) {
			this.monitorSamples = problem.getMonitorSamples();
		}
	}

	@Override
	public void startMonitoring(Observable testDriverObservable) {
		super.startMonitoring(testDriverObservable);
		testFinished = false;

		this.monitoringThread = new DefaultMonitoringThread();
		this.monitoringThread.start();

	}

	@Override
	protected void notifyController(MonitorResult result) {
		setChanged();
		notifyObservers(result);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof TestDriverObservable) {
			TestDriverObservable tdo = (TestDriverObservable) o;
			for (Map.Entry<String, Double> entry : tdo.getLatestValues().entrySet()) {
				if (latestData.get(entry.getKey()) == null) {
					latestData.put(entry.getKey(), new ArrayList<Double>());
				}
				latestData.get(entry.getKey()).add(entry.getValue());
			}
		}
	}

	@Override
	public void setSteadyStateWaitTime(long timeInMin) {
		this.steadyStateWaitTime = timeInMin;
	}

	public class DefaultMonitoringThread extends Thread {
		@Override
		public void run() {
			// wait at least 20 seconds before terminating the test
			try {
				Thread.sleep(steadyStateWaitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			testFinished = true;

			for (int sampleCount = 0; sampleCount < monitorSamples; sampleCount++) {
				try {
					HttpURLConnection httpcon = (HttpURLConnection) new URL(monitorAddress).openConnection();

					httpcon.connect();
					InputStream is = httpcon.getInputStream();

					String response = IO.readWholeStream(is);

					Gson gson = new Gson();
					MonitorResult res = gson.fromJson(response, MonitorResult.class);

					for (Map.Entry<String, Double> entry : res.getValues().entrySet()) {
						if (latestData.get(entry.getKey()) == null) {
							latestData.put(entry.getKey(), new ArrayList<Double>());
						}
						latestData.get(entry.getKey()).add(entry.getValue());
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				try {
					Thread.sleep(monitorInterval);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			MonitorResult res = new MonitorResult();

			for (Map.Entry<String, ArrayList<Double>> entry : latestData.entrySet()) {
				double[] medianData = new double[entry.getValue().size()];
				int i = 0;
				for (Double d : entry.getValue()) {
					medianData[i] = d;
					i++;
				}
				Median m = new Median();

				res.addValue(entry.getKey(), m.evaluate(medianData));
			}

			System.out.println("test finished");

			notifyController(res);
		}
	}

	@Override
	public void setMonitorInterval(long timeInMs) {
		this.monitorInterval = timeInMs;
	}

	@Override
	public void setMonitorSamples(int number) {
		this.monitorSamples = number;
	}
}
