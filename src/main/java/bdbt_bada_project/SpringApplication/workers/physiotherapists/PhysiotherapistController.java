package bdbt_bada_project.SpringApplication.workers.physiotherapists;

import bdbt_bada_project.SpringApplication.accounts.AccountDAO;
import bdbt_bada_project.SpringApplication.workers.WorkersDAO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static bdbt_bada_project.SpringApplication.SecurityConfiguration.authenticate;

@Controller
@RequestMapping(path = "/physiotherapists")
public class PhysiotherapistController {
    private final PhysiotherapistDAO dao;
    private final WorkersDAO workersDAO;
    private final AccountDAO accountDAO;


    public PhysiotherapistController(PhysiotherapistDAO dao, WorkersDAO workersDAO, AccountDAO accountDAO) {
        this.dao = dao;
        this.workersDAO = workersDAO;
        this.accountDAO = accountDAO;
    }

    @GetMapping
    public String getPhysios(Model model, Authentication authentication) {
        authenticate(model, authentication);
        List<Physiotherapist> physios = dao.list();
        model.addAttribute("physios", physios);
        return "tables/physiotherapists";
    }
    @PostMapping(value = "/save")
    public String save(@ModelAttribute("physiotherapist") Physiotherapist physiotherapist, Model model) {
        if (accountDAO.accountExists(physiotherapist.getAccount())) {
            model.addAttribute("emailExistsError", true);
            model.addAttribute("physiotherapist", physiotherapist);
            return "new/new_physio";
        }
        dao.save(physiotherapist);
        return "redirect:/physiotherapists";
    }
    @RequestMapping("/new")
    public String showNewForm(Model model, Authentication authentication) {
        authenticate(model, authentication);
        Physiotherapist physiotherapist = new Physiotherapist();
        model.addAttribute("physiotherapist", physiotherapist);
        return "new/new_physio";
    }

    @RequestMapping("/edit/{employeeNumber}")
    public ModelAndView showEditForm(@PathVariable(name = "employeeNumber") int employeeNumber, Model model, Authentication authentication) {
        authenticate(model, authentication);
        ModelAndView mav = new ModelAndView("edit/edit_physio");
        Physiotherapist physiotherapist = dao.getEmployeeById(employeeNumber);
        mav.addObject("physiotherapist", physiotherapist);
        return mav;
    }
    @PostMapping(value = "/update")
    public String update(@ModelAttribute("physio") Physiotherapist physio) {
        dao.update(physio);
        return "redirect:/physiotherapists";
    }
    @RequestMapping(value = "/dismiss/{employeeNumber}")
    public String dismiss(@PathVariable(name = "employeeNumber") int employeeNumber){
        dao.dismiss(employeeNumber);
        return "redirect:/physiotherapists";
    }
    @RequestMapping(value = "/{employeeNumber}")
    public String getPhysio(@PathVariable(name = "employeeNumber") int employeeNumber, Model model, Authentication authentication){
        authenticate(model, authentication);
        Physiotherapist physio = dao.getEmployeeById(employeeNumber);
        model.addAttribute("physio", physio);
        return "details/physio_details";
    }

}
