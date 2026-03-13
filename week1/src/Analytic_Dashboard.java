
import java.util.*;
import java.util.concurrent.*;

public class Analytic_Dashboard {

    // page -> total views
    static HashMap<String, Integer> pageViews = new HashMap<>();
    // page -> unique visitors
    static HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();
    // traffic source -> count
    static HashMap<String, Integer> trafficSources = new HashMap<>();

    // process an incoming page view
    static void processEvent(String url, String userId, String source) {
        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);
        uniqueVisitors.computeIfAbsent(url, k -> new HashSet<>()).add(userId);
        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
    }

    // display top pages and traffic sources
    static void getDashboard() {
        System.out.println("\n====== REAL-TIME DASHBOARD ======");

        // Top 10 pages using PriorityQueue
        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));

        for (Map.Entry<String, Integer> e : pageViews.entrySet()) {
            pq.offer(e);
            if (pq.size() > 10) pq.poll();
        }

        List<Map.Entry<String, Integer>> topPages = new ArrayList<>();
        while (!pq.isEmpty()) topPages.add(pq.poll());
        Collections.reverse(topPages);

        System.out.println("Top Pages:");
        int rank = 1;
        for (Map.Entry<String, Integer> e : topPages) {
            int unique = uniqueVisitors.get(e.getKey()).size();
            System.out.println(rank + ". " + e.getKey() + " - " + e.getValue()
                    + " views (" + unique + " unique)");
            rank++;
        }

        // Traffic sources percentages
        int total = trafficSources.values().stream().mapToInt(i -> i).sum();
        System.out.println("\nTraffic Sources:");
        trafficSources.forEach((s, c) -> System.out.println(
                s + ": " + String.format("%.1f", c * 100.0 / total) + "%"));
    }

    public static void main(String[] args) throws InterruptedException {

        // Scheduled task to update dashboard every 5 seconds
        ScheduledExecutorService dashboardUpdater = Executors.newScheduledThreadPool(1);
        dashboardUpdater.scheduleAtFixedRate(Analytic_Dashboard::getDashboard, 5, 5, TimeUnit.SECONDS);

        // Simulate incoming events
        String[] pages = {"/article/breaking-news", "/sports/championship",
                "/tech/new-gadget", "/health/tips"};
        String[] sources = {"Google", "Facebook", "Direct", "Twitter"};

        Random random = new Random();

        // simulate 1 event per 100ms (can adjust for high load)
        for (int i = 0; i < 50; i++) {
            String url = pages[random.nextInt(pages.length)];
            String user = "user_" + random.nextInt(1000);
            String source = sources[random.nextInt(sources.length)];
            processEvent(url, user, source);

            Thread.sleep(100); // simulate delay between events
        }

        // keep dashboard running for demo
        Thread.sleep(10000);
        dashboardUpdater.shutdown();
        System.out.println("\nDemo ended.");
    }
}