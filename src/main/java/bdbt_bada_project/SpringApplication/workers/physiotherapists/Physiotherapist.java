package bdbt_bada_project.SpringApplication.workers.physiotherapists;

import bdbt_bada_project.SpringApplication.workers.Worker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Physiotherapist extends Worker {
    private String specialization;
    private String qualifications;

    @Override
    public String toString() {
        return "Physiotherapist{" +
                "employeeNumber=" + getEmployeeNumber() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", pesel='" + getPesel() + '\'' +
                ", dateOfBirth=" + getDateOfBirth() +
                ", employmentDate=" + getEmploymentDate() +
                ", dismissalDate=" + getDismissalDate() +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", gender=" + getGender() +
                ", clubNumber=" + getClubNumber() +
                ", addressNumber=" + getAddressNumber() +
                ", positionNumber=" + getPositionNumber() +
                ", specialization='" + specialization + '\'' +
                ", qualifications='" + qualifications + '\'' +
                '}';
    }
}