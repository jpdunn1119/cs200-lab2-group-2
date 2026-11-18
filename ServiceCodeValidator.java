package Source;

public class ServiceCodeValidator {
    private ProviderDirectoryStub directory;

    public ServiceCodeValidator(ProviderDirectoryStub directory) {
        this.directory = directory;
    }

    public boolean validateServiceCode(String serviceCode) {
        return directory.serviceExists(serviceCode);
    }
}
