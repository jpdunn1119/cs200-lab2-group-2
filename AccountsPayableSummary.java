import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

/**
 * Author: Justin Dunn
 * Summarizes provider payments for an accounting period.
 */
public class AccountsPayableSummary {
    private final ServiceRecordRepository repository;

    public AccountsPayableSummary(ServiceRecordRepository repository) {
        if (repository == null) throw new IllegalArgumentException("Repository is required.");
        this.repository = repository;
    }

    public String generateSummary(LocalDate from, LocalDate through) {
        Map<String, BigDecimal> totals = new TreeMap<>();
        Map<String, Integer> counts = new TreeMap<>();
        BigDecimal grandTotal = BigDecimal.ZERO;
        int serviceCount = 0;
        for (ServiceRecord record : repository.getRecordsByWeek(from, through)) {
            String providerId = record.getProviderId();
            totals.merge(providerId, record.getFee(), BigDecimal::add);
            counts.merge(providerId, 1, Integer::sum);
            grandTotal = grandTotal.add(record.getFee());
            serviceCount++;
        }
        StringBuilder report = new StringBuilder();
        report.append("ACCOUNTS PAYABLE SUMMARY\nPeriod: ")
              .append(from).append(" to ").append(through).append("\n\n");
        for (String providerId : totals.keySet()) {
            report.append("Provider: ").append(providerId)
                  .append(" | Services: ").append(counts.get(providerId))
                  .append(" | Amount owed: $").append(totals.get(providerId).toPlainString())
                  .append('\n');
        }
        report.append("\nProviders paid: ").append(totals.size())
              .append("\nTotal services: ").append(serviceCount)
              .append("\nTotal amount owed: $").append(grandTotal.toPlainString()).append('\n');
        return report.toString();
    }

    public void printSummary(LocalDate from, LocalDate through) {
        System.out.print(generateSummary(from, through));
    }
}
