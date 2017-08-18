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

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.IntStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import uni.stuttgart.rss.fachstudie.sica.configuration.JSONConfHandler;
import uni.stuttgart.rss.fachstudie.sica.data.MonitorResult;
import uni.stuttgart.rss.fachstudie.sica.data.Problem;
import uni.stuttgart.rss.fachstudie.sica.data.Solution;
import uni.stuttgart.rss.fachstudie.sica.optimizer.RandomOptimizer;
import uni.stuttgart.rss.fachstudie.sica.util.IO;
import uni.stuttgart.rss.fachstudie.sica.util.Values;

public abstract class AbstractReportTest {

	protected Gson gson;
	protected Problem problem;
	protected Path tempFile;
	protected AbstractReport report;
	public static final String pathBase = "uni/stuttgart/rss/fachstudie/sica/report/";

	@Before
	public abstract void setUp() throws Exception;

	public void setUp(String configPath) throws Exception {
		gson = new Gson();
		problem = new Problem(JSONConfHandler.parseConfiguration(getJson(configPath)));
		tempFile = Files.createTempFile(getClass().getSimpleName() + "-test-", "");
	}

	@After
	public void tearDown() throws Exception {
		// Files.deleteIfExists(tempFile);
		System.out.println(tempFile);
		Files.deleteIfExists(Paths.get("/tmp/lastreport.html"));
		Files.createSymbolicLink(Paths.get("/tmp/lastreport.html"), tempFile);
	}

	protected String getJson(String filename) {
		try (InputStream is = getClass().getClassLoader().getResourceAsStream(filename)) {
			return IO.readWholeStream(is);
		} catch (IOException e) {
			throw new RuntimeException("cannot read test input", e);
		}
	}

	@Test
	public void testWindows() throws IOException {
		String[] testJsons = IntStream.range(0, 16)//
				.mapToObj(x -> String.format("windows-%02d.json", x))//
				.map(x -> pathBase + x)//
				.map(this::getJson)//
				.toArray(String[]::new);

		RandomOptimizer optimizer = new RandomOptimizer();

		for (int i = 0; i < testJsons.length; i++) {
			String json = testJsons[i];
			MonitorResult result = gson.fromJson(json, MonitorResult.class);

			report.addMonitorResult(result);
			optimizer.notifyNewMonitorResult(result);
			Solution sol = optimizer.optimize(problem);
			Values.not(Objects::isNull, sol, "Got a null report for result " + i);
			report.addSolution(sol);
		}

		report.generateReport(problem, tempFile);

		assertTrue("tempFile must not be empty", Files.size(tempFile) > 0);
	}

}
