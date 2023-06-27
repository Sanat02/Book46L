package kg.attractor.java.lesson44;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EmployeeDataModel {
    private final List<Employee> employees = new ArrayList<>();

    public EmployeeDataModel() {
        employees.addAll(readFile());
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
        saveEmployeesToFile();
    }

    public void setEmployeeCookieId(String email) {
        for (Employee employee : employees) {
            if (email.equals(employee.getEmail())) {
                employee.makeCookieId(email);
                break;
            }
        }
        saveEmployeesToFile();
    }

    public void setEmployeeBooks(String email, BookDataModel.Book book) {
        for (Employee employee : employees) {
            if (email.equals(employee.getEmail())) {
                employee.addBook(book);
                break;
            }
        }
        saveEmployeesToFile();
    }

    public int getEmployeeBookSize(String email) {
        for (Employee employee : employees) {
            if (email.equals(employee.getEmail())) {
                return employee.getCurrentBooks().size();
            }
        }
        return 0;
    }

    public void getEmployeeCookieId(String email) {
        for (Employee employee : employees) {
            if (email.equals(employee.getEmail())) {
                System.out.println(employee.getCookieId());
                break;
            }
        }
        saveEmployeesToFile();
    }

    private void saveEmployeesToFile() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(employees);
        try (FileWriter writer = new FileWriter("employees.json")) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Employee> readFile() {
        List<Employee> employeeList = new ArrayList<>();
        try {
            Path path = Paths.get("employees.json");
            String json = Files.readString(path);
            Type listType = new TypeToken<List<Employee>>() {}.getType();
            employeeList = new Gson().fromJson(json, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return employeeList;
    }

    public static class Employee {
        private final String firstName;
        private final String lastName;
        private final String email;
        private String job;
        private String phone;
        private String image;
        private final List<BookDataModel.Book> currentBooks = new ArrayList<>();
        private List<String> takenBooks;
        private String password;
        private String cookieId;

        public Employee(String firstName, String lastName, String email, String job, String phone, String image,
                        List<BookDataModel.Book> currentBooks, List<String> takenBooks, String password) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.job = job;
            this.phone = phone;
            this.image = image;
            this.currentBooks.addAll(currentBooks);
            this.takenBooks = takenBooks;
            this.password = password;
        }

        public void addBook(BookDataModel.Book book) {
            currentBooks.add(book);
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getEmail() {
            return email;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public List<BookDataModel.Book> getCurrentBooks() {
            return currentBooks;
        }

        public List<String> getTakenBooks() {
            return takenBooks;
        }

        public void setTakenBooks(List<String> takenBooks) {
            this.takenBooks = takenBooks;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getCookieId() {
            return cookieId;
        }

        public void setCookieId(String cookieId) {
            this.cookieId = cookieId;
        }

        public void makeCookieId(String email) {
            this.cookieId = makeCode(email);
        }

        private String makeCode(String input) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                return convertToString(md.digest(input.getBytes()));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return "";
        }

        private String convertToString(byte[] array) {
            return IntStream.range(0, array.length / 4)
                    .map(i -> array[i])
                    .map(i -> (i < 0) ? i + 127 : i)
                    .mapToObj(Integer::toHexString)
                    .collect(Collectors.joining());
        }
    }
}
