package bdbt_bada_project.SpringApplication;

import bdbt_bada_project.SpringApplication.mails.Mail;
import bdbt_bada_project.SpringApplication.mails.MailsDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MailsDAOTest {
    @Autowired
    private MailsDAO dao;

    private int getMailNumber() {
        return dao.list().get(dao.list().size() - 1).getMailNumber();
    }

    @Test
    void list() {
        List<Mail> mails = dao.list();
        assertNotNull(mails);
        assertFalse(mails.isEmpty());
    }

    @Test
    void save() {
        Mail mail = dao.get(getMailNumber());
        mail.setMailNumber(getMailNumber() + 1);
        dao.save(mail);
        assertNotNull(dao.get(getMailNumber()));
    }

    @Test
    void get() {
        Mail mail = dao.get(1);
        assertNotNull(mail);
    }

    @Test
    void getName() {
        String name = dao.getName("00-117");
        assertEquals(name, "Al. Jana Pawła II od 2 do 10");
    }

    @Test
    void mailExists() {
        Mail mail = dao.get(1);
        boolean mailExists = dao.mailExists(mail);
        assertTrue(mailExists);
    }

    @Test
    void findMailNumber() {
        Mail mail = dao.get(1);
        int mailNumber = dao.findMailNumber(mail);
        assertEquals(1, mailNumber);
    }
}