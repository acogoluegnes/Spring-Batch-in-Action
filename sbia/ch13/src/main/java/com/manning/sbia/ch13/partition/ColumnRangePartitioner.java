package com.manning.sbia.ch13.partition;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class ColumnRangePartitioner extends JdbcDaoSupport implements Partitioner {
	private String table;
	private String column;

	public Map<String, ExecutionContext> partition(int gridSize) {
		int min = getJdbcTemplate().queryForInt("SELECT MIN(" + column + ") from " + table);
		int max = getJdbcTemplate().queryForInt("SELECT MAX(" + column + ") from " + table);
		int targetSize = (max - min) / gridSize + 1;

		Map<String, ExecutionContext> result = new HashMap<String, ExecutionContext>();
		int number = 0;
		int start = min;
		int end = start + targetSize - 1;

		while (start <= max) {
			ExecutionContext value = new ExecutionContext();
			result.put("partition" + number, value);

			if (end >= max) {
				end = max;
			}
			value.putInt("minValue", start);
			value.putInt("maxValue", end);
			start += targetSize;
			end += targetSize;
			number++;
		}

		return result;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

}
