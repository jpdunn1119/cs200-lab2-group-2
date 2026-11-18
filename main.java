
package Source;

import java.util.Scanner;

public class main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        MemberRecordStub records = new MemberRecordStub();
        MemberValidator validator = new MemberValidator(records);

        ProviderDirectoryStub directory = new ProviderDirectoryStub();
        ServiceCodeValidator serviceValidator = new ServiceCodeValidator(directory);

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
                                break;
                            case 2:
                                System.out.println("Editing Member...");
                                break;
                            case 3:
                                System.out.println("Deleting Member...");
                                break;
                            case 4:
                                System.out.println("Adding Provider...");
                                break;
                            case 5:
                                System.out.println("Editing Provider...");
                                break;
                            case 6:
                                System.out.println("Deleting Provider...");
                                break;
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
                                System.out.println(validator.validateMember(memberId));
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

                            case 3:
                                System.out.println("Recording Service...");
                                break;

                            case 4:
                                System.out.println("Requesting Provider Directory...");
                                break;

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
                                break;
                            case 2:
                                System.out.println("Generating Summary Report...");
                                break;
                            case 3:
                                System.out.println("Generating Member Report...");
                                break;
                            case 4:
                                System.out.println("Generating Provider Report...");
                                break;
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
}
