
package uni.stuttgart.rss.fachstudie.sica.report;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import uni.stuttgart.rss.fachstudie.sica.data.MonitorResult;
import uni.stuttgart.rss.fachstudie.sica.data.Placeholder;
import uni.stuttgart.rss.fachstudie.sica.data.Placeholder.Value;
import uni.stuttgart.rss.fachstudie.sica.data.Problem;
import uni.stuttgart.rss.fachstudie.sica.data.Solution;
import uni.stuttgart.rss.fachstudie.sica.util.IO;

public class HTMLVisualTableReport extends AbstractReport {
	@Override
	public void printReport(Problem problem, PrintWriter printWriter) {
		try (BufferedReader br = IO.bufferedResource(HTMLVisualTableReport.class, "HTMLVisualTableReport_template.html")) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("{{json}}")) {
					JSONReport jsonReport = new JSONReport();
					this.reportData.getResults().forEach(jsonReport::addMonitorResult);
					this.reportData.getSolutions().forEach(jsonReport::addSolution);
					jsonReport.printReport(problem, printWriter);
				} else if (line.contains("{{report}}")) {

					printFinalState(printWriter);
					for (int i = Math.min(this.reportData.getResults().size(), this.reportData.getSolutions().size())
							- 1; i >= 0; i--) {
						printWriter.println("<h2>Run " + i + "</h2>");
						printState(printWriter, this.reportData.getResults().get(i),
								this.reportData.getSolutions().get(i));
					}
					printWriter.flush();
				}else{
					printWriter.println(line);
				}
			}

		} catch (IOException e) {
			throw new ReportException("Could not write report", e);
		}
	}

	private void printFinalState(PrintWriter printWriter) {
		printWriter.println("<h1>Final state</h1>");
		List<MonitorResult> results = this.reportData.getResults();
		List<Solution> solutions = this.reportData.getSolutions();
		printState(printWriter, results.get(results.size() - 1), solutions.get(solutions.size() - 1));
	}

	private static void printState(PrintWriter printWriter, MonitorResult monitor, Solution solution) {
		printWriter.println("<b>goal function score: " + solution.getGoalFunctionScore() + "</b><br><br>");

		// printWriter.println("<table width='100%'><tr><td width='50%'>");

		{
			printWriter.println("<details><summary>Parameters</summary>");
			printWriter.println("<table><tr><th>Placeholder</th><th>Value</th></tr>");
			for (Map.Entry<Placeholder, Value> entry : solution.getSolutionMap().entrySet()) {
				printWriter.println(
						"<tr><td>" + entry.getKey().getId() + "</td><td>" + entry.getValue().toString() + "</td></tr>");
			}
			printWriter.println("</table>");
			printWriter.println("</details>");
		}
		// printWriter.println("</td><td>");
		{
			printWriter.println("<details><summary>Monitor results</summary>");
			printWriter.println("<table><tr><th>Monitor metric</th><th>Value</th></tr>");

			Set<Map.Entry<String, Double>> entrySet = new TreeSet<>(Map.Entry.comparingByKey());
			entrySet.addAll(monitor.getValues().entrySet());

			for (Map.Entry<String, Double> entry : entrySet) {
				printWriter.println("<tr><td class='cellPaddingRight'>" + entry.getKey() + "</td><td>"
						+ entry.getValue() + "</td></tr>");
			}
			printWriter.println("</table>");
			printWriter.println("</details>");
		}

		// printWriter.println("</td></tr></table><br><br><br>");
	}

}
