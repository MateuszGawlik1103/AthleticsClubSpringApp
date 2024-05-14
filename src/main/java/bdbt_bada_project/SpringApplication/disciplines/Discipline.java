package bdbt_bada_project.SpringApplication.disciplines;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Discipline {
    private int disciplineNumber;
    private String disciplineName;
    private String description;
    private int clubNumber;
    public Discipline(){}

    public Discipline(int disciplineNumber, String disciplineName, String description) {
        this.disciplineNumber = disciplineNumber;
        this.disciplineName = disciplineName;
        this.description = description;
        this.clubNumber = 1;
    }

    @Override
    public String toString() {
        return "Discipline{" +
                "disciplineNumber=" + disciplineNumber +
                ", disciplineName='" + disciplineName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
