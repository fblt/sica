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

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import uni.stuttgart.rss.fachstudie.sica.data.MonitorResult;

public class JSGoalFunction implements IGoalFunction {

	/** contains the unchanged string of the problem instance */
	private String baseCode;

	private ScriptEngine engine;

	/**
	 * creates the representation of a JSGoalFunction (supports ES 5.1 -
	 * Nashorn)
	 * 
	 * @param baseCode
	 *            code of the goal function (see doc)
	 * @throws GoalFunctionException
	 */
	public JSGoalFunction(String baseCode) throws GoalFunctionException {
		this.baseCode = baseCode;
		this.engine = new ScriptEngineManager().getEngineByName("nashorn");

		if (this.baseCode.contains("function")) {
			try {
				engine.eval("var Monitor; function callGoal(monitor){Monitor = monitor; return goal();}\n"
						+ baseCode.toString());
			} catch (ScriptException e) {
				throw new GoalFunctionException("Couldn't parse js code of goal function", e);
			}
		} else {
			try {
				engine.eval(
						"var Monitor; function callGoal(monitor){Monitor = monitor; return goal();}\n function goal(){ return "
								+ baseCode.toString() + ";\n}");
			} catch (ScriptException e) {
				throw new GoalFunctionException("Couldn't parse js code of goal function", e);
			}
		}

	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see IGoalFunction#getValue(MonitorResult)
	 */
	public double getValue(MonitorResult monitorResult) throws GoalFunctionException {

		Invocable invocable = (Invocable) engine;

		if (monitorResult == null) {
			return 0;
		}

		try {
			Object result = invocable.invokeFunction("callGoal", monitorResult.getValues());
			if (result instanceof Double || result instanceof Float || result instanceof Integer) {
				return (double) result;
			} else {
				throw new GoalFunctionException("Couldn't cast result of goal function to double");
			}
		} catch (NoSuchMethodException e) {
			throw new GoalFunctionException("Couldn't find goal function with signature function goal(){...}", e);
		} catch (ScriptException e) {
			throw new GoalFunctionException("Couldn't parse js code of goal function", e);
		}
	}

}
