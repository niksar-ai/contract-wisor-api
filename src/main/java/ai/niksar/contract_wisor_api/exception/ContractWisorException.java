package ai.niksar.contract_wisor_api.exception;

public class ContractWisorException extends RuntimeException {

    public static class E001 extends RuntimeException {
        public E001() {
            super("Contract not found.");
        }
    }
    public static class E002 extends RuntimeException {
        public E002() {
            super("User not found.");
        }
    }

    public static class E003 extends RuntimeException {
        public E003() {
            super("Role not found.");
        }
    }
    public static class E004 extends RuntimeException {
        public E004() {
            super("Menu not found.");
        }
    }
    public static class E005 extends RuntimeException {
        public E005() {
            super("Analysis not found.");
        }
    }
    public static class E006 extends RuntimeException {
        public E006() {
            super("Contract not found.");
        }
    }
    public static class E007 extends RuntimeException {
        public E007() {
            super("User not found.");
        }
    }

    public static class E008 extends RuntimeException {
        public E008() {
            super("Role not found.");
        }
    }
    public static class E009 extends RuntimeException {
        public E009() {
            super("Menu not found.");
        }
    }
    public static class E010 extends RuntimeException {
        public E010() {
            super("Analysis not found.");
        }
    }
    public static class E011 extends RuntimeException {
        public E011() {
            super("User is not active.");
        }
    }
    public static class E012 extends RuntimeException {
        public E012() {
            super("No user found for the role.");
        }
    }
    public static class E013 extends RuntimeException {
        public E013() {
            super("Role name is already in use.");
        }
    }
    public static class E014 extends RuntimeException {
        public E014() {
            super("Username is already in use.");
        }
    }
    public static class E015 extends RuntimeException {
        public E015() {
            super("User not found.");
        }
    }
    public static class E016 extends RuntimeException {
        public E016() {
            super("User not found.");
        }
    }
    public static class E017 extends RuntimeException {
        public E017() {
            super("Invalid email format");
        }
    }
    public static class E018 extends RuntimeException {
        public E018() {
            super("User is blocked. The user cannot perform this action.");
        }
    }
    public static class E019 extends RuntimeException {
        public E019() {
            super("Invalid contract relationship type");
        }
    }
    public static class E020 extends RuntimeException {
        public E020() {
            super("The requested item was not found.");
        }
    }
    public static class E021 extends RuntimeException {
        public E021() {
            super("There is an active workflow started for this document");
        }
    }
    public static class E022 extends RuntimeException {
        public E022() {
            super("Role Menu not found.");
        }
    }
    public static class E023 extends RuntimeException {
        public E023() {
            super("Contract type not found.");
        }
    }
    public static class E024 extends RuntimeException {
        public E024() {
            super("FTP Server not found.");
        }
    }
    public static class E025 extends RuntimeException {
        public E025() {
            super("A contract cannot be added as related to itself.");
        }
    }
    public static class E026 extends RuntimeException {
        public E026() {
            super("Invalid email format.");
        }
    }
    public static class E027 extends RuntimeException {
        public E027() {
            super("Your current password is incorrect.");
        }
    }
    public static class E028 extends RuntimeException {
        public E028() {
            super("You do not have permission to perform the requested operation.");
        }
    }
    public static class E029 extends RuntimeException {
        public E029() {
            super("Url not found.");
        }
    }
    public static class E030 extends RuntimeException {
        public E030() {
            super("Record not found.");
        }
    }
    public static class E031 extends RuntimeException {
        public E031() {
            super("The validity period has expired.");
        }
    }
    public static class E032 extends RuntimeException {
        public E032() {
            super("There is no active record.");
        }
    }
    public static class E033 extends RuntimeException {
        public E033() {
            super("Email address is required.");
        }
    }
    public static class E034 extends RuntimeException {
        public E034() {
            super("An error occurred while sending the email. Please try again later.");
        }
    }
    public static class E035 extends RuntimeException {
        public E035() {
            super("Email address is already in use.");
        }
    }
    public static class InvalidRequestField extends RuntimeException {
        public InvalidRequestField(String message) {
            super(message);
        }
    }
    public static class CustomException extends RuntimeException {
        public CustomException(String message) {
            super(message);
        }
    }

}