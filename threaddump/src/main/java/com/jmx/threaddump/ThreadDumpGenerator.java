package com.jmx.threaddump;

import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class ThreadDumpGenerator {

	private static final String INDENT = "   ";
	private static final String INDENT_DOUBLE = "        ";

	
	private JMXConnector getJMXConnector(String ip, String jmxPort) throws Exception  {
		String jmxURL = "service:jmx:rmi:///jndi/rmi://"+ip+":"+jmxPort+"/jmxrmi" ;	
		JMXServiceURL url = new JMXServiceURL(jmxURL);
		JMXConnector jmxConnector = JMXConnectorFactory.connect(url);
		return jmxConnector;
	}
	
	private void closeJMXConnector(JMXConnector jmxConnector) {
		if(jmxConnector != null) {
			try  {
				jmxConnector.close();
			} catch(Exception e) { }
		}
	}
	
	private StringBuffer appendStackStrace(StringBuffer buffer, ThreadInfo ti) {
		StackTraceElement[] stacktrace = ti.getStackTrace();
		MonitorInfo[] monitors = ti.getLockedMonitors();
		LockInfo lock = ti.getLockInfo();
		for (int i = 0; i < stacktrace.length; i++) {
			String  ste = stacktrace[i].toString();
			buffer.append(INDENT_DOUBLE + "at " + ste + "\n");
			if(lock != null && i == 0){
				if(ste.contains("sun.misc.Unsafe.park")){
					buffer.append(INDENT_DOUBLE + "- parking to wait for <"+lock.getIdentityHashCode()+"> (a "+ lock.getClassName()+")" + " \n");
				}else{
					buffer.append(INDENT_DOUBLE + "- waiting on  <"+lock.getIdentityHashCode()+"> (a "+ lock.getClassName()+")" + " \n");
				}
			}
			for (MonitorInfo mi : monitors) {
				if (mi.getLockedStackDepth() == i) {
					buffer.append(INDENT_DOUBLE + "- locked  <"+mi.getIdentityHashCode()+"> (a "+ mi.getClassName()+")" + " \n");
				}
			}
		}
		return buffer; 
	}


	private StringBuffer appendThreadDetails(StringBuffer buffer, ThreadInfo ti) {
		buffer.append("\"" + ti.getThreadName() + "\"" + " - Thread t@"+ti.getThreadId()+"\n");
		buffer.append(INDENT+"java.lang.Thread.State: "+ti.getThreadState()+"\n");
		buffer = appendStackStrace(buffer, ti);
		buffer.append("\n");
		buffer.append(INDENT+"Locked ownable synchronizers:  "+"\n");
		LockInfo[] synchronisors = ti.getLockedSynchronizers();
		if(synchronisors != null && synchronisors.length > 0){
			for(LockInfo synchronisor: synchronisors){
				buffer.append(INDENT_DOUBLE + "- locked  <"+synchronisor.getIdentityHashCode()+"> (a "+ synchronisor.getClassName()+")" + " \n ");
			}
		}else {
			buffer.append(INDENT_DOUBLE+"- "+ "None"+" \n ");
		}

		return buffer;
	}
	
	
	public String getThreadDump(String ip, String jmxPort) throws Exception {
		JMXConnector jmxConnector = getJMXConnector(ip, jmxPort);
		StringBuffer stringBuffer = new StringBuffer(1000);
		try {
			if(jmxConnector != null) {
				MBeanServerConnection mbsc = jmxConnector.getMBeanServerConnection();
				ThreadMXBean threadMXBean = ManagementFactory.newPlatformMXBeanProxy(mbsc, 
																					 ManagementFactory.THREAD_MXBEAN_NAME, 
																					 ThreadMXBean.class);	
				ThreadInfo[] tinfos = threadMXBean.dumpAllThreads(true, true);			
				
				for (ThreadInfo ti : tinfos) {
					if(ti != null) {						
						stringBuffer = appendThreadDetails(stringBuffer, ti);						
						stringBuffer.append("\n");																				
					}
				}
						
			} else {
				System.out.println("JMX_Connection could not be established ");
			}			
		} finally {
			closeJMXConnector(jmxConnector);
		} 		
		return stringBuffer.toString();	
	}
	
}
