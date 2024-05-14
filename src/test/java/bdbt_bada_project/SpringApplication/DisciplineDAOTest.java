package bdbt_bada_project.SpringApplication;

import bdbt_bada_project.SpringApplication.disciplines.Discipline;
import bdbt_bada_project.SpringApplication.disciplines.DisciplineDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DisciplineDAOTest {

    @Autowired
    DisciplineDAO dao;

    private int getDisciplineNumber() {
        return dao.list().get(dao.list().size() - 1).getDisciplineNumber();
    }

    @Test
    void list() {
        List<Discipline> disciplines = dao.list();
        assertNotNull(disciplines);
        assertFalse(disciplines.isEmpty());
    }

    @Test
    void save() {
        Discipline discipline = dao.get(getDisciplineNumber());
        discipline.setDisciplineNumber(getDisciplineNumber() + 1);
        discipline.setDisciplineName("Test");
        dao.save(discipline);
        assertNotNull(dao.get(getDisciplineNumber()));
    }

    @Test
    void get() {
        Discipline discipline = dao.get(1);
        assertNotNull(discipline);
    }

    @Test
    void update() {
        Discipline discipline = dao.get(getDisciplineNumber());
        discipline.setDescription("Edycja");
        dao.update(discipline);
        discipline = dao.get(getDisciplineNumber());
        assertEquals("Edycja", discipline.getDescription());
    }

    @Test
    void disciplineExists() {
        Discipline discipline = dao.get(1);
        boolean disciplineExists = dao.disciplineExists(discipline);
        assertTrue(disciplineExists);
    }

}