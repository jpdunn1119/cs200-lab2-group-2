import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Author: Justin Dunn
 * Creates a provider's report using the team's existing service records.
 */
public class GenerateProviderReports {
    private final ServiceRecordRepository repository;

    public GenerateProviderReports(ServiceRecordRepository repository) {
        if (repository == null) throw new IllegalArgumentException("Repository is required.");
        this.repository = repository;
    }

    public String generateReport(String providerId, LocalDate from, LocalDate through) {
        if (!ProviderValidation.isValidNumber(providerId)) {
            throw new IllegalArgumentException("Provider ID must have 9 digits.");
        }
        List<ServiceRecord> records = repository.getRecordsByWeek(from, through);
        StringBuilder report = new StringBuilder();
        BigDecimal total = BigDecimal.ZERO;
        int count = 0;
        report.append("PROVIDER REPORT\nProvider: ").append(providerId)
              .append("\nPeriod: ").append(from).append(" to ").append(through).append("\n\n");
        for (ServiceRecord record : records) {
            if (!providerId.equals(record.getProviderId())) continue;
            count++;
            total = total.add(record.getFee());
            report.append("Service date: ").append(record.getServiceDate())
                  .append(" | Entered: ").append(record.getReceivedAt())
                  .append(" | Member: ").append(record.getMemberId())
                  .append(" | Code: ").append(record.getServiceCode())
                  .append(" | Fee: $").append(record.getFee().toPlainString()).append('\n');
        }
        report.append("\nServices: ").append(count)
              .append("\nTotal fees: $").append(total.toPlainString()).append('\n');
        return report.toString();
    }

    public void printReport(String providerId, LocalDate from, LocalDate through) {
        System.out.print(generateReport(providerId, from, through));
    }
}
