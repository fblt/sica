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

import java.io.IOException;

import org.hyperic.sigar.Sigar;

import com.google.gson.Gson;

import spark.Spark;
import uni.stuttgart.rss.fachstudie.sica.data.MonitorResult;
import uni.stuttgart.rss.fachstudie.sicamonitor.monitors.CpuMonitor;
import uni.stuttgart.rss.fachstudie.sicamonitor.monitors.MemoryMonitor;
import uni.stuttgart.rss.fachstudie.sicamonitor.monitors.NetMonitor;
import uni.stuttgart.rss.fachstudie.sicamonitor.monitors.SwapMonitor;

public class MonitorServer {
	private static MemoryMonitor memMonitor = new MemoryMonitor();
	private static CpuMonitor cpuMonitor = new CpuMonitor();
	private static NetMonitor netMonitor = new NetMonitor();
	private static SwapMonitor swapMonitor = new SwapMonitor();

	static {
		try {
			NativeLibs.prepareLibraries();
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize monitor: cannot unpack libraries");
		}
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			// default settings
		} else if (args.length == 1) {
			int port = Integer.parseInt(args[0]);
			if (port < 1 || port > 65535) {
				throw new IllegalArgumentException("port " + port + " is out of range");
			}
			Spark.port(port);
		} else {
			throw new IllegalArgumentException("too many arguments provided");
		}
		
		Gson gson = new Gson();
		Spark.get("/", (req, res) -> {
			res.status(302);
			res.header("Location", "/status");
			res.type("text/plain");
			return "Redirecting to /status";
		});
		Spark.get("/status", (req, res) -> {
			res.type("application/json");
			return gson.toJson(monitor());
		});
	}

	public static MonitorResult monitor() {
		MonitorResult res = new MonitorResult();
		Sigar sigar = new Sigar();
		memMonitor.fillIn(res, sigar);
		cpuMonitor.fillIn(res, sigar);
		netMonitor.fillIn(res, sigar);
		swapMonitor.fillIn(res, sigar);
		return res;
	}
}
