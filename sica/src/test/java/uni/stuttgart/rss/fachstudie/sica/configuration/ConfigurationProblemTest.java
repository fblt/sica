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
package uni.stuttgart.rss.fachstudie.sica.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uni.stuttgart.rss.fachstudie.sica.data.ConfigurationFile;
import uni.stuttgart.rss.fachstudie.sica.data.Constraint;
import uni.stuttgart.rss.fachstudie.sica.data.Placeholder;
import uni.stuttgart.rss.fachstudie.sica.data.PlaceholderDouble;
import uni.stuttgart.rss.fachstudie.sica.data.Problem;

public class ConfigurationProblemTest {

	private static final String sampleConfig01 = "uni/stuttgart/rss/fachstudie/sica/configuration/sampleconfig01.json";
	private static final String sampleConfig02 = "uni/stuttgart/rss/fachstudie/sica/configuration/sampleconfig02.json";

	Path tempFile;

	@Before
	public void setUp() throws Exception {
		tempFile = Files.createTempFile("gsontest-", null);
	}

	@After
	public void tearDown() throws Exception {
		Files.delete(tempFile);
	}

	private void writeFile(String resourcePath) throws Exception {
		try (InputStream is = ConfigurationProblemTest.class.getClassLoader().getResourceAsStream(resourcePath)) {
			Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
		}
	}

	@Test
	public void testConfig1() throws Exception {
		writeFile(sampleConfig01);
		ConfigurationFile cf = JSONConfHandler.loadConfiguration(tempFile);
		// System.out.println(cf);
		conf1assertions(cf.getConstraints(), cf.getPlaceholders());
		
		ProblemCreator pc = new ProblemCreator(tempFile);
		Problem problem = pc.generateProblem();
		conf1assertions(problem.getConstraints(), problem.getPlaceholders());
		
	}

	private static void conf1assertions(List<Constraint> constraints, List<Placeholder> placeholders) {
		assertFalse("constraints must not be empty", constraints.isEmpty());
		assertFalse("placeholders must not be empty", placeholders.isEmpty());

		assertEquals("1 constraint provided", 1, constraints.size());
		assertSame("it's 'less than'", Constraint.ConstraintType.SMALLER, constraints.get(0).getConstType());
		Placeholder minWait = placeholders.stream().filter(x -> x.getId().equals("$min_wait")).findAny().get();
		Placeholder maxWait = placeholders.stream().filter(x -> x.getId().equals("$max_wait")).findAny().get();
		assertSame("constraint doesn't refer to the min_wait placeholder", minWait, constraints.get(0).getParam1());
		assertSame("constraint doesn't refer to the max_wait placeholder", maxWait, constraints.get(0).getParam2());
		assertEquals(minWait.getType(), Placeholder.Type.DOUBLE);
		PlaceholderDouble minWaitDbl = (PlaceholderDouble) minWait;
		assertEquals("range min doesn't match", 1., minWaitDbl.getRange().getMin(), 0.0001);
		assertEquals("range max doesn't match", 100., minWaitDbl.getRange().getMax(), 0.0001);
	}

	@Test
	public void testConfig2() throws Exception {
		writeFile(sampleConfig02);
		ConfigurationFile cf = JSONConfHandler.loadConfiguration(tempFile);
		// System.out.println(cf);

		List<Constraint> constraints = cf.getConstraints();
		List<Placeholder> placeholders = cf.getPlaceholders();
		assertFalse("constraints must not be empty", constraints.isEmpty());
		assertFalse("placeholders must not be empty", placeholders.isEmpty());
	}
}
