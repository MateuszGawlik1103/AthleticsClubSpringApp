package bdbt_bada_project.SpringApplication;

import bdbt_bada_project.SpringApplication.accounts.Account;
import bdbt_bada_project.SpringApplication.accounts.AccountDAO;
import bdbt_bada_project.SpringApplication.members.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountDAOTest {
    @Autowired
    private AccountDAO dao;

    @Test
    void list() {
        List<Account> accounts = dao.list();
        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());
    }

    @Test
    void save() {
        Account account = new Account();
        dao.save(account);
        int accountNumber = account.getAccountNumber();
        assertNotNull(dao.get(accountNumber));
    }

    @Test
    void getByAccountNumber() {
        Account account = dao.get(1);
        assertNotNull(account);
    }

    @Test
    void getByEmail() {
        Account account = dao.get("admin");
        assertNotNull(account);
    }

    @Test
    void update() {
        Account account = dao.get(2);
        account.setEmail("janusz.kowalski@gmail.com");
        dao.update(account);

        Account updatedAccount = dao.get(2);
        assertEquals("janusz.kowalski@gmail.com", updatedAccount.getEmail());
    }

    @Test
    void getMemberByAccountNumber() {
        Member member = dao.getMember(3);
        assertNotNull(member);
    }

    @Test
    void getMemberByEmail() {
        Member member = dao.getMember("member");
        assertNotNull(member);
    }
}