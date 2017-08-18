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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Map;

import uni.stuttgart.rss.fachstudie.sica.data.Placeholder;
import uni.stuttgart.rss.fachstudie.sica.data.PlaceholderDouble;
import uni.stuttgart.rss.fachstudie.sica.data.PlaceholderEnumeration;
import uni.stuttgart.rss.fachstudie.sica.data.PlaceholderInteger;
import uni.stuttgart.rss.fachstudie.sica.data.Problem;
import uni.stuttgart.rss.fachstudie.sica.data.Solution;

public class TaurusConfigurator implements IConfigurator {

	/**
	 * generates a Taurus config file and a script file with replacements
	 */
	@Override
	public File generateNewTestFile(Problem problem, Solution solution) {
		String replacedTestFileName = problem.getOriginalTestFile().replace(".", "_temp.");

		// write replaced script file
		File scriptFile = new File(replacedTestFileName);
		String line;
		if (problem.getOriginalTestFile() != null) {
			try (//
					PrintWriter writer = new PrintWriter(scriptFile);
					InputStream fis = new FileInputStream(problem.getOriginalTestFile());
					InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
					BufferedReader br = new BufferedReader(isr);) {
				while ((line = br.readLine()) != null) {
					line = replaceLine(line, problem, solution, replacedTestFileName);
					writer.println(line);
					writer.flush();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// write taurus config
		File taurusFile = new File("TaurusConfig.yml");
		try (//
				PrintWriter writer = new PrintWriter(taurusFile);
				InputStream fis = new FileInputStream("TaurusDefaultConfig.yml");
				InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
				BufferedReader br = new BufferedReader(isr);) {
			while ((line = br.readLine()) != null) {
				line = replaceLine(line, problem, solution, replacedTestFileName);
				writer.println(line);
				writer.flush();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//System.out.println("adapted file: "+taurusFile.getName());
		return taurusFile;
	}

	/**
	 * replace all placeholders in a single line
	 * 
	 * @param line
	 * @param problem
	 * @param solution
	 * @param replacedTestFileName
	 * @return
	 */
	private static String replaceLine(String line, Problem problem, Solution solution, String replacedTestFileName) {
		if (problem.getExecutor() != null) {
			line = line.replace("[executor]", problem.getExecutor());
		}

		if (problem.getSutAddress() != null) {
			line = line.replace("[SUT]", problem.getSutAddress());
		}

		line = line.replace("[script]", replacedTestFileName);

		// replace all current values for the placeholders
		for (Map.Entry<Placeholder, Placeholder.Value> entry : solution.getSolutionMap().entrySet()) {
			if (line.contains(entry.getKey().getId())) {
				if (entry.getValue() instanceof PlaceholderDouble.Value) {
					line = line.replace(entry.getKey().getId(),
							((PlaceholderDouble.Value) entry.getValue()).getVal() + "");
				} else if (entry.getValue() instanceof PlaceholderInteger.Value) {
					line = line.replace(entry.getKey().getId(),
							((PlaceholderInteger.Value) entry.getValue()).getVal() + "");
				} else if (entry.getValue() instanceof PlaceholderEnumeration.Value) {
					line = line.replace(entry.getKey().getId(),
							((PlaceholderEnumeration.Value) entry.getValue()).getVal() + "");
				}
			}
		}

		return line;
	}

}
