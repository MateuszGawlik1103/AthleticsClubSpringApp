package bdbt_bada_project.SpringApplication;

import bdbt_bada_project.SpringApplication.workers.Worker;
import bdbt_bada_project.SpringApplication.workers.WorkersDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class WorkersDAOTest {

    @Autowired
    private WorkersDAO dao;

    private int getEmployeeNumber() {
        return dao.list().get(dao.list().size() - 1).getEmployeeNumber();
    }

    @Test
    void list() {
        List<Worker> listWorker = dao.list();
        assertNotNull(listWorker);
        assertFalse(listWorker.isEmpty());
    }

    @Test
    void save() {
        int employeeNumber = getEmployeeNumber();
        Worker worker = dao.get(employeeNumber);
        worker.setEmployeeNumber(employeeNumber + 1);
        dao.save(worker);
        assertNotNull(dao.get(getEmployeeNumber()));
    }

    @Test
    void get() {
        Worker worker = dao.get(getEmployeeNumber());
        assertNotNull(worker);
    }

    @Test
    void update() {
        Worker worker = dao.get(getEmployeeNumber());
        worker.setFirstName("Edycja");
        dao.update(worker);
        worker = dao.get(getEmployeeNumber());
        assertEquals("Edycja", worker.getFirstName());
    }

    @Test
    void delete() {
        int employeeNumber = getEmployeeNumber();
        Worker worker = dao.get(employeeNumber);
        dao.delete(employeeNumber);
        assertNotEquals(worker, dao.get(getEmployeeNumber()));
    }
}