import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory storage for member records.
 * Tracks membership status separately so Acme can suspend or reactivate members.
 */
public class MemberRepository {
    private final Map<String, Member> members = new HashMap<>();
    private final Map<String, String> statuses = new HashMap<>();

    public Member findMember(String memberId) {
        if (memberId == null) {
            return null;
        }
        return members.get(memberId);
    }

    public boolean insertMember(Member member) {
        if (member == null || member.getNumber() == null) {
            return false;
        }
        String id = member.getNumber();
        if (members.containsKey(id)) {
            return false;
        }
        members.put(id, member);
        statuses.put(id, "Active");
        return true;
    }

    public boolean updateMember(String memberId, Member member) {
        if (memberId == null || member == null || !members.containsKey(memberId)) {
            return false;
        }
        members.put(memberId, member);
        return true;
    }

    public boolean removeMember(String memberId) {
        if (memberId == null || !members.containsKey(memberId)) {
            return false;
        }
        members.remove(memberId);
        statuses.remove(memberId);
        return true;
    }

    public boolean setMemberStatus(String memberId, String newStatus) {
        if (memberId == null || newStatus == null || !members.containsKey(memberId)) {
            return false;
        }
        statuses.put(memberId, newStatus);
        return true;
    }

    public String findStatus(String memberId) {
        if (memberId == null || !members.containsKey(memberId)) {
            return "Invalid";
        }
        return statuses.getOrDefault(memberId, "Active");
    }

    public List<Member> getAllMembers() {
        return new ArrayList<>(members.values());
    }
}
