import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Author: Collin Cronin
 * Writes weekly EFT data to disk so banking systems can credit each provider.
 */
public class CreateETFFile {
    private final ServiceRecordRepository servicesRepo;
    private final Function<String, String> providerNameLookup;
    private Path lastCreatedPath;
    private String lastError = "";

    public CreateETFFile(ServiceRecordRepository servicesRepo,
                         Function<String, String> providerNameLookup) {
        this.servicesRepo = servicesRepo;
        this.providerNameLookup = providerNameLookup;
    }

    /**
     * Aggregates this week's service fees by provider and writes the EFT file.
     * @return true if the file was written successfully
     */
    public boolean create() {
        lastCreatedPath = null;
        lastError = "";

        try {
            Path dir = Path.of(".");
            Path file = dir.resolve("EFTData.txt");
            Files.writeString(file, formatEftData(computeWeeklyTotals()), StandardCharsets.UTF_8);
            lastCreatedPath = file.toAbsolutePath().normalize();
            return true;
        } catch (IOException | SecurityException ex) {
            lastError = "Unable to create EFT file: " + ex.getMessage();
            return false;
        }
    }

    private Map<String, BigDecimal> computeWeeklyTotals() {
        LocalDate from = LocalDate.now().minusDays(7);
        Map<String, BigDecimal> totals = new LinkedHashMap<>();

        for (ServiceRecord record : servicesRepo.getAllRecords()) {
            if (record.getServiceDate().isBefore(from)) {
                continue;
            }
            totals.merge(record.getProviderId(), record.getFee(), BigDecimal::add);
        }

        return totals;
    }

    private String formatEftData(Map<String, BigDecimal> totalsByProvider) {
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, BigDecimal> entry : totalsByProvider.entrySet()) {
            String providerId = entry.getKey();
            String providerName = providerNameLookup.apply(providerId);
            if (providerName == null) {
                providerName = "";
            }
            if (providerName.length() > 25) {
                providerName = providerName.substring(0, 25);
            }

            builder.append(providerName).append('\n');
            builder.append(providerId).append('\n');
            builder.append(entry.getValue().setScale(2).toPlainString()).append('\n');
        }

        return builder.toString();
    }

    public Path getLastCreatedPath() { return lastCreatedPath; }
    public String getLastError() { return lastError; }
}
