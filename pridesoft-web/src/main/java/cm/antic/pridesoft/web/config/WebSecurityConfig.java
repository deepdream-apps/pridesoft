package cm.antic.pridesoft.web.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import cm.antic.pridesoft.web.util.AuthentificationService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
    private AuthentificationService authentificationService ;

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(authentificationService) ;
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder() ;
    }
    
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
      
    }
    
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
            .csrf().disable()
          	.authorizeRequests()
          		.antMatchers("/**").permitAll()
          		.antMatchers("/css/**").permitAll()
          		.antMatchers("/fonts/**").permitAll()
          		.antMatchers("/images/**").permitAll()
          		.antMatchers("/js/**").permitAll()
          		.antMatchers("/pages/**").permitAll()
          		.antMatchers("/plugins/**").permitAll()
          		.antMatchers("/scss/**").permitAll()
          		.antMatchers("/page-login**").permitAll()
          		.antMatchers("/").permitAll()
          		.anyRequest().authenticated()
          	.and()
          		.sessionManagement()
          		.invalidSessionUrl("/page-login")
          	.and()
          		.formLogin()
          			.loginPage("/page-login")
          			.loginProcessingUrl("/perform_login")
          			.defaultSuccessUrl("/dashboard")
          			.failureUrl("/page-login") 
          	.and()
          		.logout()
          			.logoutUrl("/perform_logout")
          			.logoutSuccessUrl("/page-logoff")
          			.invalidateHttpSession(true)
          			//.deleteCookies("JSESSIONID") 
          			;

    }
    
    
    
    public static void main(String...strings) {
    	System.out.println(new BCryptPasswordEncoder().encode("admin"));
    }
    

}
