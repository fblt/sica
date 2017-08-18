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
package uni.stuttgart.rss.fachstudie.sica.optimizer;

import uni.stuttgart.rss.fachstudie.sica.data.Constraint;
import uni.stuttgart.rss.fachstudie.sica.data.MonitorResult;
import uni.stuttgart.rss.fachstudie.sica.data.Problem;
import uni.stuttgart.rss.fachstudie.sica.data.Solution;

public interface IOptimizer {

	/**
	 * this notifies the optimizer about an new monitor result
	 * 
	 * @param result
	 *            the new Monitor result
	 */
	public void notifyNewMonitorResult(MonitorResult result);

	/**
	 * optimizes a problem and generates a valid solution. return null if
	 * testing loop end
	 * 
	 * @param problem
	 *            the current problem instance
	 * @return returns a valid problem instance
	 */
	public Solution optimize(Problem problem);

	/**
	 * returns the best solution so far.
	 * 
	 * @return the currently bes solution
	 */
	public Solution getBestSolution();

	/**
	 * checks if the termination criteria has been reached
	 */
	public boolean checkTerminationCriteria(Problem problem);

	/**
	 * checks the constraints
	 * 
	 * @param p
	 *            the problem instance
	 * @param s
	 *            the solution to check
	 * @return returns true if the the solution is valid
	 */
	public static boolean checkConstraints(Problem p, Solution s) {
		for (Constraint c : p.getConstraints()) {
			if (!c.checkConstraint(s)) {
				return false;
			}
		}
		return true;
	}
}
