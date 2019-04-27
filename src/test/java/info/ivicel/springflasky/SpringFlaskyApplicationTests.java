package info.ivicel.springflasky;

import info.ivicel.springflasky.web.repository.PostRepository;
import info.ivicel.springflasky.web.repository.UserRepository;
import info.ivicel.springflasky.web.service.UserService;
import io.github.biezhi.ome.SendMailException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "application.yml", webEnvironment = WebEnvironment.RANDOM_PORT,
        classes = SpringFlaskyApplication.class)
public class SpringFlaskyApplicationTests {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Value("${flasky.securityKey}")
    private String key;

    @Autowired
    private MailProperties mailProperties;

    @Test
    public void contextLoads() throws SendMailException {
    }
}
