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
package uni.stuttgart.rss.fachstudie.sicamonitor;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uni.stuttgart.rss.fachstudie.sica.data.MonitorResult;

public abstract class SigarMonitor {
	private static Logger log = LoggerFactory.getLogger(SigarMonitor.class);

	public void fillIn(MonitorResult monres, Sigar sigar) {
		try {
			fillInThrowing(monres, sigar);
		} catch (SigarException e) {
			log.warn("could not fill in for " + getClass().getSimpleName(), e);
		}
	}

	abstract public void fillInThrowing(MonitorResult monres, Sigar sigar) throws SigarException;
}
