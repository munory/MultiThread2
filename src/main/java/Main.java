import java.util.*;

public class Main {

    public static final Map<Long, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread(() -> {

                var route = generateRoute("RLRFR", 100);
                var count = route.chars().filter(ch -> ch == 'R').count();

                synchronized (sizeToFreq) {
                    sizeToFreq.compute(count, (k, v) -> (v == null) ? 1 : v + 1);
                }
            });

            threads.add(thread);

            thread.start();
        }

        for (var thread : threads) {
            thread.join();
        }

        sizeToFreq.entrySet().stream().max(Map.Entry.comparingByValue()).ifPresent(maxEntry -> {
            System.out.println("Самое частое количество повторений " + maxEntry.getKey() + " (встретилось " + maxEntry.getValue() + " раз)");

            System.out.println("Другие размеры:");
            sizeToFreq.entrySet()
                    .stream()
                    .filter(entry -> !Objects.equals(entry.getKey(), maxEntry.getKey()))
                    .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                    .forEach(entry -> {
                        System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз)");
                    });
        });

    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

}
