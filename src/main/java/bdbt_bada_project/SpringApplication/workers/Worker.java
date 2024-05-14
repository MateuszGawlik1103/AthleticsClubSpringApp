package bdbt_bada_project.SpringApplication.workers;

import bdbt_bada_project.SpringApplication.accounts.Account;
import bdbt_bada_project.SpringApplication.accounts.AccountDAO;
import bdbt_bada_project.SpringApplication.addresses.Address;
import bdbt_bada_project.SpringApplication.addresses.AddressesDAO;
import bdbt_bada_project.SpringApplication.mails.MailsDAO;
import bdbt_bada_project.SpringApplication.positions.PositionsDAO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;

@Setter
@Getter
public class Worker {
    private int employeeNumber;
    private String firstName;
    private String lastName;
    private String pesel;
    private Date dateOfBirth;
    private Date employmentDate;
    private Date dismissalDate;
    private String phoneNumber;
    private char gender;
    private int clubNumber;
    private int addressNumber;
    private Address address;
    private int positionNumber;
    private String position;
    private int accountNumber;
    private Account account;


    public Worker(int employeeNumber,
                  String firstName,
                  String lastName,
                  String pesel,
                  Date dateOfBirth,
                  Date employmentDate,
                  Date dismissalDate,
                  String phoneNumber,
                  char gender,
                  int clubNumber,
                  int addressNumber,
                  int positionNumber,
                  int accountNumber
                  ) {
        super();
        this.employeeNumber = employeeNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.dateOfBirth = dateOfBirth;
        this.employmentDate = employmentDate;
        this.dismissalDate = dismissalDate;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.clubNumber = clubNumber;
        this.addressNumber = addressNumber;
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        this.address = new AddressesDAO(jdbcTemplate).get(addressNumber);
        this.positionNumber = positionNumber;
        this.position = new PositionsDAO(new JdbcTemplate()).getNameByPositionNumber(positionNumber);
        this.accountNumber = accountNumber;
        this.account = new AccountDAO(new JdbcTemplate()).get(accountNumber);
    }
    public Worker(){ super(); }
    @Override
    public String toString() {
        return "Worker{" +
                "employeeNumber=" + employeeNumber +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", pesel='" + pesel + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", employmentDate=" + employmentDate +
                ", dismissalDate=" + dismissalDate +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender=" + gender +
                ", clubNumber=" + clubNumber +
                ", addressNumber=" + addressNumber +
                ", address=" + address +
                ", positionNumber=" + positionNumber +
                '}';
    }
}
