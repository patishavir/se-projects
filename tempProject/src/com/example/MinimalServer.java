/*
 * Main.java - main class for the Hello MBean and QueueSampler MXBean example.
 * Create the Hello MBean and QueueSampler MXBean, register them in the platform
 * MBean server, then wait forever (or until the program is interrupted).
 */

package com.example;

import oz.infra.jmx.exit.ExitHandler;

public class MinimalServer {
	/*
	 * For simplicity, we declare "throws Exception". Real programs will usually
	 * want finer-grained exception handling.
	 */
	public static void main(String[] args) throws Exception {
		// // Get the Platform MBean Server
		// MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		//
		// // Construct the ObjectName for the Hello MBean we will register
		// ObjectName mbeanName = new
		// ObjectName(ExitHandler.OBJECT_NAME_STRING);
		//
		// // Create the Hello World MBean
		new ExitHandler();
		//
		// // Register the Hello World MBean
		// mbs.registerMBean(mbean, mbeanName);

		// Wait forever
		System.out.println("Waiting for incoming requests...");
		Thread.sleep(Long.MAX_VALUE);
	}
}
