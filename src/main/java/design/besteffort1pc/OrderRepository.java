package design.besteffort1pc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional(readOnly = true)
	public Order load(String id) {
		return DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM ex_order WHERE id = ?",
				new Object[] { id }, new RowMapper<Order>() {

					@Override
					public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
						return new Order(rs.getString("id"));
					}

				}));
	}

	@Transactional(readOnly = false)
	public void save(Order order) {
		jdbcTemplate.update("INSERT INTO ex_order(id) VALUES (?)", order.getId());
	}
	
}
