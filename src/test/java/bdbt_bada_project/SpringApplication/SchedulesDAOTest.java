package bdbt_bada_project.SpringApplication;

import bdbt_bada_project.SpringApplication.schedules.Schedule;
import bdbt_bada_project.SpringApplication.schedules.SchedulesDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class SchedulesDAOTest{

    @Autowired
    SchedulesDAO dao;

    @Test
    void save() {
        Schedule schedule = new Schedule();
        schedule.setDay("PN");
        schedule.setStartTime(LocalTime.MIDNIGHT);
        schedule.setEndTime(LocalTime.NOON);
        dao.save(schedule, 1);
    }

    @Test
    void update() {
        Schedule schedule = dao.getScheduleById(1);
        schedule.setDay("WT");
        dao.update(schedule, 1);
        schedule = dao.getScheduleById(1);
        assertEquals("WT", schedule.getDay());
    }

    @Test
    void getScheduleNumber() {
        Schedule schedule = dao.getScheduleById(1);
        int scheduleNumber = dao.getScheduleNumber(schedule);
        assertNotEquals(0, scheduleNumber);
    }

    @Test
    void getScheduleById() {
        Schedule schedule = dao.getScheduleById(1);
        assertNotNull(schedule);
    }

    @Test
    void getSchedulesByGroupNumber() {
        List<Schedule> schedules = dao.getSchedulesByGroupNumber(1);
        assertNotNull(schedules);
        assertFalse(schedules.isEmpty());
    }

    @Test
    void delete() {
        dao.delete(5, 1);
    }
}