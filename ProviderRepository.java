import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory storage for provider records.
 */
public class ProviderRepository {
    private final Map<String, Provider> providers = new HashMap<>();

    public Provider findProvider(String providerId) {
        if (providerId == null) {
            return null;
        }
        return providers.get(providerId);
    }

    public boolean insertProvider(Provider provider) {
        if (provider == null || provider.getProviderNumber() == null) {
            return false;
        }
        String id = provider.getProviderNumber();
        if (providers.containsKey(id)) {
            return false;
        }
        providers.put(id, provider);
        return true;
    }

    public boolean updateProvider(String providerId, Provider provider) {
        if (providerId == null || provider == null || !providers.containsKey(providerId)) {
            return false;
        }
        providers.put(providerId, provider);
        return true;
    }

    public boolean removeProvider(String providerId) {
        if (providerId == null || !providers.containsKey(providerId)) {
            return false;
        }
        providers.remove(providerId);
        return true;
    }

    public List<Provider> getAllProviders() {
        return new ArrayList<>(providers.values());
    }
}
