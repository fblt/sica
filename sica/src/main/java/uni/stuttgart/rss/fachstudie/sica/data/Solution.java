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

import java.util.HashMap;
import java.util.Map;

import uni.stuttgart.rss.fachstudie.sica.data.Placeholder.Value;
import uni.stuttgart.rss.fachstudie.sica.optimizer.IGoalFunction;

/**
 * represents a given solution to the problem, holding
 * {@link Placeholder}-{@link Placeholder.Value Value} pairs
 */
public class Solution {
	private final Map<Placeholder, Placeholder.Value> solutionMap = new HashMap<>();
	private double goalFunctionScore = Double.NaN;

	public Solution() {
	}

	/**
	 * create a new Solution, copying the {@link Placeholder placeholders} and
	 * their {@link Placeholder.Value values} from an existing one
	 */
	public Solution(Solution toCopy) {
		solutionMap.putAll(toCopy.solutionMap);
	}

	public Map<Placeholder, Placeholder.Value> getSolutionMap() {
		return solutionMap;
	}

	/**
	 * tries to add a {@link Placeholder}-{@link Placeholder.Value Value} pair.
	 * 
	 * will not add/overwrite a value for a placeholder that already exists.
	 * 
	 * @return <code>true</code> if the value could be added, <br>
	 *         <code>false</code> if the placeholder already existed as a key
	 */
	public boolean addSolutionValue(Placeholder p, Placeholder.Value value) {
		if (!solutionMap.containsKey(p)) {
			solutionMap.put(p, value);
			return true;
		}

		return false;
	}

	/** Get the value stored for a given {@link Placeholder} */
	public Placeholder.Value getValue(Placeholder p) {
		return solutionMap.get(p);
	}

	/**
	 * Get a value stored for the {@link Placeholder} of the given id
	 * 
	 * @deprecated because it's slow
	 */
	@Deprecated
	public Placeholder.Value getValue(String id) {
		for (Placeholder p : solutionMap.keySet()) {
			if (p.getId().equals(id)) {
				return solutionMap.get(p);
			}
		}
		return null;
	}

	public void setGoalFunctionScore(double goalFunctionScore) {
		this.goalFunctionScore = goalFunctionScore;
	}

	/**
	 * get the score of this solution according to our {@link IGoalFunction goal
	 * function}
	 * 
	 * @return the score if one is set, <code>NaN</code> otherwise
	 */
	public double getGoalFunctionScore() {
		return goalFunctionScore;
	}

	public boolean hasGoalFunctionScore() {
		return !Double.isNaN(goalFunctionScore);
	}

	public void clearGoalFunctionScore() {
		this.goalFunctionScore = Double.NaN;
	}

	@Override
	public String toString() {
		return "Solution@" + goalFunctionScore + solutionMap;
	};
	
	public boolean equals(Solution s){
		
		for(Map.Entry<Placeholder, Value> entry:solutionMap.entrySet()){
			
			if(!s.getSolutionMap().containsKey(entry.getKey())){
				return false;
			}
			Value val = s.getValue(entry.getKey());
			if(!val.equals(entry.getValue())){
				return false;
			}
		}
		
		return true;
	}
}
