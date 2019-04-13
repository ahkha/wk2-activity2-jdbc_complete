package wk2.activity2.jdbc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import wk2.activity2.jdbc.configs.Configs;
import wk2.activity2.jdbc.logger.ServiceLogger;
import wk2.activity2.jdbc.models.ConfigsModel;

import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BasicService {
    public static BasicService basicService;

    private static Configs configs = new Configs();
    private static Connection con;

    public static void main(String[] args) {
        basicService = new BasicService();
        basicService.initService(args);
    }

    private void initService(String[] args) {
        // Validate arguments
        basicService.validateArguments(args);
        // Exec the arguments
        basicService.execArguments(args);
        // Initialize logging
        initLogging();
        ServiceLogger.LOGGER.config("Starting service...");
        configs.currentConfigs();
        // Connect to database
        connectToDatabase();
        // Initialize HTTP server
        initHTTPServer();
        ServiceLogger.LOGGER.config("Service initialized.");
    }

    /*
        This method validates the arguments passed to the program at runtime. There are only two options:

            1.) --default, -d
                   This option is meant to force the program to default, hard-coded values for the parameters required
                   to initialize the HTTP server and the logging. You should only be using the option to force the
                   program into a pre-determined (through hard-coded values) state in which the program will always run.
                   We will not be using this option when grading your microservices.
           2.) --config, -c
                   This option is meant to pass the path of a configuration file to your program so that your program
                   will initialize itself using the parameters. Parameters will need to be changed depending on where
                   you are running your program (local or Openlab) and which databases you're trying to connect your
                   program to (local or Openlab). The config file is what gives your microservice modularity--it can run
                   on any machine in the world that has a JVM and a config file with appropriate settings without the
                   need to recompile your code.
     */
    private void validateArguments(String[] args) {
        boolean isConfigOptionSet = false;
        for (int i = 0; i < args.length; ++i) {
            switch (args[i]) {
                case "--default":
                case "-d":
                    if (i + 1 < args.length) {
                        exitAppFailureArgs("Invalid arg after " + args[i] + " option: " + args[i + 1]);
                    }
                case "--config":
                case "-c":
                    if (!isConfigOptionSet) {
                        isConfigOptionSet = true;
                        ++i;
                    } else {
                        exitAppFailureArgs("Conflicting configuration file arguments.");
                    }
                    break;

                default:
                    exitAppFailureArgs("Unrecognized argument: " + args[i]);
            }
        }
    }

    /*
        This method executes the arguments once they have been validated.
     */
    private void execArguments(String[] args) {
        if (args.length > 0) {
            for (int i = 0; i < args.length; ++i) {
                switch (args[i]) {
                    case "--config":
                    case "-c":
                        // Config file specified. Load it.
                        getConfigFile(args[i + 1]);
                        ++i;
                        break;
                    case "--default":
                    case "-d":
                        System.err.println("Default config options selected.");
                        configs = new Configs();
                        break;
                    default:
                        exitAppFailure("Unrecognized argument: " + args[i]);
                }
            }
        } else {
            System.err.println("No config file specified. Using default values.");
            configs = new Configs();
        }
    }

    /*
        This method attempts to create a Configs class from a file passed as an argument.
     */
    private void getConfigFile(String configFile) {
        try {
            System.err.println("Config file name: " + configFile);
            configs = new Configs(loadConfigs(configFile));
            System.err.println("Configuration file successfully loaded.");
        } catch (NullPointerException e) {
            System.err.println("Config file not found. Using default values.");
            configs = new Configs();
        }
    }

    /*
        This method attempts to create a ConfigsModel object from the yaml config file using Jackson.
     */
    private ConfigsModel loadConfigs(String file) {
        System.err.println("Loading configuration file...");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory()); // Set the mapper to handle YAML files
        ConfigsModel configs = null;

        try {
            configs = mapper.readValue(new File(file), ConfigsModel.class); // Map the file to the model
        } catch (IOException e) {
            exitAppFailure("Unable to load configuration file.");
        }
        return configs;
    }

    /*
        Initialize the logging. There's nothing particularly interesting about this.
     */
    private void initLogging() {
        try {
            ServiceLogger.initLogger(configs.getOutputDir(), configs.getOutputFile());
        } catch (IOException e) {
            exitAppFailure("Unable to initialize logging.");
        }
    }

    /*
        This method attempts to create a connection to a database using hard-coded values. In practice, you should never
        do this. For the simplicity of the example, we are hard-coding values to initialize the database connection. In
        your homework, you must expand on the provided config.yaml to include the settings needed to connect to a DB.
     */
    private void connectToDatabase() {
        ServiceLogger.LOGGER.config("Connecting to database...");
        // Set the host that the database is running on (localhost, matt-smith-v4.ics.uci.edu, etc...)
        String hostName = "localhost";
        // Set the port that your program's MySQL traffic will use. Default is 3306. This example code is using 3307
        // because it assumes you will be using an SSH tunnel to connect to the database, and you will be running your
        // program from your laptop, and not on Openlab. Check piazza and the homework documentation for details.
        int port = 3307;
        // Set the user account your program will use to connect to the desired database.
        String username = "cs122b-inst";
        // Set the corresponding password for the above user account
        String password = "cs122btest";
        // Set the name of the database to connect to
        String dbName = "cs122b_example";
        // Set the query string containing configurations for THIS connection to the database. The below string is 100%
        // ABSOLUTELY NECESSARY to connect to any MySQL server on Openlab. This is not strictly needed for your MySQL
        // server on your local machine.
        String settings = "?autoReconnect=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=PST";
        // This is the database driver our program will be using to connect to a database. Your program could connect to
        // any number of types of databases, including Oracle, DB2, or Sybase. We're using MySQL, and so must use the
        // MySQL driver.
        String driver = "com.mysql.cj.jdbc.Driver";

        try {
            // The JDBC driver must be registered in memory before your program can use it. Registering the driver is
            // the process by which the driver's class file is loaded into memory, so it can be utilized as an
            // implementation of the JDBC interfaces. The registration must happen only once in your program. The most
            // common way to register a driver is to use Java's Class.forName() method to dynamically load the driver's
            // class file into memory, which automatically registers it. This method is the most preferred way because
            // it allows you to make the driver registration configurable and portable.
            Class.forName(driver);

            // A url for the database you wish to connect to must be supplied to the Connection class in its constructor.
            // The URL format is dependent on the driver being used in your program. We are using the MySQL driver, so
            // the url must be of the form "jdbc:mysql://hostname/databaseName. The URL must be constructed from the
            // parameters supplied in our YAML configuration file.
            String url = "jdbc:mysql://" + hostName + ":" + port + "/" + dbName + settings;
            ServiceLogger.LOGGER.config("Database URL: " + url);

            // Once the driver has been loaded into memory, and the URL has been constructed, we can now establish a
            // connection using the DriverManager.getConnection() method. In this example, we are creating only a single
            // connection to our database. This means that every request that comes to our web application must share
            // only a single connection for executing queries. This is obviously a huge bottleneck in the design. For
            // right now, this will suffice for our programs. But later in the course, we will be introducing you to
            // "connection pooling", which is a means of creating and distributing access to many database connections
            // among the functions of our web application. It also worth mentioning that under NORMAL operating conditions,
            // the best practice is to explicitly close all connections to the database to end a database session. Java's
            // garbage collection will handle this automatically, but it is considered VERY poor practice. For right now,
            // we are allowing Java to handle closing stale connections.
            con = DriverManager.getConnection(url, username, password);
            ServiceLogger.LOGGER.config("Connected to database: " + con.toString());
        } catch (Exception e) {
            // Listing the exception types individually allows you to log exactly what exception was thrown, and call
            // different methods to handle the exception if so desired.
            e.printStackTrace();
            if (e instanceof ClassCastException) {
                ServiceLogger.LOGGER.warning("Unable to load class for driver \"" + driver + "\".");
            }
            if (e instanceof SQLException) {
                ServiceLogger.LOGGER.warning("Access problem while loading driver \"" + driver + "\" into memory.");
            }
            if (e instanceof NullPointerException) {
                ServiceLogger.LOGGER.warning("Unable to instantiate driver: " + driver);
            }
            ServiceLogger.LOGGER.warning("Connection to database " + dbName + " failed.");
        }
    }

    private void initHTTPServer() {
        ServiceLogger.LOGGER.config("Initializing HTTP server...");
        String scheme = configs.getScheme();
        String hostName = configs.getHostName();
        int port = configs.getPort();
        String path = configs.getPath();
        try {
            // Build the URI for which the HTTP server will accept inbound traffic for
            URI uri = UriBuilder.fromUri(scheme + hostName + path).port(port).build();
            // The resource config must contain the package for your API endpoints. The endpoints should always be
            // located within a subpackage named "resources"
            ResourceConfig rc = new ResourceConfig().packages("wk2.activity2.jdbc.resources");
            // Set Jackson to be the default serializer
            rc.register(JacksonFeature.class);
            // Create the server
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri, rc, false);
            // Start the server
            server.start();
            ServiceLogger.LOGGER.config("HTTP server started.");
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void exitAppFailure(String message) {
        System.err.println("ERROR: " + message);
        System.exit(-1);
    }

    private void exitAppFailureArgs(String message) {
        System.err.println("ERROR: " + message);
        System.err.println("Usage options: ");
        System.err.println("\tSpecify configuration file:");
        System.err.println("\t\t--config [file]");
        System.err.println("\t\t-c");
        System.err.println("\tUse default configuration:");
        System.err.println("\t\t--default");
        System.err.println("\t\t-d");
        System.exit(-1);
    }

    public static Connection getCon() {
        return con;
    }
}
