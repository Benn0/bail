package bail

import grails.converters.JSON

class ContactController {

    static scaffold = true

    def allEmailAddresses() {

        def addresses = [];

        Contact.all.each { Contact c ->
            if( ! c.isMe) {
               c.emailAddresses.each {
                    String displayName = c.name + (it.address != c.name ? " <${it.address}>" : "")
                    addresses << [
                            value: it.address,
                            name: displayName.encodeAsHTML()
                    ]
                }
            }
        }

        render addresses as JSON;
    }

}
