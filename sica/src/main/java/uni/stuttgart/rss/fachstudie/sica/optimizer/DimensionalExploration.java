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

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.interpolation.MicrosphereInterpolator;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BOBYQAOptimizer;

import uni.stuttgart.rss.fachstudie.sica.data.Dimension;
import uni.stuttgart.rss.fachstudie.sica.data.MonitorResult;
import uni.stuttgart.rss.fachstudie.sica.data.Placeholder;
import uni.stuttgart.rss.fachstudie.sica.data.PlaceholderDouble;
import uni.stuttgart.rss.fachstudie.sica.data.PlaceholderEnumeration;
import uni.stuttgart.rss.fachstudie.sica.data.PlaceholderInteger;
import uni.stuttgart.rss.fachstudie.sica.data.Problem;
import uni.stuttgart.rss.fachstudie.sica.data.Range;
import uni.stuttgart.rss.fachstudie.sica.data.Solution;

public class DimensionalExploration implements IOptimizer {

	private LinkedList<MonitorResult> monitorResults = new LinkedList<>();
	private LinkedList<Solution> solutionList = new LinkedList<>();
	private Set<Dimension> dimensions = new HashSet<>();
	private LocalDateTime time;
	private Solution referenceSolution;
	private ArrayList<Placeholder> keys;
	private int maxCount = -1;

	private boolean firstOptimize = true;
	private boolean randomSample = true;

	public DimensionalExploration() {
		time = LocalDateTime.now();
	}

	@Override
	public void notifyNewMonitorResult(MonitorResult result) {
		monitorResults.add(result);

	}

	@Override
	public Solution optimize(Problem problem) {

		if (checkTerminationCriteria(problem)) {
			return null;
		}

		if (firstOptimize) {
			System.out.println("--> start random sampling");
			keys = new ArrayList<>(problem.getPlaceholders());
			keys.sort(new Comparator<Placeholder>() {
				@Override
				public int compare(Placeholder o1, Placeholder o2) {
					return o1.getId().compareToIgnoreCase(o2.getId());
				}
			});
			firstOptimize = false;
			System.out.println("--> quitting random sampling");
			maxCount = problem.getCounter();
			generateDimensions(problem);
		}

		if (randomSample) {
			referenceSolution = generateReferenceSolution(problem);
			solutionList.add(referenceSolution);
			checkRandomSampleCriteria(problem);
			updateTerminationCriteria(problem);
			System.out.println("--> random sampling");
			return referenceSolution;
		}

		if (solutionList.size() != monitorResults.size()) {
			throw new IllegalArgumentException();
		}

		MultivariateFunction func = interpolate(problem);
		double[] doubleArray = bobyqaMaximum(func);

		Solution s = new Solution();

		for (int i = 0; i < keys.size(); i++) {
			Placeholder local = keys.get(i);
			switch (local.getType()) {
			case DOUBLE:
				PlaceholderDouble doublePlaceholder = (PlaceholderDouble) local;
				s.addSolutionValue(doublePlaceholder, doublePlaceholder.new Value(doubleArray[i]));
				break;
			case ENUMERATION:
				PlaceholderEnumeration enumPlaceholder = (PlaceholderEnumeration) local;
				s.addSolutionValue(enumPlaceholder,
						enumPlaceholder.new Value(enumPlaceholder.getValues().get((int) doubleArray[i])));
				break;
			case INTEGER:
				PlaceholderInteger integerPlaceholder = (PlaceholderInteger) local;
				s.addSolutionValue(integerPlaceholder, integerPlaceholder.new Value((int) doubleArray[i]));
				break;
			default:
				break;
			}
		}

		solutionList.add(s);
		updateTerminationCriteria(problem);
		System.out.println("--> calculated sampling");
		return s;
	}

	private void checkRandomSampleCriteria(Problem problem) {
		if (problem.getCounter() != -1) {
			if (problem.getCounter() < 0.5 * maxCount) {
				System.out.println("--> quitting random sampling");
				randomSample = false;
			}
		} else if (problem.getDueTime() != -1) {
			if ((time.toInstant(ZoneOffset.ofHours(0)).toEpochMilli() + (1000 * problem.getDueTime() * 0.5) > System
					.currentTimeMillis())) {
				System.out.println("--> quitting random sampling");
				randomSample = false;
			}
		}
	}

	@Override
	public Solution getBestSolution() {

		Solution s = solutionList.getFirst();
		if (s == null) {
			return null;
		}
		return solutionList.stream().skip(1).reduce(s,
				(x, y) -> x.getGoalFunctionScore() > y.getGoalFunctionScore() ? x : y);
	}

	@Override
	public boolean checkTerminationCriteria(Problem problem) {
		if (problem.getCounter() != -1) {
			if (problem.getCounter() > 0)
				return false;
		} else if (problem.getDueTime() != -1) {
			if ((time.toInstant(ZoneOffset.ofHours(0)).toEpochMilli() + (1000 * problem.getDueTime()) > System
					.currentTimeMillis()))
				return false;
		}
		return true;
	}

	private static void updateTerminationCriteria(Problem problem) {
		if (problem.getCounter() != -1) {
			problem.setCounter(problem.getCounter() - 1);
		}
	}

	private void generateDimensions(Problem problem) {
		for (Placeholder p : problem.getPlaceholders()) {
			Dimension dim = new Dimension(p.getId());
			dimensions.add(dim);
		}
	}

	private MultivariateFunction interpolate(Problem p) {
		MicrosphereInterpolator interpolator = new MicrosphereInterpolator();
		double[][] testMatrix = new double[1][2];
		System.out.println(testMatrix);
		double[][] inputMatrix = new double[solutionList.size()][p.getPlaceholders().size()];
		double[] outputVector = new double[solutionList.size()];

		for (int solutionCounter = 0; solutionCounter < solutionList.size(); solutionCounter++) {
			Solution localSolution = solutionList.get(solutionCounter);

			for (int placeCounter = 0; placeCounter < p.getPlaceholders().size(); placeCounter++) {
				inputMatrix[solutionCounter][placeCounter] = mapPlaceholderValueToDouble(
						localSolution.getSolutionMap().get(keys.get(placeCounter)));
			}

			outputVector[solutionCounter] = solutionList.get(solutionCounter).getGoalFunctionScore();
		}

		return interpolator.interpolate(inputMatrix, outputVector);
	}

	private double mapPlaceholderValueToDouble(Placeholder.Value value) {
		double d = 0;

		switch (value.outer().getType()) {
		case INTEGER:
			PlaceholderInteger.Value integerValue = (PlaceholderInteger.Value) value;
			d = integerValue.getVal();
			break;
		case DOUBLE:
			PlaceholderDouble.Value doubleValue = (PlaceholderDouble.Value) value;
			d = doubleValue.getVal();
			break;
		case ENUMERATION:
			PlaceholderEnumeration.Value enumValue = (PlaceholderEnumeration.Value) value;
			d = enumValue.outer().getValues().indexOf(enumValue.getVal());
			break;

		default:
			break;
		}

		return d;
	}

	private Solution generateReferenceSolution(Problem problem) {
		Solution solution = new Solution();

		for (Placeholder p : problem.getPlaceholders()) {
			switch (p.getType()) {
			case INTEGER:
				PlaceholderInteger integerPlaceholder = (PlaceholderInteger) p;
				int integerValue = (int) ((integerPlaceholder.getRange().getMax()
						- integerPlaceholder.getRange().getMin()) * Math.random()
						+ integerPlaceholder.getRange().getMin());
				solution.addSolutionValue(integerPlaceholder, integerPlaceholder.new Value(integerValue));
				break;

			case DOUBLE:
				PlaceholderDouble doublePlaceholder = (PlaceholderDouble) p;
				double doubleValue = (doublePlaceholder.getRange().getMax() - doublePlaceholder.getRange().getMin())
						* Math.random() + doublePlaceholder.getRange().getMin();
				solution.addSolutionValue(doublePlaceholder, doublePlaceholder.new Value(doubleValue));
				break;

			case ENUMERATION:
				PlaceholderEnumeration enumerationPlaceholder = (PlaceholderEnumeration) p;
				int enumerationValue = (int) ((p.getEffectiveRange().getMax() - p.getEffectiveRange().getMin())
						* Math.random() + p.getEffectiveRange().getMin());
				solution.addSolutionValue(enumerationPlaceholder,
						enumerationPlaceholder.new Value(enumerationPlaceholder.getValues().get(enumerationValue)));
				break;

			default:
				break;
			}
		}

		return solution;
	}

	private double[] bobyqaMaximum(MultivariateFunction func) {
		int dim = dimensions.size();
		double[] lowerBounds = new double[dim];
		double[] upperBounds = new double[dim];
		double[] initial = new double[dim];

		for (int i = 0; i < dim; i++) {
			Range r = keys.get(i).getEffectiveRange();
			double min = r.getMin();
			double max = r.getMax();
			lowerBounds[i] = min;
			upperBounds[i] = max;
			initial[i] = (min + max) / 2;
		}

		BOBYQAOptimizer bobyqa = new BOBYQAOptimizer(dim + 2);

		return bobyqa.optimize(//
				new ObjectiveFunction(func), //
				new SimpleBounds(lowerBounds, upperBounds), //
				new InitialGuess(initial), //
				GoalType.MAXIMIZE, //
				MaxEval.unlimited() //
		).getKey();

	}
}
