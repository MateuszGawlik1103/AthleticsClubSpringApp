package bdbt_bada_project.SpringApplication.groups;

import bdbt_bada_project.SpringApplication.disciplines.DisciplineDAO;
import bdbt_bada_project.SpringApplication.members.MembersDAO;
import bdbt_bada_project.SpringApplication.workers.coaches.CoachesDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class GroupDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DisciplineDAO disciplineDAO;
    @Lazy
    @Autowired
    private CoachesDAO coachesDAO;
    @Lazy
    @Autowired
    private MembersDAO membersDAO;

    public GroupDAO(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<Group> listAll(){
        String sql = "SELECT * FROM GRUPY";
        List<Group> groups = jdbcTemplate.query(sql, new GroupDAO.GroupRowMapper());
        groups.forEach(group -> group.setDiscipline(disciplineDAO.getDisciplineById(group.getDisciplineNumber())));
        groups.forEach(group -> group.setCoachNumbers(getCoachNumbersByGroupNumber(group.getGroupNumber())));
        return groups;
    }

    public List<Group> list(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            List<String> roles = authorities
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            String email = authentication.getName();
            if(roles.contains("ADMIN") || roles.contains("PHYSIOTHERAPIST")){
                String sql = "SELECT * FROM GRUPY";
                List<Group> groups = jdbcTemplate.query(sql, new GroupDAO.GroupRowMapper());
                groups.forEach(group -> group.setDiscipline(disciplineDAO.getDisciplineById(group.getDisciplineNumber())));
                groups.forEach(group -> group.setCoachNumbers(getCoachNumbersByGroupNumber(group.getGroupNumber())));
                return groups;
            } else if (roles.contains("COACH")) {
                int coachNumber = coachesDAO.getCoachNumberByEmail(email);
                List<Group> groups = getGroupsByCoachNumber(coachNumber);
                groups.forEach(group -> group.setDiscipline(disciplineDAO.getDisciplineById(group.getDisciplineNumber())));
                groups.forEach(group -> group.setCoachNumbers(getCoachNumbersByGroupNumber(group.getGroupNumber())));
                return groups;
            } else if (roles.contains("MEMBER")){
                int memberNumeber = membersDAO.getMemberNumberByEmail(email);
                List<Group> groups = getGroupsByMemberNumber(memberNumeber);
                groups.forEach(group -> group.setDiscipline(disciplineDAO.getDisciplineById(group.getDisciplineNumber())));
                groups.forEach(group -> group.setCoachNumbers(getCoachNumbersByGroupNumber(group.getGroupNumber())));
                return groups;
            }
        }
        return new ArrayList<>();
    }

    public List<Group> getGroupsByCoachNumber(int employeeNumber){
        String sql1 = "SELECT NR_GRUPY FROM TRENERZY_GRUP WHERE NR_PRACOWNIKA = ?";
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql1, employeeNumber);

        List<Group> groups = new ArrayList<>();
        for (Map<String, Object> row : resultList) {
            sql1 = "SELECT * FROM GRUPY WHERE NR_GRUPY = ?";
            groups.add(jdbcTemplate.queryForObject(sql1, new GroupDAO.GroupRowMapper(),row.get("NR_GRUPY")));
        }
        return groups;
    }
    public List<Group> getGroupsByMemberNumber(int memberNumber){
        String sql1 = "SELECT NR_GRUPY FROM CZLONKOWIE_GRUP WHERE NR_CZLONKA = ?";
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql1, memberNumber);

        List<Group> groups = new ArrayList<>();
        for (Map<String, Object> row : resultList) {
            sql1 = "SELECT * FROM GRUPY WHERE NR_GRUPY = ?";
            groups.add(jdbcTemplate.queryForObject(sql1, new GroupDAO.GroupRowMapper(), row.get("NR_GRUPY")));
        }
        return groups;
    }


    public Group getGroupById(int groupNumber) {
        Object[] args = {groupNumber};
        String sql = "SELECT * FROM GRUPY WHERE NR_GRUPY = " + args[0];
        Group group = jdbcTemplate.queryForObject(sql, new GroupDAO.GroupRowMapper());
        assert group != null;
        group.setCoachNumbers(getCoachNumbersByGroupNumber(groupNumber));
        return group;
    }
    public void save(Group group) {

        SimpleJdbcInsert insertGroup = new SimpleJdbcInsert(jdbcTemplate);
        insertGroup.withTableName("GRUPY").usingGeneratedKeyColumns("NR_GRUPY").usingColumns("CENA_MIESIAC","MAKSYMALNA_LICZBA_CZLONKOW","KAT_WIEKOWA","NR_DYSCYPLINY","NR_KLUBU");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CENA_MIESIAC", group.getPricePerMonth());
        parameters.put("MAKSYMALNA_LICZBA_CZLONKOW", group.getMaxNumberOfMembers());
        parameters.put("KAT_WIEKOWA", group.getAgeCategory());
        parameters.put("NR_DYSCYPLINY", group.getDisciplineNumber());
        parameters.put("NR_KLUBU", 1);

        Number groupId = insertGroup.executeAndReturnKey(parameters);
        int groupNumber = groupId.intValue();

        addCoaches(group.getCoachNumbers(),groupNumber);

    }

    public boolean groupExists(int groupNumber) {
        String sql = "SELECT COUNT(*) FROM GRUPY WHERE NR_GRUPY = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, groupNumber);
        return count > 0;
    }


    public void update(Group group) {
        String selectSql = "SELECT NR_PRACOWNIKA FROM TRENERZY_GRUP WHERE NR_GRUPY = ?";
        List<Integer> actualCoachesNumbers = jdbcTemplate.queryForList(selectSql, Integer.class, group.getGroupNumber());
        List<Integer> updatedCoachesNumbers = group.getCoachNumbers();
        if(updatedCoachesNumbers == null) updatedCoachesNumbers = new ArrayList<>();

        String sql = "UPDATE GRUPY SET CENA_MIESIAC = ?, MAKSYMALNA_LICZBA_CZLONKOW = ?, KAT_WIEKOWA = ?, NR_DYSCYPLINY = ?  WHERE NR_GRUPY = ?";
        if (!groupExists(group.getGroupNumber())) save(group);

        jdbcTemplate.update(sql, group.getPricePerMonth(), group.getMaxNumberOfMembers(), group.getAgeCategory(), group.getDisciplineNumber(), group.getGroupNumber());
        List<Integer> addedCoachesNumbers = new ArrayList<>();

        for (int updatedCoachNumber : updatedCoachesNumbers) {
            if (!actualCoachesNumbers.contains(updatedCoachNumber)) {
                addedCoachesNumbers.add(updatedCoachNumber);
            }
        }
        addCoaches(addedCoachesNumbers,group.getGroupNumber());

        String sqlDelete = "Delete From TRENERZY_GRUP WHERE NR_PRACOWNIKA = ? AND NR_GRUPY = ?";
        for (Integer actualCoachNumber : actualCoachesNumbers) {
            if (!updatedCoachesNumbers.contains(actualCoachNumber)) {
                jdbcTemplate.update(sqlDelete, actualCoachNumber, group.getGroupNumber());
            }
        }
    }

    public void delete(int groupNumber) {
        String sql1 = "Delete FROM TRENERZY_GRUP WHERE NR_GRUPY = ?";
        jdbcTemplate.update(sql1,groupNumber);
        String sql2 = "DELETE FROM TERMINY_ZAJEC_GRUP WHERE NR_GRUPY = ?";
        jdbcTemplate.update(sql2, groupNumber);
        String sql3 = "DELETE FROM CZLONKOWIE_GRUP WHERE NR_GRUPY = ?";
        jdbcTemplate.update(sql3, groupNumber);
        String sql4 = "DELETE FROM GRUPY WHERE NR_GRUPY = ?";
        jdbcTemplate.update(sql4, groupNumber);

    }
    public void addCoaches(List<Integer> coachNumbers, int groupNumber) {
        SimpleJdbcInsert insertCoachToGroup = new SimpleJdbcInsert(jdbcTemplate);
        insertCoachToGroup.withTableName("TRENERZY_GRUP").usingColumns("NR_PRACOWNIKA", "NR_GRUPY");
        if(coachNumbers == null) coachNumbers = new ArrayList<>();

        for (Integer coachNumber : coachNumbers) {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("NR_PRACOWNIKA", coachNumber);
            parameters.addValue("NR_GRUPY", groupNumber);

            insertCoachToGroup.execute(parameters);
        }
    }
    public List<Integer> getCoachNumbersByGroupNumber(int groupNumber) {
        String sql = "SELECT NR_PRACOWNIKA FROM TRENERZY_GRUP WHERE NR_GRUPY = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, groupNumber);
    }

    public static class GroupRowMapper implements RowMapper<Group> {
        @Override
        public Group mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Group group = new Group();
            group.setGroupNumber(resultSet.getInt("NR_GRUPY"));
            group.setPricePerMonth(resultSet.getInt("CENA_MIESIAC"));
            group.setMaxNumberOfMembers(resultSet.getInt("MAKSYMALNA_LICZBA_CZLONKOW"));
            group.setAgeCategory(resultSet.getString("KAT_WIEKOWA"));
            group.setDisciplineNumber(resultSet.getInt("NR_DYSCYPLINY"));
            group.setClubNumber(resultSet.getInt("NR_KLUBU"));
            return group;
        }
    }
}
