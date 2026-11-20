
// verifies manager login, after 3 wrong passwords, manager is locked out for 20 minutes

import java.util.ArrayList; 
import java.util.Collections; //protect lists from changes
import java.util.HashMap; //store info with an ID
import java.util.List; 
import java.util.Map; // connect IDs to information

//checks manager login and keeps track of failed attempts
public final class AuthenticationController {

    //number of wrong attempts allowed before lockout
    private static final int MAX_FAILURES = 3;

    //how many minutes manager is locked out
    private static final int LOCKOUT_MINUTES = 20;

    //where manager info and passwords are checked
    private final ManagerRepository repository;

    //keeps track of how long manager is locked out
    private final LockoutTimer lockoutTimer;

    //keeps track of how many times each manager entered wrong password, manager ID is connected to number of failed attempts
    private final Map<String, Integer> failedAttempts = new HashMap<>();

    //list of successful and failed login attempts (does not save actual passwords)
    private final List<String> verificationLog = new ArrayList<>();

    //remembers message from last login attempt
    private String lastMessage = "";


    //sets up manager records and lockout timer
    public AuthenticationController(ManagerRepository repository, LockoutTimer timer) {

        //if manager records or timer is missing program can't check login
        if (repository == null || timer == null)
            throw new IllegalArgumentException("repository and timer are required");

        //save manager records and timer so we can use them
        this.repository = repository;
        this.lockoutTimer = timer;
    }


    //sets up manager records with a normal timer
    public AuthenticationController(ManagerRepository repository) {

        //make new timer automatically
        this(repository, new LockoutTimer());
    }


    //check if manager entered correct ID and password
    //return true if login works false if it doesn't
    public boolean verifyManager(String managerId, String password) {

        //make sure manager ID has 9 digits and password was entered
        if (this.repository.findManager(managerId) == null || password == null) {

            //tell manager login info is wrong
            lastMessage = "invalid manager credentials";

            //if login did not work
            return false;
        }

        //check if manager's 20 minute lockout is over, if yes reset number of wrong attempts
        if (lockoutTimer.lockoutExpired(managerId))
            resetFailedAttempts(managerId);

        //check if manager is still locked out
        if (lockoutTimer.isLocked(managerId)) {

            //tell manager account is locked
            lastMessage = "manager login temporarily locked";

            //do not allow login while locked
            return false;
        }


        //find manager using their ID number, if manager doesn't exist give us null
        Manager manager = repository.findManager(managerId);

        //check if manager's password is correct
        boolean valid = checkCredentials(manager, password);

        //write down if login worked or failed
        logVerificationAttempt(managerId, valid);

        //if manager entered correct login info
        if (valid) {

            //erase prev wrong attempts
            resetFailedAttempts(managerId);

            //tell manager login worked
            lastMessage = "manager verified. access granted";

            // can log in
            return true;
        }

        //if login wrong increase failed attempt
        int attempts = incrementFailedAttempts(managerId);


        //check if manager entered wrong info 3 or more times
        if (attempts >= MAX_FAILURES) {

            //lock manager account for 20 minutes
            lockoutTimer.startLockout(managerId, LOCKOUT_MINUTES);

            //tell manager account is locked
            lastMessage = "manager login temporarily locked";

        } else {

            //if less than 3 wrong attempts manager can try again
            lastMessage = "invalid manager credentials, retry ok";
        }


        //login failed, so do not give access
        return false;
    }


    //checks manager's password using stored manager records
    public boolean checkCredentials(Manager managerRecord, String password) {

        //ask repository if ID and password match
        return repository.verifyCredentials(managerRecord, password);
    }


    //reset number of wrong login attempts for manager
    public void resetFailedAttempts(String managerId) {

        //remove manager's failed attempt count, next time count will start at 0
        failedAttempts.remove(managerId);
    }


    //increase every time manager enters wrong password
    public int incrementFailedAttempts(String managerId) {

        //get current failed count, or 0 if there isn't one
        int count = failedAttempts.getOrDefault(managerId, 0) + 1;

        //save new failed attempt count 
        failedAttempts.put(managerId, count);
       
        return count;
    }


    //writes down if manager login worked or failed
    public void logVerificationAttempt(String managerId, boolean success) {

        //save manager ID and whether login was success or failure
        verificationLog.add(managerId + ": " + (success ? "SUCCESS" : "FAILURE"));
    }


    //checks how many wrong attempts manager has made
    public int getFailedAttempts(String managerId) {

        //return number of failed attempts, or 0 if none
        return failedAttempts.getOrDefault(managerId, 0);
    }


    //lets us see list of manager login attempts
    public List<String> getVerificationLog() {

        //give back login list without letting other code change it
        return Collections.unmodifiableList(verificationLog);
    }


    //shows message from most recent login attempt
    public String getLastMessage() {
        return lastMessage;
    }
}
