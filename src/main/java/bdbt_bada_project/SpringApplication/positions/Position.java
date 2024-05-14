package bdbt_bada_project.SpringApplication.positions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Position {
    private int positionNumber;
    private String name;
    private String description;


    public Position(int positionNumber, String name, String description) {
        super();
        this.positionNumber = positionNumber;
        this.name = name;
        this.description = description;

    }

    public Position() {
        super();
    }


    @Override
    public String toString() {
        return "Position{" +
                "positionNumber=" + positionNumber +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
