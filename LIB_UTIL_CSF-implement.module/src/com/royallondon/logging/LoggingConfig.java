/**
 * 
 */
package com.royallondon.logging;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author richard
 *
 */
public class LoggingConfig {

	static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(LoggingConfig.class);

	private static final String BASE_LOGGING_AUDIT = "csf.audit";
	private static final String BASE_LOGGING_TRACE = "csf.trace";
	private static final String BASE_LOGGING_LOG = "csf.log";

	// Create Concurrent HashMaps to persist the loggers so that level settings will persist
	private final static ConcurrentHashMap<String, Logger> _auditLoggers = new ConcurrentHashMap<String, Logger>();
	private final static ConcurrentHashMap<String, Logger> _traceLoggers = new ConcurrentHashMap<String, Logger>();
	private final static ConcurrentHashMap<String, Logger> _logLoggers = new ConcurrentHashMap<String, Logger>();
	
	private static final Level LOG_OFF = Level.FINE;
	private static final Level LOG_ON = Level.FINER;
	private static final Level LOG_PAYLOAD = Level.FINEST;

	public static final String LEVEL_OFF = "OFF";
	public static final String LEVEL_ON = "ON";
	public static final String LEVEL_PAYLOAD = "PAYLOAD";
	
	private final static int STATUS_OFF = 0;
	private final static int STATUS_ON = 1;
	private final static int STATUS_PAYLOAD = 2;
	
	private final static ConcurrentHashMap<String, ConcurrentHashMap<String, Long>> PROCESS_INFO = new ConcurrentHashMap<>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LoggingConfig.reset();

		LoggingConfig.setAuditLevel("", "PAYLOAD");
		assert(LoggingConfig.isAuditEnabled("") == true);
		assert(LoggingConfig.isAuditPayload("") == true);
		assert(LoggingConfig.isAuditEnabled("fred") == true);
		assert(LoggingConfig.isAuditPayload("fred") == true);
		assert(LoggingConfig.isAuditPayload("harry.fred") == true);

		LoggingConfig.setAuditLevel("harry", "ON");
		LoggingConfig.setAuditLevel("harry.joe", "OFF");
		assert(LoggingConfig.isAuditEnabled("fred") == true);
		assert(LoggingConfig.isAuditPayload("harry.fred") == false);
		assert(LoggingConfig.isAuditEnabled("harry.fred") == true);
		assert(LoggingConfig.isAuditEnabled("harry.joe") == false);

		LoggingConfig.setAuditLevel("", "ON");
		assert(LoggingConfig.isAuditEnabled("") == true);
		assert(LoggingConfig.isAuditPayload("") == false);
		assert(LoggingConfig.isAuditEnabled("fred") == true);
		assert(LoggingConfig.isAuditPayload("fred_v1") == false);
		assert(LoggingConfig.isAuditPayload("fred") == false);

		LoggingConfig.setAuditLevel("fred", "OFF");
		assert(LoggingConfig.isAuditEnabled("") == true);
		assert(LoggingConfig.isAuditPayload("") == false);
		assert(LoggingConfig.isAuditEnabled("fred") == false);
		assert(LoggingConfig.isAuditPayload("fred") == false);
	
		LoggingConfig.reset();
		assert(LoggingConfig.isAuditEnabled("") == false);
		assert(LoggingConfig.isAuditPayload("") == false);
		assert(LoggingConfig.isAuditEnabled("fred") == false);
		assert(LoggingConfig.isAuditPayload("fred") == false);

		LoggingConfig.setAuditLevel("fred", "PAYLOAD");
		assert(LoggingConfig.isAuditEnabled("") == false);
		assert(LoggingConfig.isAuditPayload("") == false);
		assert(LoggingConfig.isAuditEnabled("fred") == true);
		assert(LoggingConfig.isAuditPayload("fred") == true);
	}

	public LoggingConfig() {
		super();
	}

	public static int getLogStatus(String name) {
		Logger logger = Logger.getLogger(logName(name));
		if (logger.isLoggable(LOG_PAYLOAD))
			return STATUS_PAYLOAD;
		else if (logger.isLoggable(LOG_ON))
			return STATUS_ON;
		else
			return STATUS_OFF;
	}
		
	public static boolean isLogEnabled(String name) {
		return Logger.getLogger(logName(name)).isLoggable(LOG_ON);
	}

	public static boolean isLogPayload(String name) {
		return Logger.getLogger(logName(name)).isLoggable(LOG_PAYLOAD);
	}

	public static void setLogLevel(String name, String level) {
		setLogLevel(name, levelFromString(level));
	}

	public static void setLogLevel(String name, Level level) {
		LOG.debug("Set log level name='" + name + "', level='" + level.getName() + "'");
		
		String context = logName(name);
		Logger logger = Logger.getLogger(context);
		logger.setLevel(level);

		// Persist the logger so that the setting remains after garbage collection
		_logLoggers.put(context, logger);
	}

	public static int getTraceStatus(String name) {
		Logger logger = Logger.getLogger(traceName(name));
		if (logger.isLoggable(LOG_PAYLOAD))
			return STATUS_PAYLOAD;
		else if (logger.isLoggable(LOG_ON))
			return STATUS_ON;
		else
			return STATUS_OFF;
	}

	public static boolean isTraceEnabled(String name) {
		return Logger.getLogger(traceName(name)).isLoggable(LOG_ON);
	}

	public static boolean isTracePayload(String name) {
		return Logger.getLogger(traceName(name)).isLoggable(LOG_PAYLOAD);
	}

	public static void setTraceLevel(String name, String level) {
		setTraceLevel(name, levelFromString(level));
	}

	public static void setTraceLevel(String name, Level level) {
		LOG.debug("Set trace level name='" + name + "', level='" + level.getName() + "'");

		String context = traceName(name);
		Logger logger = Logger.getLogger(context);
		logger.setLevel(level);

		// Persist the logger so that the setting remains after garbage collection
		_traceLoggers.put(context, logger);
	}

	public static int getAuditStatus(String name) {
		Logger logger = Logger.getLogger(auditName(name));
		if (logger.isLoggable(LOG_PAYLOAD))
			return STATUS_PAYLOAD;
		else if (logger.isLoggable(LOG_ON))
			return STATUS_ON;
		else
			return STATUS_OFF;
	}
	
	public static boolean isAuditEnabled(String name) {
		return Logger.getLogger(auditName(name)).isLoggable(LOG_ON);
	}

	public static boolean isAuditPayload(String name) {
		return Logger.getLogger(auditName(name)).isLoggable(LOG_PAYLOAD);
	}

	public static void setAuditLevel(String name, String level) {
		setAuditLevel(name, levelFromString(level));
	}

	public static void setAuditLevel(String name, Level level) {
		LOG.debug("Set audit level name='" + name + "', level='" + level.getName() + "'");

		String context = auditName(name);
		Logger logger = Logger.getLogger(context);
		logger.setLevel(level);

		// Persist the logger so that the setting remains after garbage collection
		_auditLoggers.put(context, logger);
	}
	
	
	public static long recordStart(String linkId, String context, long startTime) {
		ConcurrentHashMap<String, Long> process = PROCESS_INFO.get(linkId);
		boolean procMissing = false;
		if (process == null) {
			process = new ConcurrentHashMap<String, Long>();
			procMissing = true;
		}

		long time = startTime > 0 ? startTime : System.currentTimeMillis();
		process.put(context, Long.valueOf(time));

		// Add the new map to the process table if it wasn't there previously. This
		// way if we fail before this point, the map entry will not be created and
		// orphaned
		if (procMissing)
			PROCESS_INFO.put(linkId, process);

		return time;
	}
	
	public static long recordEnd(String linkId, String context) {
		ConcurrentHashMap<String, Long> process = PROCESS_INFO.get(linkId);
		if (process != null) {
			Long start = process.get(context);
			if (start != null) {
				return System.currentTimeMillis() - start.longValue();
			}
		}

		return -1;
	}
	
	public static void endProcessStats(String linkId) {
		PROCESS_INFO.remove(linkId);
	}
	

	public static String makeContextMessage(String operation, String transactionID, String correlationID, String testID, String loggingContext, int loggingSequence, String application, String location, String context, long elapsedTime, long durationTime) {
		StringBuilder str = new StringBuilder(80);
		
		str.append(operation);

		if (!"".equals(transactionID)) {
			str.append("[[");
			str.append(transactionID);
			str.append("]]");
		}

		if (!"".equals(correlationID)) {
			str.append("[#");
			str.append(correlationID);
			str.append("#]");
		}

		if (!"".equals(testID)) {
			str.append("[@");
			str.append(testID);
			str.append("@]");
		}
		
		str.append("<<");
		str.append(loggingContext);
		if (loggingSequence < 10)
			str.append("0");
		str.append(loggingSequence);
		str.append(">>");

		if (!"".equals(application)) {
			str.append("(@");
			str.append(application);
			str.append("@)");
		}

		str.append("((");
		str.append(location);
		str.append("))");

		if (!"".equals(context)) {
			str.append("(#");
			str.append(context);
			str.append("#)");
		}

		if (elapsedTime >= 0) {
			str.append("{{+");
			str.append(elapsedTime);
			str.append("}}");
		}
		
		if (durationTime >= 0) {
			str.append("{{=");
			str.append(durationTime);
			str.append("}}");
		}
		
		return str.toString();
	}

	public static void reset() {
		// Reset audit loggers to use parent level and remove strong references
		for (Entry<String, Logger> logger : _auditLoggers.entrySet()) {
			logger.getValue().setLevel(null);
		}
		_auditLoggers.clear();
		
		// Reset log loggers to use parent level and remove strong references
		for (Entry<String, Logger> logger : _logLoggers.entrySet()) {
			logger.getValue().setLevel(null);
		}
		_logLoggers.clear();
		
		// Reset trace loggers to use parent level and remove strong references
		for (Entry<String, Logger> logger : _traceLoggers.entrySet()) {
			logger.getValue().setLevel(null);
		}
		_traceLoggers.clear();
	}

	
	/**
	 * Removes all old entries from the PROCESS_INFO map. An entry is regarded as old if all the
	 * entries within that map were created before the threshold time.
	 * 
	 * @param thresholdSecs
	 */
	public static void scavengeSessions(long thresholdSecs) {
		long expireThreshold = System.currentTimeMillis() - (thresholdSecs * 1000);
		for (String id : PROCESS_INFO.keySet()) {
			ConcurrentHashMap<String, Long>map = PROCESS_INFO.get(id);
			if (map != null) {
				// Look for any entries in the map that were created AFTER the threshold. As
				// soon as we have one then we know the map is NOT a scavenge candidate
				boolean expireCandidate = true;
				for (Long value : map.values()) {
					if (value > expireThreshold) {
						expireCandidate = false;
						break;
					}
				}
				
				if (expireCandidate)
					PROCESS_INFO.remove(map);
			}
		}
	}

	
	public static void config() {
//		_rootLogLevel = Logger.getLogger(BASE_LOGGING_LEVEL);
//		_rootLogPayload = Logger.getLogger(BASE_LOGGING_PAYLOAD);
//
//		try {
//			for (Handler handler : _rootLogLevel.getHandlers()) {
//				_rootLogLevel.removeHandler(handler);
//			}
//
//			for (Handler handler : _rootLogPayload.getHandlers()) {
//				_rootLogPayload.removeHandler(handler);
//			}
//		} catch (SecurityException ex) {
//		}
	}


	private static Level levelFromString(String level)
	{
		if (LEVEL_PAYLOAD.equals(level))
			return LOG_PAYLOAD;
		else if (LEVEL_ON.equals(level))
			return LOG_ON;
		else
			return LOG_OFF;
	}

	private static String logName(String context)
	{
		return contextFromString(BASE_LOGGING_LOG, context);
	}

	private static String traceName(String context)
	{
		return contextFromString(BASE_LOGGING_TRACE, context);
	}

	private static String auditName(String context)
	{
		return contextFromString(BASE_LOGGING_AUDIT, context);
	}
	
	private static String contextFromString(String base, String context)
	{
		if ("".equals(context))
			return base;
		else
			return base + "." + context;
	}
	
//	private static String normaliseName(String name) {
//		if (name.indexOf("_v") < 0)
//			return name;
//		
//		String[] elements = name.indexOf('.') > 0 ? name.split(".") : new String[] { name };
//		for (int i = 0; i < elements.length; ++i) {
//			int len = elements[i].length();
//			if ((len > 3) && Character.isDigit(elements[i].charAt(len-1)) && (elements[i].charAt(len-2) == 'v') && (elements[i].charAt(len-3) == '_'))
//				elements[i] = elements[i].substring(0, len - 3);
//		}
//	
//		if (elements.length == 1)
//			return elements[0];
//		
//		StringBuilder str = new StringBuilder(name.length());
//		for (int i = 0; i < elements.length; ++i) {
//			if (i > 0)
//				str.append('.');
//			str.append(elements[i]);
//		}
//
//		return str.toString();
//	}
}
