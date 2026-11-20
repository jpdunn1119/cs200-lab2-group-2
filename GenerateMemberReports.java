import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Function;

/**
 * Author: Collin Cronin
 * 
 */
public class GenerateMemberReports {
    private static final DateTimeFormatter SERVICE_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MM-dd-yyyy");

    ServiceRecordRepository servicesRepo;
    MemberValidation memberValidation;
    ProviderDirectory providerDirectory;
    Function<String, String> providerNameLookup;
    MemberRepository membersRepo;

    public GenerateMemberReports(ServiceRecordRepository servicesRepo,
                                 MemberValidation memberValidation,
                                 ProviderDirectory providerDirectory,
                                 Function<String, String> providerNameLookup, MemberRepository membersRepo) {
        this.servicesRepo = servicesRepo;
        this.memberValidation = memberValidation;
        this.providerDirectory = providerDirectory;
        this.providerNameLookup = providerNameLookup;
        this.membersRepo = membersRepo;
    }

    private boolean verifyMember(String memberId) {
        String result = this.memberValidation.validateMember(memberId);
        return result.equals("Validated");
    }

    public ArrayList<ServiceRecord> getWeekServiceRecords(String memberId) {
        ArrayList<ServiceRecord> records = new ArrayList<>();
        LocalDate from = LocalDate.now().minusDays(7);

        for (ServiceRecord record : this.servicesRepo.getAllRecords()) {
            if (!record.getMemberId().equals(memberId)) continue;
            if (record.getServiceDate().isBefore(from)) continue;
            records.add(record);
        }

        return records;
    }

    private static String fit(String value, int maxLength) {
        if (value == null) {
            return "";
        }
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }

    private String formatReport(Member member, ArrayList<ServiceRecord> records) {
        StringBuilder report = new StringBuilder();

        report.append(fit(member.getName(), 25)).append('\n');
        report.append(fit(member.getId(), 9)).append('\n');
        report.append(fit(member.getAddress(), 25)).append('\n');
        report.append(fit(member.getCity(), 14)).append('\n');
        report.append(fit(member.getState(), 2)).append('\n');
        report.append(fit(member.getZip(), 5)).append('\n');

        for (ServiceRecord record : records) {
            String providerName = providerNameLookup.apply(record.getProviderId());
            String serviceName = providerDirectory.getServiceName(record.getServiceCode());

            report.append(record.getServiceDate().format(SERVICE_DATE_FORMAT)).append('\n');
            report.append(fit(providerName, 25)).append('\n');
            report.append(fit(serviceName, 20)).append('\n');
        }

        return report.toString();
    }

    /**
     * @return Result of the generate operation.
     */
    public String generate(String memberId) {
        // verify
        Member member = this.membersRepo.findMember(memberId);
        if (!this.verifyMember(memberId) || member == null) {
            return "Invalid member";
        }

        // get week's records
        ArrayList<ServiceRecord> records = this.getWeekServiceRecords(memberId);
        if (records.isEmpty()) {
            return "No services found for member this week";
        }

        // sort by ascending service date
        records.sort(Comparator.comparing(ServiceRecord::getServiceDate));

        String report = this.formatReport(member, records);

        return report;
    }

    public void printReport(String memberId) {
        String report = this.generate(memberId);
        System.out.println(report);
    }
}
