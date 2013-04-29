package bail

import org.joda.time.DateTime

import javax.mail.internet.MailDateFormat

class RawEmail {

    private static final MailDateFormat mailDateFormat = new MailDateFormat();

    String subject

    List<KeyValue> headers

    List<ContentPart> parts

    static belongsTo = [message: Message]

    static embedded = ['headers', 'parts']

    static constraints = {
        subject nullable: true
    }

    List<String> getHeaders(String name) {
        headers.findAll {
            it.key == name
        }*.value
    }

    String getFirstHeader(String name) {
        headers.find {
            it.key == name
        }?.value
    }

    DateTime getHeaderDate(String name) {
        KeyValue header = headers.find {
            it.key == name
        }
        if(header) {
            return new DateTime(mailDateFormat.parse(header.value))
        }
        return null
    }

    static mapping = {
        message index: true
    }
}
