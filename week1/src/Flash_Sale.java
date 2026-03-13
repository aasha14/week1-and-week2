import java.util.*;

public class Flash_Sale {

    // productId -> stock
    static HashMap<String, Integer> inventory = new HashMap<>();

    // waiting list (FIFO)
    static LinkedHashMap<Integer, String> waitingList = new LinkedHashMap<>();

    // check stock instantly O(1)
    public static void checkStock(String productId) {
        int stock = inventory.getOrDefault(productId, 0);
        System.out.println(" Product: " + productId + " | Stock Available: " + stock);
    }

    // synchronized purchase to avoid overselling
    public synchronized static void purchaseItem(String productId, int userId) {

        int stock = inventory.getOrDefault(productId, 0);

        if (stock > 0) {
            inventory.put(productId, stock - 1);
            System.out.println(" User " + userId + " purchased " + productId +
                    " | Remaining stock: " + (stock - 1));
        } else {
            waitingList.put(userId, productId);
            System.out.println(" Stock finished! User " + userId +
                    " added to waiting list at position #" + waitingList.size());
        }
    }

    // show waiting list
    public static void showWaitingList() {
        if (waitingList.isEmpty()) {
            System.out.println("No users in waiting list.");
            return;
        }

        System.out.println("\n Waiting List:");
        int pos = 1;
        for (Map.Entry<Integer, String> entry : waitingList.entrySet()) {
            System.out.println("Position " + pos + " → User " + entry.getKey() +
                    " waiting for " + entry.getValue());
            pos++;
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // initial products
        inventory.put("IPHONE15_256GB", 5);
        inventory.put("PS5_CONSOLE", 3);
        inventory.put("NIKE_LIMITED", 2);

        while (true) {

            System.out.println("\n====== FLASH SALE INVENTORY SYSTEM ======");
            System.out.println("1. Check Stock");
            System.out.println("2. Purchase Product");
            System.out.println("3. View Waiting List");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter Product ID: ");
                    String productId = sc.nextLine();
                    checkStock(productId);
                    break;

                case 2:
                    System.out.print("Enter Product ID: ");
                    productId = sc.nextLine();
                    System.out.print("Enter User ID: ");
                    int userId = sc.nextInt();
                    purchaseItem(productId, userId);
                    break;

                case 3:
                    showWaitingList();
                    break;

                case 4:
                    System.out.println(" Flash Sale System Closed");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}