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

import java.io.IOException;
import java.nio.file.Path;

import uni.stuttgart.rss.fachstudie.sica.data.ConfigurationFile;
import uni.stuttgart.rss.fachstudie.sica.data.Problem;
import uni.stuttgart.rss.fachstudie.sica.optimizer.GoalFunctionException;

/** points to a file that holds a problem definition */
public class ProblemCreator {

	private final Path problemFile;

	/**
	 * @param problemFile
	 *            path to the referenced file
	 */
	public ProblemCreator(Path problemFile) {
		this.problemFile = problemFile;
	}

	/** loads and generates the Problem referenced by this ProblemCreator */
	public Problem generateProblem() {
		ConfigurationFile config;
		try {
			config = JSONConfHandler.loadConfiguration(problemFile);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}

		try {
			Problem p = new Problem(config);
			p.setProblemConfigurationFile(problemFile.toString());
			return p;
		} catch (NumberFormatException e) {
			throw new InvalidConfigurationException("can't parse termination creteria");
		} catch (GoalFunctionException e) {
			throw new InvalidConfigurationException("can't parse Goal function");
		}
	}

}
