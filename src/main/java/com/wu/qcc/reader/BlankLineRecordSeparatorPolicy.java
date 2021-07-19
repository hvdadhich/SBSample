/**
 * 
 */
package com.wu.qcc.reader;

import org.springframework.batch.item.file.separator.SimpleRecordSeparatorPolicy;

/**
 * @author 67052936
 *
 */
public class BlankLineRecordSeparatorPolicy extends SimpleRecordSeparatorPolicy {

	@Override
	public boolean isEndOfRecord(String line) {
		if (line.trim().length() == 0) {
			return false;
		}
		return super.isEndOfRecord(line);
	}

	@Override
	public String postProcess(String record) {
		if (record == null || record.trim().length() == 0) {
			return null;
		}
		return super.postProcess(record);
	}
}
