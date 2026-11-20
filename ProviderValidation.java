/**
 * Author: Justin Dunn
 * Checks provider numbers and whether providers are active.
 * The provider list can be populated by the team's provider management code.
 */
public class ProviderValidation {
    private ProviderRepository repository;

    public ProviderValidation(ProviderRepository repository) {
        this.repository = repository;
    }

    public String verifyProvider(String providerId) {
        boolean valid = ProviderValidation.isValidNumber(providerId) && this.repository.findProvider(providerId) != null;
        if (valid) return "Validated";
        else return "Invalid number";
    }

    public static boolean isValidNumber(String providerId) {
        return providerId != null && providerId.matches("\\d{9}");
    }
}
