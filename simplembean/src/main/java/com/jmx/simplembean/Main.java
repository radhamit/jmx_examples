package com.jmx.simplembean;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.jmx.simplembean.mbean.Calculator;
import com.jmx.simplembean.mbean.CalculatorMBean;

public class Main {

	private void registerCalculatorMBean() throws Exception  {
		  MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
	      ObjectName name = new ObjectName("management:type=Calculator"); 
	      CalculatorMBean mbean = new Calculator(); 
	      mbs.registerMBean(mbean, name);
	      System.out.println("Registered calculator Mbean successfully ... ");
	}
	
	 public static void main(String[] args) throws Exception { 
	/*	 
		 System.setProperty("com.sun.management.jmxremote.port", "3040");
		 System.setProperty("com.sun.management.jmxremote.authenticate", "false");
		 System.setProperty("com.sun.management.jmxremote.ssl", "false");
		 
		 */
		 Main main = new Main();
		 main.registerCalculatorMBean();
		 
	      Thread.currentThread().join();
	   } 
	 
}


/* -Dcom.sun.management.jmxremote.port=3040 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false */