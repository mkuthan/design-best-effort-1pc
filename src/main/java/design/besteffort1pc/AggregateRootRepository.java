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
public class AggregateRootRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional(readOnly = true)
	public AggregateRoot load(String id) {
		return DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM aggregate_root WHERE id = ?",
				new Object[] { id }, new RowMapper<AggregateRoot>() {

					@Override
					public AggregateRoot mapRow(ResultSet rs, int rowNum) throws SQLException {
						return new AggregateRoot(rs.getString("id"));
					}

				}));
	}

	@Transactional(readOnly = false)
	public void save(AggregateRoot aggregateRoot) {
		jdbcTemplate.update("INSERT INTO aggregate_root(id) VALUES (?)", aggregateRoot.getId());
	}
	
}
