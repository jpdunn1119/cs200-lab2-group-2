import java.util.List;
import java.util.Objects;

/**
 * Author: Grayson Henneke.
 * Implements Request Provider Directory: read, sort, display, and save.
 */
public class DirectoryController {
    private final ProviderDirectory directory;
    private final DirectoryFileWriter fileWriter;
    private String lastError = "";

    public DirectoryController(ProviderDirectory directory, DirectoryFileWriter fileWriter) {
        this.directory = Objects.requireNonNull(directory, "directory");
        this.fileWriter = Objects.requireNonNull(fileWriter, "fileWriter");
    }

    /** Human-readable text for the provider terminal/menu. */
    public String displayDirectory() {
        return DirectoryFileWriter.formatDirectory(getSortedServices());
    }

    /** Returns the created file's absolute path or null on failure. */
    public String requestProviderDirectory() {
        lastError = "";
        if (!fileWriter.createDirectoryFile(getSortedServices())) {
            lastError = fileWriter.getLastError();
            return null;
        }
        return fileWriter.getLastCreatedPath().toString();
    }

    /** Matches the requestDirectory(providerId) operation in Project 3 UML. */
    public String requestDirectory(String providerId) {
        if (providerId == null || !providerId.matches("\\d{9}")) {
            lastError = "Provider number must contain exactly 9 digits.";
            return null;
        }
        // Provider authentication is enforced by the team's login/menu code.
        return requestProviderDirectory();
    }

    public String getLastError() { return lastError; }

    private List<ServiceEntry> getSortedServices() {
        return directory.sortServicesAlphabetically(directory.getServices());
    }
}
