package bdbt_bada_project.SpringApplication.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/accounts")
public class AccountController {
    private final AccountDAO dao;
    @Autowired
    public AccountController(AccountDAO dao) {
        this.dao = dao;
    }

    @RequestMapping(value = "/{email}")
    public String redirect(@PathVariable(name = "email") int accountNumber) {
        Account account = dao.get(accountNumber);
        if (account == null) return "redirect:/error";
        String role = account.getRole();

        switch (role) {
            case "ADMIN":
                return "redirect:/main_admin";
            case "COACH":
            case "PHYSIOTHERAPIST":
                int employeeNumber = dao.getWorker(accountNumber).getEmployeeNumber();
                return String.format("redirect:/workers/%d", employeeNumber);
            case "MEMBER":
                int memberNumber = dao.getMember(accountNumber).getMemberNumber();
                return String.format("redirect:/members/%d", memberNumber);
            default:
                return "redirect:/main";
        }
    }
}
