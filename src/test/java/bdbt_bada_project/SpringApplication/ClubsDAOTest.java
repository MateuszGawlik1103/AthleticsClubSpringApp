package bdbt_bada_project.SpringApplication;

import bdbt_bada_project.SpringApplication.clubs.Club;
import bdbt_bada_project.SpringApplication.clubs.ClubsDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
@SpringBootTest
class ClubsDAOTest {
    @Autowired
    private ClubsDAO dao;

    @Test
    void get() {
        Club club = dao.get(1);
        assertNotNull(club);
    }
}