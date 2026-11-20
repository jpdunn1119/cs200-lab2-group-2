
// apply acme membership updates each evening Acme provides account-status changes. 

import java.util.ArrayList; //lets us use and make lists
import java.util.List;


public final class AcmeUpdateController {
    // reuse the shared member repository. acme can change only status (ACTIVE/SUSPENDED), not normal member profile.
    private final MemberRepository repository;
    //remember what happened last time we check acme's list
    //0's mean 0 updates recieved, completed, failed respectively
    private UpdateSummary lastSummary = new UpdateSummary(0, 0, 0, new ArrayList<>());
    //message about member update (ie: member changed to active)
    private List<String> batchMessages = new ArrayList<>();

    
    public AcmeUpdateController(MemberRepository repository) {
        if (repository == null) throw new IllegalArgumentException("repository required");
        this.repository = repository;
    }

    //ask other method to check acme's list
    public boolean applyUpdates(List<StatusUpdate> updates) {
        //send list to main update method, return whatever answer it gives us
        return applyAcmeMembershipUpdates(updates);
    }

    
    public boolean applyAcmeMembershipUpdates(List<StatusUpdate> updates) {
        
        if (updates == null) {
            //empty list for error messages
            List<String> errors = new ArrayList<>();
            //write down what went wrong
            errors.add("no acme update batch supplied (null input)");
            lastSummary = new UpdateSummary(0, 0, 1, errors);
            //false mean process no succeed
            return false;
        }
       
       //checks new list 

       //makes new list so old msgs don't get mixed with new updates
        batchMessages = new ArrayList<>();

        //success vs failed updates counters
        int applied = 0;
        int failed = 0;

     //look at every update acme sent
        for (StatusUpdate update : updates) {
            
            //is update missing?
            if (update == null) {
                //if yes add to failed count
                failed++;

                //notify why update failed
                recordUpdateFailure(null, "null entry");
                continue;
            }
            
            //find member with id number
            Member member = repository.findMember(update.getMemberId());
            
            //can't find member
            if (member == null) {
                //increase fail count
                failed++;
                recordUpdateFailure(update, "not found");
                continue;
            }
            

            //make sure member num correct, update has date, new status is active or suspended
            if (!validateAcmeUpdate(update)) {
                failed++;
                recordUpdateFailure(update, "invalid status");
                continue;
            }

            //change member sttus 

            //if status not saved successfully
            if (!repository.setMemberStatus(update.getMemberId(), update.getNewStatus())) {
                failed++;
                //record save not working
                recordUpdateFailure(update, "save failed");
                continue;
            }


            //if member exists, update valid, and status saved 
            applied++;
            //write down which member was updated
            recordAppliedUpdate(update);
        }


        //if list is empty 
        if (updates.isEmpty()) batchMessages.add("no membership updates supplied");
        
        //make report
        lastSummary = new UpdateSummary(updates.size(), applied, failed, batchMessages);

        //return false if one or more update fail
        return failed == 0;
    }

 
    public boolean validateAcmeUpdate(StatusUpdate update) {

        MemberValidation memberValidator = new MemberValidation(this.repository);

        //check update exist
        return update != null
        //AND check nine dig member number
                && "Validated".equals(memberValidator.validateMember(update.getMemberId()))

            // AND check date was provided 
                && update.getUpdateDate() != null

                // AND check that status is one of the two allowed
                && (Member.ACTIVE.equals(update.getNewStatus())

                //or the member suspended
                    || Member.SUSPENDED.equals(update.getNewStatus()));
    }

   //turn into readable report
    public UpdateSummary getLastSummary() { return lastSummary; }
    public String summarizeUpdates() { return lastSummary.toString(); }

   //explains what went wrong
    public void recordUpdateFailure(StatusUpdate update, String reason) {
        //includes reason AND member ID if update exists, if not only add reason
        batchMessages.add(reason + (update == null ? "" : ": " + update.getMemberId()));
    }

 //successful update
    public void recordAppliedUpdate(StatusUpdate update) {
        if (update != null) {
            batchMessages.add("applied: " + update.getMemberId() + " -> " + update.getNewStatus());
        }
    }
}
