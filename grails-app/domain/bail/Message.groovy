package bail

import org.joda.time.DateTime

class Message {

    String messageId = "";

    DateTime sent;

    EmailAddress sender;

    String subject = "";

    String text = "";

    String fullText= "";

    boolean convertedFromHtml

    String inReplyTo = "";

    RawEmail rawEmail

    static hasMany = [attachments : Attachment]

    static belongsTo = [topic: Topic]

    static constraints = {
        inReplyTo nullable: true
        messageId nullable: true
        sent nullable: true
    }

    static mapping = {
        topic index: true
        sender index: true
        rawEmail index: true
    }
}
