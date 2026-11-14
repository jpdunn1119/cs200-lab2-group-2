import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Author: Grayson Henneke.
 * One provider's billable service for one ChocAn member.
 */
public class ServiceRecord {
    private final String recordId;
    private final LocalDate serviceDate;
    private final LocalDateTime receivedAt;
    private final String memberId;
    private final String providerId;
    private final String serviceCode;
    private final BigDecimal fee;
    private String comments = "";

    public ServiceRecord(LocalDate serviceDate, LocalDateTime receivedAt,
                         String memberId, String providerId, String serviceCode, BigDecimal fee) {
        this.recordId = UUID.randomUUID().toString();
        this.serviceDate = Objects.requireNonNull(serviceDate, "serviceDate");
        this.receivedAt = Objects.requireNonNull(receivedAt, "receivedAt");
        if (memberId == null || !memberId.matches("\\d{9}")) {
            throw new IllegalArgumentException("Member number must contain exactly 9 digits.");
        }
        if (providerId == null || !providerId.matches("\\d{9}")) {
            throw new IllegalArgumentException("Provider number must contain exactly 9 digits.");
        }
        if (serviceCode == null || !serviceCode.matches("\\d{6}")) {
            throw new IllegalArgumentException("Service code must contain exactly 6 digits.");
        }
        this.fee = Objects.requireNonNull(fee, "fee");
        this.memberId = memberId;
        this.providerId = providerId;
        this.serviceCode = serviceCode;
    }

    public String getRecordId() { return recordId; }
    public LocalDate getServiceDate() { return serviceDate; }
    public LocalDateTime getReceivedAt() { return receivedAt; }
    public String getMemberId() { return memberId; }
    public String getProviderId() { return providerId; }
    public String getServiceCode() { return serviceCode; }
    public BigDecimal getFee() { return fee; }
    public String getComments() { return comments; }

    /** Optional; maximum of 100 characters per the original ChocAn description. */
    public boolean setComments(String comments) {
        if (comments == null || comments.length() > 100) {
            return false;
        }
        this.comments = comments;
        return true;
    }
}
