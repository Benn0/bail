package bail

import javax.mail.internet.InternetAddress

class EmailAddress {

    String name;

    String address;

    static belongsTo = [ contact : Contact ]

    public EmailAddress() {
    }

    public EmailAddress(InternetAddress internetAddress) {
        name = internetAddress.getPersonal();
        address = internetAddress.getAddress().trim().toLowerCase();
    }

    static constraints = {
        name nullable: true
    }

    static mapping = {
        contact index: true
    }
}
