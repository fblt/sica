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
package uni.stuttgart.rss.fachstudie.sicamonitor.monitors;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import uni.stuttgart.rss.fachstudie.sica.data.MonitorResult;
import uni.stuttgart.rss.fachstudie.sicamonitor.SigarMonitor;

public class MemoryMonitor extends SigarMonitor {
	@Override
	public void fillInThrowing(MonitorResult monres, Sigar sigar) throws SigarException {
		Mem mem = sigar.getMem();
		monres.addValue("mem_total", mem.getTotal());
		monres.addValue("mem_free_without_caches", mem.getFree());
		monres.addValue("mem_free", mem.getActualFree());
		monres.addValue("mem_used_without_caches", mem.getUsed());
		monres.addValue("mem_used", mem.getActualUsed());
	}
}
