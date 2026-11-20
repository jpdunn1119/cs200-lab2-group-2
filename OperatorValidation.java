/**
 * Author: Collin Cronin
 * Verifies an operator's identity and returns authorization to access ChocAn system.
 */
public class OperatorValidation {
    public OperatorValidation() {}

    /**
     * @return Result "Validated" or "Invalid number"
     */
    public String verifyOperator(String operatorId) {
        boolean isValid = operatorId.matches("^[0-9]{9}$");
        if (isValid) return "Validated operator";
        else return "Invalid operator number. Must contain 9 digits.";
    }
}
