package bdbt_bada_project.SpringApplication.workers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static bdbt_bada_project.SpringApplication.SecurityConfiguration.authenticate;

@Controller
@RequestMapping(path = "/workers")
public class WorkersController {
    private final WorkersDAO dao;

    @Autowired
    public WorkersController(WorkersDAO workersDAO) {
        this.dao = workersDAO;
    }

    @GetMapping
    public String getWorkers(Model model, Authentication authentication) {
        authenticate(model, authentication);
        List<Worker> workers = dao.list();
        model.addAttribute("workers", workers);
        return "tables/workers";
    }
    @RequestMapping(value = "/{employeeNumber}")
    public String redirect(@PathVariable(name = "employeeNumber") int employeeNumber) {
        String position = dao.get(employeeNumber).getPosition();
        if (position.equals("Trener")) return "redirect:/coaches/{employeeNumber}";
        else return "redirect:/physiotherapists/{employeeNumber}";
    }
}
