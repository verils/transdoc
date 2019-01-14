package com.github.verils.transdoc.cli.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * DetailFormatter
 *
 * @author tbano
 * @date 2017-10-20
 */
public class DetailFormatter extends Formatter {

	@Override
	public String format(LogRecord record) {
		StringBuffer sb = new StringBuffer();
		Date date = new Date(record.getMillis());
		sb.append(DateFormat.getDateTimeInstance().format(date)).append(" ");
		sb.append("[").append(formatLevel(record.getLevel())).append("] ");
		sb.append(record.getLoggerName()).append(" - ");
		sb.append(formatMessage(record));
		sb.append("\n");
		if (record.getThrown() != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			record.getThrown().printStackTrace(pw);
			pw.close();
			sb.append(sw.toString());
		}
		return sb.toString();
	}

	private String formatLevel(Level level) {
		if (Level.FINE == level) {
			return "TRACE";
		} else if (Level.CONFIG == level) {
			return "DEBUG";
		} else if (Level.INFO == level) {
			return "INFO";
		} else if (Level.WARNING == level) {
			return "WARN";
		} else if (Level.SEVERE == level) {
			return "ERROR";
		} else {
			return "ALL";
		}
	}
}
