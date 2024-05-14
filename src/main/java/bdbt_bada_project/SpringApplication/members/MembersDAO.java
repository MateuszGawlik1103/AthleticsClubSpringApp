package bdbt_bada_project.SpringApplication.members;

import bdbt_bada_project.SpringApplication.accounts.AccountDAO;
import bdbt_bada_project.SpringApplication.addresses.AddressesDAO;
import bdbt_bada_project.SpringApplication.disciplines.DisciplineDAO;
import bdbt_bada_project.SpringApplication.groups.Group;
import bdbt_bada_project.SpringApplication.groups.GroupDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MembersDAO {

    @Autowired
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    private AddressesDAO addressesDAO;
    @Autowired
    private AccountDAO accountDAO;
    @Autowired
    private DisciplineDAO disciplineDAO;

    public MembersDAO(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Member> list(){
        String sql = "SELECT * FROM Czlonkowie";
        List<Member> members = jdbcTemplate.query(sql, new MemberRowMapper());
        members.forEach(member -> member.setAddress(addressesDAO.get(member.getAddressNumber())));
        members.forEach(member -> {
            if(member.getWithdrawalDate() == null){
                member.setAccount(accountDAO.get(member.getAccountNumber()));
            }
        });
        return members;
    }
    public int getMemberNumberByEmail(String email){
        String sql1 = "SELECT NR_KONTA FROM KONTA WHERE EMAIL = ?";
        int accountNumber = jdbcTemplate.queryForObject(sql1, Integer.class, email);
        String sql2 = "SELECT NR_CZLONKA FROM CZLONKOWIE WHERE NR_KONTA = ?";
        return jdbcTemplate.queryForObject(sql2, Integer.class, accountNumber);
    }

    public void save(Member member) {
        accountDAO.save(member.getAccount());
        addressesDAO.save(member.getAddress());

        SimpleJdbcInsert insertActor = new SimpleJdbcInsert(jdbcTemplate);
        insertActor.withTableName("Czlonkowie").
                usingGeneratedKeyColumns("NR_CZLONKA")
                .usingColumns(
                        "Imie",
                        "Nazwisko",
                        "PESEL",
                        "Data_urodzenia",
                        "Data_zapisania",
                        "Data_wypisania",
                        "Plec",
                        "Badania_lekarskie_waznosc",
                        "Nr_klubu",
                        "Nr_adresu",
                        "Nr_konta");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("IMIE", member.getFirstName());
        parameters.put("NAZWISKO", member.getLastName());
        parameters.put("PESEL", member.getPesel());
        parameters.put("DATA_URODZENIA", member.getDateOfBirth());
        parameters.put("DATA_ZAPISANIA", member.getRegistrationDate());
        parameters.put("DATA_WYPISANIA", member.getWithdrawalDate());
        parameters.put("PLEC", String.valueOf(member.getGender()));
        parameters.put("BADANIA_LEKARSKIE_WAZNOSC", member.getMedicalExaminationValidity());
        parameters.put("NR_KLUBU", 1);
        parameters.put("NR_ADRESU", addressesDAO.findAddressNumber(member.getAddress()));
        parameters.put("NR_KONTA", accountDAO.findAccountNumber(member.getAccount()));

        Number memberKey = insertActor.executeAndReturnKey(parameters).intValue();
        SimpleJdbcInsert insertToGroup = new SimpleJdbcInsert(jdbcTemplate);
        insertToGroup.withTableName("CZLONKOWIE_GRUP").usingColumns("NR_CZLONKA", "NR_GRUPY");

        Map<String, Object> parameters2 = new HashMap<>();
        List<Integer> groupsNumbers = member.getGroupsNumbers();
        if(groupsNumbers != null){
            groupsNumbers.forEach(groupNumber -> {
                System.out.println(groupNumber);
                parameters2.put("NR_CZLONKA", memberKey);
                parameters2.put("NR_GRUPY", groupNumber);
                insertToGroup.execute(parameters2);
            });
        }

    }

    public Member get(int memberNumber) {
        Object[] args = {memberNumber};
        String sql = "SELECT * FROM CZLONKOWIE WHERE NR_CZLONKA = " + args[0];
        Member member = jdbcTemplate.queryForObject(sql, new MemberRowMapper());
        assert member != null;
        member.setAddress(addressesDAO.get(member.getAddressNumber()));
        if(member.getAccountNumber() != 0){
            member.setAccount(accountDAO.get(member.getAccountNumber()));
        }


        return member;
    }

    public void update(Member member) {
        String selectSql = "SELECT NR_GRUPY FROM CZLONKOWIE_GRUP WHERE NR_CZLONKA = ?";
        List<Integer> actualGroupsNumbers = jdbcTemplate.queryForList(selectSql, Integer.class, member.getMemberNumber());
        List<Integer> updatedGroupsNumbers = member.getGroupsNumbers();

        String sql = "UPDATE CZLONKOWIE SET IMIE = ?, NAZWISKO = ?, PESEL = ?, " +
                "DATA_URODZENIA = ?, DATA_ZAPISANIA = ?, " +
                "PLEC = ?, BADANIA_LEKARSKIE_WAZNOSC = ?, NR_ADRESU = ?" +
                "WHERE NR_CZLONKA = ?";

        addressesDAO.update(member.getAddress());


        jdbcTemplate.update(sql, member.getFirstName(), member.getLastName(),
                member.getPesel(), member.getDateOfBirth(),
                member.getRegistrationDate(),
                String.valueOf(member.getGender()),
                member.getMedicalExaminationValidity(),
                addressesDAO.findAddressNumber(member.getAddress()),
                member.getMemberNumber());
        List<Integer> addedGroupsNumbers = new ArrayList<>();

        for (int updatedGroupNumber : updatedGroupsNumbers) {
            if (!actualGroupsNumbers.contains(updatedGroupNumber)) {
                addedGroupsNumbers.add(updatedGroupNumber);
            }
        }
        addGroups(addedGroupsNumbers, member.getMemberNumber());

        String sqlDelete = "Delete From CZLONKOWIE_GRUP WHERE NR_CZLONKA = ? AND NR_GRUPY = ?";
        for (Integer actualGroupNumber : actualGroupsNumbers) {
            if (!updatedGroupsNumbers.contains(actualGroupNumber)) {
                jdbcTemplate.update(sqlDelete, member.getMemberNumber(), actualGroupNumber);
            }
        }
    }
    public void addGroups(List<Integer> groupsNumbers, int memberNumber){
        SimpleJdbcInsert insertMemberToGroup = new SimpleJdbcInsert(jdbcTemplate);
        insertMemberToGroup.withTableName("CZLONKOWIE_GRUP").usingColumns("NR_CZLONKA", "NR_GRUPY");

        for (Integer groupNumber : groupsNumbers) {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("NR_CZLONKA", memberNumber);
            parameters.addValue("NR_GRUPY", groupNumber);

            insertMemberToGroup.execute(parameters);
        }
    }

    public void withdraw(int memberNumber) {
        LocalDate actualDate = LocalDate.now();
        String sql1 = "DELETE FROM CZLONKOWIE_GRUP WHERE NR_CZLONKA = ?";
        String sql2 = "DELETE FROM KONTA WHERE NR_KONTA = ?";
        String sql3 = "UPDATE CZLONKOWIE SET DATA_WYPISANIA = ?, NR_KONTA = ? WHERE NR_CZLONKA = ?";
        jdbcTemplate.update(sql1, memberNumber);
        jdbcTemplate.update(sql2, get(memberNumber).getAccountNumber());
        jdbcTemplate.update(sql3, actualDate, null,  memberNumber);
    }

    public List<Group> getGroups(int memberNumber) {
        String sql = "SELECT grupy.* " +
                "FROM CZLONKOWIE_GRUP czlonkowie_grup " +
                "JOIN GRUPY grupy ON czlonkowie_grup.NR_GRUPY = grupy.NR_GRUPY " +
                "WHERE czlonkowie_grup.NR_CZLONKA = ?";
        List<Group> groups = jdbcTemplate.query(sql, new GroupDAO.GroupRowMapper(), memberNumber);
        groups.forEach(group -> group.setDiscipline(disciplineDAO.getDisciplineById(group.getDisciplineNumber())));
        return groups;
    }

    public List<Member> getMembersFromGroup(int groupNumber){
        String sql1 = "SELECT NR_CZLONKA FROM CZLONKOWIE_GRUP WHERE NR_GRUPY = ?";
        List<Integer> membersNumbers = jdbcTemplate.queryForList(sql1, Integer.class, groupNumber);
        List<Member> addedMembers = new ArrayList<>();
        membersNumbers.forEach(memberNumber -> {
            String sql2 = "SELECT * FROM CZLONKOWIE WHERE NR_CZLONKA = ?";
            Member member = jdbcTemplate.queryForObject(sql2, new MemberRowMapper(), memberNumber);
            assert member != null;
            member.setAddress(addressesDAO.get(member.getAddressNumber()));
            addedMembers.add(member);
        });
        return addedMembers;
    }

    public Member getMemberByAccountNumber(int accountNumber) {
        Object[] args = {accountNumber};
        String sql = "SELECT * FROM CZLONKOWIE WHERE NR_KONTA = " + args[0];
        Member member = jdbcTemplate.queryForObject(sql, new MemberRowMapper());
        assert member != null;
        member.setAddress(addressesDAO.get(member.getAddressNumber()));

        return member;
    }

    public static class MemberRowMapper implements RowMapper<Member> {
        @Override
        public Member mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Member member = new Member();
            member.setMemberNumber(resultSet.getInt("NR_CZLONKA"));
            member.setFirstName(resultSet.getString("IMIE"));
            member.setLastName(resultSet.getString("NAZWISKO"));
            member.setRegistrationDate(resultSet.getDate("DATA_ZAPISANIA"));
            member.setWithdrawalDate(resultSet.getDate("DATA_WYPISANIA"));
            member.setDateOfBirth(resultSet.getDate("DATA_URODZENIA"));
            member.setPesel(resultSet.getString("PESEL"));
            member.setGender(resultSet.getString("PLEC").charAt(0));
            member.setMedicalExaminationValidity(resultSet.getDate("BADANIA_LEKARSKIE_WAZNOSC"));
            member.setClubNumber(resultSet.getInt("NR_KLUBU"));
            member.setAddressNumber(resultSet.getInt("NR_ADRESU"));
            int accountNumber = resultSet.getInt("NR_KONTA");
            member.setAccountNumber(resultSet.wasNull() ? 0 : accountNumber);

            return member;
        }
    }
}
