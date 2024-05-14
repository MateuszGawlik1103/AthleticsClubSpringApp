package bdbt_bada_project.SpringApplication;

import bdbt_bada_project.SpringApplication.positions.Position;
import bdbt_bada_project.SpringApplication.positions.PositionsDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PositionsDAOTest {
    @Autowired
    private PositionsDAO dao;

    @Test
    void list() {
        List<Position> listPosition = dao.list();
        assertFalse(listPosition.isEmpty());
    }

    @Test
    void get() {
        Position position = dao.get(1);
        assertNotNull(position);
    }

    @Test
    void getNameByPositionNumber() {
        String name = dao.getNameByPositionNumber(1);
        assertEquals("Trener", name);
    }

    @Test
    void getPositionNumberByName() {
        int positionNumber = dao.getPositionNumberByName("Trener");
        assertEquals(1, positionNumber);
    }

}