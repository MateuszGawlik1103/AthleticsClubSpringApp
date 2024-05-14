package bdbt_bada_project.SpringApplication.mails;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
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
public class MailsDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public MailsDAO(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Mail> list(){
        String sql = "SELECT * FROM POCZTY";
        return jdbcTemplate.query(sql, new MailRowMapper());
    }

    public void save(Mail mail) {
        if (mailExists(mail)) return;
        SimpleJdbcInsert insertActor = new SimpleJdbcInsert(jdbcTemplate);
        insertActor.withTableName("POCZTY").usingColumns("KOD_POCZTY", "POCZTA");

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("KOD_POCZTY", mail.getPostalCode());
        parameters.put("POCZTA", getName(mail.getPostalCode()));

        insertActor.execute(parameters);
    }

    public Mail get(int mailNumber) {
        Object[] args = {mailNumber};
        String sql = "SELECT * FROM POCZTY WHERE NR_POCZTY = " + args[0];
        return jdbcTemplate.queryForObject(sql, new MailRowMapper());
    }

    public void update(Mail mail) {
        String sql = "UPDATE POCZTY SET POCZTA = ?, KOD_POCZTY = ? WHERE NR_POCZTY = ?";
        if (!mailExists(mail)) save(mail);
        jdbcTemplate.update(sql, getName(mail.getPostalCode()),
                mail.getPostalCode(),mail.getMailNumber());
    }

    public String getName(String postalCode) {
        try {
            String url = String.format("http://00-000.pl/a/a/%s/", postalCode);
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select("td.row");
            return elements.get(1).text().split(",")[0];
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public boolean mailExists(Mail mail) {
        String sql = "SELECT COUNT(*) FROM POCZTY WHERE KOD_POCZTY = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, mail.getPostalCode());
        return count > 0;
    }

    public int findMailNumber(Mail mail) {
        if (mailExists(mail)) {
            String sql = "SELECT NR_POCZTY FROM POCZTY WHERE KOD_POCZTY = ? FETCH FIRST 1 ROW ONLY";
            return jdbcTemplate.queryForObject(sql, Integer.class, mail.getPostalCode());
        } else return -1;
    }

    private static class MailRowMapper implements RowMapper<Mail> {
        @Override
        public Mail mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Mail mail = new Mail();
            mail.setMailNumber(resultSet.getInt("NR_POCZTY"));
            mail.setPostalCode(resultSet.getString("KOD_POCZTY"));
            mail.setName(resultSet.getString("POCZTA"));

            return mail;
        }
    }
}
