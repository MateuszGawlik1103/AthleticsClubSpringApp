package bdbt_bada_project.SpringApplication.accounts;

import bdbt_bada_project.SpringApplication.members.Member;
import bdbt_bada_project.SpringApplication.members.MembersDAO;
import bdbt_bada_project.SpringApplication.workers.Worker;
import bdbt_bada_project.SpringApplication.workers.WorkersDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class AccountDAO {
    @Autowired
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public AccountDAO(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Account> list(){
        String sql = "SELECT * FROM KONTA";
        return jdbcTemplate.query(sql, new AccountRowMapper());
    }

    public void save(Account account) {
        try {
            SimpleJdbcInsert insertActor = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("KONTA");

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("EMAIL", account.getEmail());
            parameters.put("HASLO", passwordEncoder.encode(account.getPassword()));
            parameters.put("ROLA", account.getRole());

            insertActor.execute(parameters);
        } catch (DuplicateKeyException e)  {
            handleDuplicateKeyException(e);
        }
    }

    private void handleDuplicateKeyException(DuplicateKeyException e) {
        throw new AccountAlreadyExistsException("Konto o podanym adresie email już istnieje.", e);
    }

    public Account get(int accountNumber) {
        String sql = "SELECT * FROM KONTA WHERE NR_KONTA = ?";
        return jdbcTemplate.queryForObject(sql, new AccountRowMapper(), accountNumber);
    }

    public Account get(String email) {
        String sql = "SELECT * FROM KONTA WHERE EMAIL = ?";
        return jdbcTemplate.queryForObject(sql, new AccountRowMapper(), email);
    }

    public void update(Account account) {
        String sql = "UPDATE KONTA SET EMAIL = ?, HASLO = ?, ROLA = ? WHERE NR_KONTA = ?";
        jdbcTemplate.update(sql, account.getEmail(), account.getPassword(),
                account.getRole(), account.getAccountNumber());
    }


    public Worker getWorker(int accountNumber) {
        String sql = "SELECT * FROM PRACOWNICY WHERE NR_KONTA = ?";
        return Objects.requireNonNull(jdbcTemplate.queryForObject(sql, new WorkersDAO.WorkerRowMapper(), accountNumber));
    }

    public Worker getWorker(String email) {
        Account account = get(email);
        Worker worker = getWorker(findAccountNumber(account));
        worker.setAccount(account);
        return worker;
    }

    public Member getMember(int accountNumber) {
        String sql = "SELECT * FROM CZLONKOWIE WHERE NR_KONTA = ?";
        return jdbcTemplate.queryForObject(sql, new MembersDAO.MemberRowMapper(), accountNumber);
    }

    public Member getMember(String email) {
        Account account = get(email);
        Member member = getMember(findAccountNumber(account));
        member.setAccount(account);
        return member;
    }

    public boolean accountExists(Account account) {
        String sql = "SELECT COUNT(*) FROM KONTA WHERE EMAIL = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, account.getEmail());
        return count > 0;
    }


    public int findAccountNumber(Account account) {
        if (accountExists(account)) {
            String sql = "SELECT NR_KONTA FROM KONTA WHERE EMAIL = ?";
            return Objects.requireNonNull(jdbcTemplate.queryForObject(sql, Integer.class, account.getEmail()));
        } else return -1;
    }

    public static class AccountRowMapper implements RowMapper<Account> {
        @Override
        public Account mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Account account = new Account();
            account.setEmail(resultSet.getString("EMAIL"));
            account.setPassword(resultSet.getString("HASLO"));
            account.setRole(resultSet.getString("ROLA"));

            return account;
        }
    }
    public class AccountAlreadyExistsException extends RuntimeException {
        public AccountAlreadyExistsException(String message) {
            super(message);
        }

        public AccountAlreadyExistsException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
