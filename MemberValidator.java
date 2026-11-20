public class MemberValidator {
    private MemberRepository records;
    
    public MemberValidator(MemberRepository records) {
        this.records = records;
    }
    public String validateMember(String memberId) {
        if (memberId == null || !memberId.matches("[0-9]{9}")) {
            return "Invalid Member ID";
        }
                
        String status = records.findStatus(memberId);
        if (status.equals("Active")) {
            return "Valid Member";
        } else if (status.equals("Suspended")) {
            return "Member Suspended";
        } else {
            return "Invalid Member ID";
        }
    }
}
