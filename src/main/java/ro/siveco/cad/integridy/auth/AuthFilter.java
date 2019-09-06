package ro.siveco.cad.integridy.auth;

import java.util.Set;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;


/**
 * Apache shiro filter that checks if user has a role.
 * @author SIVECO
 */
public class AuthFilter extends AuthorizationFilter{

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = getSubject(request, response);
        String[] rolesArray = (String[]) mappedValue;
        Set<String> roles = org.apache.shiro.util.CollectionUtils.asSet(rolesArray);
        if(rolesArray == null || rolesArray.length==0){
            return false;
        }
        for(String role : roles){
            if(subject.hasRole(role)){
                return true;
            }
        }
        return false;
    }
    
    
}
