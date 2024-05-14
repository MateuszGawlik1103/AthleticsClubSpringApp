package bdbt_bada_project.SpringApplication;

import bdbt_bada_project.SpringApplication.competitions.Competition;
import bdbt_bada_project.SpringApplication.competitions.CompetitionsDAO;
import bdbt_bada_project.SpringApplication.members.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CompetitionsDAOTest {

    @Autowired
    CompetitionsDAO dao;

    private int getCompetitionNumber() {
         return dao.list().get(dao.list().size() - 1).getCompetitionNumber();
    }

    @Test
    void list() {
        List<Competition> competitions = dao.list();
        assertNotNull(competitions);
        assertFalse(competitions.isEmpty());
    }

    @Test
    void get() {
        Competition competition = dao.get(getCompetitionNumber());
        assertNotNull(competition);
    }

    @Test
    void save() {
        Competition competition = dao.get(getCompetitionNumber());
        competition.setCompetitionNumber(getCompetitionNumber() + 1);
        dao.save(competition);
        assertNotNull(dao.get(getCompetitionNumber()));
    }

    @Test
    void update() {
        Competition competition = dao.get(getCompetitionNumber());
        competition.setDescription("Edycja");
        dao.update(competition);
        competition = dao.get(getCompetitionNumber());
        assertEquals("Edycja", competition.getDescription());
    }

    @Test
    void delete() {
        int competitionNumber = getCompetitionNumber();
        Competition competition = dao.get(getCompetitionNumber());
        dao.delete(competitionNumber);
        assertNotEquals(competition, dao.get(getCompetitionNumber()));
    }

    @Test
    void getCompetitors() {
        List<Member> members = dao.getCompetitors(1);
        assertNotNull(members);
    }
}