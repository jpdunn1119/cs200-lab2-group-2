public class Manager {
    private String id;
    private String name;
    private String number;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String password;

    public Manager(String id, String name, String number, String address, String city, String state, String zip, String password) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.password = password;
    }

    public String getId() { return this.id; }
    public String getName() { return this.name; }
    public String getNumber() { return this.number; }
    public String getManagerNumber() { return this.number; }
    public String getAddress() { return this.address; }
    public String getCity() { return this.city; }
    public String getState() { return this.state; }
    public String getZip() { return this.zip; }
    public String getPassword() { return this.password; }
}
