package bdbt_bada_project.SpringApplication.workers.coaches;

import bdbt_bada_project.SpringApplication.accounts.AccountDAO;
import bdbt_bada_project.SpringApplication.groups.Group;
import bdbt_bada_project.SpringApplication.groups.GroupDAO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static bdbt_bada_project.SpringApplication.SecurityConfiguration.authenticate;

@Controller
@RequestMapping(path = "/coaches")
public class CoachesController {
    private final CoachesDAO coachesDAO;
    private final GroupDAO groupDAO;
    private final AccountDAO accountDAO;

    public CoachesController(CoachesDAO coachesDAO, GroupDAO groupDAO, AccountDAO accountDAO) {
        this.coachesDAO = coachesDAO;
        this.groupDAO = groupDAO;
        this.accountDAO = accountDAO;
    }

    @GetMapping
    public String getCoaches(Model model, Authentication authentication) {
        List<Coach> coaches = coachesDAO.list();
        model.addAttribute("coaches", coaches);
        authenticate(model, authentication);
        return "tables/coaches";
    }
    @PostMapping(value = "/save")
    public String save(@ModelAttribute("coach") Coach coach, Model model) {
        if (accountDAO.accountExists(coach.getAccount())) {
            model.addAttribute("emailExistsError", true);
            model.addAttribute("coach");
            return "new/new_coach";
        }
        coachesDAO.save(coach);
        return "redirect:/coaches";
    }
    @RequestMapping("/new")
    public String showNewForm(Model model, Authentication authentication) {
        authenticate(model, authentication);
        Coach coach = new Coach();
        model.addAttribute("coach", coach);
        return "new/new_coach";
    }
    @RequestMapping("/edit/{employeeNumber}")
    public ModelAndView showEditForm(@PathVariable(name = "employeeNumber") int employeeNumber, Model model, Authentication authentication) {
        authenticate(model, authentication);
        ModelAndView mav = new ModelAndView("edit/edit_coach");
        Coach coach = coachesDAO.getEmployeeById(employeeNumber);

        mav.addObject("coach", coach);
        return mav;
    }
    @PostMapping(value = "/update")
    public String update(@ModelAttribute("coach") Coach coach) {
        coachesDAO.update(coach);
        return "redirect:/coaches";
    }
    @RequestMapping(value = "/dismiss/{employeeNumber}")
    public String dismiss(@PathVariable(name = "employeeNumber") int employeeNumber) {

        coachesDAO.dismiss(employeeNumber);
        return "redirect:/coaches";
    }
    @RequestMapping(value = "/{employeeNumber}")
    public String getCoach(@PathVariable(name = "employeeNumber") int employeeNumber, Model model, Authentication authentication){
        authenticate(model, authentication);
        Coach coach = coachesDAO.getEmployeeById(employeeNumber);
        List<Group> allGroups = groupDAO.listAll();
        List<Group> groups = new ArrayList<>();
        for (Group group : allGroups){
            List<Integer> coachNumbers = group.getCoachNumbers();
            if(coachNumbers != null){
                if(group.getCoachNumbers().contains(employeeNumber)){
                    groups.add(group);
                }
            }
        }
        model.addAttribute("coach", coach);
        model.addAttribute("groups", groups);
        return "details/coach_details";
    }
}
