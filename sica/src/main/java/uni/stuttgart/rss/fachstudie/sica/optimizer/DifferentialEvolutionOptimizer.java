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

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;

import uni.stuttgart.rss.fachstudie.sica.data.MonitorResult;
import uni.stuttgart.rss.fachstudie.sica.data.Placeholder;
import uni.stuttgart.rss.fachstudie.sica.data.PlaceholderDouble;
import uni.stuttgart.rss.fachstudie.sica.data.PlaceholderEnumeration;
import uni.stuttgart.rss.fachstudie.sica.data.PlaceholderInteger;
import uni.stuttgart.rss.fachstudie.sica.data.Problem;
import uni.stuttgart.rss.fachstudie.sica.data.Solution;

public class DifferentialEvolutionOptimizer implements IOptimizer {
	private MonitorResult lastResult;
	private Solution bestSolution = null;
	private GregorianCalendar calendar;
	private ArrayList<Solution> population;
	private Solution currentSolution;

	private float crossoverRate = 0.9f;
	private float differentialWeight = 0.5f;

	public DifferentialEvolutionOptimizer() {
		calendar = new GregorianCalendar();
		population = new ArrayList<>();
	}

	@Override
	public void notifyNewMonitorResult(MonitorResult result) {
		lastResult = result;

	}

	@Override
	public Solution optimize(Problem problem) {
		// calculate goal function score of current run
		if (currentSolution != null && lastResult != null) {
			if (bestSolution == null || bestSolution.getGoalFunctionScore() < currentSolution.getGoalFunctionScore()) {
				bestSolution = currentSolution;
			}

		}

		if (checkTerminationCriteria(problem)) {
			return null;
		}

		// generate initial population
		if (population.isEmpty()) {
			if (problem.getPlaceholders().size() == 0) {
				return null;
			}
			generateInitialPopulation(problem);
		}

		// check whether all initial solutions have been tested
		for (Solution s : population) {
			if (Double.isNaN(s.getGoalFunctionScore())) {
				currentSolution = s;
				return s;
			}
		}

		Solution s = null;

		removeDuplicates();

		do {
			Solution a, b, c;
			population.sort((x, y) -> -Double.compare(x.getGoalFunctionScore(), y.getGoalFunctionScore()));
			if (Math.random() > 0.8) {
				Collections.shuffle(population);
			}

			a = population.get(0);
			b = population.get(1);
			if(population.size()>2){
				c = population.get(2);
			}else{
				c = population.get(0);
			}

			s = new Solution(a);
			for (Placeholder p : problem.getPlaceholders()) {
				if (Math.random() < crossoverRate) {
					Placeholder.Value val = getCrossOverValue(p, s, b, c);
					s.getSolutionMap().put(p, val);
					/*for (int i = 0; i < 10000; i++) {
						Collections.shuffle(population);
						a = population.get(0);
						b = population.get(1);
						c = new Solution(population.get((int) (Math.round(Math.random()))));
						if (population.size() > 2) {
							c = population.get(2);
						}
						val = getCrossOverValue(p, s, b, c);
						if (val != null) {
							s.getSolutionMap().put(p, val);

							boolean isNew = true;
							for (Solution st : population) {
								if (st.equals(s) && st != s) {
									isNew = false;
								}
							}
							if (isNew) {
								break;
							}
						}
					}*/
					

				}
			}

			for(Solution t:population){
				if(t.equals(s)){
					s = null;
					break;
				}
			}

		} while (s == null || !IOptimizer.checkConstraints(problem, s));

		population.add(s);


		updateTerminationCriteria(problem);

		return s;
	}

	private void removeDuplicates() {
		for (int s2 = population.size() - 1; s2 >= 0; s2--) {
			for (int s1 = s2 - 1; s1 >= 0; s1--) {
				if (population.get(s2).equals(population.get(s1))) {
					population.get(s1).setGoalFunctionScore((population.get(s1).getGoalFunctionScore()+population.get(s2).getGoalFunctionScore())/2);
					population.remove(s2);

					if (population.size() < 4) {
						return;
					}
					break;
				}
			}
		}
	}

	private Placeholder.Value getCrossOverValue(Placeholder p, Solution n, Solution b, Solution c) {
		switch (p.getType()) {
		case DOUBLE:
			PlaceholderDouble pd = (PlaceholderDouble) p;
			PlaceholderDouble.Value pn = (PlaceholderDouble.Value) n.getValue(p);
			PlaceholderDouble.Value pb = (PlaceholderDouble.Value) b.getValue(p);
			PlaceholderDouble.Value pc = (PlaceholderDouble.Value) c.getValue(p);

			return pd.new Value(Math.max(pd.getRange().getMin(),
					Math.min(pd.getRange().getMax(), (pn.getVal() + pb.getVal()) / 2)));
		case INTEGER:
			PlaceholderInteger pid = (PlaceholderInteger) p;
			PlaceholderInteger.Value pin = (PlaceholderInteger.Value) n.getValue(p);
			PlaceholderInteger.Value pib = (PlaceholderInteger.Value) b.getValue(p);
			PlaceholderInteger.Value pic = (PlaceholderInteger.Value) c.getValue(p);

			int newVali = Math.round((pin.getVal() + pib.getVal()) / 2);

			if (newVali > pid.getRange().getMax() || newVali < pid.getRange().getMin()) {
				return null;
			}

			return pid.new Value(newVali);
		case ENUMERATION:
			PlaceholderEnumeration ped = (PlaceholderEnumeration) p;
			PlaceholderEnumeration.Value pen = (PlaceholderEnumeration.Value) n.getValue(p);
			PlaceholderEnumeration.Value peb = (PlaceholderEnumeration.Value) b.getValue(p);
			PlaceholderEnumeration.Value pec = (PlaceholderEnumeration.Value) c.getValue(p);

			int an = ped.getValues().indexOf(pen.getVal());
			int bn = ped.getValues().indexOf(peb.getVal());
			int cn = ped.getValues().indexOf(pec.getVal());

			int newVal = Math.round((an +bn)/2);
			if (newVal >= 0 && newVal < ped.getValues().size()) {
				return ped.new Value(ped.getValues().get(newVal));
			}
			return pen;

		default:
			throw new IllegalArgumentException(
					"Placeholder is neither PlaceholderDouble, PlaceholderInteger nor PlaceholderEnumeration");		
		}
	}

	private void generateInitialPopulation(Problem problem) {
		population = new ArrayList<>();

		for (Placeholder p : problem.getPlaceholders()) {
			Solution minSol = new Solution();
			Solution maxSol = new Solution();
			minSol.addSolutionValue(p, getPlaceholderMinValue(p));
			maxSol.addSolutionValue(p, getPlaceholderMaxValue(p));

			for (Placeholder q : problem.getPlaceholders()) {
				if (p == q) {
					continue;
				}

				if (Math.random() > 0.5) {
					minSol.addSolutionValue(q, getPlaceholderMinValue(q));
				} else {
					minSol.addSolutionValue(q, getPlaceholderMaxValue(q));
				}
				if (Math.random() > 0.5) {
					maxSol.addSolutionValue(q, getPlaceholderMinValue(q));
				} else {
					maxSol.addSolutionValue(q, getPlaceholderMaxValue(q));
				}
			}

			population.add(minSol);
			population.add(maxSol);
		}

	}


	/**
	 * returns a value for the minimum value of the placeholder
	 * 
	 * @param ph
	 * @return
	 */
	private static Placeholder.Value getPlaceholderMinValue(Placeholder ph) {
		if (ph instanceof PlaceholderDouble) {
			PlaceholderDouble pd = (PlaceholderDouble) ph;
			return pd.new Value(pd.getRange().getMin());
		} else if (ph instanceof PlaceholderInteger) {
			PlaceholderInteger pi = (PlaceholderInteger) ph;
			return pi.new Value((int) pi.getRange().getMin());
		} else if (ph instanceof PlaceholderEnumeration) {
			PlaceholderEnumeration pe = (PlaceholderEnumeration) ph;
			return pe.new Value(pe.getValues().get(0));
		}
		throw new IllegalArgumentException(
				"Placeholder is neither PlaceholderDouble, PlaceholderInteger nor PlaceholderEnumeration");
	}

	/**
	 * returns a value for the maximum value of the placeholder
	 * 
	 * @param ph
	 * @return
	 */
	private static Placeholder.Value getPlaceholderMaxValue(Placeholder ph) {
		if (ph instanceof PlaceholderDouble) {
			PlaceholderDouble pd = (PlaceholderDouble) ph;
			return pd.new Value(pd.getRange().getMax());
		} else if (ph instanceof PlaceholderInteger) {
			PlaceholderInteger pi = (PlaceholderInteger) ph;
			return pi.new Value((int) pi.getRange().getMax());
		} else if (ph instanceof PlaceholderEnumeration) {
			PlaceholderEnumeration pe = (PlaceholderEnumeration) ph;
			return pe.new Value(pe.getValues().get(pe.getValues().size() - 1));
		}
		throw new IllegalArgumentException(
				"Placeholder is neither PlaceholderDouble, PlaceholderInteger nor PlaceholderEnumeration");
	}

	@Override
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
	public Solution getBestSolution() {
		return bestSolution;
	}
}
