/**
 * Author: Grayson Henneke (integration interface).
 * Connects service recording to the teammate's member-validation logic.
 * Expected result: "Validated", "Invalid number", or "Member suspended".
 */
@FunctionalInterface
public interface MemberValidation {
    String validateMember(String memberId);
}
