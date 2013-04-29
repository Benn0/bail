package bail

class EmailProcessorService {

    DaoService daoService;

    RelevantTextExtractorService relevantTextExtractorService

    ContactService contactService

    AttachmentProcessorService attachmentProcessorService

    public void processNewEmail(Message message) {

        log.info("Processing new e-mail with subject [${message.subject}]")

        contactService.linkToContact(message)

        Topic topic = attachToTopic(message);

        relevantTextExtractorService.extract(message)

        updateLastActivity(message)

        attachmentProcessorService.processAttachments(message)

        message.sender.contact.save()

        topic.updateParticipants()
        topic.save()
    }

    private void updateLastActivity(Message message) {
        if(message.topic.lastActivity?.compareTo(message.getSent()) < 0) {
            message.topic.lastActivity = message.getSent();
        }
    }

    private Topic attachToTopic(Message email) {
        Topic topic = null;
        if(email.getInReplyTo()) {
            topic = daoService.findTopicWithEmailMessageId(email.getInReplyTo());
        }
        if(topic == null && email.getSubject()) {
            topic = daoService.findTopicWithSubject(email.getSubject());
        }
        if(topic == null && email?.getSender()?.getAddress()) {
            topic = daoService.findTopicWhichCollectsFromSender(email.getSender().getAddress());
        }
        if(topic == null) {
            topic = daoService.newTopic(email);
        } else {
            topic.addToEmails(email);
        }
        return topic
    }
}
