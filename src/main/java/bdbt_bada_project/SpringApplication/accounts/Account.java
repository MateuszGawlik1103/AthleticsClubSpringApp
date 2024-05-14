package bdbt_bada_project.SpringApplication.accounts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private int accountNumber;
    private String email;
    private String password;
    private String role;
}
