import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Author: Grayson Henneke.
 * Creates the file representing the requested Provider Directory.
 * No email transmission is needed for the ChocAn acceptance test.
 */
public class DirectoryFileWriter {
    private final Path outputDirectory;
    private Path lastCreatedPath;
    private String lastError = "";

    public DirectoryFileWriter(Path outputDirectory) {
        this.outputDirectory = Objects.requireNonNull(outputDirectory, "outputDirectory");
    }

    public boolean createDirectoryFile(List<ServiceEntry> sortedServices) {
        lastCreatedPath = null;
        lastError = "";
        try {
            Files.createDirectories(outputDirectory);
            Path file = outputDirectory.resolve("ProviderDirectory.txt");
            Files.writeString(file, formatDirectory(sortedServices), StandardCharsets.UTF_8);
            lastCreatedPath = file.toAbsolutePath().normalize();
            return true;
        } catch (IOException | SecurityException ex) {
            lastError = "Unable to create directory file: " + ex.getMessage();
            return false;
        }
    }

    public static String formatDirectory(List<ServiceEntry> sortedServices) {
        Objects.requireNonNull(sortedServices, "sortedServices");
        StringBuilder builder = new StringBuilder();
        builder.append("CHOCAN PROVIDER DIRECTORY\n");
        builder.append("-----------------------------------------------\n");
        builder.append(String.format("%-20s  %-6s  %8s%n", "Service", "Code", "Fee"));
        builder.append("-----------------------------------------------\n");
        for (ServiceEntry entry : sortedServices) {
            builder.append(String.format("%-20s  %-6s  $%7s%n",
                    entry.getServiceName(), entry.getServiceCode(), entry.getFee().toPlainString()));
        }
        if (sortedServices.isEmpty()) {
            builder.append("No services are currently listed.\n");
        }
        return builder.toString();
    }

    public Path getLastCreatedPath() { return lastCreatedPath; }
    public String getLastError() { return lastError; }
}
