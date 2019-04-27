package info.ivicel.springflasky.util;

import com.mitchellbosecke.pebble.PebbleEngine;
import info.ivicel.springflasky.core.common.Const;
import info.ivicel.springflasky.web.model.domain.Post;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

@Slf4j
public class CommonUtil {

    private static final PolicyFactory POLICY_FACTORY = new HtmlPolicyBuilder()
            .allowElements(Const.ALLOWED_HTML_TAG).toFactory();

    /**
     * sanitize html tag
     * @param text
     * @return
     */
    public static String sanitize(final String text) {
        return POLICY_FACTORY.sanitize(text);
    }

    /**
     * generate avatar hash
     * @param email
     * @return
     */
    public static String hashAvatar(String email) {
        return Base64.getUrlEncoder().encodeToString(
                (UUID.randomUUID().toString() + email).getBytes(StandardCharsets.UTF_8));
    }

    public static String renderEmailTemplate(String template) {
        return renderEmailTemplate(template, new HashMap<>());
    }

    public static String renderEmailTemplate(String template, Map<String, Object> context) {
        StringWriter writer = new StringWriter();
        try {
            new PebbleEngine.Builder().build().getTemplate(template).evaluate(writer, context);
        } catch (IOException e) {
            log.debug("Can't render template \"{}\": {}", template, e.getMessage());
            return null;
        }
        return writer.toString();
    }
}
