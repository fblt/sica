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

import java.util.DoubleSummaryStatistics;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import uni.stuttgart.rss.fachstudie.sica.data.MonitorResult;
import uni.stuttgart.rss.fachstudie.sicamonitor.SigarMonitor;

public class CpuMonitor extends SigarMonitor {
	@Override
	public void fillInThrowing(MonitorResult monres, Sigar sigar) throws SigarException {
		CpuPerc[] cpus = sigar.getCpuPercList();
		
		DoubleSummaryStatistics idleStat = new DoubleSummaryStatistics();
		DoubleSummaryStatistics usedStat = new DoubleSummaryStatistics();
		DoubleSummaryStatistics hirqStat = new DoubleSummaryStatistics();
		DoubleSummaryStatistics sirqStat = new DoubleSummaryStatistics();
		DoubleSummaryStatistics stolenStat = new DoubleSummaryStatistics();
		DoubleSummaryStatistics sysStat = new DoubleSummaryStatistics();
		DoubleSummaryStatistics userStat = new DoubleSummaryStatistics();
		DoubleSummaryStatistics waitStat = new DoubleSummaryStatistics();
		DoubleSummaryStatistics niceStat = new DoubleSummaryStatistics();
		
		for (int i = 0; i < cpus.length; i++) {
			double idle = cpus[i].getIdle();
			monres.addValue("cpu_"+i+"_idle", idle);
			idleStat.accept(idle);
			double used = cpus[i].getCombined();
			monres.addValue("cpu_"+i+"_used", used);
			usedStat.accept(used);
			double hirq = cpus[i].getIrq();
			monres.addValue("cpu_"+i+"_hirq", hirq);
			hirqStat.accept(hirq);
			double sirq = cpus[i].getSoftIrq();
			monres.addValue("cpu_"+i+"_sirq", sirq);
			sirqStat.accept(sirq);
			double stolen = cpus[i].getStolen();
			monres.addValue("cpu_"+i+"_stolen", stolen);
			stolenStat.accept(stolen);
			double sys = cpus[i].getSys();
			monres.addValue("cpu_"+i+"_sys", sys);
			sysStat.accept(sys);
			double user = cpus[i].getUser();
			monres.addValue("cpu_"+i+"_user", user);
			userStat.accept(user);
			double wait = cpus[i].getWait();
			monres.addValue("cpu_"+i+"_wait", wait);
			waitStat.accept(wait);
			double nice = cpus[i].getNice();
			monres.addValue("cpu_"+i+"_nice", nice);
			niceStat.accept(nice);
		}
		
		monres.addValue("cpu_avg_idle", idleStat.getAverage());
		monres.addValue("cpu_avg_used", usedStat.getAverage());
		monres.addValue("cpu_avg_hirq", hirqStat.getAverage());
		monres.addValue("cpu_avg_sirq", sirqStat.getAverage());
		monres.addValue("cpu_avg_stolen", stolenStat.getAverage());
		monres.addValue("cpu_avg_sys", sysStat.getAverage());
		monres.addValue("cpu_avg_user", userStat.getAverage());
		monres.addValue("cpu_avg_wait", waitStat.getAverage());
		monres.addValue("cpu_avg_nice", niceStat.getAverage());
	}
}
