import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Author: Grayson Henneke.
 * Provides code/name/fee lookups and an alphabetical listing of services.
 * The owning application supplies the official service entries.
 */
public class ProviderDirectory {
    private final Map<String, ServiceEntry> entries = new LinkedHashMap<>();

    public ProviderDirectory() { }

    public ProviderDirectory(Collection<ServiceEntry> initialEntries) {
        Objects.requireNonNull(initialEntries, "initialEntries");
        for (ServiceEntry entry : initialEntries) {
            Objects.requireNonNull(entry, "entry");
            if (entries.putIfAbsent(entry.getServiceCode(), entry) != null) {
                throw new IllegalArgumentException("Duplicate service code: " + entry.getServiceCode());
            }
        }
    }

    public boolean addService(ServiceEntry entry) {
        Objects.requireNonNull(entry, "entry");
        if (entries.containsKey(entry.getServiceCode())) {
            return false;
        }
        entries.put(entry.getServiceCode(), entry);
        return true;
    }

    public boolean validateServiceCode(String code) {
        return code != null && entries.containsKey(code);
    }

    // Returns null if the service code is not present.
    public ServiceEntry getServiceEntry(String code) {
        return entries.get(code);
    }

    // These helpers are useful to other teammates implementing validation and reports.
    public String getServiceName(String code) {
        ServiceEntry entry = getServiceEntry(code);
        return entry == null ? null : entry.getServiceName();
    }

    public BigDecimal getServiceFee(String code) {
        ServiceEntry entry = getServiceEntry(code);
        return entry == null ? null : entry.getFee();
    }

    public List<ServiceEntry> getServices() {
        return new ArrayList<>(entries.values());
    }

    public List<ServiceEntry> sortServicesAlphabetically(List<ServiceEntry> services) {
        Objects.requireNonNull(services, "services");
        List<ServiceEntry> sorted = new ArrayList<>(services);
        sorted.sort(Comparator.comparing(ServiceEntry::getServiceName, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(ServiceEntry::getServiceCode));
        return sorted;
    }
}
