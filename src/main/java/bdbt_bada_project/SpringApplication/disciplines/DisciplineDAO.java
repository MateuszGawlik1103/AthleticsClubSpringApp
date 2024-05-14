package bdbt_bada_project.SpringApplication.disciplines;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DisciplineDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public DisciplineDAO(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<Discipline> list(){
        String sql = "SELECT * FROM DYSCYPLINY";
        return jdbcTemplate.query(sql, new DisciplineDAO.DisciplineRowMapper());
    }

    public void save(Discipline discipline) {
        if (disciplineExists(discipline)) return;
        SimpleJdbcInsert insertDiscipline = new SimpleJdbcInsert(jdbcTemplate);
        insertDiscipline.withTableName("DYSCYPLINY").usingColumns("NAZWA", "OPIS","NR_KLUBU");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("NAZWA", discipline.getDisciplineName());
        parameters.put("OPIS", discipline.getDescription());
        parameters.put("NR_KLUBU", 1);

        insertDiscipline.execute(parameters);
    }

    public Discipline get(int disciplineNumber) {
        Object[] args = {disciplineNumber};
        String sql = "SELECT * FROM DYSCYPLINY WHERE NR_DYSCYPLINY = " + args[0];
        return jdbcTemplate.queryForObject(sql, new DisciplineDAO.DisciplineRowMapper());
    }

    public void update(Discipline discipline) {
        String sql = "UPDATE DYSCYPLINY SET NAZWA = ?, OPIS = ? WHERE NR_DYSCYPLINY = ?";
        if (!disciplineExists(discipline)) save(discipline);
        jdbcTemplate.update(sql,
                discipline.getDisciplineName(),
                discipline.getDescription(),
                discipline.getDisciplineNumber());
    }

    public boolean disciplineExists(Discipline discipline) {
        String sql = "SELECT COUNT(*) FROM DYSCYPLINY WHERE NR_DYSCYPLINY = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, discipline.getDisciplineNumber());
        return count > 0;
    }

    public Discipline getDisciplineById(int disciplineNumber) {
        Object[] args = {disciplineNumber};
        String sql = "SELECT * FROM DYSCYPLINY WHERE NR_DYSCYPLINY = " + args[0];
        Discipline discipline = jdbcTemplate.queryForObject(sql, new DisciplineDAO.DisciplineRowMapper());
        assert discipline != null;
        return discipline;
    }

    public static class DisciplineRowMapper implements RowMapper<Discipline> {
        @Override
        public Discipline mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Discipline discipline = new Discipline();
            discipline.setDisciplineNumber(resultSet.getInt("NR_DYSCYPLINY"));
            discipline.setDisciplineName(resultSet.getString("NAZWA"));
            discipline.setDescription(resultSet.getString("OPIS"));

            return discipline;
        }
    }
}
