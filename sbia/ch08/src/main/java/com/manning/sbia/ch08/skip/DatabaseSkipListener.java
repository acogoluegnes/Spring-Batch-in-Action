/**
 * 
 */
package com.manning.sbia.ch08.skip;

import javax.sql.DataSource;

import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author acogoluegnes
 *
 */
public class DatabaseSkipListener {

	private JdbcTemplate jdbcTemplate;
	
	public DatabaseSkipListener(DataSource ds) {
		this.jdbcTemplate = new JdbcTemplate(ds);
	}
	
	@OnSkipInRead
	public void log(Throwable t) {
		if(t instanceof FlatFileParseException) {
			FlatFileParseException ffpe = (FlatFileParseException) t;
			jdbcTemplate.update(
				"insert into skipped_product (line,line_number) values (?,?)",
				ffpe.getInput(),ffpe.getLineNumber()
			);
		}
	}
	
}
