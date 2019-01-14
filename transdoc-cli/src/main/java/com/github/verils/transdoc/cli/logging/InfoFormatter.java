package com.github.verils.transdoc.cli.logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * InfoFormatter
 *
 * @author tbano
 * @date 2017-10-20
 */
public class InfoFormatter extends Formatter {

	@Override
	public String format(LogRecord record) {
		return record.getMessage() + "\n";
	}
}
