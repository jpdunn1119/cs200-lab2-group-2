import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Result of applying one Acme membership-update batch.
 */
public class UpdateSummary {
    private final int received;
    private final int applied;
    private final int failed;
    private final List<String> messages;

    public UpdateSummary(int received, int applied, int failed, List<String> messages) {
        this.received = received;
        this.applied = applied;
        this.failed = failed;
        this.messages = messages == null
                ? List.of()
                : Collections.unmodifiableList(new ArrayList<>(messages));
    }

    public int getReceived() { return received; }
    public int getApplied() { return applied; }
    public int getFailed() { return failed; }
    public List<String> getMessages() { return messages; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("received=").append(received)
                .append(", applied=").append(applied)
                .append(", failed=").append(failed);
        if (!messages.isEmpty()) {
            sb.append(", messages=").append(messages);
        }
        return sb.toString();
    }
}
