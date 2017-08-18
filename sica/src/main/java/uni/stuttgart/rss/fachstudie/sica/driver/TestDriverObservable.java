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

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public abstract class TestDriverObservable extends Observable{
	
	/** stores the latest dataset */
	private Map<String, Double> lastValues; 
	
	/**
	 * adds new monitoring data to the observable and notifies all observers
	 * @param values
	 */
	protected void addNewTestDriverData(Map<String, Double> values){
		this.lastValues = values;
		this.setChanged();
		this.notifyObservers();
	}
	
	/**
	 * returns the latest monitoring data of the test driver
	 * @return
	 */
	public Map<String, Double> getLatestValues(){
		return new HashMap<>(lastValues);
	}
	
}
