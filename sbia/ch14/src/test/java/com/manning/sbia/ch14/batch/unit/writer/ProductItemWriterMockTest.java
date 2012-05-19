/**
 *
 */
package com.manning.sbia.ch14.batch.unit.writer;

import static com.manning.sbia.ch14.batch.ProductItemWriter.INSERT_SQL;
import static com.manning.sbia.ch14.batch.ProductItemWriter.UPDATE_SQL;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.manning.sbia.ch14.batch.ProductItemWriter;
import com.manning.sbia.ch14.domain.Product;

/**
 * Unit with mock
 *
 * @author bazoud
 *
 */
public class ProductItemWriterMockTest {
    private Product p;
    private SimpleJdbcTemplate jdbcTemplate;
    private ProductItemWriter writer = new ProductItemWriter();
    private ItemSqlParameterSourceProvider<Product> ispsp;
    private List<Product> items;

    @Before
    public void setUp() {
        p = new Product();
        p.setId("211");
        p.setName("BlackBerry");
        items = new ArrayList<Product>();
        items.add(p);
        writer = new ProductItemWriter();
        ispsp = new BeanPropertyItemSqlParameterSourceProvider<Product>();
        writer.setItemSqlParameterSourceProvider(ispsp);
        jdbcTemplate = mock(SimpleJdbcTemplate.class);
        writer.setSimpleJdbcTemplate(jdbcTemplate);
        writer.setItemSqlParameterSourceProvider(ispsp);
    }

    @Test
    public void testUpdateProduct() throws Exception {
        when(jdbcTemplate.update(eq(UPDATE_SQL), any(SqlParameterSource.class))).thenReturn(1);
        writer.write(items);
        verify(jdbcTemplate, times(1)).update(eq(UPDATE_SQL), any(SqlParameterSource.class));
        verify(jdbcTemplate, times(0)).update(eq(INSERT_SQL), any(SqlParameterSource.class));
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    public void testInsertProduct() throws Exception {
        when(jdbcTemplate.update(eq(UPDATE_SQL), any(SqlParameterSource.class))).thenReturn(0);
        writer.write(items);
        verify(jdbcTemplate, times(1)).update(eq(UPDATE_SQL), any(SqlParameterSource.class));
        verify(jdbcTemplate, times(1)).update(eq(INSERT_SQL), any(SqlParameterSource.class));
        verifyNoMoreInteractions(jdbcTemplate);
    }
}
