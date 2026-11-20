
public final class ProviderController {

    private final ProviderRepository repository;

    private String lastMessage = "";

    public ProviderController(
            ProviderRepository repository) {

        if (repository == null) {
            throw new IllegalArgumentException(
                    "repository required");
        }

        this.repository = repository;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public ProviderRepository getRepository() {
        return repository;
    }

    public boolean validateProviderData(Provider data) {
        ProviderValidation providerValidator = new ProviderValidation(this.repository);
        return "Valid Provider".equals(providerValidator.verifyProvider(data.getId()));
    }

    public void rejectChange(String reason) {
        lastMessage =
                "provider change rejected because: " + reason;
    }

    public boolean maintainProviderRecords(
            String action,
            String id,
            Provider data,
            boolean confirmed) {

        if (action == null) {
            rejectChange("unknwon action");
            return false;
        }

        switch (action.trim().toUpperCase(
                java.util.Locale.ROOT)) {

            case "ADD":

                if (!validateProviderData(data)
                        || (id != null
                            && !id.isEmpty()
                            && !id.equals(
                                    data.getProviderNumber()))) {

                    rejectChange("invalid data");
                    return false;
                }

                if (repository.findProvider(
                        data.getProviderNumber()) != null) {

                    rejectChange("duplicate id");
                    return false;
                }

                if (!repository.insertProvider(data)) {
                    rejectChange("not saved");
                    return false;
                }

                lastMessage = "provider added: "
                        + data.getProviderNumber();

                return true;

            case "UPDATE":

                if (repository.findProvider(id) == null) {
                    rejectChange("not found");
                    return false;
                }

                if (!validateProviderData(data)
                        || !id.equals(
                                data.getProviderNumber())) {

                    rejectChange("invalid data");
                    return false;
                }

                if (!repository.updateProvider(id, data)) {
                    rejectChange("not saved");
                    return false;
                }

                lastMessage = "provider updated: " + id;
                return true;

            case "DELETE":

                if (repository.findProvider(id) == null) {
                    rejectChange("not found");
                    return false;
                }

                if (!confirmed) {
                    rejectChange("cancelled");
                    return false;
                }

                if (!repository.removeProvider(id)) {
                    rejectChange("not saved");
                    return false;
                }

                lastMessage = "provider deleted: " + id;
                return true;

            case "CANCEL":

                rejectChange("cancelled");
                return false;

            default:

                rejectChange("unknown action");
                return false;
        }
    }

    public boolean maintainProvider(String action, Provider provider) {
        String id = provider == null
                ? null
                : provider.getProviderNumber();

        return maintainProviderRecords(
                action, id, provider, false);
    }
}
