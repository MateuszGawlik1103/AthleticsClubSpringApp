package bdbt_bada_project.SpringApplication.competitions;

import bdbt_bada_project.SpringApplication.addresses.AddressesDAO;
import bdbt_bada_project.SpringApplication.disciplines.Discipline;
import bdbt_bada_project.SpringApplication.disciplines.DisciplineDAO;
import bdbt_bada_project.SpringApplication.members.Member;
import bdbt_bada_project.SpringApplication.members.MembersDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CompetitionsDAO {
    @Autowired
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    private AddressesDAO addressesDAO;

    public CompetitionsDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Competition> list(){
        String sql = "SELECT * FROM Zawody";
        List<Competition> competitions = jdbcTemplate.query(sql, new CompetitionRowMapper());
        competitions.forEach(member -> member.setAddress(addressesDAO.get(member.getAddressNumber())));

        return competitions;
    }

    public Competition get(int competitionNumber) {
        Object[] args = {competitionNumber};
        String sql = "SELECT * FROM ZAWODY WHERE NR_ZAWODOW = " + args[0];
        Competition competition = jdbcTemplate.queryForObject(sql, new CompetitionRowMapper());
        assert competition != null;
        competition.setAddress(addressesDAO.get(competition.getAddressNumber()));

        return competition;
    }

    public void save(Competition competition) {
        addressesDAO.save(competition.getAddress());

        SimpleJdbcInsert insertActor = new SimpleJdbcInsert(jdbcTemplate);
        insertActor.withTableName("Zawody").
                usingGeneratedKeyColumns("NR_ZAWODOW")
                .usingColumns("Data", "Opis", "Nr_adresu");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("Data", competition.getDate());
        parameters.put("Opis", competition.getDescription());
        parameters.put("Nr_adresu", addressesDAO.findAddressNumber(competition.getAddress()));

        insertActor.execute(parameters);
    }

    public void update(Competition competition) {
        String sql = "UPDATE ZAWODY SET DATA = ?, OPIS = ?, " +
                "NR_ADRESU = ? WHERE NR_ZAWODOW = ?";
        addressesDAO.update(competition.getAddress());

        jdbcTemplate.update(sql, competition.getDate(), competition.getDescription(),
                addressesDAO.findAddressNumber(competition.getAddress()),
                competition.getCompetitionNumber());
    }

    public void delete(int competitionNumber) {
        String sql = "DELETE FROM ZAWODY WHERE NR_ZAWODOW = ?";
        jdbcTemplate.update(sql, competitionNumber);
    }

    public List<Member> getCompetitors(int competitionNumber) {
        String sql = "SELECT c.* " +
                "FROM UCZESTNICY_ZAWODOW u " +
                "JOIN CZLONKOWIE c ON u.NR_CZLONKA = c.NR_CZLONKA " +
                "WHERE u.NR_ZAWODOW = ?";

        return jdbcTemplate.query(sql, new MembersDAO.MemberRowMapper(), competitionNumber);
    }

    private static class CompetitionRowMapper implements RowMapper<Competition> {
        @Override
        public Competition mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Competition competition = new Competition();
            competition.setCompetitionNumber(resultSet.getInt("NR_ZAWODOW"));
            competition.setDate(resultSet.getDate("DATA"));
            competition.setDescription(resultSet.getString("OPIS"));
            competition.setAddressNumber(resultSet.getInt("NR_ADRESU"));

            return competition;
        }
    }
}
