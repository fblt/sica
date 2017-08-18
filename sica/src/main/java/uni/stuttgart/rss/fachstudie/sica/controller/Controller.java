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
package uni.stuttgart.rss.fachstudie.sica.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;

import uni.stuttgart.rss.fachstudie.sica.configuration.IConfigurator;
import uni.stuttgart.rss.fachstudie.sica.configuration.ProblemCreator;
import uni.stuttgart.rss.fachstudie.sica.configuration.TaurusConfigurator;
import uni.stuttgart.rss.fachstudie.sica.data.MonitorResult;
import uni.stuttgart.rss.fachstudie.sica.data.Problem;
import uni.stuttgart.rss.fachstudie.sica.data.Solution;
import uni.stuttgart.rss.fachstudie.sica.driver.ITestDriver;
import uni.stuttgart.rss.fachstudie.sica.driver.TaurusDriver;
import uni.stuttgart.rss.fachstudie.sica.driver.TestDriverObservable;
import uni.stuttgart.rss.fachstudie.sica.monitor.DefaultMonitor;
import uni.stuttgart.rss.fachstudie.sica.monitor.IMonitor;
import uni.stuttgart.rss.fachstudie.sica.optimizer.DifferentialEvolutionOptimizer;
import uni.stuttgart.rss.fachstudie.sica.optimizer.GoalFunctionException;
import uni.stuttgart.rss.fachstudie.sica.optimizer.IOptimizer;
import uni.stuttgart.rss.fachstudie.sica.report.AbstractReport;
import uni.stuttgart.rss.fachstudie.sica.report.HTMLVisualTableReport;

public class Controller implements Observer {

	private IMonitor monitor;
	private ITestDriver driver;
	private Problem problem;
	private IOptimizer optimizer;
	private IConfigurator configurator;
	private AbstractReport report;

	// flag for single test
	private boolean singleTest = false;
	private Solution lastOptSolution;

	public static void main(String[] args) {
		if (args.length > 0) {
			Controller controller = new Controller(Paths.get(args[0]));
			controller.startFirstTest();
		} else {
			System.err.println("No command line argument given!\nproper usage: sica <config file name>");
			System.exit(1);
		}
	}

	public Controller(Path problemFile) {
		this.optimizer = new DifferentialEvolutionOptimizer();
		this.configurator = new TaurusConfigurator();
		this.driver = new TaurusDriver();
		this.report = new HTMLVisualTableReport();
		

		
		this.problem = new ProblemCreator(problemFile).generateProblem();

		this.monitor = new DefaultMonitor(this.problem);
		
		monitor.setSteadyStateWaitTime(problem.getSteadyStateWaitTimeInMs());
	}

	@Override
	public void update(Observable o, Object arg) {
		if (singleTest)
			return;
		if (arg instanceof MonitorResult){
			this.driver.stopTest();
			this.report.addMonitorResult((MonitorResult) arg);

			
			try {
				lastOptSolution.setGoalFunctionScore(problem.getFunction().getValue((MonitorResult)arg));
			} catch (GoalFunctionException e) {
				e.printStackTrace();
			}

			this.report.generateReport(problem, Paths.get("report.html"));
			
			startTest((MonitorResult) arg);
		}

	}

	public void startFirstTest() {
		lastOptSolution = optimizer.optimize(problem);

		this.report.addSolution(lastOptSolution);
		
		
		
		File file = configurator.generateNewTestFile(problem, lastOptSolution);
		TestDriverObservable testDriverObservable = driver.startTest(file);
		this.monitor.startMonitoring(testDriverObservable);
		
		}

	public void startSingleTest() {
		singleTest = true;
		// TODO implementation
	}

	protected void startTest(MonitorResult result) {
		optimizer.notifyNewMonitorResult(result);

		lastOptSolution = optimizer.optimize(problem);
		// optimizer finished
		if(lastOptSolution == null){
			this.report.addSolution(this.optimizer.getBestSolution());
			this.report.addMonitorResult(new MonitorResult());
			
			this.report.generateReport(problem, Paths.get("report.html"));
			this.driver.stopTest();
			return;
		}
		
		this.report.addSolution(lastOptSolution);
		
		File file = configurator.generateNewTestFile(problem, lastOptSolution);
		TestDriverObservable testDriverObservable = driver.startTest(file);
		this.monitor.startMonitoring(testDriverObservable);

	}

	public IMonitor getMonitor() {
		return monitor;
	}

	public void setMonitor(IMonitor monitor) {
		this.monitor = monitor;
	}

	public ITestDriver getDriver() {
		return driver;
	}

	public void setDriver(ITestDriver driver) {
		this.driver = driver;
	}

	public IOptimizer getOptimizer() {
		return optimizer;
	}

	public void setOptimizer(IOptimizer optimizer) {
		this.optimizer = optimizer;
	}

	public IConfigurator getConfigurator() {
		return configurator;
	}

	public void setConfigurator(IConfigurator configurator) {
		this.configurator = configurator;
	}

	public Problem getProblem() {
		return problem;
	}

	// getter & setter for configuration

}
