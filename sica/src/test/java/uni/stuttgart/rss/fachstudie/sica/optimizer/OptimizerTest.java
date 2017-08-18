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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import uni.stuttgart.rss.fachstudie.sica.data.Constraint;
import uni.stuttgart.rss.fachstudie.sica.data.Constraint.ConstraintType;
import uni.stuttgart.rss.fachstudie.sica.data.MonitorResult;
import uni.stuttgart.rss.fachstudie.sica.data.Placeholder;
import uni.stuttgart.rss.fachstudie.sica.data.PlaceholderDouble;
import uni.stuttgart.rss.fachstudie.sica.data.Problem;
import uni.stuttgart.rss.fachstudie.sica.data.Range;
import uni.stuttgart.rss.fachstudie.sica.data.Solution;

@Ignore
public class OptimizerTest {
	
	Problem p;
	private Constraint c1;
	
	@Before
	public void initialization() {
		p = new Problem();
		ArrayList<Placeholder> placeHolderList = new ArrayList<>();
		
		PlaceholderDouble pl1 = new PlaceholderDouble("pl1", new Range(0,  5));
		placeHolderList.add(pl1);
		PlaceholderDouble pl2 = new PlaceholderDouble("pl1", new Range(3, 8));
		placeHolderList.add(pl2);
		
		p.setPlaceholders(placeHolderList);
		
		c1 = new Constraint(pl1, pl2, ConstraintType.UNEQUALS);
		p.setConstraints(Arrays.asList(c1));
	}

	@Test
	public void test() {
		RandomOptimizer r = new RandomOptimizer();
		r.notifyNewMonitorResult(new MonitorResult());
		Solution s = r.optimize(p);
		
		assertEquals(2, s.getSolutionMap().size());
		assertTrue(c1.checkConstraint(s));
	}

}
