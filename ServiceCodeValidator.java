public class ServiceCodeValidator {
    private ProviderDirectory directory;

    public ServiceCodeValidator(ProviderDirectory directory) {
        this.directory = directory;
    }

    public boolean validateServiceCode(String serviceCode) {
        return directory.serviceExists(serviceCode);
    }
}
