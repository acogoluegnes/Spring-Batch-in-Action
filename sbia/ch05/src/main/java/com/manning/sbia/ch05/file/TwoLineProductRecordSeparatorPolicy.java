/**
 * 
 */
package com.manning.sbia.ch05.file;

import org.springframework.batch.item.file.separator.RecordSeparatorPolicy;

/**
 * @author templth
 *
 */
public class TwoLineProductRecordSeparatorPolicy
                          implements RecordSeparatorPolicy {

	public String postProcess(String record) {
		return record;
	}

	public String preProcess(String line) {
		return line;
	}

	private int countComma(String s) {
		String tmp = s;
		int index = -1;
		int count = 0;
		while ((index=tmp.indexOf(","))!=-1) {
			tmp = tmp.substring(index+1);
			count++;
		}
		return count;
	}

	public boolean isEndOfRecord(String line) {
		return countComma(line)==3;
	}
}

