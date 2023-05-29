package oz.utils.load;

import java.lang.reflect.Constructor;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.datetime.StopWatch;
import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.reflection.ReflectionUtils;
import oz.infra.system.SystemUtils;
import oz.infra.thread.ThreadUtils;
import oz.utils.load.generators.AbstractLoadGenerator;

public class LoadGenerationDispatcher implements Runnable {
	private String loadGeneratorClass = null;
	private int numberOfInvocations = 0;
	private int intervalBetweenInvocationsInMillis = 0;
	private AbstractLoadGenerator abstractLoadGenerator = null;
	private static Logger logger = JulUtils.getLogger();

	public LoadGenerationDispatcher(final Properties loadGeneratorProperties) {
		ReflectionUtils.setFieldsFromProperties(loadGeneratorProperties, this);
		try {
			Class<?> monitorClassObject = Class.forName(loadGeneratorClass);
			Class<?> propertiesClassObject = Class.forName("java.util.Properties");
			Constructor<?> monitorConstructor = monitorClassObject
					.getConstructor(new Class[] { propertiesClassObject });
			abstractLoadGenerator = (AbstractLoadGenerator) monitorConstructor
					.newInstance(new Object[] { loadGeneratorProperties });
			logger.info("Monitor " + loadGeneratorClass + " has started on " + SystemUtils.getLocalHostName() + " ...");
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
	}

	public AbstractLoadGenerator getAbstractLoadGenerator() {
		return abstractLoadGenerator;
	}

	public final void run() {
		StopWatch stopWatch = new StopWatch();
		logger.info("Start running ...");
		for (int i = 0; i < numberOfInvocations; i++) {
			Thread.yield();
			abstractLoadGenerator.generateLoad();
			if (intervalBetweenInvocationsInMillis > 0) {
				ThreadUtils.sleep(intervalBetweenInvocationsInMillis, Level.INFO);
			}
		}
		stopWatch.logElapsedTimeMessage("*** Load geneartion has completed in ");
	}

	public void setAbstractLoadGenerator(AbstractLoadGenerator abstractLoadGenerator) {
		this.abstractLoadGenerator = abstractLoadGenerator;
	}

	public void setLoadGeneratorClass(String loadGeneratorClass) {
		this.loadGeneratorClass = loadGeneratorClass;
	}

	public void setNumberOfInvocations(int numberOfInvocations) {
		this.numberOfInvocations = numberOfInvocations;
	}

	public void setIntervalBetweenInvocationsInMillis(int intervalBetweenInvocationsInMillis) {
		this.intervalBetweenInvocationsInMillis = intervalBetweenInvocationsInMillis;
	}
}
