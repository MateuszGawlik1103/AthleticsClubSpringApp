package bdbt_bada_project.SpringApplication.members;

import bdbt_bada_project.SpringApplication.accounts.Account;
import bdbt_bada_project.SpringApplication.accounts.AccountDAO;
import bdbt_bada_project.SpringApplication.addresses.Address;
import bdbt_bada_project.SpringApplication.addresses.AddressesDAO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class Member {
    private int memberNumber;
    private String firstName;
    private String lastName;
    private String pesel;
    private Date dateOfBirth;
    private Date registrationDate;
    private Date withdrawalDate;
    private char gender;
    private Date medicalExaminationValidity;
    private int clubNumber;
    private int addressNumber;
    private Address address;
    private List<Integer> groupsNumbers;
    private int accountNumber;
    private Account account;

    public Member(int memberNumber,
                  String firstName,
                  String lastName,
                  String pesel,
                  Date dateOfBirth,
                  Date registrationDate,
                  Date withdrawalDate,
                  int accountNumber,
                  char gender,
                  Date medicalExaminationValidity,
                  int clubNumber,
                  int addressNumber) {
        super();
        this.memberNumber = memberNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.dateOfBirth = dateOfBirth;
        this.registrationDate = registrationDate;
        this.withdrawalDate = withdrawalDate;
        this.gender = gender;
        this.medicalExaminationValidity = medicalExaminationValidity;
        this.clubNumber = clubNumber;
        this.addressNumber = addressNumber;
        this.address = new AddressesDAO(new JdbcTemplate()).get(addressNumber);
        this.groupsNumbers = new ArrayList<>();
        this.accountNumber = accountNumber;
        this.account = new AccountDAO(new JdbcTemplate()).get(accountNumber);
    }

    @Override
    public String toString() {
        return "Worker{" +
                "memberNumber=" + memberNumber +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", pesel='" + pesel + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", registrationDate=" + registrationDate +
                ", withdrawalDate=" + withdrawalDate +
                ", gender=" + gender +
                ", phoneNumber='" + medicalExaminationValidity + '\'' +
                ", clubNumber=" + clubNumber +
                ", addressNumber=" + addressNumber +
                '}';
    }

}