import java.util.HashMap;
import java.util.Map;

//keeps track of how long each manager is locked out after too many wrong passwords
public class LockoutTimer {

    //connects manager ID to the time their lockout ends (in milliseconds)
    private final Map<String, Long> lockoutUntil = new HashMap<>();


    //sets up an empty lockout timer
    public LockoutTimer() {
    }


    //starts a lockout for this manager for the given number of minutes
    public void startLockout(String managerId, int minutes) {

        //if manager ID is missing or minutes is not positive, do nothing
        if (managerId == null || minutes <= 0)
            return;

        //figure out when the lockout should end
        long endTime = System.currentTimeMillis() + (minutes * 60L * 1000L);

        //save the lockout end time for this manager
        lockoutUntil.put(managerId, endTime);
    }


    //checks if this manager is still locked out right now
    public boolean isLocked(String managerId) {

        //if manager ID is missing they are not locked
        if (managerId == null)
            return false;

        //get the time this manager's lockout ends, or null if not locked
        Long endTime = lockoutUntil.get(managerId);

        //if there is no lockout record, manager is not locked
        if (endTime == null)
            return false;

        //locked only if current time is still before the end time
        return System.currentTimeMillis() < endTime;
    }


    //checks if a lockout was active but has now finished
    //if yes, clears the lockout record so the manager can try again
    public boolean lockoutExpired(String managerId) {

        //if manager ID is missing there is no expired lockout
        if (managerId == null)
            return false;

        //get the time this manager's lockout ends, or null if not locked
        Long endTime = lockoutUntil.get(managerId);

        //if there is no lockout record, nothing has expired
        if (endTime == null)
            return false;

        //if lockout time is still in the future, it has not expired yet
        if (System.currentTimeMillis() < endTime)
            return false;

        //lockout time has passed, so remove the lockout record
        lockoutUntil.remove(managerId);

        //tell caller the lockout is over
        return true;
    }
}
