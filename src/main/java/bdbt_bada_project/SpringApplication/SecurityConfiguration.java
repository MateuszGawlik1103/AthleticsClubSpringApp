package bdbt_bada_project.SpringApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final DataSource dataSource;
    @Autowired
    public SecurityConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT EMAIL, HASLO, 1 AS ENABLED FROM KONTA WHERE EMAIL = ?")
                .authoritiesByUsernameQuery("SELECT EMAIL, ROLA FROM KONTA WHERE EMAIL = ?")
                .passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/index").permitAll()
                .antMatchers("/resources/static/**").permitAll()
                .antMatchers("/main").authenticated()
                .antMatchers("/main_admin").hasAuthority("ADMIN")
                .antMatchers("/main_member").hasAuthority("MEMBER")
                .antMatchers("/main_coach").hasAuthority("COACH")
                .antMatchers("/main_physiotherapist").hasAuthority("PHYSIOTHERAPIST")
                .antMatchers("/members").hasAnyAuthority("ADMIN", "PHYSIOTHERAPIST")
                .antMatchers("/workers").hasAnyAuthority("ADMIN")
                .antMatchers("/competitions").permitAll()
                .antMatchers("/groups").permitAll()
                .antMatchers("/physiotherapists").hasAnyAuthority("ADMIN", "PHYSIOTHERAPIST", "COACH", "MEMBER")
                .antMatchers("/coaches").hasAnyAuthority("ADMIN", "PHYSIOTHERAPIST", "COACH", "MEMBER")
                .antMatchers("/coaches_details").hasAnyAuthority("ADMIN", "PHYSIOTHERAPIST", "COACH", "MEMBER")
                .antMatchers("/competition_details").permitAll()
                .antMatchers("/edit_coach").hasAuthority("ADMIN")
                .antMatchers("/edit_competitions").hasAnyAuthority("ADMIN", "COACH")
                .antMatchers("/edit_group").hasAnyAuthority("ADMIN", "COACH")
                .antMatchers("/edit_member").hasAnyAuthority("ADMIN", "COACH")
                .antMatchers("/edit_physio").hasAnyAuthority("ADMIN")
                .antMatchers("/edit_schedule").hasAnyAuthority("ADMIN", "COACH")
                .antMatchers("/group_details").hasAnyAuthority("ADMIN", "PHYSIOTHERAPIST", "COACH", "MEMBER")
                .antMatchers("/member_details").hasAnyAuthority("ADMIN", "PHYSIOTHERAPIST", "COACH")
                .antMatchers("/new_coach").hasAnyAuthority("ADMIN")
                .antMatchers("/new_competition").hasAnyAuthority("ADMIN","COACH")
                .antMatchers("/new_group").hasAnyAuthority("ADMIN")
                .antMatchers("/new_member").hasAnyAuthority("ADMIN")
                .antMatchers("/new_physio").hasAnyAuthority("ADMIN")
                .antMatchers("/new_schedule").hasAnyAuthority("ADMIN", "COACH")
                .antMatchers("/physio_details").hasAnyAuthority("ADMIN", "PHYSIOTHERAPIST", "COACH", "MEMBER")

                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/main")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/index")
                .logoutSuccessUrl("/index")
                .permitAll();
    }

    public static void authenticate(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            List<String> roles = authorities
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            model.addAttribute("userRoles", roles);
        } else {
            List<String> auth = new ArrayList<>();
            model.addAttribute("userRoles", auth);
        }
    }

}
