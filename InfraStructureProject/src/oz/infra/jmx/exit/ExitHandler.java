package oz.infra.jmx.exit;

import java.lang.management.ManagementFactory;
import java.util.logging.Logger;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import oz.infra.constants.OzConstants;
import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.infra.thread.ThreadUtils;

public class ExitHandler implements ExitHandlerMBean {
	public static final String OBJECT_NAME_STRING = "oz.infra:type=Exit";
	private static final long WAIT_BEFORE_EXIT_MILLI_SECONDS = 100l;
	private MBeanServer mbs = null;
	private static Logger logger = JulUtils.getLogger();

	public ExitHandler() {
		try {
			// Get the Platform MBean Server
			mbs = ManagementFactory.getPlatformMBeanServer();
			// Construct the ObjectName for the Hello MBean we will register
			ObjectName mbeanName = new ObjectName(OBJECT_NAME_STRING);
			// Register the Hello World MBean
			mbs.registerMBean(this, mbeanName);
			logger.info(StringUtils.concat(this.toString(), " has been successfully registered with mbean server ",
					mbs.toString()));
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
	}

	public final void exit() {
		logger.info(StringUtils.concat("Terminating ", SystemUtils.getLocalHostName(), " ..."));
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					ThreadUtils.sleep(WAIT_BEFORE_EXIT_MILLI_SECONDS);
					ObjectName mbeanName = new ObjectName(OBJECT_NAME_STRING);
					mbs.unregisterMBean(mbeanName);
					logger.info(StringUtils.concat("\nunregistered mbean ", mbeanName.toString(),
							"\nabout to perfom system.exit"));
					System.exit(OzConstants.EXIT_STATUS_OK);
				} catch (Exception ex) {
					ExceptionUtils.printMessageAndStackTrace(ex);
				}
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}
}
