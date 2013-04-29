package bail

import org.joda.time.DateTime

class Topic {

    static hasMany = [emails: Message]

    String collectFromSender

    String title

    DateTime lastActivity

    Set<String> participants

    public void updateParticipants() {
        participants = emails.collect { it.sender.contact.name }
    }

    static embedded = ['participants']

    static constraints = {
        collectFromSender nullable: true
    }


}
