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

import java.io.PrintWriter;

import com.google.gson.GsonBuilder;

import uni.stuttgart.rss.fachstudie.sica.data.MonitorResult;
import uni.stuttgart.rss.fachstudie.sica.data.Problem;
import uni.stuttgart.rss.fachstudie.sica.data.Solution;

public class JSONReport extends AbstractReport {

	@Override
	public void printReport(Problem problem, PrintWriter output) {
		GsonBuilder gb = new GsonBuilder();
		gb.registerTypeAdapter(Solution.class, new SolutionSerializer());
		gb.registerTypeAdapter(MonitorResult.class, new ResultSerializer());
		output.println(gb.create().toJson(reportData));
	}

}
