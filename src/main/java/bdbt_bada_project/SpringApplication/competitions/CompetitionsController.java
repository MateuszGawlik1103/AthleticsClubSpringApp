package bdbt_bada_project.SpringApplication.competitions;

import bdbt_bada_project.SpringApplication.members.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static bdbt_bada_project.SpringApplication.SecurityConfiguration.authenticate;

@Controller
@RequestMapping(path = "/competitions")
public class CompetitionsController {
    private final CompetitionsDAO dao;

    @Autowired
    public CompetitionsController(CompetitionsDAO competitionsDAO) {
        this.dao = competitionsDAO;
    }

    @GetMapping
    public String getCompetitions(Model model, Authentication authentication) {
        List<Competition> competitions = dao.list();
        model.addAttribute("competitions", competitions);
        authenticate(model, authentication);
        return "tables/competitions";
    }

    @RequestMapping("/new")
    public String showNewForm(Model model, Authentication authentication) {
        authenticate(model, authentication);
        Competition competition = new Competition();
        model.addAttribute("competition", competition);
        return "new/new_competition";
    }

    @PostMapping(value = "/save")
    public String save(@ModelAttribute("competition") Competition competition) {
        dao.save(competition);
        return "redirect:/competitions";
    }

    @RequestMapping("/edit/{competitionNumber}")
    public ModelAndView showEditForm(@PathVariable(name = "competitionNumber") int competitionNumber, Model model, Authentication authentication) {
        authenticate(model, authentication);
        ModelAndView mav = new ModelAndView("edit/edit_competition");
        Competition competition = dao.get(competitionNumber);
        mav.addObject("competition", competition);
        return mav;
    }

    @PostMapping(value = "/update")
    public String update(@ModelAttribute("competition") Competition competition) {
        dao.update(competition);
        return "redirect:/competitions";
    }

    @RequestMapping(value = "/delete/{competitionNumber}")
    public String delete(@PathVariable(name = "competitionNumber") int competitionNumber) {
        dao.delete(competitionNumber);
        return "redirect:/competitions";
    }

    @RequestMapping(value = "/{competitionNumber}")
    public String getCompetition(@PathVariable(name = "competitionNumber") int competitionNumber, Model model, Authentication authentication) {
        authenticate(model, authentication);
        Competition competition = dao.get(competitionNumber);
        List<Member> competitors = dao.getCompetitors(competitionNumber);
        model.addAttribute("competition", competition);
        model.addAttribute("competitors", competitors);
        return "details/competition_details";
    }

}
