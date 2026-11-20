public class Member {
    public static final String ACTIVE = "Active";
    public static final String SUSPENDED = "Suspended";

    private String id;
    private String name;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String status;

    public Member(String id, String name, String address, String city, String state, String zip, String status) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.status = status;
    }

    public String getId() { return this.id; }
    public String getName() { return this.name; }
    public String getNumber() { return this.id; }
    public String getMemberNumber() { return this.id; }
    public String getAddress() { return this.address; }
    public String getCity() { return this.city; }
    public String getState() { return this.state; }
    public String getZip() { return this.zip; }
    public String getStatus() { return this.status; }

    public boolean isActive() {
        return ACTIVE.equals(this.status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Member withProfilePreservingStatus(Member data) {
        return new Member(
                this.id,
                data.name,
                data.address,
                data.city,
                data.state,
                data.zip,
                this.status);
    }
}
