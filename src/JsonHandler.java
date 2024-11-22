import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class JsonHandler {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java -jar DestinationHashGenerator.jar <RollNumber> <JsonFilePath>");
            return;
        }

        // Parse command-line arguments
        String rollNumber = args[0].toLowerCase().replaceAll("\\s", ""); // Normalize Roll Number
        String jsonFilePath = args[1];

        // Proceed to process JSON and generate hash
        processJsonAndGenerateHash(rollNumber, jsonFilePath);
    }

    private static void processJsonAndGenerateHash(String rollNumber, String jsonFilePath) {
        try {
            // Parse the JSON file
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));

            // Find the "destination" key
            String destinationValue = findDestination(rootNode);
            if (destinationValue == null) {
                System.out.println("Key 'destination' not found in the JSON file.");
                return;
            }

            // Generate a random string
            String randomString = generateRandomString(8);

            // Create MD5 hash
            String concatenatedString = rollNumber + destinationValue + randomString;
            String hash = generateMD5Hash(concatenatedString);

            // Output the result
            System.out.println(hash + ";" + randomString);
        } catch (IOException e) {
            System.err.println("Error reading the JSON file: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error generating MD5 hash: " + e.getMessage());
        }
    }

    private static String findDestination(JsonNode node) {
        if (node.isObject()) {
            var fields = node.fields(); // Get the iterator for fields
            while (fields.hasNext()) {
                var field = fields.next(); // Fetch the next field
                if (field.getKey().equals("destination")) {
                    return field.getValue().asText();
                }
                String result = findDestination(field.getValue());
                if (result != null) return result;
            }
        } else if (node.isArray()) {
            for (JsonNode element : node) {
                String result = findDestination(element);
                if (result != null) return result;
            }
        }
        return null;
    }


    private static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private static String generateMD5Hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashBytes = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // Example POJO class
    static class Person {
        private String name;
        private int age;

        // No-argument constructor
        public Person() {
        }

        // Constructor with arguments
        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
    }
}
