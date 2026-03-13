import java.util.*;

// Trie Node
class TrieNode {
    Map<Character, TrieNode> children;
    boolean isEnd;

    TrieNode() {
        children = new HashMap<>();
        isEnd = false;
    }
}

// Autocomplete System
class AutocompleteSystem {

    TrieNode root;
    HashMap<String, Integer> frequencyMap;

    public AutocompleteSystem() {
        root = new TrieNode();
        frequencyMap = new HashMap<>();
    }

    // Insert query
    public void insert(String query) {

        TrieNode node = root;

        for (char c : query.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }

        node.isEnd = true;

        frequencyMap.put(query, frequencyMap.getOrDefault(query, 0) + 1);
    }

    // Update frequency when searched again
    public void updateFrequency(String query) {
        insert(query);
    }

    // Search suggestions
    public List<String> search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return new ArrayList<>();
            }
            node = node.children.get(c);
        }

        List<String> results = new ArrayList<>();

        dfs(node, prefix, results);

        // Sort by frequency (highest first)
        results.sort((a, b) -> frequencyMap.get(b) - frequencyMap.get(a));

        if (results.size() > 10)
            return results.subList(0, 10);

        return results;
    }

    // DFS to collect words
    private void dfs(TrieNode node, String word, List<String> results) {

        if (node.isEnd) {
            results.add(word);
        }

        for (char c : node.children.keySet()) {
            dfs(node.children.get(c), word + c, results);
        }
    }
}

// Main class
public class Main {

    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        // Sample queries
        system.insert("java tutorial");
        system.insert("javascript");
        system.insert("java download");
        system.insert("java features");
        system.insert("java tutorial");
        system.insert("java tutorial");
        system.insert("java compiler");
        system.insert("java hashmap");
        system.insert("java stream api");

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n1. Search");
            System.out.println("2. Update Frequency");
            System.out.println("3. Exit");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {

                System.out.print("Enter prefix: ");
                String prefix = sc.nextLine();

                List<String> suggestions = system.search(prefix);

                System.out.println("\nTop Suggestions:");

                int rank = 1;

                for (String s : suggestions) {
                    System.out.println(rank + ". " + s + " (" + system.frequencyMap.get(s) + " searches)");
                    rank++;
                }
            }

            else if (choice == 2) {

                System.out.print("Enter query: ");
                String query = sc.nextLine();

                system.updateFrequency(query);

                System.out.println("Updated Frequency: " + system.frequencyMap.get(query));
            }

            else {
                break;
            }
        }

        sc.close();
    }
}