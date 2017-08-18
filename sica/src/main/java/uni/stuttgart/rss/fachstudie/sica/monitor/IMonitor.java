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

import java.util.Observable;
import java.util.Observer;

import uni.stuttgart.rss.fachstudie.sica.data.MonitorResult;

public abstract class IMonitor extends Observable implements Observer {

	/**
	 * starts monitoring for a new test run and waits for new test results
	 */
	public void startMonitoring(Observable testDriverObservable) {
		testDriverObservable.addObserver(this);
	}

	/**
	 * notifies the controller if new results arrived
	 */
	protected abstract void notifyController(MonitorResult result);
	
	public abstract void setSteadyStateWaitTime (long timeInMs);
	public abstract void setMonitorInterval (long timeInMs);
	public abstract void setMonitorSamples (int number);
}
