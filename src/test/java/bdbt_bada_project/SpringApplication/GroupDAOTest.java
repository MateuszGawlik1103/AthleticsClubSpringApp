package bdbt_bada_project.SpringApplication;

import bdbt_bada_project.SpringApplication.groups.Group;
import bdbt_bada_project.SpringApplication.groups.GroupDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class GroupDAOTest {

    @Autowired
    GroupDAO dao;

    private int getGroupNumber() {
        return dao.listAll().get(dao.listAll().size() - 1).getGroupNumber();
    }


    @Test
    void listAll() {
        List<Group> groups = dao.listAll();
        assertNotNull(groups);
        assertFalse(groups.isEmpty());
    }

    @Test
    void list() {
        List<Group> groups = dao.list();
        assertNotNull(groups);
    }

    @Test
    void getGroupsByCoachNumber() {
        List<Group> groups = dao.getGroupsByCoachNumber(1);
        assertNotNull(groups);
    }

    @Test
    void getGroupsByMemberNumber() {
        List<Group> groups = dao.getGroupsByMemberNumber(1);
        assertNotNull(groups);
        assertFalse(groups.isEmpty());
    }

    @Test
    void getGroupById() {
        Group group = dao.getGroupById(1);
        assertNotNull(group);
    }

    @Test
    void save() {
        Group group = dao.getGroupById(1);
        group.setGroupNumber(getGroupNumber() + 1);
        dao.save(group);
        assertNotNull(dao.getGroupById(getGroupNumber()));
    }

    @Test
    void groupExists() {
        boolean disciplineExists = dao.groupExists(1);
        assertTrue(disciplineExists);
    }

    @Test
    void update() {
        Group group = dao.getGroupById(1);
        group.setAgeCategory("U19");
        dao.update(group);
        assertEquals("U19", dao.getGroupById(1).getAgeCategory());
    }

    @Test
    void addCoaches() {
        List<Integer> coachNumbers = dao.getCoachNumbersByGroupNumber(1);
        dao.addCoaches(coachNumbers, 1);
        assertFalse(dao.getGroupById(1).getCoachNumbers().isEmpty());
    }

    @Test
    void getCoachNumbersByGroupNumber() {
        List<Integer> coachNumbers = dao.getCoachNumbersByGroupNumber(1);
        assertNotNull(coachNumbers);
        assertFalse(coachNumbers.isEmpty());
    }
}