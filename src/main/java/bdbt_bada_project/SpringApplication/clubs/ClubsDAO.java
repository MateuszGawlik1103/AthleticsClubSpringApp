package bdbt_bada_project.SpringApplication.clubs;

import bdbt_bada_project.SpringApplication.addresses.AddressesDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class ClubsDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AddressesDAO addressesDAO;

    public ClubsDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Club get(int clubNumber) {
        Object[] args = {clubNumber};
        String sql = "SELECT * FROM KLUBY_LEKKOATLETYCZNE WHERE NR_KLUBU = " + args[0];
        Club club = jdbcTemplate.queryForObject(sql, new ClubRowMapper());
        assert club != null;
        club.setAddress(addressesDAO.get(club.getAddressNumber()));
        return club;
    }

    private static class ClubRowMapper implements RowMapper<Club> {
        @Override
        public Club mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Club club = new Club();
            club.setClubNumber(resultSet.getInt("NR_KLUBU"));
            club.setName(resultSet.getString("NAZWA"));
            club.setCreationDate(resultSet.getDate("DATA_ZALOZENIA"));
            club.setAddressNumber(resultSet.getInt("NR_ADRESU"));

            return club;
        }
    }
}
