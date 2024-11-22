import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class JacksonExample {
    public static void main(String[] args) {
        try {
            // Create an ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            // Create a simple object
            Person person = new Person("John", 25);

            // Convert the object to JSON
            String json = objectMapper.writeValueAsString(person);

            // Print the JSON string
            System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

class Person {
    private String name;
    private int age;

    // Constructor
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Getters and Setters (optional)
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
