package wk2.activity2.jdbc.models;

import java.util.Map;

/*
    This is the POJO that Jackson will attempt to map a YAML configuration file to. Each grouping of parameters in
    the YAML config file correspond to a Map<String,String> within this model. If the YAML config file were expanded to
    include additional parameter groupings, then this model would need to be expanded to a Map<String,String> for that
    grouping. Remember that Jackson can only perform a mapping if their exists a Map<String,String> of the same name as
    the name for each of the parameter groupings in the config file. Jackson also needs getters and setters for each of
    the Maps. For HW1, you will need to expand on this class to include extracting configurations for the database
    connection parameters.
 */
public class ConfigsModel {
    private Map<String,String> serviceConfig;
    private Map<String,String> loggerConfig;

    public ConfigsModel() { }

    public Map<String, String> getServiceConfig() {
        return serviceConfig;
    }

    public void setServiceConfig(Map<String, String> serviceConfig) {
        this.serviceConfig = serviceConfig;
    }

    public Map<String, String> getLoggerConfig() {
        return loggerConfig;
    }

    public void setLoggerConfig(Map<String, String> loggerConfig) {
        this.loggerConfig = loggerConfig;
    }
}
