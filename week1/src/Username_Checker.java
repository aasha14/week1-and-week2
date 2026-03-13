import java.util.*;

public class Username_Checker {

    static HashMap<String, Integer> users = new HashMap<>();
    static HashMap<String, Integer> attempts = new HashMap<>();

    public static boolean checkAvailability(String username) {
        attempts.put(username, attempts.getOrDefault(username, 0) + 1);
        return !users.containsKey(username);
    }

    public static List<String> suggestAlternatives(String username) {
        List<String> list = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            String newName = username + i;
            if (!users.containsKey(newName)) {
                list.add(newName);
            }
        }

        if (username.contains("_")) {
            list.add(username.replace("_", "."));
        }

        return list;
    }

    public static String getMostAttempted() {
        String name = "";
        int max = 0;

        for (String key : attempts.keySet()) {
            if (attempts.get(key) > max) {
                max = attempts.get(key);
                name = key;
            }
        }

        return name + " (" + max + " attempts)";
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // existing usernames
        users.put("john_doe", 1);
        users.put("admin", 2);
        users.put("alex", 3);

        System.out.print("Enter username to check: ");
        String username = sc.nextLine();

        if (checkAvailability(username)) {
            System.out.println("Username is available ");
        } else {
            System.out.println("Username already taken ");
            System.out.println("Suggestions: " + suggestAlternatives(username));
        }

        System.out.println("Most attempted username: " + getMostAttempted());

        sc.close();
    }
}