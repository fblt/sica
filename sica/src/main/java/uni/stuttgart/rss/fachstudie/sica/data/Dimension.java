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
package uni.stuttgart.rss.fachstudie.sica.data;

import java.util.ArrayList;

public class Dimension {
	
	private String parameterName;
	private ArrayList<DimensionData> exploration = new ArrayList<>();
	
	public Dimension(String parameterName) {
		this.parameterName = parameterName;
	}
	
	public void addNewExplorationPoint (double value, double goalFunctionValue){
		DimensionData data = new DimensionData();
		data.value = value;
		data.goalFunctionValue = goalFunctionValue;
		exploration.add(data);
	}
	
	private class DimensionData{
		public double value;
		public double goalFunctionValue;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public ArrayList<DimensionData> getExploration() {
		return exploration;
	}

}
