import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory storage for manager records.
 * Supports lookup and credential checks used by manager login.
 */
public class ManagerRepository {
    private final Map<String, Manager> managers = new HashMap<>();

    public Manager findManager(String managerId) {
        if (managerId == null) {
            return null;
        }
        return managers.get(managerId);
    }

    public boolean insertManager(Manager manager) {
        if (manager == null || manager.getNumber() == null) {
            return false;
        }
        String id = manager.getNumber();
        if (managers.containsKey(id)) {
            return false;
        }
        managers.put(id, manager);
        return true;
    }

    public boolean updateManager(String managerId, Manager manager) {
        if (managerId == null || manager == null || !managers.containsKey(managerId)) {
            return false;
        }
        managers.put(managerId, manager);
        return true;
    }

    public boolean removeManager(String managerId) {
        if (managerId == null || !managers.containsKey(managerId)) {
            return false;
        }
        managers.remove(managerId);
        return true;
    }

    public boolean verifyCredentials(Manager managerRecord, String password) {
        if (managerRecord == null || password == null) {
            return false;
        }
        return password.equals(managerRecord.getPassword());
    }

    public List<Manager> getAllManagers() {
        return new ArrayList<>(managers.values());
    }
}
