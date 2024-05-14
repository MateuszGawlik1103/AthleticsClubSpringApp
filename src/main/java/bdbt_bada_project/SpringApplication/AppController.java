package bdbt_bada_project.SpringApplication;

import bdbt_bada_project.SpringApplication.accounts.AccountDAO;
import bdbt_bada_project.SpringApplication.clubs.Club;
import bdbt_bada_project.SpringApplication.clubs.ClubsDAO;
import bdbt_bada_project.SpringApplication.groups.Group;
import bdbt_bada_project.SpringApplication.groups.GroupDAO;
import bdbt_bada_project.SpringApplication.members.Member;
import bdbt_bada_project.SpringApplication.workers.Worker;
import bdbt_bada_project.SpringApplication.workers.coaches.Coach;
import bdbt_bada_project.SpringApplication.workers.coaches.CoachesDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static bdbt_bada_project.SpringApplication.SecurityConfiguration.authenticate;

@Configuration
@Controller
@RequestMapping(value = "/")
public class AppController implements WebMvcConfigurer {
    private ClubsDAO clubsDAO;
    private GroupDAO groupDAO;
    private CoachesDAO coachesDAO;
    private AccountDAO accountDAO;
    private static String email;
    @Lazy
    @Autowired
    AppController(ClubsDAO clubsDAO, GroupDAO groupDAO,
                  CoachesDAO coachesDAO, AccountDAO accountDAO) {
        this.clubsDAO = clubsDAO;
        this.groupDAO = groupDAO;
        this.coachesDAO = coachesDAO;
        this.accountDAO = accountDAO;
    }

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/main").setViewName("main");
        registry.addViewController("/login").setViewName("login");

        registry.addViewController("/main_admin").setViewName("admin/main_admin");
        registry.addViewController("/main_member").setViewName("member/main_member");
        registry.addViewController("/main_worker").setViewName("worker/main_worker");
    }
    @Controller
    public static class DashboardController
    {
        @RequestMapping("/main")
        public String defaultAfterLogin (HttpServletRequest request) {
            email = request.getRemoteUser();
            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = context.getAuthentication();
            String role = authentication.getAuthorities()
                    .toArray()[0]
                    .toString();

            return switch (role) {
                case "ADMIN" -> "redirect:/main_admin";
                case "COACH", "PHYSIOTHERAPIST" -> "redirect:/main_worker";
                case "MEMBER" -> "redirect:/main_member";
                default -> "redirect:/index";
            };
        }
    }

    private void getData(Model model, Authentication authentication) {
        authenticate(model, authentication);
        Club club = clubsDAO.get(1);
        double[] localisation = club.getAddress().getLatitude();
        List<Group> groups = groupDAO.listAll();
        groups.forEach(group -> {
            List<Integer> coachesNumbers = groupDAO.getCoachNumbersByGroupNumber(group.getGroupNumber());
            List<Coach> coaches = new ArrayList<>();
            for(int coachNumber : coachesNumbers){
                coaches.add(coachesDAO.getEmployeeById(coachNumber));
            }
            group.setCoaches(coaches);
        });
        model.addAttribute("club", club);
        model.addAttribute("lat", localisation[0]);
        model.addAttribute("lon", localisation[1]);
        model.addAttribute("groups", groups);
    }

    @RequestMapping(value = {"/", "/index"})
    public String index(Model model, Authentication authentication) {
        getData(model, authentication);
        return "index";
    }

    @RequestMapping(value={"/main_admin"})
    public String showAdminPage(Model model, Authentication authentication) {
        getData(model, authentication);
        return "admin/main_admin";
    }
    @RequestMapping(value={"/main_member"})
    public String showMemberPage(Model model, Authentication authentication) {
        getData(model, authentication);
        Member member = accountDAO.getMember(email);
        model.addAttribute("member", member);
        return "member/main_member";
    }
    @RequestMapping(value={"/main_worker"})
    public String showWorkerPage(Model model, Authentication authentication) {
        getData(model, authentication);
        Worker worker = accountDAO.getWorker(email);
        model.addAttribute("worker", worker);
        return "worker/main_worker";
    }
}