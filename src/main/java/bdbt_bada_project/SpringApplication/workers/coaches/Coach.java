package bdbt_bada_project.SpringApplication.workers.coaches;

import bdbt_bada_project.SpringApplication.workers.Worker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Coach extends Worker {
    private String qualifications;
    private String coachingExperience;

    @Override
    public String toString() {
        return "Coach{" +
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
                ", qualifications='" + qualifications + '\'' +
                ", coachingExperience='" + coachingExperience + '\'' +
                '}';
    }
}
