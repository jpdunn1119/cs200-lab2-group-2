import java.math.BigDecimal;
import java.util.Objects;

/**
 * Author: Grayson Henneke.
 * A single entry in the ChocAn Provider Directory.
 */
public class ServiceEntry {
    private final String serviceCode;
    private final String serviceName;
    private final BigDecimal fee;

    public ServiceEntry(String serviceCode, String serviceName, BigDecimal fee) {
        if (serviceCode == null || !serviceCode.matches("\\d{6}")) {
            throw new IllegalArgumentException("Service code must contain exactly 6 digits.");
        }
        if (serviceName == null || serviceName.isBlank() || serviceName.length() > 20) {
            throw new IllegalArgumentException("Service name must contain 1 to 20 characters.");
        }
        Objects.requireNonNull(fee, "Fee cannot be null.");
        if (fee.signum() < 0 || fee.compareTo(new BigDecimal("999.99")) > 0 || fee.scale() > 2) {
            throw new IllegalArgumentException("Fee must be between $0.00 and $999.99 with at most 2 decimal places.");
        }
        this.serviceCode = serviceCode;
        this.serviceName = serviceName;
        this.fee = fee.setScale(2);
    }

    public String getServiceCode() { return serviceCode; }
    public String getServiceName() { return serviceName; }
    public BigDecimal getFee() { return fee; }
}
