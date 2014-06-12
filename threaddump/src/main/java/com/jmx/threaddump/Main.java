package com.jmx.threaddump;

public class Main {

	public static void main(String[] args) throws Exception {
		ThreadDumpGenerator threadDumpGenerator = new ThreadDumpGenerator();
		String threadDump = threadDumpGenerator.getThreadDump("localhost", "3040");
		System.out.println("ThreadDump= " + threadDump);
	}
}
