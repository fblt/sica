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

import org.junit.Test;

import uni.stuttgart.rss.fachstudie.sica.data.MonitorResult;

public class JSGoalFunctionTest {

	@Test
	public void testEasy() throws Throwable {
		JSGoalFunction goal = null;
		goal = new JSGoalFunction("Monitor.CPU+Monitor.RAM");
		MonitorResult monRes = new MonitorResult();
		monRes.addValue("CPU", 5.3);
		monRes.addValue("RAM", -0.3);

		double result = goal.getValue(monRes);
		assertEquals(result, 5, 0.01);
	}

	@Test
	public void testNormal() throws Throwable {
		JSGoalFunction goal = null;
		goal = new JSGoalFunction("function goal(){return Monitor.CPU+Monitor.RAM;};");

		MonitorResult monRes = new MonitorResult();
		monRes.addValue("CPU", 5.3);
		monRes.addValue("RAM", -0.3);

		double result = goal.getValue(monRes);
		assertEquals(result, 5, 0.01);

	}

	@Test(expected = WrappedException.class)
	public void testFail() throws Throwable {
		JSGoalFunction goal = null;
		goal = new JSGoalFunction("function goal(){return \"Hallo\";};");

		MonitorResult monRes = new MonitorResult();
		monRes.addValue("CPU", 5.3);
		monRes.addValue("RAM", -0.3);

		try {
			goal.getValue(monRes);
		} catch (GoalFunctionException e) {
			throw new WrappedException(e);
		}

	}

	@SuppressWarnings("serial") /* never serialized */
	private static class WrappedException extends Throwable {
		public WrappedException(Throwable wrapped) {
			super(wrapped);
		}
	}

}
