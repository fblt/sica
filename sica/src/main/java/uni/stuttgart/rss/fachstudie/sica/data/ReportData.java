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

import java.util.LinkedList;
import java.util.List;

public class ReportData {
	private int version = 1;
	private List<MonitorResult> results;
	private List<Solution> solutions;

	public ReportData() {
		this.results = new LinkedList<>();
		this.solutions = new LinkedList<>();
	}

	public List<MonitorResult> getResults() {
		return results;
	}

	public void setResults(List<MonitorResult> results) {
		this.results = results;
	}

	public List<Solution> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<Solution> solutions) {
		this.solutions = solutions;
	}

	public int getVersion() {
		return version;
	}
}
