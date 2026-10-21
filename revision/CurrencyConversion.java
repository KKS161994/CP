import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class CurrencyConversion {
    record ExchangeRate(String from, String to, double rate) {
    }

    record Node(String to, double amount) {
    }

    public double convert(
            List<ExchangeRate> rates,
            String fromCurrency,
            String toCurrency) {
        
        Map<String, Map<String, Double>> currencies = new HashMap<>();
        buildGraph(rates, currencies);
        if (!currencies.containsKey(fromCurrency))
            return -1;
        if (fromCurrency.equals(toCurrency))
            return 1;
        Queue<Node> queue = new LinkedList<>();
        Set<String> seen = new HashSet<>();
        queue.offer(new Node(fromCurrency, 1));
        seen.add(fromCurrency);
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            String from = node.to;
            double amount = node.amount;

            Map<String, Double> neighour = currencies.get(from);
            for (Map.Entry<String, Double> entry : neighour.entrySet()) {
                if (seen.contains(entry.getKey()))
                    continue;
                if (entry.getKey().equals(toCurrency))
                    return amount * entry.getValue();
                seen.add(entry.getKey());
                queue.add(new Node(entry.getKey(), amount * entry.getValue()));
            }
        }
        return -1;
    }

    private void buildGraph(List<ExchangeRate> rates,
            Map<String, Map<String, Double>> currencies) {
        for (ExchangeRate rate : rates) {
            currencies.putIfAbsent(rate.from, new HashMap<>());
            currencies.putIfAbsent(rate.to, new HashMap<>());
            currencies.get(rate.from).put(rate.to, rate.rate);
            currencies.get(rate.to).put(rate.from, 1 / rate.rate);
        }
    }
}
