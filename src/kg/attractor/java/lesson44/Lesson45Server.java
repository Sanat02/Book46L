package kg.attractor.java.lesson44;

import com.sun.net.httpserver.HttpExchange;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import kg.attractor.java.server.ContentType;


import java.io.*;
import java.nio.file.Path;
import java.util.Map;



import static kg.attractor.java.server.Utils.parseUrlEncoded;

public class Lesson45Server extends Lesson44Server {

    private final EmployeeDataModel employeeDataModel;

    private final Configuration freemarker;

    public Lesson45Server(String host, int port) throws IOException {
        super(host, port);
        employeeDataModel = new EmployeeDataModel();
        freemarker = initFreeMarker();

        registerGet("/register", this::handleRegisterGet);
        registerPost("/register", this::handleRegisterPost);
        registerGet("/login", this::handleLoginGet);
        registerPost("/login", this::handleLoginPost);
        registerGet("/profile", this::handleProfileGet);
    }

    private void handleRegisterGet(HttpExchange exchange) {
        renderTemplate(exchange, "register.html", employeeDataModel);
    }

    private void handleRegisterPost(HttpExchange exchange) {
        String raw = getBody(exchange);
        Map<String, String> parsed = parseUrlEncoded(raw, "&");
        System.out.println(parsed);

        for (EmployeeDataModel.Employee employee : employeeDataModel.getEmployees()) {
            if (employee.getEmail().equals(parsed.get("email"))) {
                Path path = makeFilePath("registrationError.html");
                sendFile(exchange, path, ContentType.TEXT_HTML);
                return;
            }
        }

        String email = parsed.get("email");
        String password = parsed.get("password");
        String name = parsed.get("name");
        String lastName = parsed.get("lname");
        if (hasNumber(name) == 1 || hasNumber(lastName) == 1) {
            Path path = makeFilePath("invalid.html");
            sendFile(exchange, path, ContentType.TEXT_HTML);
        } else {

            EmployeeDataModel.Employee employee = new EmployeeDataModel.Employee(name, lastName, email, null, null, "user.png", null, null, password);
            employeeDataModel.addEmployee(employee);

            renderTemplate(exchange, "newUser.html", employee);
        }

    }

    public void handleLoginGet(HttpExchange exchange) {
        Path path = makeFilePath("login.html");
        sendFile(exchange, path, ContentType.TEXT_HTML);
    }

    public void handleLoginPost(HttpExchange exchange) {
        String raw = getBody(exchange);
        Map<String, String> parsed = parseUrlEncoded(raw, "&");

        EmployeeDataModel.Employee user = null;
        for (EmployeeDataModel.Employee employee : employeeDataModel.getEmployees()) {
            if (employee.getEmail().equals(parsed.get("email")) && employee.getPassword().equals(parsed.get("user-password"))) {
                user = employee;
                break;
            }
        }


        if (user != null) {
            renderTemplate(exchange, "profile.html", user);

        } else {
            Path path = makeFilePath("loginError.html");
            sendFile(exchange, path, ContentType.TEXT_HTML);
        }

    }


    public void handleProfileGet(HttpExchange exchange) {
        renderTemplate(exchange, "profile.html", new BookDataModel());
    }

    private static Configuration initFreeMarker() {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
            cfg.setDirectoryForTemplateLoading(new File("data"));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            cfg.setFallbackOnNullLoopVariable(false);
            return cfg;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public static int hasNumber(String input) {
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                return 1;
            }
        }
        return 0;
    }



}
