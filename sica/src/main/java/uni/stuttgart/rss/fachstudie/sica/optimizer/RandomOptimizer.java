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

import java.util.GregorianCalendar;

import uni.stuttgart.rss.fachstudie.sica.data.MonitorResult;
import uni.stuttgart.rss.fachstudie.sica.data.Placeholder;
import uni.stuttgart.rss.fachstudie.sica.data.PlaceholderDouble;
import uni.stuttgart.rss.fachstudie.sica.data.PlaceholderEnumeration;
import uni.stuttgart.rss.fachstudie.sica.data.PlaceholderInteger;
import uni.stuttgart.rss.fachstudie.sica.data.Problem;
import uni.stuttgart.rss.fachstudie.sica.data.Solution;

public class RandomOptimizer implements IOptimizer {
	private MonitorResult lastResult;
	private Solution bestSolution = null;
	private GregorianCalendar calendar;
	private Solution lastSolution;

	public RandomOptimizer() {
		calendar = new GregorianCalendar();
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see IOptimizer#notifyNewMonitorResult(MonitorResult)
	 */
	public void notifyNewMonitorResult(MonitorResult result) {
		lastResult = result;
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see IOptimizer#optimize(Problem)
	 */
	public Solution optimize(Problem problem) {
		Solution s;

		if (checkTerminationCriteria(problem)) {
			return null;
		}

		do {
			s = generateSolution(problem);

			// last run
			if (s == null) {
				return null;
			}
		} while (!IOptimizer.checkConstraints(problem, s));

		updateTerminationCriteria(problem);

		return s;
	}

	/**
	 * generates an Solution within the ranges of the values returns null if no
	 * Monitor result was notified first
	 * 
	 * @param problem
	 *            the problem instance
	 * @return returns a valid solution without checking the constraints or null
	 *         if no Monitor result is present
	 */
	private Solution generateSolution(Problem problem) {
		Solution result = new Solution();
		double random;

		for (Placeholder p : problem.getPlaceholders()) {
			Placeholder.Type t = p.getType();
			switch (t) {
			case INTEGER:
				PlaceholderInteger placeInt = (PlaceholderInteger) p;
				double minInt = placeInt.getRange().getMin();
				double maxInt = placeInt.getRange().getMax();

				random = Math.random() * (maxInt - minInt) + minInt;
				result.addSolutionValue(placeInt, placeInt.new Value((int) random));
				break;

			case DOUBLE:
				PlaceholderDouble placeDouble = (PlaceholderDouble) p;
				double minDouble = placeDouble.getRange().getMin();
				double maxDouble = placeDouble.getRange().getMax();

				random = Math.random() * (maxDouble - minDouble) + minDouble;
				result.addSolutionValue(placeDouble, placeDouble.new Value(random));
				break;

			case ENUMERATION:
				PlaceholderEnumeration placeEnum = (PlaceholderEnumeration) p;
				double minEnum = 0;
				double maxEnum = placeEnum.getValues().size();

				random = Math.random() * (maxEnum - minEnum) + minEnum;
				result.addSolutionValue(placeEnum, placeEnum.new Value(placeEnum.getValues().get((int) random)));
				break;

			default:
				break;
			}

		}

		try {
			if (lastSolution != null && lastResult != null){
				lastSolution.setGoalFunctionScore(problem.getFunction().getValue(lastResult));
				if (bestSolution == null || bestSolution.getGoalFunctionScore() < lastSolution.getGoalFunctionScore())
					bestSolution = lastSolution;
			}
		} catch (GoalFunctionException gfe) {
			gfe.printStackTrace();
			return null;
		}
		
		lastSolution = result;

		return result;
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see IOptimizer#checkTerminationCriteria(Problem)
	 */
	public boolean checkTerminationCriteria(Problem problem) {
		if (problem.getCounter() != -1) {
			if (problem.getCounter() > 0)
				return false;
		} else if (problem.getDueTime() != -1) {
			if ((calendar.getTimeInMillis() + (1000 * problem.getDueTime()) > System.currentTimeMillis()))
				return false;
		}
		return true;
	}

	private static void updateTerminationCriteria(Problem problem) {
		if (problem.getCounter() != -1) {
			problem.setCounter(problem.getCounter() - 1);
		}
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see IOptimizer#getBestSolution()
	 */
	public Solution getBestSolution() {
		return bestSolution;
	}
}
