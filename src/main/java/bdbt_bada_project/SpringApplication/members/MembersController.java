package bdbt_bada_project.SpringApplication.members;

import bdbt_bada_project.SpringApplication.accounts.AccountDAO;
import bdbt_bada_project.SpringApplication.groups.Group;
import bdbt_bada_project.SpringApplication.groups.GroupDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static bdbt_bada_project.SpringApplication.SecurityConfiguration.authenticate;

@Controller
@RequestMapping(path = "/members")
public class MembersController {
    private final MembersDAO dao;
    private final GroupDAO groupDAO;
    private final MembersDAO membersDAO;
    private final AccountDAO accountDAO;

    @Autowired
    public MembersController(MembersDAO membersDAO, GroupDAO groupDAO, MembersDAO membersDAO1, AccountDAO accountDAO) {
        this.dao = membersDAO;
        this.groupDAO = groupDAO;
        this.membersDAO = membersDAO1;
        this.accountDAO = accountDAO;
    }

    @GetMapping
    public String getMembers(Model model, Authentication authentication) {
        List<Member> members = dao.list();
        model.addAttribute("members", members);
        authenticate(model, authentication);
        return "tables/members";
    }

    @RequestMapping("/new")
    public String showNewForm(Model model, Authentication authentication) {
        authenticate(model, authentication);
        Member member = new Member();
        List<Group> groups = groupDAO.list();
        model.addAttribute("member", member);
        model.addAttribute("groups", groups);
        return "new/new_member";
    }

    @PostMapping(value = "/save")
    public String save(@ModelAttribute("member") Member member,
                       Model model) {
        if (accountDAO.accountExists(member.getAccount())) {
            List<Group> groups = groupDAO.list();
            model.addAttribute("emailExistsError", true);
            model.addAttribute("member", member);
            model.addAttribute("groups", groups);

            return "new/new_member";
        }
        dao.save(member);
        return "redirect:/members";
    }

    @RequestMapping("/edit/{memberNumber}")
    public ModelAndView showEditForm(@PathVariable(name = "memberNumber") int memberNumber, Authentication authentication) {
        ModelAndView mav = new ModelAndView("edit/edit_member");
        Member member = dao.get(memberNumber);
        List<Integer> membersGroups = membersDAO.getGroups(memberNumber)
                .stream()
                .map(Group::getGroupNumber)
                .collect(Collectors.toList());
        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            List<String> roles = authorities
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            mav.addObject("userRoles", roles);
        } else{
            List<String> auth = new ArrayList<>();
            mav.addObject("userRoles", auth);
        }

        List<Group> groups = groupDAO.list();
        mav.addObject("membersGroups", membersGroups);
        mav.addObject("member", member);
        mav.addObject("groups", groups);
        return mav;
    }

    @PostMapping(value = "/update")
    public String update(@ModelAttribute("member") Member member, Model model) {
        dao.update(member);
        return "redirect:/groups";
    }

    @RequestMapping(value = "/withdraw/{memberNumber}")
    public String withdraw(@PathVariable(name = "memberNumber") int memberNumber) {
        dao.withdraw(memberNumber);
        return "redirect:/groups";
    }

    @RequestMapping(value = "/{memberNumber}")
    public String getMember(@PathVariable(name = "memberNumber") int memberNumber, Model model, Authentication authentication){
        authenticate(model, authentication);
        Member member = dao.get(memberNumber);
        List<Group> groups = dao.getGroups(memberNumber);
        model.addAttribute("member", member);
        model.addAttribute("groups", groups);
        return "details/member_details";
    }

}
