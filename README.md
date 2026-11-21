# ChocAn System — CS 200 Project 4

**University of Alabama — Fall 2026**  
**Course:** CS 200 — Software Engineering  
**Team:** Group 2  
**Team Members:** Collin, Justin, Grayson, Jacob, Sarah

## Project Overview

This project implements the Chocoholics Anonymous (ChocAn) information system using Java and object-oriented programming principles.

The application uses a command-line interface (CLI) that allows operators, providers, and managers to access different system functions. It supports member and provider management, service validation and recording, provider directory access, weekly accounting, and report generation.

## System Features

### Operator Functions
- Log in using a nine-digit operator ID.
- Add, edit, and delete member records.
- Add, edit, and delete provider records.
- Confirm deletions before removing records.

### Provider Functions
- Log in using a valid provider ID.
- Validate member numbers and membership status.
- Validate six-digit service codes.
- Record services provided to members.
- Confirm service details and add optional comments.
- Request and generate the Provider Directory.

### Manager Functions
- Log in using a manager ID and password.
- Run the weekly accounting process.
- Generate an accounts payable summary.
- Generate individual member reports.
- Generate individual provider reports.
- Create an electronic funds transfer (EFT) data file during weekly accounting.

## Technologies Used

- Java
- Java Development Kit (JDK) 21
- Object-oriented programming
- Git and GitHub
- UMLet for UML class diagrams
- Command-line interface

## How to Compile and Run

### Requirements

- Java JDK 21 or later
- Visual Studio Code or a terminal
- All Java source files from the GitHub repository

### Instructions for Windows / VS Code

1. Download or clone the GitHub repository.
2. Extract the downloaded ZIP file if necessary.
3. Open the extracted project in Visual Studio Code.
4. Open a terminal using **Terminal > New Terminal**.
5. Navigate to the folder containing `main.java` and the other `.java` files.

If the downloaded ZIP contains a second project folder, enter it using:

```powershell
cd .\cs200-lab2-group-2-main
```

6. Create the build directory:

```powershell
New-Item -ItemType Directory -Force build
```

7. Compile all Java source files using PowerShell:

```powershell
javac -d build (Get-ChildItem -Filter *.java | ForEach-Object { $_.FullName })
```

8. Run the ChocAn application:

```powershell
java -cp build main
```

### Alternative: Windows Command Prompt (CMD)

If using Command Prompt instead of PowerShell, run these commands from the folder containing the Java source files:

```cmd
mkdir build
javac -d build *.java
java -cp build main
```

If the `build` folder already exists, skip the `mkdir build` command.

### Expected Output

```text
===== CHOCAN SYSTEM =====
1. Operator Login
2. Provider Login
3. Manager Login
0. Exit
Enter Choice:
```

Enter the number corresponding to the desired operation and follow the prompts.

### Troubleshooting

**Error: `Invalid filename: *.java`**

This may occur when compiling with PowerShell. Use the PowerShell compilation command instead of `javac -d build *.java`:

```powershell
javac -d build (Get-ChildItem -Filter *.java | ForEach-Object { $_.FullName })
```

**Error: `no source files`**

This means the terminal is likely not in the folder containing the Java files.

To locate them, run:

```powershell
Get-ChildItem -Recurse -Filter *.java
```

Navigate into the folder containing `main.java` before compiling.

**Error: `Could not find or load main class main`**

Make sure compilation completed successfully before running the program.

Verify that `main.class` exists in the `build` directory.

The application must be launched with the lowercase class name:

```powershell
java -cp build main
```

### Additional Notes

- The Java classes use the default package.
- The entry-point class is named `main`.
- Compiled `.class` files are stored in the `build` directory.
- No external Java libraries or dependencies are required.

## Demonstration Accounts

The application initializes sample records for demonstration and testing.

| Role | ID | Password |
|---|---|---|
| Operator | Any nine-digit ID | Not required |
| Provider | 222222222 | Not required |
| Manager | 333333333 | demo123 |
| Sample Member | 111111111 | N/A |

### Sample Service Codes

| Service Code | Service | Fee |
|---|---|---|
| 598470 | Dietitian | $75.00 |
| 883948 | Aerobics Exercise | $30.00 |
| 123456 | Consultation | $50.00 |

These sample records are initialized when the application starts.

## Program Structure

The project is organized into Java classes with different responsibilities.

### Models

- `Member`
- `Provider`
- `Manager`
- `ServiceRecord`
- `ServiceEntry`
- `StatusUpdate`
- `UpdateSummary`

These classes represent the information stored and processed by the system.

### Repositories

- `MemberRepository`
- `ProviderRepository`
- `ManagerRepository`
- `ServiceRecordRepository`

Repositories manage the in-memory records used throughout the application.

### Controllers

- `MemberController`
- `ProviderController`
- `ServiceController`
- `DirectoryController`
- `AuthenticationController`
- `AcmeUpdateController`

Controllers coordinate operations such as maintaining records, authenticating users, handling service entries, and applying membership updates.

### Validation

- `MemberValidation`
- `MemberValidator`
- `ProviderValidation`
- `OperatorValidation`
- `ServiceCodeValidator`

These classes validate identification numbers, membership status, and service codes.

### Accounting and Reporting

- `WeeklyAccountingEvent`
- `GenerateMemberReports`
- `GenerateProviderReports`
- `AccountsPayableSummary`
- `CreateETFFile`

These classes process service records, calculate provider payments, generate reports, and produce EFT data.

### Supporting Classes

- `ProviderDirectory`
- `DirectoryFileWriter`
- `LockoutTimer`

These classes support provider directory generation and authentication lockout behavior.

### Main Entry Point

`main.java` contains the command-line interface and connects the application's components.

The classes work together to provide a modular, object-oriented implementation of the ChocAn system.

## Generated Output Files

The system can generate the following files:

- `ProviderDirectory.txt` — Contains an alphabetically sorted list of available services, service codes, and fees.
- `EFTData.txt` — Contains provider payment information generated during weekly accounting.

Accounting and report information is also displayed in the terminal.

## Data Storage

The application uses in-memory repositories to store members, providers, managers, and service records during execution.

Data entered during a session is not automatically preserved after the program exits. Sample records are initialized when the program starts.

## UML Class Diagram

The final UML class diagram is provided as `proj 4 uml.uxf`.

It documents the Java classes implemented in Project 4, including their attributes, methods, and relationships.

The diagram was created using UMLet and is included in the project submission.

## Team Contributions

This project was developed collaboratively by Group 2.

Team members implemented separate components and integrated them through shared Java classes and repositories.

Individual contributions and development history are documented through GitHub commits and the project demonstration.

## Project Demonstration

The project demonstration covers the application's primary functions, including:

- Role-specific menus and authentication
- Member and provider management
- Member and service code validation
- Service recording and confirmation
- Provider Directory generation
- Manager reporting
- Weekly accounting and EFT file generation
- Class interactions and team contributions

## Project Notes

- The system is a command-line application; no graphical user interface is required.
- The implementation uses Java classes, objects, controllers, repositories, and validation methods.
- The system supports basic error handling and user input validation.
- Membership information and service records are stored in memory during execution.
- The project was developed for the CS 200 Software Engineering course at the University of Alabama.
