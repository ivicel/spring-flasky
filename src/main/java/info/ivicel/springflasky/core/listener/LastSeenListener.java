package info.ivicel.springflasky.core.listener;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.Assert;

public class LastSeenListener {
    @PostPersist
    @PostUpdate
    public void syncAuthentication(Object target) {
        Assert.isTrue(UserDetails.class.isAssignableFrom(target.getClass()),
                String.format("%s is not an instance of UserDetails", target.getClass().getName()));

        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication authentication = ctx.getAuthentication();

        // not current login user
        if (!authentication.getPrincipal().equals(target)) {
            return;
        }

        UserDetails userDetails = (UserDetails) target;
        PreAuthenticatedAuthenticationToken auth = new PreAuthenticatedAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        ctx.setAuthentication(auth);
    }
}
