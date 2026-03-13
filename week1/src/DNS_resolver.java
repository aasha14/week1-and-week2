import java.util.*;

class DNS_resolver {

    // DNS Entry class
    static class DNSEntry {
        String ip;
        long expiryTime;

        DNSEntry(String ip, int ttl) {
            this.ip = ip;
            this.expiryTime = System.currentTimeMillis() + ttl * 1000;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    // LRU Cache using LinkedHashMap
    static LinkedHashMap<String, DNSEntry> cache =
            new LinkedHashMap<String, DNSEntry>(5, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                    return size() > 5; // max cache size
                }
            };

    static int hits = 0;
    static int misses = 0;

    // simulate upstream DNS lookup
    static String queryUpstream(String domain) {
        Random r = new Random();
        return "172.217." + r.nextInt(100) + "." + r.nextInt(255);
    }

    // resolve domain
    static void resolve(String domain) {

        long start = System.nanoTime();

        if (cache.containsKey(domain)) {
            DNSEntry entry = cache.get(domain);

            if (!entry.isExpired()) {
                hits++;
                long time = System.nanoTime() - start;
                System.out.println("Cache HIT → " + entry.ip +
                        " (" + time / 1_000_000.0 + " ms)");
                return;
            } else {
                System.out.println("Cache EXPIRED → refreshing...");
                cache.remove(domain);
            }
        }

        misses++;
        String ip = queryUpstream(domain);
        cache.put(domain, new DNSEntry(ip, 10)); // TTL = 10s

        System.out.println("Cache MISS → Upstream DNS → " + ip + " (TTL:10s)");
    }

    // show stats
    static void getCacheStats() {
        int total = hits + misses;
        double rate = total == 0 ? 0 : (hits * 100.0 / total);

        System.out.println("\nCache Stats:");
        System.out.println("Hits: " + hits);
        System.out.println("Misses: " + misses);
        System.out.println("Hit Rate: " + rate + "%");
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n==== DNS CACHE SYSTEM ====");
            System.out.println("1. Resolve Domain");
            System.out.println("2. View Cache Stats");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter domain: ");
                    String domain = sc.nextLine();
                    resolve(domain);
                    break;

                case 2:
                    getCacheStats();
                    break;

                case 3:
                    System.out.println("DNS Cache Closed");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid option");
            }
        }
    }
}
