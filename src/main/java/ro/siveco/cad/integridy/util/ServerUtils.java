package ro.siveco.cad.integridy.util;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class ServerUtils {

    /**
     * Get application path.
     * @return app path
     */
    public static String getAppPath(){
        return "http://" + getApplicationHost() + ":" + getApplicationPort() + getApplicationName();
    }

    /**
     * Get application context name.
     * @return app context name
     */
    public static String getApplicationName(){
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getContextPath();
    }

    /**
     * Get application host.
     * @return app host
     */
    public static String getApplicationHost(){
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getServerName();
    }

    /**
     * Get application server port.
     * @return server port
     */
    public static int getApplicationPort(){
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getServerPort();
    }

}
