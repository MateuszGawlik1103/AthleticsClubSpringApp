package bdbt_bada_project.SpringApplication.workers.coaches;

import bdbt_bada_project.SpringApplication.accounts.AccountDAO;
import bdbt_bada_project.SpringApplication.positions.PositionsDAO;
import bdbt_bada_project.SpringApplication.workers.Worker;
import bdbt_bada_project.SpringApplication.workers.WorkersDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Repository
public class CoachesDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private WorkersDAO workersDAO;
    @Autowired
    private AccountDAO accountDAO;

    public CoachesDAO(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Coach> list() {
        String sql = "SELECT * FROM TRENERZY";
        List<Coach> coaches = jdbcTemplate.query(sql, new CoachesDAO.CoachRowMapper());
        coaches.forEach(coach -> coach.setFirstName(workersDAO.get(coach.getEmployeeNumber()).getFirstName()));
        coaches.forEach(coach -> coach.setLastName(workersDAO.get(coach.getEmployeeNumber()).getLastName()));
        coaches.forEach(coach -> coach.setDismissalDate(workersDAO.get(coach.getEmployeeNumber()).getDismissalDate()));
        return coaches;
    }

    public List<Coach> listFreeCoaches() {
        String sql = "SELECT * FROM TRENERZY t WHERE NOT EXISTS (" +
                " SELECT 1" +
                " FROM TRENERZY_GRUP tg" +
                " WHERE tg.NR_PRACOWNIKA = t.NR_PRACOWNIKA )";
        List<Coach> coaches = jdbcTemplate.query(sql, new CoachesDAO.CoachRowMapper());
        coaches.forEach(coach -> coach.setFirstName(workersDAO.get(coach.getEmployeeNumber()).getFirstName()));
        coaches.forEach(coach -> coach.setLastName(workersDAO.get(coach.getEmployeeNumber()).getLastName()));
        return coaches;
    }
        public Coach getEmployeeById (int employeeNumber){
            Object[] args = {employeeNumber};
            String sql = "SELECT * FROM TRENERZY WHERE NR_PRACOWNIKA = " + args[0];
            Coach coach = jdbcTemplate.queryForObject(sql, new CoachRowMapper());
            assert coach != null;
            Worker worker = workersDAO.get(employeeNumber);
            coach.setFirstName(worker.getFirstName());
            coach.setLastName(worker.getLastName());
            coach.setPesel(worker.getPesel());
            coach.setDateOfBirth(worker.getDateOfBirth());
            coach.setEmploymentDate(worker.getEmploymentDate());
            coach.setDismissalDate(worker.getDismissalDate());
            coach.setPhoneNumber(worker.getPhoneNumber());
            coach.setGender(worker.getGender());
            coach.setClubNumber(1);
            coach.setAddressNumber(worker.getAddressNumber());
            coach.setAddress(worker.getAddress());
            coach.setPosition(worker.getPosition());
            coach.setPositionNumber(worker.getPositionNumber());
            coach.setAccount(worker.getAccount());
            coach.setAccountNumber(worker.getAccountNumber());

            return coach;
        }
        public void save (Coach coach){
            accountDAO.save(coach.getAccount());

            coach.setPositionNumber(new PositionsDAO(jdbcTemplate).getPositionNumberByName("Trener"));

            SimpleJdbcInsert insertCoach = new SimpleJdbcInsert(jdbcTemplate);
            int employeeNumber = workersDAO.save(coach);

            insertCoach.withTableName("TRENERZY").usingColumns("NR_PRACOWNIKA", "KWALIFIKACJE", "DOSWIADCZENIE_TRENERSKIE");

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("NR_PRACOWNIKA", employeeNumber);
            parameters.put("KWALIFIKACJE", coach.getQualifications());
            parameters.put("DOSWIADCZENIE_TRENERSKIE", coach.getCoachingExperience());

            insertCoach.execute(parameters);
        }

        public boolean coachExists ( int employeeNumber){
            String sql = "SELECT COUNT(*) FROM TRENERZY WHERE NR_PRACOWNIKA = ?";
            int count = jdbcTemplate.queryForObject(sql, Integer.class, employeeNumber);
            return count > 0;
        }


        public void update (Coach coach){
            workersDAO.update(coach);

            String sql = "UPDATE TRENERZY SET KWALIFIKACJE = ?, DOSWIADCZENIE_TRENERSKIE = ? WHERE NR_PRACOWNIKA = ?";
            if (!coachExists(coach.getEmployeeNumber())) save(coach);
            jdbcTemplate.update(sql, coach.getQualifications(), coach.getCoachingExperience(), coach.getEmployeeNumber());
        }

        public void dismiss ( int employeeNumber){

            String sqlDelete1 = "DELETE FROM KONTA WHERE NR_KONTA = ?";
            jdbcTemplate.update(sqlDelete1, getEmployeeById(employeeNumber).getAccountNumber());

            String sqlDelete2 = "DELETE FROM TRENERZY_GRUP WHERE NR_PRACOWNIKA = ?";
            jdbcTemplate.update(sqlDelete2, employeeNumber);


            LocalDate actualDate = LocalDate.now();
            String sql = "UPDATE PRACOWNICY SET DATA_ZWOLNIENIA = ?, NR_KONTA = ?  WHERE NR_PRACOWNIKA = ?";
            jdbcTemplate.update(sql, actualDate, null, employeeNumber);


        }

        public int getCoachNumberByEmail(String email){
            String sql1 = "SELECT NR_KONTA FROM KONTA WHERE EMAIL = ?";
            int accountNumber = jdbcTemplate.queryForObject(sql1, Integer.class, email);
            String sql2 = "SELECT NR_PRACOWNIKA FROM PRACOWNICY WHERE NR_KONTA = ?";
            return jdbcTemplate.queryForObject(sql2, Integer.class, accountNumber);
        }

        private static class CoachRowMapper implements RowMapper<Coach> {
            @Override
            public Coach mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                Coach coach = new Coach();
                coach.setEmployeeNumber(resultSet.getInt("NR_PRACOWNIKA"));
                coach.setQualifications(resultSet.getString("KWALIFIKACJE"));
                coach.setCoachingExperience(resultSet.getString("DOSWIADCZENIE_TRENERSKIE"));

                return coach;
            }
        }

    }

