
//maintains member records by adding updating or deleting members
//also checks member status and sends acme updates to the acme controller

import java.util.List; 

public final class MemberController {

    //stores and finds member records
    private final MemberRepository repository;

    //handles membership changes sent by acme
    private final AcmeUpdateController acmeController;

    //remembers message from last action
    private String lastMessage = "";


    //sets up member records and acme controller
    public MemberController(MemberRepository repository) {

        //if member records are missing, program can't work
        if (repository == null) {
            throw new IllegalArgumentException(
                    "a repository is required");
        }

        //save member records so we can use them
        this.repository = repository;

        //make acme controller use the same member records
        this.acmeController =
                new AcmeUpdateController(repository);
    }


    //gets message from last action
    public String getLastMessage() {
        return lastMessage;
    }

    //gets stored member records
    public MemberRepository getRepository() {
        return repository;
    }

    //gets controller that handles acme updates
    public AcmeUpdateController getAcmeController() {
        return acmeController;
    }


    //checks if member info is correct
    public boolean validateMemberData(Member data) {
        return RecordValidation.validMember(data);
    }


    //saves message explaining why member change failed
    public void rejectChange(String reason) {
        lastMessage =
                "the member change is rejected because: " + reason;
    }


    //adds updates deletes or cancels member changes
    public boolean maintainMemberRecords(
            String action,
            String id,
            Member data,
            boolean confirmed) {

        //if no action was given reject it
        if (action == null) {
            rejectChange("unknown action");
            return false;
        }

        //check which action was requested
        switch (action.trim().toUpperCase(
                java.util.Locale.ROOT)) {

            case "ADD":

                //make sure new member info is correct and status is active
                if (!validateMemberData(data)
                        || !Member.ACTIVE.equals(
                                data.getStatus())
                        || (id != null
                            && !id.isEmpty()
                            && !id.equals(
                                    data.getMemberNumber()))) {

                    //reject if member info is invalid
                    rejectChange("invalid data");
                    return false;
                }

                //check if member id is already used
                if (repository.findMember(
                        data.getMemberNumber()) != null) {

                    //can't add two members with same id
                    rejectChange("duplicate id");
                    return false;
                }

                //try to save new member
                if (!repository.insertMember(data)) {

                    //if saving doesn't work, reject change
                    rejectChange("wasn't saved");
                    return false;
                }

                //member successfully added
                lastMessage = "member added: "
                        + data.getMemberNumber();

                return true;


            case "UPDATE":

                //find member using their id
                Member old = repository.findMember(id);

                //if member doesn't exist can't update
                if (old == null) {
                    rejectChange("not found");
                    return false;
                }

                //make sure new info is correct, member id shld stay the same
                if (!validateMemberData(data)
                        || !old.getMemberNumber().equals(
                                data.getMemberNumber())) {

                    rejectChange("invalid data");
                    return false;
                }

                //change member info but keep original membership status, only acme can suspend or reactivate members
                Member replacement =
                        old.withProfilePreservingStatus(data);

                //try to save updated member info
                if (!repository.updateMember(
                        id, replacement)) {

                    //if saving fails reject change
                    rejectChange("wasn't saved");
                    return false;
                }

                //member info was updated successfully
                lastMessage = "member updated: " + id;
                return true;


            case "DELETE":

                //make sure member exists before deleting
                if (repository.findMember(id) == null) {
                    rejectChange("not found");
                    return false;
                }

                //if deletion wasn't confirmed, don't delete member
                if (!confirmed) {
                    rejectChange("cancelled");
                    return false;
                }

                //try to remove member from records
                if (!repository.removeMember(id)) {
                    rejectChange("not saved");
                    return false;
                }

                //member was deleted successfully
                lastMessage = "member deleted: " + id;
                return true;


            case "CANCEL":

                //don't change anything if action was cancelled
                rejectChange("cancelled");
                return false;


            default:

                //reject any action other than add update delete or cancel
                rejectChange("unknown action");
                return false;
        }
    }


    
    //doesn't allow deletion because confirmed is false
    public boolean maintainMember(
            String action, Member member) {

        //get member ID if member exists else use null
        String id = member == null
                ? null
                : member.getMemberNumber();

        //send info to main member records method
        return maintainMemberRecords(
                action, id, member, false);
    }


    //checks if member is active suspended or doesn't exist
    public String checkMemberStatus(Member member) {

        //if member doesn't exist number is invalid
        if (member == null) {
            return "invalid number";
        }

        // member is active allow services
        if (member.isActive()) {
            return "Validated";
        }

        //member isn't active show suspended
        return "member suspended";
    }


    //find member by id and check their status
    public String validateMember(String memberId) {

        //look up member and return their status
        return checkMemberStatus(
                repository.findMember(memberId));
    }


    //checks if member is allowed to receive services
    public boolean isValidMember(String memberId) {

        //returns true only if member status is validated
        return "Validated".equals(
                validateMember(memberId));
    }


    //sends acme's list of membership updates to acme controller
    public boolean applyAcmeMembershipUpdates(
            List<StatusUpdate> updates) {

        //acme controller checks and applies membership changes
        return acmeController
                .applyAcmeMembershipUpdates(updates);
    }
}
