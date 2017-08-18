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
package uni.stuttgart.rss.fachstudie.sica.report;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import uni.stuttgart.rss.fachstudie.sica.data.MonitorResult;
import uni.stuttgart.rss.fachstudie.sica.data.Problem;
import uni.stuttgart.rss.fachstudie.sica.data.ReportData;
import uni.stuttgart.rss.fachstudie.sica.data.Solution;
import uni.stuttgart.rss.fachstudie.sica.util.IO;

/**
 * Represents a reporter that records monitor results and solutions and writes a
 * report to an output file
 */
public abstract class AbstractReport {
	protected ReportData reportData;

	public AbstractReport() {
		this.reportData = new ReportData();
	}

	/** Add the given monitor result to the report */
	public void addMonitorResult(MonitorResult res) {
		this.reportData.getResults().add(res);
	}

	/** Add the given solution to the report */
	public void addSolution(Solution sol) {
		this.reportData.getSolutions().add(sol);
	}

	/** Write the report to the given output file */
	public void generateReport(Problem problem, Path output) {
		try (PrintWriter writer = IO.newPrintWriter(output, StandardCharsets.UTF_8)) {
			printReport(problem, writer);
		} catch (IOException e) {
			throw new ReportException("Could not write report", e);
		}
	}

	/** Write the report to the given output file */
	public abstract void printReport(Problem problem, PrintWriter output);

}
