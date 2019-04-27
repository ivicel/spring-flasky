package info.ivicel.springflasky;

import info.ivicel.springflasky.web.repository.base.BaseRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties({MailProperties.class})
@EnableJpaRepositories(basePackages = "info.ivicel.springflasky.web.repository",
        repositoryBaseClass = BaseRepositoryImpl.class)
public class SpringFlaskyApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(SpringFlaskyApplication.class, args);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
