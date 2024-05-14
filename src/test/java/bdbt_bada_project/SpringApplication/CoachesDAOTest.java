package bdbt_bada_project.SpringApplication;

import bdbt_bada_project.SpringApplication.workers.coaches.Coach;
import bdbt_bada_project.SpringApplication.workers.coaches.CoachesDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CoachesDAOTest {
    @Autowired
    CoachesDAO dao;
    @Test
    void listFreeCoaches() {
        List<Coach> coaches = dao.listFreeCoaches();
        assertNotNull(coaches);
    }

    @Test
    void getEmployeeById() {
        Coach coach = dao.getEmployeeById(21);
        assertNotNull(coach);
    }

    @Test
    void coachExists() {
        boolean coachExists = dao.coachExists(21);
        assertTrue(coachExists);
    }

    @Test
    void getCoachNumberByEmail() {
        int employeeNumber = dao.getCoachNumberByEmail("janusz");
        assertNotEquals(0, employeeNumber);
    }
}