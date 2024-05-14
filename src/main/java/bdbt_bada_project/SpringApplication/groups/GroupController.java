package bdbt_bada_project.SpringApplication.groups;

import bdbt_bada_project.SpringApplication.disciplines.Discipline;
import bdbt_bada_project.SpringApplication.disciplines.DisciplineDAO;
import bdbt_bada_project.SpringApplication.members.Member;
import bdbt_bada_project.SpringApplication.members.MembersDAO;
import bdbt_bada_project.SpringApplication.schedules.Schedule;
import bdbt_bada_project.SpringApplication.schedules.SchedulesDAO;
import bdbt_bada_project.SpringApplication.workers.coaches.Coach;
import bdbt_bada_project.SpringApplication.workers.coaches.CoachesDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

import static bdbt_bada_project.SpringApplication.SecurityConfiguration.authenticate;

@Controller
@RequestMapping(path = "/groups")
public class GroupController {

    private final GroupDAO dao;
    private final DisciplineDAO disciplineDAO;
    private final CoachesDAO coachesDAO;
    private final SchedulesDAO schedulesDAO;

    private final MembersDAO membersDAO;

    @Autowired
    public GroupController(GroupDAO groupDAO, DisciplineDAO disciplineDAO, CoachesDAO coachesDAO, SchedulesDAO schedulesDAO, MembersDAO membersDAO) {
        this.dao = groupDAO;
        this.disciplineDAO = disciplineDAO;
        this.coachesDAO = coachesDAO;
        this.schedulesDAO = schedulesDAO;
        this.membersDAO = membersDAO;
    }

    @GetMapping
    public String getGroups(Model model, Authentication authentication) {
        List<Group> groups = dao.listAll();
        groups.forEach(group -> {
            List<Coach> coaches = new ArrayList<>();
            for(int coachNumber : group.getCoachNumbers()){
                coaches.add(coachesDAO.getEmployeeById(coachNumber));
            group.setCoaches(coaches);
            }
        });
        authenticate(model, authentication);
        model.addAttribute("groups", groups);
        return "tables/groups";
    }

    @RequestMapping("/new")
    public String showNewForm(Model model, Authentication authentication) {
        authenticate(model, authentication);
        Group group = new Group();
        List<Discipline> disciplines = disciplineDAO.list();
        List<Coach> coaches = coachesDAO.list();
        model.addAttribute("group", group);
        model.addAttribute("listOfDisciplines", disciplines);
        model.addAttribute("coaches", coaches);
        return "new/new_group";
    }

    @PostMapping(value = "/save")
    public String save(@ModelAttribute("group") Group group) {
        dao.save(group);
        return "redirect:/groups";
    }

    @RequestMapping("/edit/{groupNumber}")
    public ModelAndView showEditForm(@PathVariable(name = "groupNumber") int groupNumber, Model model, Authentication authentication) {
        authenticate(model, authentication);
        ModelAndView mav = new ModelAndView("edit/edit_group");
        Group group = dao.getGroupById(groupNumber);
        List<Discipline> disciplines = disciplineDAO.list();
        List<Coach> coaches = coachesDAO.list();
        List<Integer> selectedCoachesNumbers = group.getCoachNumbers();
        mav.addObject("selectedCoachesNumbers", selectedCoachesNumbers);
        mav.addObject("group", group);
        mav.addObject("listOfDisciplines", disciplines);
        mav.addObject("coaches", coaches);
        return mav;
    }

    @PostMapping(value = "/update")
    public String update(@ModelAttribute("group") Group group) {
        dao.update(group);
        return "redirect:/groups";
    }

    @RequestMapping(value = "/delete/{groupNumber}")
    public String delete(@PathVariable(name = "groupNumber") int groupNumber) {
        dao.delete(groupNumber);
        return "redirect:/groups";
    }
    //For schedules
    @RequestMapping(value = "/{groupNumber}")
    public String getGroup(@PathVariable(name = "groupNumber") int groupNumber, Model model, Authentication authentication) {
        authenticate(model, authentication);
        Group group = dao.getGroupById(groupNumber);
        List<Coach> coaches = new ArrayList<>();
        for( int coachNumber : group.getCoachNumbers()){
            coaches.add(coachesDAO.getEmployeeById(coachNumber));
        }
        group.setCoaches(coaches);
        group.setDiscipline(disciplineDAO.getDisciplineById(group.getDisciplineNumber()));
        List<Schedule> schedules = schedulesDAO.getSchedulesByGroupNumber(groupNumber);

        schedules.forEach(schedule -> schedule.setDayNumber(schedule.getDayNumberByName(schedule.getDay())));
        schedules.sort(Comparator.comparing(Schedule::getDayNumber));

        List<Member> members = membersDAO.getMembersFromGroup(groupNumber);
        model.addAttribute("coaches", coaches);
        model.addAttribute("group", group);
        model.addAttribute("schedules", schedules);
        model.addAttribute("members",members);
        return "details/group_details";
    }
    @PostMapping(value = "/{groupNumber}/save")
    public String save(@ModelAttribute("schedule") Schedule schedule,
                       @PathVariable(name = "groupNumber") int groupNumber) {
        schedulesDAO.save(schedule, groupNumber);
        return "redirect:/groups/" + groupNumber;
    }
    @RequestMapping("/{groupNumber}/new")
    public String showFormForNewSchedule(@PathVariable(name = "groupNumber") int groupNumber, Model model, Authentication authentication) {
        authenticate(model, authentication);
        Schedule schedule = new Schedule();
        model.addAttribute("schedule", schedule);
        model.addAttribute("groupNumber", groupNumber);
        return "new/new_schedule";
    }
    @RequestMapping("/{groupNumber}/edit/{scheduleNumber}")
    public ModelAndView showEditFormForSchedule(@PathVariable(name = "scheduleNumber") int scheduleNumber,
                                                @PathVariable(name = "groupNumber") int groupNumber,
                                                Model model, Authentication authentication) {
        authenticate(model, authentication);
        ModelAndView mav = new ModelAndView("edit/edit_schedule");
        Schedule schedule = schedulesDAO.getScheduleById(scheduleNumber);
        mav.addObject("groupNumber", groupNumber);
        mav.addObject("schedule", schedule);
        return mav;
    }
    @PostMapping(value = "/{groupNumber}/update")
    public String update(@ModelAttribute("schedule") Schedule schedule,
                         @ModelAttribute("groupNumber") int groupNumber) {
        schedulesDAO.update(schedule, groupNumber);
        return "redirect:/groups/" + groupNumber;
    }
    @RequestMapping(value = "/{groupNumber}/delete/{scheduleNumber}")
    public String delete(@PathVariable(name = "scheduleNumber") int scheduleNumber,
                         @PathVariable(name = "groupNumber") int groupNumber){
        schedulesDAO.delete(scheduleNumber, groupNumber);
        return "redirect:/groups/" + groupNumber;
    }
}
