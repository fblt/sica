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
package uni.stuttgart.rss.fachstudie.sica.driver;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

import junit.framework.TestCase;

@Ignore
public class TaurusDriverTest extends TestCase {

	private TaurusDriver driver = new TaurusDriver();
	
	@Test
	public void testTaurusAvailable(){
		System.out.println("Taurus available: "+driver.isTaurusAvailable());
	}
	
	@Test
	public void testInstallTaurus(){
		if(!driver.isTaurusAvailable()){
			driver.installTaurus();
		}
	}
	
	@Test
	public void testInstallLocust(){
		//driver.installLocust();
	}
	
	@Test
	public void testStartTest(){
		driver.startTest(new File("TaurusConfig.yml"));
	}
}
