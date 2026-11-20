
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.function.Function;

public class main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        MemberRepository membersRepository = new MemberRepository();
        ProviderRepository providersRepository = new ProviderRepository();
        ManagerRepository managersRepository = new ManagerRepository();
        ServiceRecordRepository serviceRecordRepository = new ServiceRecordRepository();

        seedMembers(membersRepository);
        seedProviders(providersRepository);

        ProviderDirectory providerDirectory = new ProviderDirectory();
        seedProviderDirectory(providerDirectory);

        MemberValidation membersValidator = new MemberValidation(membersRepository);
        ProviderValidation providersValidator = new ProviderValidation(providersRepository);
        ServiceCodeValidator serviceValidator = new ServiceCodeValidator(providerDirectory);
        OperatorValidation operatorValidator = new OperatorValidation();

        MemberController memberController = new MemberController(membersRepository);
        ProviderController providerController = new ProviderController(providersRepository);
        ServiceController serviceController = new ServiceController(
                membersValidator, providerDirectory, serviceRecordRepository);
        DirectoryController directoryController = new DirectoryController(
                providerDirectory, new DirectoryFileWriter(Path.of(".")));

        Function<String, String> providerNameLookup = providerId -> {
            Provider provider = providersRepository.findProvider(providerId);
            return provider == null ? "" : provider.getName();
        };
        WeeklyAccountingEvent weeklyAccounting = new WeeklyAccountingEvent(
                serviceRecordRepository, membersValidator, providerDirectory,
                providerNameLookup, membersRepository, providersRepository);
        GenerateMemberReports memberReports = new GenerateMemberReports(
                serviceRecordRepository, membersValidator, providerDirectory,
                providerNameLookup, membersRepository);
        GenerateProviderReports providerReports = new GenerateProviderReports(serviceRecordRepository);
        AccountsPayableSummary accountsPayableSummary =
                new AccountsPayableSummary(serviceRecordRepository);

        int choice = -1;

        // MAIN MENU
        while (choice != 0) {

            System.out.println("\n===== CHOCAN SYSTEM =====");
            System.out.println("1. Operator Login");
            System.out.println("2. Provider Login");
            System.out.println("3. Manager Login");
            System.out.println("0. Exit");
            System.out.print("Enter Choice: ");

            choice = input.nextInt();

            switch (choice) {

                // OPERATOR MENU
                case 1: {
                    System.out.print("Enter Operator ID: ");
                    String operatorId = input.next();


                    String operatorValidationResult = operatorValidator.verifyOperator(operatorId);
                    System.out.println(operatorValidationResult);
                    if (!operatorValidationResult.equals("Validated operator")) {
                        break;
                    }

                    int option = -1;

                    while (option != 0) {
                        System.out.println("\n===== OPERATOR MENU =====");
                        System.out.println("1. Add Member");
                        System.out.println("2. Edit Member");
                        System.out.println("3. Delete Member");
                        System.out.println("4. Add Provider");
                        System.out.println("5. Edit Provider");
                        System.out.println("6. Delete Provider");
                        System.out.println("0. Back");
                        System.out.print("Enter Choice: ");

                        option = input.nextInt();

                        switch (option) {
                            case 1:
                                System.out.println("Adding Member...");
                                System.out.print("Enter Member ID: ");
                                String memberId = input.next();
                                System.out.print("Enter Member Name: ");
                                String memberName = input.next();
                                System.out.print("Enter Member Address: ");
                                String memberAddress = input.next();
                                System.out.print("Enter Member City: ");
                                String memberCity = input.next();
                                System.out.print("Enter Member State: ");
                                String memberState = input.next();
                                System.out.print("Enter Member Zip: ");
                                String memberZip = input.next();
                                System.out.print("Enter Member Phone: ");
                                Member member = new Member(memberId, memberName, memberAddress, memberCity, memberState, memberZip, "Active");
                                memberController.maintainMember("ADD", member);
                                break;
                            case 2: {
                                System.out.println("Editing Member...");
                                System.out.print("Enter Member ID: ");
                                String editMemberId = input.next();
                                System.out.print("Enter Member Name: ");
                                String editMemberName = input.next();
                                System.out.print("Enter Member Address: ");
                                String editMemberAddress = input.next();
                                System.out.print("Enter Member City: ");
                                String editMemberCity = input.next();
                                System.out.print("Enter Member State: ");
                                String editMemberState = input.next();
                                System.out.print("Enter Member Zip: ");
                                String editMemberZip = input.next();
                                Member editMember = new Member(editMemberId, editMemberName, editMemberAddress, editMemberCity, editMemberState, editMemberZip, "Active");
                                memberController.maintainMember("UPDATE", editMember);
                                System.out.println(memberController.getLastMessage());
                                break;
                            }
                            case 3: {
                                System.out.println("Deleting Member...");
                                System.out.print("Enter Member ID: ");
                                String deleteMemberId = input.next();
                                System.out.print("Confirm deletion (y/n): ");
                                String deleteConfirm = input.next();
                                boolean confirmed = deleteConfirm.equalsIgnoreCase("y");
                                memberController.maintainMemberRecords("DELETE", deleteMemberId, null, confirmed);
                                System.out.println(memberController.getLastMessage());
                                break;
                            }
                            case 4: {
                                System.out.println("Adding Provider...");
                                System.out.print("Enter Provider Number: ");
                                String providerNumber = input.next();
                                System.out.print("Enter Provider Name: ");
                                String providerName = input.next();
                                System.out.print("Enter Provider Address: ");
                                String providerAddress = input.next();
                                System.out.print("Enter Provider City: ");
                                String providerCity = input.next();
                                System.out.print("Enter Provider State: ");
                                String providerState = input.next();
                                System.out.print("Enter Provider Zip: ");
                                String providerZip = input.next();
                                Provider provider = new Provider(providerNumber, providerName, providerNumber, providerAddress, providerCity, providerState, providerZip);
                                providerController.maintainProvider("ADD", provider);
                                System.out.println(providerController.getLastMessage());
                                break;
                            }
                            case 5: {
                                System.out.println("Editing Provider...");
                                System.out.print("Enter Provider Number: ");
                                String editProviderNumber = input.next();
                                System.out.print("Enter Provider Name: ");
                                String editProviderName = input.next();
                                System.out.print("Enter Provider Address: ");
                                String editProviderAddress = input.next();
                                System.out.print("Enter Provider City: ");
                                String editProviderCity = input.next();
                                System.out.print("Enter Provider State: ");
                                String editProviderState = input.next();
                                System.out.print("Enter Provider Zip: ");
                                String editProviderZip = input.next();
                                Provider editProvider = new Provider(editProviderNumber, editProviderName, editProviderNumber, editProviderAddress, editProviderCity, editProviderState, editProviderZip);
                                providerController.maintainProvider("UPDATE", editProvider);
                                System.out.println(providerController.getLastMessage());
                                break;
                            }
                            case 6: {
                                System.out.println("Deleting Provider...");
                                System.out.print("Enter Provider Number: ");
                                String deleteProviderNumber = input.next();
                                System.out.print("Confirm deletion (y/n): ");
                                String deleteProviderConfirm = input.next();
                                boolean providerConfirmed = deleteProviderConfirm.equalsIgnoreCase("y");
                                providerController.maintainProviderRecords("DELETE", deleteProviderNumber, null, providerConfirmed);
                                System.out.println(providerController.getLastMessage());
                                break;
                            }
                            case 0:
                                System.out.println("Going Back...");
                                break;
                            default:
                                System.out.println("Invalid choice.");
                        }
                    }
                    break;
                }

                // PROVIDER MENU
                case 2: {
                    System.out.print("Enter Provider ID: ");
                    String providerId = input.next();

                    int option = -1;

                    while (option != 0) {
                        System.out.println("\n===== PROVIDER MENU =====");
                        System.out.println("1. Validate Member");
                        System.out.println("2. Validate Service Code");
                        System.out.println("3. Record Service");
                        System.out.println("4. Request Provider Directory");
                        System.out.println("0. Back");
                        System.out.print("Enter Choice: ");

                        option = input.nextInt();

                        switch (option) {
                            case 1:
                                System.out.println("Validating Member...");
                                System.out.print("Enter Member ID: ");
                                String memberId = input.next();
                                System.out.println(membersValidator.validateMember(memberId));
                                break;

                            case 2:
                                System.out.println("Validating Service Code...");
                                System.out.print("Enter Service Code: ");
                                String serviceCode = input.next();

                                if (serviceValidator.validateServiceCode(serviceCode)) {
                                    System.out.println("Valid Service Code");
                                } else {
                                    System.out.println("Invalid Service Code");
                                }
                                break;

                            case 3: {
                                System.out.println("Recording Service...");
                                System.out.print("Enter Member Number: ");
                                String recordMemberId = input.next();
                                System.out.print("Enter Date of Service (YYYY-MM-DD): ");
                                String serviceDateText = input.next();
                                System.out.print("Enter Service Code: ");
                                String recordServiceCode = input.next();

                                LocalDate serviceDate;
                                try {
                                    serviceDate = LocalDate.parse(serviceDateText);
                                } catch (DateTimeParseException ex) {
                                    System.out.println("Invalid date format. Use YYYY-MM-DD.");
                                    break;
                                }

                                if (!serviceController.recordService(
                                        recordMemberId, providerId, serviceDate, recordServiceCode)) {
                                    System.out.println(serviceController.getLastError());
                                    break;
                                }

                                System.out.println("Service: " + serviceController.getPendingServiceName());
                                System.out.println("Fee: $" + serviceController.getPendingFee());
                                System.out.print("Confirm service (y/n): ");
                                String serviceConfirm = input.next();
                                if (!serviceController.confirmService(serviceConfirm.equalsIgnoreCase("y"))) {
                                    System.out.println(serviceController.getLastError());
                                    break;
                                }

                                System.out.print("Add comments? (y/n): ");
                                String addCommentsChoice = input.next();
                                if (addCommentsChoice.equalsIgnoreCase("y")) {
                                    input.nextLine();
                                    System.out.print("Enter comments (max 100 chars): ");
                                    String comments = input.nextLine();
                                    if (!serviceController.addServiceComments(comments)) {
                                        System.out.println(serviceController.getLastError());
                                        break;
                                    }
                                }

                                if (serviceController.saveServiceRecord()) {
                                    System.out.println("Service recorded successfully.");
                                    System.out.println("Record ID: "
                                            + serviceController.getLastSavedRecord().getRecordId());
                                } else {
                                    System.out.println(serviceController.getLastError());
                                }
                                break;
                            }

                            case 4: {
                                System.out.println("Requesting Provider Directory...");
                                System.out.println(directoryController.displayDirectory());
                                String directoryPath = directoryController.requestDirectory(providerId);
                                if (directoryPath == null) {
                                    System.out.println(directoryController.getLastError());
                                } else {
                                    System.out.println("Provider directory saved to: " + directoryPath);
                                }
                                break;
                            }

                            case 0:
                                System.out.println("Going Back...");
                                break;

                            default:
                                System.out.println("Invalid choice.");
                        }
                    }
                    break;
                }

                // MANAGER MENU
                case 3: {
                    System.out.print("Enter Manager ID: ");
                    String managerId = input.next();

                    int option = -1;

                    while (option != 0) {
                        System.out.println("\n===== MANAGER MENU =====");
                        System.out.println("1. Run Weekly Accounting");
                        System.out.println("2. Generate Summary Report");
                        System.out.println("3. Generate Member Report");
                        System.out.println("4. Generate Provider Report");
                        System.out.println("0. Back");
                        System.out.print("Enter Choice: ");

                        option = input.nextInt();

                        switch (option) {
                            case 1:
                                System.out.println("Running Weekly Accounting...");
                                System.out.println(weeklyAccounting.run());
                                break;
                            case 2: {
                                System.out.println("Generating Summary Report...");
                                LocalDate summaryThrough = LocalDate.now();
                                LocalDate summaryFrom = summaryThrough.minusDays(7);
                                accountsPayableSummary.printSummary(summaryFrom, summaryThrough);
                                break;
                            }
                            case 3: {
                                System.out.println("Generating Member Report...");
                                System.out.print("Enter Member Number: ");
                                String reportMemberId = input.next();
                                memberReports.printReport(reportMemberId);
                                break;
                            }
                            case 4: {
                                System.out.println("Generating Provider Report...");
                                System.out.print("Enter Provider Number: ");
                                String reportProviderId = input.next();
                                LocalDate providerThrough = LocalDate.now();
                                LocalDate providerFrom = providerThrough.minusDays(7);
                                try {
                                    providerReports.printReport(
                                            reportProviderId, providerFrom, providerThrough);
                                } catch (IllegalArgumentException ex) {
                                    System.out.println(ex.getMessage());
                                }
                                break;
                            }
                            case 0:
                                System.out.println("Going Back...");
                                break;
                            default:
                                System.out.println("Invalid choice.");
                        }
                    }
                    break;
                }

                case 0:
                    System.out.println("Exiting ChocAn...");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        input.close();
    }

    private static void seedMembers(MemberRepository repository) {
        repository.insertMember(new Member(
                "111111111",
                "Jane Doe",
                "123 Main St",
                "Tuscaloosa",
                "AL",
                "35405",
                Member.ACTIVE));
    }

    private static void seedProviders(ProviderRepository repository) {
        repository.insertProvider(new Provider(
                "222222222",
                "Dr. John Smith",
                "1",
                "456 Oak Ave",
                "Tuscaloosa",
                "AL",
                "35405"));
    }

    private static void seedProviderDirectory(ProviderDirectory directory) {
        directory.addService(new ServiceEntry("598470", "Dietitian", new BigDecimal("75.00")));
        directory.addService(new ServiceEntry("883948", "Aerobics Exercise", new BigDecimal("30.00")));
        directory.addService(new ServiceEntry("123456", "Consultation", new BigDecimal("50.00")));
    }
}
