package newegg.ec.disnotice.web;


import java.io.IOException;
import java.net.URL;

import newegg.ec.warden.PropertyLoader;
import newegg.ec.warden.WardenLogging;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.resource.Resource;
import org.slf4j.Logger;

public class DistNoticeWebServerMain {
    private static Logger log = WardenLogging.getLog(DistNoticeWebServerMain.class);

    public static void main(String[] args) {
        try {
            PropertyLoader propertyLoader = new PropertyLoader("disnotice-web.properties");
            final String appDir = new DistNoticeWebServerMain().getWebAppsPath();
            Server server = new Server(Integer.parseInt(propertyLoader.getValue("disnotice.web.port", "8080")));
            WebAppContext webapp = new WebAppContext();
            webapp.setContextPath("/");
            webapp.setBaseResource(Resource.newResource(appDir));
            server.addHandler(webapp);
            server.start();
            server.join();
        } catch (Throwable t) {
            log.error("start server error ... shoud exit", t);
            System.exit(1);
        }
    }

    protected String getWebAppsPath() throws IOException {
        URL url = getClass().getClassLoader().getResource("webapps");
        if (url == null)
            throw new IOException("webapps not found in CLASSPATH");
        return url.toString();
    }

}
