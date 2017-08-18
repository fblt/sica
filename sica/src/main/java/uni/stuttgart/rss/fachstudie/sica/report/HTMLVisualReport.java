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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import uni.stuttgart.rss.fachstudie.sica.data.Problem;
import uni.stuttgart.rss.fachstudie.sica.util.IO;

public class HTMLVisualReport extends AbstractReport {
	@Override
	public void printReport(Problem problem, PrintWriter printWriter) {
		try (BufferedReader br = IO.bufferedResource(HTMLVisualTableReport.class, "HTMLVisualReport_template.html")) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("{{json}}")) {
					JSONReport jsonReport = new JSONReport();
					this.reportData.getResults().forEach(jsonReport::addMonitorResult);
					this.reportData.getSolutions().forEach(jsonReport::addSolution);
					jsonReport.printReport(problem, printWriter);
				} else {
					printWriter.println(line);
				}
			}

		} catch (IOException e) {
			throw new ReportException("Could not write report", e);
		}
	}


}
