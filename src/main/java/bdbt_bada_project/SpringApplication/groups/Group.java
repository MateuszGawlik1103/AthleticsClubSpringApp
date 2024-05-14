package bdbt_bada_project.SpringApplication.groups;

import bdbt_bada_project.SpringApplication.disciplines.Discipline;
import bdbt_bada_project.SpringApplication.disciplines.DisciplineDAO;
import bdbt_bada_project.SpringApplication.workers.coaches.Coach;
import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Getter
@Setter
public class Group {
    private int groupNumber;
    private int pricePerMonth;
    private int maxNumberOfMembers;
    private String ageCategory;
    private int disciplineNumber;
    private Discipline discipline;
    private int clubNumber;
    private List<Integer> coachNumbers;
    private List<Coach> coaches;


    public Group(int groupNumber, int pricePerMonth, int maxNumberOfMembers, String ageCategory, int disciplineNumber, List<Integer> coachNumbers, List<Coach> coaches) {
        this.groupNumber = groupNumber;
        this.pricePerMonth = pricePerMonth;
        this.maxNumberOfMembers = maxNumberOfMembers;
        this.ageCategory = ageCategory;
        this.disciplineNumber = disciplineNumber;
        this.discipline = new DisciplineDAO(new JdbcTemplate()).getDisciplineById(disciplineNumber);
        this.clubNumber = 1;
        this.coachNumbers = coachNumbers;
        this.coaches = coaches;

    }
    public Group(){}


    @Override
    public String toString() {
        return "Group{" +
                "groupNumber=" + groupNumber +
                ", pricePerMonth=" + pricePerMonth +
                ", maxNumberOfMembers=" + maxNumberOfMembers +
                ", ageCategory='" + ageCategory + '\'' +
                ", disciplineNumber=" + disciplineNumber +
                ", clubNumber=" + clubNumber +
                ", coachNumbers=" + coachNumbers +
                '}';
    }
}
