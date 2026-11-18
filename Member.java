public class Member {
    private String name;
    private String number;
    private String address;
    private String city;
    private String state;
    private String zip;

    public Member(String name, String number, String address, String city, String state, String zip) {
        this.name = name;
        this.number = number;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public String getName() { return this.name; }
    public String getNumber() { return this.number; }
    public String getAddress() { return this.address; }
    public String getCity() { return this.city; }
    public String getState() { return this.state; }
    public String getZip() { return this.zip; }
}
