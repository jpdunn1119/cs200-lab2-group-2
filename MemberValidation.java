/**
 * Author: Grayson Henneke (integration interface).
 * Connects service recording to the teammate's member-validation logic.
 * Expected result: "Validated", "Invalid number", or "Member suspended".
 */
public class MemberValidation {
    private MemberRepository repository;

    public MemberValidation(MemberRepository repository) {
        this.repository = repository;
    }

    public String validateMember(String memberId) {
        if (!MemberValidation.isValidNumber(memberId) || this.repository.findMember(memberId) == null) {
            return "Invalid number";
        }
        String status = this.repository.findStatus(memberId);
        if (Member.SUSPENDED.equals(status)) {
            return "Member suspended";
        }
        return "Validated";
    }

    public static boolean isValidNumber(String memberId) {
        return memberId != null && memberId.matches("\\d{9}");
    }
}
