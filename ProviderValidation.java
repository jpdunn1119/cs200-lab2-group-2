import java.util.HashMap;
import java.util.Map;

/**
 * Author: Justin Dunn
 * Checks provider numbers and whether providers are active.
 * The provider list can be populated by the team's provider management code.
 */
public class ProviderValidation {
    private final Map<String, Boolean> providers = new HashMap<>();

    public boolean addProvider(String providerId) {
        if (!isValidNumber(providerId) || providers.containsKey(providerId)) return false;
        providers.put(providerId, true);
        return true;
    }

    public boolean setProviderActive(String providerId, boolean active) {
        if (!providers.containsKey(providerId)) return false;
        providers.put(providerId, active);
        return true;
    }

    public boolean removeProvider(String providerId) {
        return providers.remove(providerId) != null;
    }

    public boolean verifyProvider(String providerId) {
        return isValidNumber(providerId) && Boolean.TRUE.equals(providers.get(providerId));
    }

    public static boolean isValidNumber(String providerId) {
        return providerId != null && providerId.matches("\\d{9}");
    }
}
