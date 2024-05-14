package bdbt_bada_project.SpringApplication.positions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PositionsDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public PositionsDAO(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Position> list(){
        String sql = "SELECT * FROM Stanowiska";
        return jdbcTemplate.query(sql, new PositionRowMapper());
    }

    public String getNameByPositionNumber(int positionNumber){
        String sql = "SELECT NAZWA FROM STANOWISKA WHERE NR_STANOWISKA = ?";
        return jdbcTemplate.queryForObject(sql, String.class, positionNumber);
    }

    public Position get(int positionNumber) {
        Object[] args = {positionNumber};
        String sql = "SELECT * FROM STANOWISKA WHERE NR_STANOWISKA = " + args[0];
        Position position = jdbcTemplate.queryForObject(sql, new PositionRowMapper());
        assert position != null;
        return position;
    }

    public int getPositionNumberByName(String positionName){
        String sql = "SELECT NR_STANOWISKA FROM STANOWISKA WHERE NAZWA = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, positionName);
    }

    public static class PositionRowMapper implements RowMapper<Position> {
        @Override
        public Position mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Position position = new Position();
            position.setPositionNumber(resultSet.getInt("Nr_stanowiska"));
            position.setName(resultSet.getString("Nazwa"));
            position.setDescription(resultSet.getString("Opis"));
            return position;
        }
    }
}
