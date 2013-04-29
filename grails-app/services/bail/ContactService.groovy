package bail

import org.apache.commons.lang.StringUtils

class ContactService {

    public void linkToContact(Message message) {

        EmailAddress existing = EmailAddress.findByAddress(message.sender.address)
        if(existing) {
            message.sender = existing
            return
        }

        Contact contact = Contact.findByName(message.sender.name)
        if(contact) {
            contact.addToEmailAddresses(message.sender)
            return
        }

        contact = new Contact()
        contact.addToEmailAddresses(message.sender)

        if(StringUtils.isNotEmpty(message.sender.name)) {
            contact.name = message.sender.name
        } else {
            contact.name = message.sender.address
        }
    }


}
