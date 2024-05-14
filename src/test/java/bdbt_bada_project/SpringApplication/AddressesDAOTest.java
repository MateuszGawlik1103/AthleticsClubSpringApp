package bdbt_bada_project.SpringApplication;

import bdbt_bada_project.SpringApplication.addresses.Address;
import bdbt_bada_project.SpringApplication.addresses.AddressesDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AddressesDAOTest {

    @Autowired
    private AddressesDAO dao;

    @Test
    void list() {
        List<Address> addresses = dao.list();
        assertNotNull(addresses);
        assertFalse(addresses.isEmpty());
    }

    @Test
    void save() {
        Address address = dao.get(1);
        dao.save(address);
        int addressNumber = address.getAddressNumber();
        assertNotNull(dao.get(addressNumber));
    }

    @Test
    void get() {
        Address address = dao.get(1);
        assertNotNull(address);
    }

    @Test
    void update() {
        Address address = dao.get(1);
        address.setCity("Warszawa");
        dao.update(address);
        address = dao.get(1);
        assertEquals("Warszawa", address.getCity());
    }

    @Test
    void addressExists() {
        Address address = dao.get(1);
        boolean addressExists = dao.addressExists(address);
        assertTrue(addressExists);
    }
}