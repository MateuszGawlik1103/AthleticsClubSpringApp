package bdbt_bada_project.SpringApplication;

import bdbt_bada_project.SpringApplication.workers.physiotherapists.Physiotherapist;
import bdbt_bada_project.SpringApplication.workers.physiotherapists.PhysiotherapistDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PhysiotherapistDAOTest {

    @Autowired
    PhysiotherapistDAO dao;

    @Test
    void getEmployeeById() {
        Physiotherapist physiotherapist = dao.getEmployeeById(3);
        assertNotNull(physiotherapist);
    }

    @Test
    void physioExists() {
        boolean coachExists = dao.physioExists(3);
        assertTrue(coachExists);
    }

}