package bdbt_bada_project.SpringApplication.addresses;

import bdbt_bada_project.SpringApplication.mails.MailsDAO;
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
public class AddressesDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MailsDAO mailsDAO;

    public AddressesDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Address> list() {
        String sql = "SELECT * FROM ADRESY";
        return jdbcTemplate.query(sql, new AddressRowMapper());
    }

    public void save(Address address) {
        mailsDAO.save(address.getMail());

        if (addressExists(address)) return;
        SimpleJdbcInsert insertAddress = new SimpleJdbcInsert(jdbcTemplate);
        insertAddress.withTableName("ADRESY").usingColumns("MIASTO", "ULICA", "NUMER_LOKALU", "NR_POCZTY");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("MIASTO", address.getCity());
        parameters.put("ULICA", address.getStreet());
        parameters.put("NUMER_LOKALU", address.getApartmentNumber());
        parameters.put("NR_POCZTY", mailsDAO.findMailNumber(address.getMail()));

        insertAddress.execute(parameters);
    }

    public Address get(int addressNumber) {
        Object[] args = {addressNumber};
        String sql = "SELECT * FROM ADRESY WHERE NR_ADRESU = " + args[0];
        Address address = jdbcTemplate.queryForObject(sql, new AddressRowMapper());
        assert address != null;
        address.setMail(mailsDAO.get(address.getMailNumber()));
        return address;

    }

    public void update(Address address) {
        String sql = "UPDATE ADRESY SET MIASTO = ?, ULICA = ?, NUMER_LOKALU = ?, NR_POCZTY = ? WHERE NR_ADRESU = ?";
        mailsDAO.update(address.getMail());
        if (!addressExists(address)) save(address);
        jdbcTemplate.update(sql, address.getCity(), address.getStreet(), address.getApartmentNumber(),
                mailsDAO.findMailNumber(address.getMail()), address.getAddressNumber());
    }

    public boolean addressExists(Address address) {
        String sql = "SELECT COUNT(*) FROM ADRESY WHERE MIASTO = ? AND ULICA = ? AND NUMER_LOKALU = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, address.getCity(), address.getStreet(), address.getApartmentNumber());
        return count > 0;
    }

    public int findAddressNumber(Address address) {
        if (addressExists(address)) {
            String sql = "SELECT NR_ADRESU FROM ADRESY WHERE MIASTO = ? AND ULICA = ? AND NUMER_LOKALU = ? FETCH FIRST 1 ROW ONLY";
            return jdbcTemplate.queryForObject(sql, Integer.class,
                    address.getCity(), address.getStreet(), address.getApartmentNumber());
        } else return -1;
    }


    private static class AddressRowMapper implements RowMapper<Address> {
        @Override
        public Address mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Address address = new Address();
            address.setAddressNumber(resultSet.getInt("NR_ADRESU"));
            address.setCity(resultSet.getString("MIASTO"));
            address.setStreet(resultSet.getString("ULICA"));
            address.setApartmentNumber(resultSet.getString("NUMER_LOKALU"));
            address.setMailNumber(resultSet.getInt("NR_POCZTY"));

            return address;
        }
    }
}
