package bdbt_bada_project.SpringApplication;

import bdbt_bada_project.SpringApplication.groups.Group;
import bdbt_bada_project.SpringApplication.members.Member;
import bdbt_bada_project.SpringApplication.members.MembersDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MembersDAOTest {

    @Autowired
    MembersDAO dao;

    private int getMemberNumber() {
        return dao.list().get(dao.list().size() - 1).getMemberNumber();
    }


    @Test
    void list() {
        List<Member> members = dao.list();
        assertNotNull(members);
        assertFalse(members.isEmpty());
    }

    @Test
    void getMemberNumberByEmail() {
        int memberNumber = dao.getMemberNumberByEmail("member");
        assertEquals(21, memberNumber);
    }

    @Test
    void get() {
        Member member = dao.get(1);
        assertNotNull(member);
    }

    @Test
    void update() {
        Member member = dao.get(getMemberNumber());
        member.setFirstName("Edycja");
        dao.update(member);
        assertEquals("Edycja", member.getFirstName());
    }

    @Test
    void withdraw() {
        dao.withdraw(getMemberNumber());
        Member member = dao.get(getMemberNumber());
        assertNotNull(member.getWithdrawalDate());
    }

    @Test
    void getGroups() {
        List<Group> groups = dao.getGroups(1);
        assertNotNull(groups);
    }

    @Test
    void getMembersFromGroup() {
        List<Member> members = dao.getMembersFromGroup(1);
        assertNotNull(members);
    }

    @Test
    void getMemberByAccountNumber() {
        Member member = dao.getMemberByAccountNumber(3);
        assertNotNull(member);
    }
}