package bail

import org.apache.commons.lang.StringUtils

import javax.mail.MessagingException
import javax.mail.internet.InternetAddress

class MessageCreatorService {

    NormalizerService normalizerService;

    public Message create(RawEmail rawEmail) {
        Message message = new Message()
        message.rawEmail = rawEmail
        rawEmail.message = message

        message.subject = normalizerService.normalizeSubject(rawEmail.subject)

        message.sender = findAppropriateSender(rawEmail)

        message.sent = rawEmail.getHeaderDate("Date")

        message.messageId = rawEmail.getFirstHeader("Message-ID")

        message.inReplyTo = rawEmail.getFirstHeader("In-Reply-To")

        message.fullText = findFullText(message)
        message.text = message.fullText

        return message
    }

    private EmailAddress findAppropriateSender(RawEmail message) throws MessagingException {
        List<String> strings = []

        strings.addAll(message.getHeaders("From"))
        strings.addAll(message.getHeaders("Sender"))

        List<InternetAddress> addresses = []

        strings.each { String candidate ->
            try {
                InternetAddress[] address = InternetAddress.parseHeader(candidate, false);
                log.debug("Candidate $candidate is valid");
                addresses.addAll(address)
            } catch(Exception e) {
                log.info("Candidate $candidate is invalid!");
            }
        }

        InternetAddress address = addresses.find {
            StringUtils.isNotBlank(it.personal) && StringUtils.isNotBlank(it.address)
        }

        if( ! address) {
            address = addresses.find {
                StringUtils.isNotBlank(it.address)
            }
        }

        if(address) {
            return new EmailAddress(address)
        }
        return null
    }

    private String findFullText(Message message) {
        ContentPart part = message.rawEmail.parts.find {
            it.isText()
        }
        if(part) {
            return normalizerService.sanitizeHtml(part.content.replaceAll("\n\r", "<br/>"))
        }

        part = message.rawEmail.parts.find {
            it.isHtml()
        }
        if(part) {
            log.info("Converted html2text")
            message.convertedFromHtml = true
            return normalizerService.sanitizeHtml(part.content)
        }

        return "No text found!"
    }
}
