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

import org.junit.Ignore;
import org.junit.Test;

import junit.framework.TestCase;
import uni.stuttgart.rss.fachstudie.sica.data.Problem;
import uni.stuttgart.rss.fachstudie.sica.data.Solution;

@Ignore
public class TaurusConfiguratorTest extends TestCase {

	private TaurusConfigurator conf = new TaurusConfigurator();
	
	@Test
	public void testTaurusConfigurator(){
		
		Problem problem = new Problem();
		problem.setExecutor("temp");
		
		Solution solution = new Solution();
		
		/*try {
			PrintWriter writer = new PrintWriter("test.txt");
			writer.write("[executor] [executor] [executor]");
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		conf.generateNewTestFile(problem, solution);
		
		
		
	}
}
