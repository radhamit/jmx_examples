package com.jmx.simplembean.jmxclient;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JMXClient {
	
	
	private void addTwoNumbers(MBeanServerConnection mbsc,
							   ObjectName objName, 
							   int a, 
							   int b) throws Exception {
		String operationName = "add";
		Object[] params = new Object[] { a,b};
		String[] signature = new String[] { "int", "int" };
		
		Object output = mbsc.invoke(objName, operationName, params, signature);
		
		System.out.println("Result_of_Add_Two_numbers=" + output);
		
		
		
	}
	public static void main(String[] args) {
		String IP = "localhost";
		String port = "3040";
		try {
			String jmxURL = "service:jmx:rmi:///jndi/rmi://"+IP+":"+port+"/jmxrmi" ;	
			JMXServiceURL url = new JMXServiceURL(jmxURL);
			JMXConnector jmxConnector = JMXConnectorFactory.connect(url);
			MBeanServerConnection mbsc = jmxConnector.getMBeanServerConnection();
			ObjectName objName = new ObjectName("management:type=Calculator");
			
			JMXClient jmxClient = new JMXClient();
			jmxClient.addTwoNumbers(mbsc, objName, 1, 2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
