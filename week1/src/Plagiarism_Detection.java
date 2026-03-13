import java.util.*;

public class Plagiarism_Detection {

    // n-gram → documents containing it
    static HashMap<String, Set<String>> index = new HashMap<>();

    static int N = 5; // 5-gram

    // generate n-grams
    public static List<String> generateNgrams(String text) {
        String[] words = text.toLowerCase().split("\\s+");
        List<String> grams = new ArrayList<>();

        for (int i = 0; i <= words.length - N; i++) {
            String gram = "";
            for (int j = 0; j < N; j++) {
                gram += words[i + j] + " ";
            }
            grams.add(gram.trim());
        }
        return grams;
    }

    // add document to database
    public static void addDocument(String docId, String text) {
        List<String> grams = generateNgrams(text);

        for (String g : grams) {
            index.putIfAbsent(g, new HashSet<>());
            index.get(g).add(docId);
        }

        System.out.println(docId + " indexed with " + grams.size() + " n-grams.");
    }

    // analyze plagiarism
    public static void analyzeDocument(String docId, String text) {

        List<String> grams = generateNgrams(text);
        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String g : grams) {
            if (index.containsKey(g)) {
                for (String otherDoc : index.get(g)) {
                    matchCount.put(otherDoc,
                            matchCount.getOrDefault(otherDoc, 0) + 1);
                }
            }
        }

        System.out.println("\nAnalysis for " + docId);
        System.out.println("Extracted " + grams.size() + " n-grams");

        for (String d : matchCount.keySet()) {
            int matches = matchCount.get(d);
            double similarity = (matches * 100.0) / grams.size();

            System.out.println("Matched with " + d +
                    " → " + matches + " n-grams | Similarity: " +
                    String.format("%.2f", similarity) + "%");

            if (similarity > 50)
                System.out.println("⚠ PLAGIARISM DETECTED!");
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // existing documents
        addDocument("essay_089",
                "machine learning improves computer systems using data analysis and algorithms");

        addDocument("essay_092",
                "machine learning improves computer systems using data analysis and algorithms for predictions");

        System.out.println("\nEnter new essay text:");
        String text = sc.nextLine();

        analyzeDocument("essay_new", text);

        sc.close();
    }
}
