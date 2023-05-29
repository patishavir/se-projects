package oz.test.log4j.thread;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Log {
	
	public static Logger logger;
	public static Logger error_logger;

	public static void init( String fname, String loggerNm ) {
  	logger = Logger.getLogger("p8cm.subscr.trace." + loggerNm); 
		error_logger = Logger.getLogger("p8cm.subscr.error." + loggerNm);
	  PropertyConfigurator.configureAndWatch(fname, 60000);	
	}
	public static void  info( String msg ) {
		logger.info(msg);
	}

//	public static void  info( String msg, ObjectChangeEvent event ) {
//		logger.info( formatRecord( msg, event ) );
//	}
//	
	public static void  warn( String msg ) {
		logger.warn(msg);
	}

//	public static void  warn( String msg, ObjectChangeEvent event ) {
//		logger.warn( formatRecord( msg, event ) );
//	}
//	
	public static void  debug( String msg ) {
		logger.debug(msg);
	}

//	public static void  debug( String msg, ObjectChangeEvent event ) {
//		logger.debug( formatRecord( msg, event ) );
//	}
//	
	public static void  trace( String msg ) {
    if( logger.isTraceEnabled() ) 
		  logger.trace(msg);
	}
//
//	public static void  trace( String msg, ObjectChangeEvent event ) {
//		logger.trace( formatRecord( msg, event ) );
//	}
//
	public static void  error( String msg ) {
		error_logger.error(msg);
	}
//
//	public static void  error( String msg, ObjectChangeEvent event ) {
//		error_logger.error( formatRecord( msg, event ) );
//	}
//
	public static boolean isDebug() {
		return logger.isDebugEnabled();
	}
//
//	public static String formatRecord( String msg, ObjectChangeEvent event ) {
//		StringBuffer sb = new StringBuffer();
//		if( event != null ) {
//			sb.append("event class:" + event.getClassName());
//			//sb.append(" ,last modifier:" + event.get_LastModifier());
//		}
//		sb.append(",message:"+msg);
//		return sb.toString();
//	}
	
}
