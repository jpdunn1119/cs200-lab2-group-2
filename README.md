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
- A command-line terminal or IDE such as Visual Studio Code

### Instructions

1. Download or clone the GitHub repository.
2. Open a terminal in the folder containing the Java source files.
3. Compile the Java files:

```bash
mkdir build
javac -d build *.java
```

4. Run the program:

```bash
java -cp build main
```

If a `build` directory already exists, skip the `mkdir build` command.

### Main Menu

```text
===== CHOCAN SYSTEM =====
1. Operator Login
2. Provider Login
3. Manager Login
0. Exit
```

Enter the number corresponding to the desired operation and follow the prompts.

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

The project is organized into Java classes with different responsibilities:

**Models:** `Member`, `Provider`, `Manager`, `ServiceRecord`, `ServiceEntry`, and `StatusUpdate`.

**Repositories:** `MemberRepository`, `ProviderRepository`, `ManagerRepository`, and `ServiceRecordRepository`.

**Controllers:** `MemberController`, `ProviderController`, `ServiceController`, `DirectoryController`, `AuthenticationController`, and `AcmeUpdateController`.

**Validation:** `MemberValidation`, `MemberValidator`, `ProviderValidation`, `OperatorValidation`, and `ServiceCodeValidator`.

**Accounting and Reporting:** `WeeklyAccountingEvent`, `GenerateMemberReports`, `GenerateProviderReports`, `AccountsPayableSummary`, and `CreateETFFile`.

**Main Entry Point:** `main.java`.

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

The final UML class diagram documents the implemented Java classes, including their attributes, methods, and relationships.

The diagram is included separately in the project submission as an editable UMLet (`.uxf`) file.

## Team Contributions

This project was developed collaboratively by Group 2. Team members implemented separate components and integrated them through shared Java classes and repositories.

Individual contributions and development history are documented through the team's GitHub commits.

## Project Demonstration

The project demonstration shows the application's primary functions, including:

- Role-specific menus and authentication
- Member and provider management
- Member and service code validation
- Service recording
- Provider Directory generation
- Manager reporting and weekly accounting

## Notes

- The system is a command-line application; no graphical user interface is required.
- The implementation uses Java classes, objects, controllers, repositories, and validation methods.
- The project was developed for the CS 200 Software Engineering course at the University of Alabama.
