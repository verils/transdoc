package com.github.verils.transdoc.cli.logging;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * TransdocLogger
 *
 * @author tbano
 * @date 2017-10-20
 */
public class TransdocLogger {

	private Logger logger;

	static {
		LogManager logManager = LogManager.getLogManager();
		try {
			logManager.readConfiguration(
					TransdocLogger.class.getResourceAsStream("/logging.properties"));
		} catch (Exception e) {
		}
	}

	/**
	 * 获取一个日志记录器
	 * 
	 * @param clazz
	 *            使用日志记录器的类
	 * @return 日志记录器
	 */
	public static TransdocLogger getLog(Class<?> clazz) {
		return new TransdocLogger(Logger.getLogger(clazz.getName()));
	}

	/**
	 * 私有构造函数
	 * 
	 * @param logger
	 *            JUL日志记录器
	 */
	private TransdocLogger(Logger logger) {
		this.logger = logger;
	}

	public void trace(String message) {
		log(Level.FINE, message);
	}

	public void trace(String message, Throwable e) {
		log(Level.FINE, message, e);
	}

	public void debug(String message) {
		log(Level.CONFIG, message);
	}

	public void debug(String message, Throwable e) {
		log(Level.CONFIG, message, e);
	}

	public void info(String message) {
		log(Level.INFO, message);
	}

	public void info(String message, Throwable e) {
		log(Level.INFO, message, e);
	}

	public void warn(String message) {
		log(Level.WARNING, message);
	}

	public void warn(String message, Throwable e) {
		log(Level.WARNING, message, e);
	}

	public void error(String message) {
		log(Level.SEVERE, message);
	}

	public void error(String message, Throwable e) {
		log(Level.SEVERE, message, e);
	}

	private void log(Level level, String message) {
		if (logger.isLoggable(level)) {
			try {
				logger.log(level, message);
			} catch (Exception e) {
				handleIOException(e);
			}
		}
	}

	private void log(Level level, String message, Throwable thrown) {
		if (logger.isLoggable(level)) {
			try {
				logger.log(level, message, thrown);
			} catch (Exception e) {
				handleIOException(e);
			}
		}
	}

	private void handleIOException(Exception e) {
		// 打印出控制台
		if (e instanceof IOException) {
			logger.log(Level.INFO, "日志记录发生错误，无法写出内容到文件！");
		}
	}

}
