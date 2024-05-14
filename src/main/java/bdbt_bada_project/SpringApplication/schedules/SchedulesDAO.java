package bdbt_bada_project.SpringApplication.schedules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class SchedulesDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public SchedulesDAO(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Schedule schedule, int groupNumber) {
        SimpleJdbcInsert insertSchedule = new SimpleJdbcInsert(jdbcTemplate);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
        Integer scheduleNumber = getScheduleNumber(schedule);
        if(scheduleNumber == null) {
            insertSchedule.withTableName("TERMINY").usingGeneratedKeyColumns("NR_TERMINU").usingColumns("DZIEN_TYGODNIA", "GODZINA_OD", "GODZINA_DO");

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("DZIEN_TYGODNIA", schedule.getDay());
            parameters.put("GODZINA_OD", schedule.getStartTime());
            parameters.put("GODZINA_DO", schedule.getEndTime());

            Number key = insertSchedule.executeAndReturnKey(new MapSqlParameterSource(parameters));

            insert.withTableName("TERMINY_ZAJEC_GRUP").usingColumns("NR_GRUPY", "NR_TERMINU");

            Map<String, Object> parameters2 = new HashMap<>();
            parameters2.put("NR_GRUPY", groupNumber);
            parameters2.put("NR_TERMINU", key);
            insert.execute(parameters2);
        } else {
            insert.withTableName("TERMINY_ZAJEC_GRUP").usingColumns("NR_GRUPY", "NR_TERMINU");

            Map<String, Object> parameters2 = new HashMap<>();
            parameters2.put("NR_GRUPY", groupNumber);
            parameters2.put("NR_TERMINU", scheduleNumber);
            insert.execute(parameters2);
        }
    }

    public void update(Schedule schedule, int groupNumber) {
        Integer scheduleNumber = getScheduleNumber(schedule);
        if (scheduleNumber == null) {
            String sql = "UPDATE TERMINY SET DZIEN_TYGODNIA = ?, GODZINA_OD = ?, GODZINA_DO = ? WHERE NR_TERMINU = ?";
            String startTime = String.valueOf(schedule.getStartTime());
            String endTime = String.valueOf(schedule.getEndTime());
            jdbcTemplate.update(sql, schedule.getDay(), startTime, endTime, schedule.getScheduleNumber());
        } else {
            String sqlUpdate = "UPDATE TERMINY_ZAJEC_GRUP SET NR_TERMINU = ? WHERE NR_TERMINU = ? AND NR_GRUPY = ?";
            jdbcTemplate.update(sqlUpdate, scheduleNumber, schedule.getScheduleNumber(), groupNumber);
        }
    }
    public Integer getScheduleNumber(Schedule schedule) {
        String sql = "SELECT NR_TERMINU FROM TERMINY WHERE DZIEN_TYGODNIA = ? AND GODZINA_OD = ? AND GODZINA_DO = ?";
        List<Integer> result = jdbcTemplate.queryForList(sql, Integer.class, schedule.getDay(), String.valueOf(schedule.getStartTime()), String.valueOf(schedule.getEndTime()));
        return result.isEmpty() ? null : result.get(0);
    }

    public Schedule getScheduleById(int scheduleNumber) {
        Object[] args = {scheduleNumber};
        String sql = "SELECT * FROM TERMINY WHERE NR_TERMINU = " + args[0];
        Schedule schedule = jdbcTemplate.queryForObject(sql, new SchedulesRowMapper());
        assert schedule != null;
        return schedule;
    }

    public List<Schedule> getSchedulesByGroupNumber(int groupNumber){
        String sql = "SELECT NR_TERMINU FROM TERMINY_ZAJEC_GRUP WHERE NR_GRUPY = ?";
        List<Integer> schedulesNumbers = jdbcTemplate.queryForList(sql, Integer.class, groupNumber);
        return schedulesNumbers.stream()
                .map(this::getScheduleById)
                .collect(Collectors.toList());
    }

    public void delete(int scheduleNumber, int groupNumber) {
        String sql1 = "DELETE FROM TERMINY_ZAJEC_GRUP WHERE NR_TERMINU = ? AND NR_GRUPY = ?";
        jdbcTemplate.update(sql1, scheduleNumber, groupNumber);
    }

    public static class SchedulesRowMapper implements RowMapper<Schedule> {
        @Override
        public Schedule mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Schedule schedule = new Schedule();
            schedule.setScheduleNumber(resultSet.getInt("NR_TERMINU"));
            schedule.setDay(resultSet.getString("DZIEN_TYGODNIA"));
            schedule.setStartTime(LocalTime.parse(resultSet.getString("GODZINA_OD")));
            schedule.setEndTime(LocalTime.parse(resultSet.getString("GODZINA_DO")));
            return schedule;
        }
    }
}
