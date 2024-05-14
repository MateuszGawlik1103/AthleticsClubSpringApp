package bdbt_bada_project.SpringApplication.competitions;

import bdbt_bada_project.SpringApplication.addresses.Address;
import bdbt_bada_project.SpringApplication.addresses.AddressesDAO;
import bdbt_bada_project.SpringApplication.members.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;
import java.util.List;

@Setter
@Getter
public class Competition {
    private int competitionNumber;
    private Date date;
    private String description;
    private int addressNumber;
    private Address address;
    private List<Member> competitors;

    public Competition(int competitionNumber,
                  Date date,
                  String description,
                  int addressNumber) {
        this.competitionNumber = competitionNumber;
        this.date = date;
        this.description = description;
        this.addressNumber = addressNumber;
        this.address = new AddressesDAO(new JdbcTemplate()).get(addressNumber);
    }

    public Competition() {}

    @Override
    public String toString() {
        return "Competition{" +
                "competitionNumber=" + competitionNumber +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", addressNumber=" + addressNumber +
                '}';
    }
}
