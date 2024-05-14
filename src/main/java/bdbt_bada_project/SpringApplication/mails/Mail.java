package bdbt_bada_project.SpringApplication.mails;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Mail {

    private int mailNumber;
    private String postalCode;
    private String name;

    public Mail() {}

    public Mail(int mailNumber, String postalCode, String name) {
        this.mailNumber = mailNumber;
        this.postalCode = postalCode;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Mail{" +
                "mailId=" + mailNumber +
                ", postalCode='" + postalCode + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}