package bdbt_bada_project.SpringApplication.workers;

import bdbt_bada_project.SpringApplication.accounts.AccountDAO;
import bdbt_bada_project.SpringApplication.addresses.AddressesDAO;
import bdbt_bada_project.SpringApplication.positions.PositionsDAO;
import bdbt_bada_project.SpringApplication.workers.coaches.CoachesDAO;
import bdbt_bada_project.SpringApplication.workers.physiotherapists.PhysiotherapistDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WorkersDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AddressesDAO addressesDAO;
    @Autowired
    private PositionsDAO positionsDAO;
    @Autowired
    private AccountDAO accountDAO;
    @Autowired
    @Lazy
    private CoachesDAO coachesDAO;
    @Autowired
    @Lazy
    private PhysiotherapistDAO physiotherapistDAO;

    public WorkersDAO(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Worker> list(){
        String sql = "SELECT * FROM Pracownicy";
        List<Worker> workers = jdbcTemplate.query(sql, new WorkerRowMapper());
        workers.forEach(worker -> worker.setAddress(addressesDAO.get(worker.getAddressNumber())));
        workers.forEach(worker -> worker.setPosition(positionsDAO.getNameByPositionNumber(worker.getPositionNumber())));
        workers.forEach(worker -> {
            if(worker.getDismissalDate() == null){
                worker.setAccount(accountDAO.get(worker.getAccountNumber()));
            }
        });

        return workers;
    }


    public int save(Worker worker) {
        addressesDAO.save(worker.getAddress());

        SimpleJdbcInsert insertActor = new SimpleJdbcInsert(jdbcTemplate);
        insertActor.withTableName("Pracownicy").
                usingGeneratedKeyColumns("NR_PRACOWNIKA")
                .usingColumns(
                        "Imie",
                        "Nazwisko",
                        "PESEL",
                        "Data_urodzenia",
                        "Data_zatrudnienia",
                        "Data_zwolnienia",
                        "Numer_telefonu",
                        "Plec",
                        "Nr_klubu",
                        "Nr_adresu",
                        "Nr_stanowiska",
                        "Nr_konta");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("IMIE", worker.getFirstName());
        parameters.put("NAZWISKO", worker.getLastName());
        parameters.put("PESEL", worker.getPesel());
        parameters.put("DATA_URODZENIA", worker.getDateOfBirth());
        parameters.put("DATA_ZATRUDNIENIA", worker.getEmploymentDate());
        parameters.put("DATA_ZWOLNIENIA", worker.getDismissalDate());
        parameters.put("NUMER_TELEFONU", worker.getPhoneNumber());
        parameters.put("PLEC", String.valueOf(worker.getGender()));
        parameters.put("NR_KLUBU", 1);
        parameters.put("NR_ADRESU", addressesDAO.findAddressNumber(worker.getAddress()));
        parameters.put("NR_STANOWISKA", worker.getPositionNumber());
        parameters.put("NR_KONTA", accountDAO.findAccountNumber(worker.getAccount()));
        return insertActor.executeAndReturnKey(parameters).intValue();
    }
    public Worker get(int employeeNumber) {
        Object[] args = {employeeNumber};
        String sql = "SELECT * FROM PRACOWNICY WHERE NR_PRACOWNIKA = " + args[0];
        Worker worker = jdbcTemplate.queryForObject(sql, new WorkerRowMapper());
        assert worker != null;
        worker.setAddress(addressesDAO.get(worker.getAddressNumber()));
        worker.setPosition(positionsDAO.getNameByPositionNumber(worker.getPositionNumber()));
        if(worker.getAccountNumber() != 0){
            worker.setAccount(accountDAO.get(worker.getAccountNumber()));
        }
        return worker;
    }

    public void update(Worker worker) {
        String sql = "UPDATE PRACOWNICY SET IMIE = ?, NAZWISKO = ?, PESEL = ?, " +
                "DATA_URODZENIA = ?, DATA_ZATRUDNIENIA = ?, " +
                "NUMER_TELEFONU = ?, " +
                "PLEC = ?, NR_ADRESU = ?" +
                "WHERE NR_PRACOWNIKA = ?";
        addressesDAO.update(worker.getAddress());
        jdbcTemplate.update(sql, worker.getFirstName(), worker.getLastName(),
                worker.getPesel(), worker.getDateOfBirth(),
                worker.getEmploymentDate(), worker.getPhoneNumber(),
                String.valueOf(worker.getGender()),
                addressesDAO.findAddressNumber(worker.getAddress()),
                worker.getEmployeeNumber());
    }

    public void delete(int employeeNumber) {
        String sql = "DELETE FROM PRACOWNICY WHERE NR_PRACOWNIKA = ?";
        jdbcTemplate.update(sql, employeeNumber);
    }

    public static class WorkerRowMapper implements RowMapper<Worker> {
        @Override
        public Worker mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Worker worker = new Worker();
            worker.setEmployeeNumber(resultSet.getInt("NR_PRACOWNIKA"));
            worker.setFirstName(resultSet.getString("IMIE"));
            worker.setLastName(resultSet.getString("NAZWISKO"));
            worker.setPesel(resultSet.getString("PESEL"));
            worker.setDateOfBirth(resultSet.getDate("DATA_URODZENIA"));
            worker.setEmploymentDate(resultSet.getDate("DATA_ZATRUDNIENIA"));
            worker.setDismissalDate(resultSet.getDate("DATA_ZWOLNIENIA"));
            worker.setPhoneNumber(resultSet.getString("NUMER_TELEFONU"));
            worker.setGender(resultSet.getString("PLEC").charAt(0));
            worker.setClubNumber(resultSet.getInt("NR_KLUBU"));
            worker.setAddressNumber(resultSet.getInt("NR_ADRESU"));
            worker.setPositionNumber(resultSet.getInt("NR_STANOWISKA"));
            worker.setAccountNumber(resultSet.getInt("NR_KONTA"));

            return worker;
        }
    }
}
