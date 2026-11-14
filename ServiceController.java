import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Author: Grayson Henneke.
 * Implements Record Provider Service and Add Service Comments.
 *
 * Workflow: recordService -> show service -> confirmService ->
 *           optionally addServiceComments -> saveServiceRecord.
 * Member validation is supplied by a teammate through MemberValidation.
 */
public class ServiceController {
    private final MemberValidation memberValidation;
    private final ProviderDirectory directory;
    private final ServiceRecordRepository repository;
    private final Clock clock;

    private ServiceRecord pendingRecord;
    private boolean confirmed;
    private ServiceRecord lastSavedRecord;
    private String lastError = "";

    public ServiceController(MemberValidation memberValidation,
                             ProviderDirectory directory,
                             ServiceRecordRepository repository) {
        this(memberValidation, directory, repository, Clock.systemDefaultZone());
    }

    public ServiceController(MemberValidation memberValidation,
                             ProviderDirectory directory,
                             ServiceRecordRepository repository,
                             Clock clock) {
        this.memberValidation = Objects.requireNonNull(memberValidation, "memberValidation");
        this.directory = Objects.requireNonNull(directory, "directory");
        this.repository = Objects.requireNonNull(repository, "repository");
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    /**
     * Performs the validation steps and stages a record, but does NOT save it.
     * This permits the provider to inspect the name and confirm the service.
     */
    public boolean recordService(String memberId, String providerId,
                                 LocalDate serviceDate, String serviceCode) {
        clearPending();
        lastError = "";
        if (memberId == null || !memberId.matches("\\d{9}")) {
            return fail("Member number must contain exactly 9 digits.");
        }
        if (providerId == null || !providerId.matches("\\d{9}")) {
            return fail("Provider number must contain exactly 9 digits.");
        }
        if (serviceDate == null) {
            return fail("A date of service is required.");
        }
        if (serviceDate.isAfter(LocalDate.now(clock))) {
            return fail("Date of service cannot be in the future.");
        }
        String memberStatus = memberValidation.validateMember(memberId);
        if (memberStatus == null || !memberStatus.trim().equalsIgnoreCase("Validated")) {
            return fail("Member validation failed: " +
                    ((memberStatus == null || memberStatus.isBlank()) ? "Invalid number" : memberStatus));
        }
        ServiceEntry entry = directory.getServiceEntry(serviceCode);
        if (entry == null) {
            return fail("Invalid service code.");
        }
        pendingRecord = new ServiceRecord(serviceDate, LocalDateTime.now(clock),
                memberId, providerId, serviceCode, entry.getFee());
        return true;
    }

    /** Convenient overload for callers that already have comments. */
    public boolean recordService(String memberId, String providerId,
                                 LocalDate serviceDate, String serviceCode, String comments) {
        if (!recordService(memberId, providerId, serviceDate, serviceCode)) {
            return false;
        }
        if (!addServiceComments(comments == null ? "" : comments)) {
            clearPending();
            return false;
        }
        return true;
    }

    /** Returns the matching service name, or null when the code is invalid. */
    public String validateServiceCode(String code) {
        return directory.getServiceName(code);
    }

    public String getPendingServiceName() {
        return pendingRecord == null ? null : directory.getServiceName(pendingRecord.getServiceCode());
    }

    public BigDecimal getPendingFee() {
        return pendingRecord == null ? null : pendingRecord.getFee();
    }

    /** Rejecting confirmation discards the staged record. */
    public boolean confirmService(boolean isConfirmed) {
        if (pendingRecord == null) {
            return fail("There is no pending service to confirm.");
        }
        if (!isConfirmed) {
            clearPending();
            return fail("Service was not confirmed; nothing was saved.");
        }
        confirmed = true;
        lastError = "";
        return true;
    }

    /** Add optional comments to the staged record; also works after confirming. */
    public boolean addServiceComments(String comments) {
        if (pendingRecord == null) {
            return fail("There is no pending service record.");
        }
        if (!pendingRecord.setComments(comments)) {
            return fail("Comments must be at most 100 characters.");
        }
        lastError = "";
        return true;
    }

    /** Optional editing method for an already-saved record (if the team needs it). */
    public boolean addServiceComments(String recordId, String comments) {
        if (comments == null || comments.length() > 100) {
            return fail("Comments must be at most 100 characters.");
        }
        if (!repository.updateComments(recordId, comments)) {
            return fail("Record not found or comments could not be updated.");
        }
        lastError = "";
        return true;
    }

    /** Saves ONLY a confirmed record; the reporting team sees the same record. */
    public boolean saveServiceRecord() {
        if (pendingRecord == null || !confirmed) {
            return fail("Service must be recorded and confirmed before saving.");
        }
        // Capture the received timestamp when the confirmed record is actually saved.
        ServiceRecord finalRecord = new ServiceRecord(
                pendingRecord.getServiceDate(), LocalDateTime.now(clock),
                pendingRecord.getMemberId(), pendingRecord.getProviderId(),
                pendingRecord.getServiceCode(), pendingRecord.getFee());
        finalRecord.setComments(pendingRecord.getComments());
        if (!repository.saveServiceRecord(finalRecord)) {
            return fail("The service record could not be saved.");
        }
        lastSavedRecord = finalRecord;
        clearPending();
        lastError = "";
        return true;
    }

    public void cancelPendingService() {
        clearPending();
        lastError = "Service recording cancelled.";
    }

    public ServiceRecord getLastSavedRecord() { return lastSavedRecord; }
    public String getLastError() { return lastError; }

    private boolean fail(String message) {
        lastError = message;
        return false;
    }

    private void clearPending() {
        pendingRecord = null;
        confirmed = false;
    }
}
