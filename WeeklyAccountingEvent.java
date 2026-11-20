import java.util.function.Function;
import java.time.LocalDate;

/**
 * Author: Collin Cronin
 * Runs weekly accounting event and generates reports and files based on the previous week's services.
 */
public class WeeklyAccountingEvent {
    private final ServiceRecordRepository servicesRepo;
    private final MemberValidation memberValidation;
    private final ProviderDirectory providerDirectory;
    private final Function<String, String> providerNameLookup;
    private final MemberRepository membersRepo;
    private final ProviderRepository providersRepo;

    public WeeklyAccountingEvent(ServiceRecordRepository servicesRepo,
                                 MemberValidation memberValidation,
                                 ProviderDirectory providerDirectory,
                                 Function<String, String> providerNameLookup,
                                 MemberRepository membersRepo,
                                 ProviderRepository providersRepo) {
        this.servicesRepo = servicesRepo;
        this.memberValidation = memberValidation;
        this.providerDirectory = providerDirectory;
        this.providerNameLookup = providerNameLookup;
        this.membersRepo = membersRepo;
        this.providersRepo = providersRepo;
    }

    /**
     * @return Result of the run operation.
     */
    public String run() {
        LocalDate through = LocalDate.now();
        LocalDate from = through.minusDays(7);

        GenerateMemberReports generateMemberReports = new GenerateMemberReports(
                servicesRepo, memberValidation, providerDirectory, providerNameLookup, membersRepo);
        for (Member member : this.membersRepo.getAllMembers()) {
            generateMemberReports.printReport(member.getId());
        }

        GenerateProviderReports generateProviderReports = new GenerateProviderReports(this.servicesRepo);
        for (Provider provider : this.providersRepo.getAllProviders()) {
            generateProviderReports.printReport(provider.getId(), from, through);
        }
        
        AccountsPayableSummary accountsPayableSummary = new AccountsPayableSummary(this.servicesRepo);
        accountsPayableSummary.printSummary(from, through);

        CreateETFFile createETFFile = new CreateETFFile(servicesRepo, providerNameLookup);
        createETFFile.create();

        return "Weekly accounting event completed successfully";
    }
}
