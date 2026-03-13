import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    String account;
    int time; // minutes from start of day

    Transaction(int id, int amount, String merchant, String account, int time) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.account = account;
        this.time = time;
    }
}

 class TransactionAnalyzer {

    List<Transaction> transactions = new ArrayList<>();

    // Add transaction
    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    // Classic Two-Sum
    public void findTwoSum(int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction other = map.get(complement);

                System.out.println("Two-Sum Pair: "
                        + other.id + " + " + t.id
                        + " = " + target);
            }

            map.put(t.amount, t);
        }
    }

    // Two-Sum within 1 hour (60 minutes)
    public void twoSumWithinHour(int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction other = map.get(complement);

                if (Math.abs(t.time - other.time) <= 60) {

                    System.out.println("Two-Sum within 1hr: "
                            + other.id + " + " + t.id);
                }
            }

            map.put(t.amount, t);
        }
    }

    // Duplicate detection
    public void detectDuplicates() {

        HashMap<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : transactions) {

            String key = t.amount + "-" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        for (String key : map.keySet()) {

            List<Transaction> list = map.get(key);

            if (list.size() > 1) {

                System.out.println("Duplicate Transactions:");

                for (Transaction t : list) {
                    System.out.println("ID: " + t.id +
                            " Account: " + t.account +
                            " Amount: " + t.amount +
                            " Merchant: " + t.merchant);
                }
            }
        }
    }

    // K-Sum
    public void findKSum(int k, int target) {

        List<Integer> amounts = new ArrayList<>();

        for (Transaction t : transactions) {
            amounts.add(t.amount);
        }

        kSumHelper(amounts, k, target, 0, new ArrayList<>());
    }

    private void kSumHelper(List<Integer> nums, int k, int target,
                            int start, List<Integer> path) {

        if (k == 0 && target == 0) {

            System.out.println("K-Sum Combination: " + path);
            return;
        }

        if (k == 0 || start >= nums.size())
            return;

        for (int i = start; i < nums.size(); i++) {

            path.add(nums.get(i));

            kSumHelper(nums, k - 1,
                    target - nums.get(i),
                    i + 1, path);

            path.remove(path.size() - 1);
        }
    }

    public static void main(String[] args) {

        TransactionAnalyzer analyzer = new TransactionAnalyzer();

        analyzer.addTransaction(new Transaction(1, 500, "StoreA", "acc1", 600));
        analyzer.addTransaction(new Transaction(2, 300, "StoreB", "acc2", 615));
        analyzer.addTransaction(new Transaction(3, 200, "StoreC", "acc3", 630));
        analyzer.addTransaction(new Transaction(4, 500, "StoreA", "acc4", 640));

        System.out.println("\n--- Two Sum ---");
        analyzer.findTwoSum(500);

        System.out.println("\n--- Two Sum within 1 hour ---");
        analyzer.twoSumWithinHour(500);

        System.out.println("\n--- Duplicate Detection ---");
        analyzer.detectDuplicates();

        System.out.println("\n--- K Sum ---");
        analyzer.findKSum(3, 1000);
    }
}
