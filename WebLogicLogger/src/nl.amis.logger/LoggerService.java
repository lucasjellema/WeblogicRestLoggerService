package nl.amis.logger;

import java.util.logging.Logger;

import weblogic.logging.NonCatalogLogger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;


// http://141.144.34.222/WebLogicLoggerService/resources/application.wadl
@Path("logger")
@Consumes("application/json")
public class LoggerService {

    static NonCatalogLogger _logger;
    static String _lastModule;


    public LoggerService() {
    }

    @POST
    @Produces("text/plain")
    @Path("log") //  host:port/WebLogicLoggerService/logger/log
    public String log(LogMessage message) {
        if (_logger == null || !(_lastModule.equalsIgnoreCase(message.getModule()))) {
            // create new logger
            _lastModule = message.getModule();
            _logger = new NonCatalogLogger(_lastModule);
        }
        // TODO check message.getLogLevel();
        _logger.info(message.getMessage());
        return "ok";
    }

    @POST
    @Produces("text/plain")
    @Path("bulklog") //  host:port/WebLogicLoggerService/logger/log
    public String bulklog(LogMessageBundle bundle) {
        // Provide method implementation.
        // TODO
        System.out.println("log bundle of messages for module " + bundle.getModule());
        if (_logger == null || !(_lastModule.equalsIgnoreCase(bundle.getModule()))) {
            // create new logger
            _lastModule = bundle.getModule();
            _logger = new NonCatalogLogger(_lastModule);
        }
        for (LogMessage msg : bundle.getMessages()) {
            switch (msg.getLogLevel()==null?"info":msg.getLogLevel().toLowerCase()) {
            case "error":
                _logger.error(msg.getMessage());
                break;
            case "warn":
                _logger.warning(msg.getMessage());
                break;
            case "info":
                _logger.info(msg.getMessage());
                break;
            case "debug":
                _logger.debug(msg.getMessage());
                break;
            case "finest":
                _logger.debug(msg.getMessage());
                break;
            default:
                _logger.info(msg.getMessage());
            }

        } // loop over all messages in bundle
        return "ok";
    }

}
