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

import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import uni.stuttgart.rss.fachstudie.sica.data.MonitorResult;
import uni.stuttgart.rss.fachstudie.sicamonitor.SigarMonitor;

public class NetMonitor extends SigarMonitor {
	@Override
	public void fillInThrowing(MonitorResult monres, Sigar sigar) throws SigarException {
		for (String interf : sigar.getNetInterfaceList()) {
			NetInterfaceStat ifstat = sigar.getNetInterfaceStat(interf);
			monres.addValue("net_"+interf+"_rx_bytes", ifstat.getRxBytes());
			monres.addValue("net_"+interf+"_rx_packets", ifstat.getRxPackets());
			monres.addValue("net_"+interf+"_rx_errors", ifstat.getRxErrors());
			monres.addValue("net_"+interf+"_rx_dropped", ifstat.getRxDropped());
			monres.addValue("net_"+interf+"_rx_overruns", ifstat.getRxOverruns());
			monres.addValue("net_"+interf+"_rx_frame", ifstat.getRxFrame());
			monres.addValue("net_"+interf+"_tx_bytes", ifstat.getTxBytes());
			monres.addValue("net_"+interf+"_tx_packets", ifstat.getTxPackets());
			monres.addValue("net_"+interf+"_tx_errors", ifstat.getTxErrors());
			monres.addValue("net_"+interf+"_tx_dropped", ifstat.getTxDropped());
			monres.addValue("net_"+interf+"_tx_overruns", ifstat.getTxOverruns());
			monres.addValue("net_"+interf+"_tx_collisions", ifstat.getTxCollisions());
			monres.addValue("net_"+interf+"_tx_carrier", ifstat.getTxCarrier());
			monres.addValue("net_"+interf+"_speed", ifstat.getSpeed());
		}
	}
}
