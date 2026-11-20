import java.time.LocalDate;

/**
 * One Acme membership status change (Active <-> Suspended).
 * Profile edits stay in MemberController; Acme only changes status.
 */
public class StatusUpdate {
    private final String memberId;
    private final LocalDate updateDate;
    private final String newStatus;

    public StatusUpdate(String memberId, LocalDate updateDate, String newStatus) {
        this.memberId = memberId;
        this.updateDate = updateDate;
        this.newStatus = newStatus;
    }

    public String getMemberId() { return memberId; }
    public LocalDate getUpdateDate() { return updateDate; }
    public String getNewStatus() { return newStatus; }
}
