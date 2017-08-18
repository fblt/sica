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

import static uni.stuttgart.rss.fachstudie.sica.util.Values.notEmpty;
import static uni.stuttgart.rss.fachstudie.sica.util.Values.notNull;

import java.util.List;
import java.util.Map;

import uni.stuttgart.rss.fachstudie.sica.optimizer.GoalFunctionException;
import uni.stuttgart.rss.fachstudie.sica.optimizer.IGoalFunction;
import uni.stuttgart.rss.fachstudie.sica.optimizer.JSGoalFunction;

public class Problem {

	private String sutAddress;
	private String problemConfigurationFile;
	private String originalTestFile;
	private String executor;
	private int dueTime = -1;
	private int counter = -1;

	private long steadyStateWaitTimeInMs = 50000;
	private int monitorSamples = 5;
	private int monitorInterval = 5000;
	private String monitorAddress;
	private List<Placeholder> placeholders;
	private List<Constraint> constraints;
	private IGoalFunction function;

	public Problem() {
	}

	public Problem(ConfigurationFile conf) throws GoalFunctionException {
		this.constraints = notNull(conf.getConstraints());
		this.placeholders = notEmpty(conf.getPlaceholders());

		Map<String, String> values = notNull(conf.getConfigurations());
		this.sutAddress = notEmpty(values.get(ConfigurationFile.SUT_ADDRESS));
		this.problemConfigurationFile = null; // TODO how do I get the address?
		this.originalTestFile = notEmpty(values.get(ConfigurationFile.ORIGINAL_TEST_FILE));
		this.executor = notEmpty(values.get(ConfigurationFile.EXECUTOR));
		this.monitorAddress = notEmpty(values.get(ConfigurationFile.MONITOR_ADDRESS));

		String dueTimeString = values.get(ConfigurationFile.DUE_TIME);
		if (dueTimeString != null) {
			this.dueTime = Integer.parseInt(notEmpty(dueTimeString));
		}

		String counterString = values.get(ConfigurationFile.COUNTER);
		if (counterString != null) {
			this.counter = Integer.parseInt(notEmpty(counterString));
		}

		String steadyString = values.get(ConfigurationFile.STEADY_STATE_TIME);
		if (steadyString != null) {
			this.steadyStateWaitTimeInMs = Long.parseLong(steadyString);
		}

		String samplesString = values.get(ConfigurationFile.MONITOR_SAMPLES);
		if (samplesString != null) {
			this.monitorSamples = Integer.parseInt(notEmpty(samplesString));
		}

		String intervalString = values.get(ConfigurationFile.MONITOR_INTERVAL);
		if (intervalString != null) {
			this.monitorInterval = Integer.parseInt(notEmpty(intervalString));
		}

		this.function = new JSGoalFunction(notNull(values.get(ConfigurationFile.GOALFUNCTION)));

		// error handling
		if (dueTime == -1 && counter == -1) {
			throw new IllegalArgumentException("due Time and counter wasn't set");
		}

		if (steadyStateWaitTimeInMs == -1) {
			throw new IllegalArgumentException("steady state time wasn't set");
		}
	}

	public String getSutAddress() {
		return sutAddress;
	}

	public void setSutAddress(String sutAddress) {
		this.sutAddress = sutAddress;
	}

	public String getProblemConfigurationFile() {
		return problemConfigurationFile;
	}

	public void setProblemConfigurationFile(String problemConfigurationFile) {
		this.problemConfigurationFile = problemConfigurationFile;
	}

	public String getOriginalTestFile() {
		return originalTestFile;
	}

	public void setOriginalTestFile(String originalTestFile) {
		this.originalTestFile = originalTestFile;
	}

	public String getExecutor() {
		return executor;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	public List<Placeholder> getPlaceholders() {
		return placeholders;
	}

	public void setPlaceholders(List<Placeholder> placeholders) {
		this.placeholders = placeholders;
	}

	public List<Constraint> getConstraints() {
		return constraints;
	}

	public void setConstraints(List<Constraint> constraints) {
		this.constraints = constraints;
	}

	public IGoalFunction getFunction() {
		return function;
	}

	public void setFunction(IGoalFunction function) {
		this.function = function;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int count) {
		this.counter = count;
	}

	public int getDueTime() {
		return dueTime;
	}

	public void setDueTime(int dueTime) {
		this.dueTime = dueTime;
	}

	public long getSteadyStateWaitTimeInMs() {
		return steadyStateWaitTimeInMs;
	}

	public void setSteadyStateWaitTimeInMs(long steadyStateWaitTimeInMs) {
		this.steadyStateWaitTimeInMs = steadyStateWaitTimeInMs;
	}

	public int getMonitorSamples() {
		return monitorSamples;
	}

	public void setMonitorSamples(int monitorSamples) {
		this.monitorSamples = monitorSamples;
	}

	public int getMonitorInterval() {
		return monitorInterval;
	}

	public void setMonitorInterval(int monitorInterval) {
		this.monitorInterval = monitorInterval;
	}

	public String getMonitorAddress() {
		return monitorAddress;
	}

	public void setMonitorAddress(String monitorAddress) {
		this.monitorAddress = monitorAddress;
	}
}
