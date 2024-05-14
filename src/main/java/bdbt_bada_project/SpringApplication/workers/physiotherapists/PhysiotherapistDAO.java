package bdbt_bada_project.SpringApplication.workers.physiotherapists;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class PhysiotherapistDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private WorkersDAO workersDAO;
    @Autowired
    private AccountDAO accountDAO;
    public PhysiotherapistDAO(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Physiotherapist> list(){
        String sql = "SELECT * FROM FIZJOTERAPEUCI";
        List<Physiotherapist> physios = jdbcTemplate.query(sql, new PhysiotherapistDAO.PhysioRowMapper());
        physios.forEach(physio -> physio.setFirstName(workersDAO.get(physio.getEmployeeNumber()).getFirstName()));
        physios.forEach(physio -> physio.setLastName(workersDAO.get(physio.getEmployeeNumber()).getLastName()));
        physios.forEach(physio -> physio.setDismissalDate(workersDAO.get(physio.getEmployeeNumber()).getDismissalDate()));
        return physios;
    }
    public Physiotherapist getEmployeeById(int employeeNumber) {
        Object[] args = {employeeNumber};
        String sql = "SELECT * FROM FIZJOTERAPEUCI WHERE NR_PRACOWNIKA = " + args[0];
        Physiotherapist physio = jdbcTemplate.queryForObject(sql, new PhysioRowMapper());
        assert physio != null;
        Worker worker = workersDAO.get(employeeNumber);
        physio.setFirstName(worker.getFirstName());
        physio.setLastName(worker.getLastName());
        physio.setPesel(worker.getPesel());
        physio.setDateOfBirth(worker.getDateOfBirth());
        physio.setEmploymentDate(worker.getEmploymentDate());
        physio.setDismissalDate(worker.getDismissalDate());
        physio.setPhoneNumber(worker.getPhoneNumber());
        physio.setGender(worker.getGender());
        physio.setClubNumber(1);
        physio.setAddressNumber(worker.getAddressNumber());
        physio.setAddress(worker.getAddress());
        physio.setPosition(worker.getPosition());
        physio.setPositionNumber(worker.getPositionNumber());
        physio.setAccount(worker.getAccount());
        physio.setAccountNumber(worker.getAccountNumber());
        return physio;
    }

    public void save(Physiotherapist physio) {
        accountDAO.save(physio.getAccount());

        physio.setPositionNumber(new PositionsDAO(jdbcTemplate).getPositionNumberByName("Fizjoterapeuta"));

        SimpleJdbcInsert insertPhysio = new SimpleJdbcInsert(jdbcTemplate);
        int employeeNumber = workersDAO.save(physio);

        insertPhysio.withTableName("FIZJOTERAPEUCI").usingColumns("NR_PRACOWNIKA", "SPECJALIZACJA","KWALIFIKACJE");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("NR_PRACOWNIKA", employeeNumber);
        parameters.put("SPECJALIZACJA", physio.getSpecialization());
        parameters.put("KWALIFIKACJE", physio.getQualifications());

        insertPhysio.execute(parameters);
    }

    public boolean physioExists(int employeeNumber) {
        String sql = "SELECT COUNT(*) FROM FIZJOTERAPEUCI WHERE NR_PRACOWNIKA = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, employeeNumber);
        return count > 0;
    }


    public void update(Physiotherapist physio) {
        workersDAO.update(physio);
        String sql = "UPDATE FIZJOTERAPEUCI SET SPECJALIZACJA = ?, KWALIFIKACJE = ? WHERE NR_PRACOWNIKA = ?";
        if (!physioExists(physio.getEmployeeNumber())) save(physio);
        jdbcTemplate.update(sql, physio.getSpecialization(), physio.getQualifications(), physio.getEmployeeNumber());
    }

    public void dismiss(int employeeNumber) {

        String sqlDelete1 = "DELETE FROM KONTA WHERE NR_KONTA = ?";
        jdbcTemplate.update(sqlDelete1, getEmployeeById(employeeNumber).getAccountNumber());


        LocalDate actualDate = LocalDate.now();
        String sql = "UPDATE PRACOWNICY SET DATA_ZWOLNIENIA = ?, NR_KONTA = ?  WHERE NR_PRACOWNIKA = ?";
        jdbcTemplate.update(sql, actualDate, null, employeeNumber);

    }
    private static class PhysioRowMapper implements RowMapper<Physiotherapist> {
        @Override
        public Physiotherapist mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Physiotherapist physio = new Physiotherapist();
            physio.setEmployeeNumber(resultSet.getInt("NR_PRACOWNIKA"));
            physio.setSpecialization(resultSet.getString("SPECJALIZACJA"));
            physio.setQualifications(resultSet.getString("KWALIFIKACJE"));

            return physio;
        }
    }
}
