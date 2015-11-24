package org.konurbaev.notification.logreader;

import java.util.Enumeration;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogReaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StdoutLogReaderActivator implements BundleActivator, LogListener {

    private final static Logger logger = LoggerFactory.getLogger(StdoutLogReaderActivator.class);

    public void stop(BundleContext arg0) throws Exception {
    }

    @SuppressWarnings("unchecked")
    public void start(BundleContext context) throws Exception {
        ServiceReference serviceReference = context.getServiceReference(LogReaderService.class.getName());

        LogReaderService logReader = (LogReaderService) context.getService(serviceReference);

        logReader.addLogListener(this);

        LogEntry entry;
        Enumeration<LogEntry> logs = logReader.getLog();

        while (logs.hasMoreElements()) {
            entry = logs.nextElement();

            logger.debug(entry.getBundle().getSymbolicName() + ": " + entry.getMessage());
        }
    }

    public void logged(LogEntry entry) {
        logger.debug("log entry = " + entry.getMessage());

        if (entry.getException() instanceof ConfigurationException) {
            ConfigurationException configExcep = (ConfigurationException) entry.getException();

            logger.debug("config exception = " + configExcep.getMessage());
//            
//            
//            if (configExcep.getProperty().equals("port")) {
//                // ...
//            }
        }
    }

}