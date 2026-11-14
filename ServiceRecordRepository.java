import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Grayson Henneke.
 * In-memory service record storage. Reports can query this same repository.
 * Persistence across program restarts is intentionally left to team integration.
 */
public class ServiceRecordRepository {
    private final List<ServiceRecord> records = new ArrayList<>();

    public boolean save(ServiceRecord record) {
        if (record == null || findRecord(record.getRecordId()) != null) {
            return false;
        }
        return records.add(record);
    }

    public boolean saveServiceRecord(ServiceRecord record) {
        return save(record);
    }

    public ServiceRecord findRecord(String recordId) {
        for (ServiceRecord record : records) {
            if (record.getRecordId().equals(recordId)) {
                return record;
            }
        }
        return null;
    }

    public boolean updateComments(String recordId, String comments) {
        ServiceRecord record = findRecord(recordId);
        return record != null && record.setComments(comments);
    }

    public List<ServiceRecord> getAllRecords() {
        return new ArrayList<>(records);
    }

    public List<ServiceRecord> getRecordsByMember(String memberId) {
        List<ServiceRecord> found = new ArrayList<>();
        for (ServiceRecord record : records) {
            if (record.getMemberId().equals(memberId)) {
                found.add(record);
            }
        }
        return found;
    }

    public List<ServiceRecord> getRecordsByProvider(String providerId) {
        List<ServiceRecord> found = new ArrayList<>();
        for (ServiceRecord record : records) {
            if (record.getProviderId().equals(providerId)) {
                found.add(record);
            }
        }
        return found;
    }

    /**
     * Inclusive date range based on when a service was entered into ChocAn,
     * which may differ from the day the provider actually delivered the service.
     */
    public List<ServiceRecord> getRecordsByWeek(LocalDate from, LocalDate through) {
        if (from == null || through == null || from.isAfter(through)) {
            throw new IllegalArgumentException("Provide a valid inclusive date range.");
        }
        List<ServiceRecord> found = new ArrayList<>();
        for (ServiceRecord record : records) {
            LocalDate enteredOn = record.getReceivedAt().toLocalDate();
            if (!enteredOn.isBefore(from) && !enteredOn.isAfter(through)) {
                found.add(record);
            }
        }
        return found;
    }
}
