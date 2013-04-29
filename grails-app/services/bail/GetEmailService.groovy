package bail

import com.sun.mail.util.BASE64DecoderStream
import org.apache.commons.io.IOUtils
import org.apache.commons.lang.StringUtils

import javax.mail.BodyPart
import javax.mail.Folder
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.Store
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

class GetEmailService {

    EmailProcessorService emailProcessorService

    MessageCreatorService messageCreatorService

    public RawEmail read(String filename) throws Exception {
        FileInputStream stream = new FileInputStream(filename)
        Session session = Session.getDefaultInstance(new Properties(), null)
        MimeMessage message = new MimeMessage(session, stream)
        return read(message);
    }

    public synchronized void readTestfiles() {
        File testdir = new File("/home/benno/bemail/testdata");
        File[] files = testdir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.endsWith(".eml");
            }
        });
        log.info("reading ${files.length} test e-mails...");
        for(File file : files) {
            try {
                RawEmail email = read(file.getAbsolutePath())

                bail.Message message = messageCreatorService.create(email)

                emailProcessorService.processNewEmail(message);
            } catch (Exception e) {
                log.error("failed to read file ${file.name}", e)
            }
        }
    }

    public void downloadPop3() {
        Properties props = new Properties();

        String host = "pop.gmail.com";
        String username = "davidbenninger32@gmail.com";
        String password = "4hgmdrqp";
        String provider = "pop3";

        String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        Properties pop3Props = new Properties();

        pop3Props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
        pop3Props.setProperty("mail.pop3.socketFactory.fallback", "false");
        pop3Props.setProperty("mail.pop3.port",  "995");
        pop3Props.setProperty("mail.pop3.socketFactory.port", "995");

        try {
            Session session = Session.getDefaultInstance(pop3Props, null);
            Store store = session.getStore(provider);
            log.info("connecting to $host");
            store.connect(host, username, password);

            Folder inbox = store.getFolder("INBOX");
            if (inbox == null) {
                throw new Exception("No INBOX");
            }
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();
            log.info("fetching ${messages.length} messages...");
            for (int i = 0; i < messages.length; i++) {
                if(messages[i] instanceof MimeMessage) {
                    try {
                        bail.Message wrap = read((MimeMessage) messages[i]);
                        emailProcessorService.processNewEmail(wrap);
                    } catch (Exception e) {
                        log.info("Error processing new e-mail.", e);
                    }
                }
            }
            inbox.close(false);
            store.close();
        } catch (Exception e) {
            log.info("Error downloading E-mails from POP3 account!", e);
        }
    }

    String removeQuotation(String text) {
        String[] lines = StringUtils.splitByWholeSeparatorPreserveAllTokens(text.trim(), "\n");
        boolean quote = false;
        for(int i = lines.length-1; i >= 0; i--) {
            if(lines[i].startsWith(">") || StringUtils.isBlank(lines[i])) {
                lines[i] = "";
                quote = true;
            } else if(quote) {
                if(lines[i].endsWith(":")) {
                    lines[i] = "";
                }
            } else {
                quote = false;
            }
        }
        text = StringUtils.join(lines, '\n');
        return text.trim();
    }

    RawEmail read(MimeMessage message) throws MessagingException, IOException {

        RawEmail rawEmail = new RawEmail()
        rawEmail.subject = message.subject
        rawEmail.headers = message.allHeaders.collect { new KeyValue(key: it.name, value: it.value) }
        rawEmail.parts = new ArrayList<>()

        collectContentParts(rawEmail.parts, message.content, message.contentType, [], 5)

        return rawEmail;
    }

    void collectContentParts(List<ContentPart> parts, Object content, String contentType, List<String> headers, int maxDepth) {
        if(maxDepth == 0) {
            log.info("maxDepth reached. E-Mail processing aborted!");
            return
        }

        ContentPart part = new ContentPart()
        part.contentType = contentType
        part.headers = headers

        if(content instanceof String) {
            part.content = content.toString()
            parts << part
        } else if(content instanceof MimeMultipart) {
            MimeMultipart multipart = (MimeMultipart)content;

            for(int i=0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                List<String> bodyPartHeaders = bodyPart.allHeaders.collect { it.name + ": " + it.value }
                collectContentParts(parts, bodyPart.content, bodyPart.contentType, bodyPartHeaders, maxDepth-1)
            }
        } else if(content instanceof BASE64DecoderStream) {
            BASE64DecoderStream stream = (BASE64DecoderStream)content
            part.binary = IOUtils.toByteArray(stream)
            parts << part
        } else {
            log.info("Ignore content part of type ${content.class.name}")
        }
    }

}
