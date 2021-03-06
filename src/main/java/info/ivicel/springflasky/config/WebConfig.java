package info.ivicel.springflasky.config;

import io.github.biezhi.ome.OhMyEmail;
import java.util.Properties;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebConfig {
    private MailProperties mailProperties;

    @Autowired
    public WebConfig(MailProperties mailProperties) {
        this.mailProperties = mailProperties;
    }

    @PostConstruct
    public void initEmail() {
        Properties props = new Properties();
        props.setProperty("username", mailProperties.getUsername());
        props.setProperty("password", mailProperties.getPassword());
        props.setProperty("mail.smtp.host", mailProperties.getHost());
        props.setProperty("mail.smtp.port", String.valueOf(mailProperties.getPort()));
        props.putAll(mailProperties.getProperties());

        OhMyEmail.config(props);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
