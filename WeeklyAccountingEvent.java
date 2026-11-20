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

    public WeeklyAccountingEvent(ServiceRecordRepository servicesRepo,
                                 MemberValidation memberValidation,
                                 ProviderDirectory providerDirectory,
                                 Function<String, String> providerNameLookup) {
        this.servicesRepo = servicesRepo;
        this.memberValidation = memberValidation;
        this.providerDirectory = providerDirectory;
        this.providerNameLookup = providerNameLookup;
    }

    /**
     * @return Result of the run operation.
     */
    public String run() {
        GenerateMemberReports generateMemberReports = new GenerateMemberReports(
                servicesRepo, memberValidation, providerDirectory, providerNameLookup);
        // generate for each member

        GenerateProviderReports generateProviderReports = new GenerateProviderReports(this.servicesRepo);
        // generate for each provider
        
        AccountsPayableSummary accountsPayableSummary = new AccountsPayableSummary(this.servicesRepo);
        LocalDate through = LocalDate.now();
        LocalDate from = through.minusDays(7);
        accountsPayableSummary.printSummary(from, through);

        CreateETFFile createETFFile = new CreateETFFile(servicesRepo, providerNameLookup);
        createETFFile.create();

        return "Weekly accounting event completed successfully";
    }
}
