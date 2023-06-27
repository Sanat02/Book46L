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
    private List<Employee> employees = new ArrayList<>();

    public EmployeeDataModel() {
        employees = readFile();
    }


    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
        saveEmployeesToFile();
    }



    public void setEmployeeCookieId(String email)
    {
        for(int i=0;i<employees.size();i++)
        {
            if(email.equals(employees.get(i).getEmail()))
            {
                employees.get(i).makeCookieId(email);
            }
        }

        saveEmployeesToFile();
    }
    public void setEmployeeBooks(String email, BookDataModel.Book book)
    {
        List<BookDataModel.Book> bookList=new ArrayList<>();

        for(int i=0;i<employees.size();i++)
        {
            if(email.equals(employees.get(i).getEmail()))
            {
                bookList=employees.get(i).getCurrentBooks();
                bookList.add(book);
                employees.get(i).setCurrentBooks(bookList);
            }
        }

        saveEmployeesToFile();
    }
    public int getEmployeeBookSize(String email)
    {
        int size=0;
        for(int i=0;i<employees.size();i++)
        {
            if(email.equals(employees.get(i).getEmail()))
            {
                size=employees.get(i).getCurrentBooks().size();
                break;
            }
        }

        saveEmployeesToFile();
        return size;
    }
    public void getEmployeeCookieId(String email)
    {
        for(int i=0;i<employees.size();i++)
        {
            if(email.equals(employees.get(i).getEmail()))
            {
                System.out.println(employees.get(i).getCookieId());
                break;
            }
        }

        saveEmployeesToFile();
    }

    public void saveEmployeesToFile() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(employees);
        try (FileWriter writer = new FileWriter("employees.json")) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Employee> readFile() {
        List<Employee> emplist = new ArrayList<>();
        try {
            Type listType = new TypeToken<ArrayList<Employee>>() {
            }.getType();
            Path path = Paths.get("employees.json");
            String json = Files.readString(path);
            emplist = new Gson().fromJson(json, listType);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return emplist;
    }

    public static class Employee {
        private String firstName;
        private String lastName;
        private String password;
        private String email;
        private String job;
        private String phone;
        private String image;
        private List<BookDataModel.Book> currentBooks=new ArrayList<>();
        private List<String> takenBooks;
        private String cookieId;


        public Employee(String firstName, String lastName, String email, String job, String phone, String image, List<BookDataModel.Book> currentBooks, List<String> takenBooks, String password) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.job = job;
            this.phone = phone;
            this.image = image;
            this.currentBooks = currentBooks;
            this.takenBooks = takenBooks;
            this.password = password;
        }
        public void addBook(BookDataModel.Book book) {
            currentBooks.add(book);
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
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


        public void setCurrentBooks(List<BookDataModel.Book> currentBooks) {
            this.currentBooks = currentBooks;
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

        public String makeCode(String input) {
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