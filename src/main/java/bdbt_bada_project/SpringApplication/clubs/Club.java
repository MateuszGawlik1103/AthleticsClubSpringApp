package bdbt_bada_project.SpringApplication.clubs;

import bdbt_bada_project.SpringApplication.addresses.Address;
import bdbt_bada_project.SpringApplication.addresses.AddressesDAO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class Club {
    private int clubNumber;
    private String name;
    private Date creationDate;
    private int addressNumber;
    private Address address;

    public Club(int clubNumber, String name, Date creationDate, int addressNumber) {
        this.clubNumber = clubNumber;
        this.name = name;
        this.creationDate = creationDate;
        this.addressNumber = addressNumber;
        this.address = new AddressesDAO(new JdbcTemplate()).get(addressNumber);
    }
}
