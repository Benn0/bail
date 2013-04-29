package bail

class DaoService {

    public Topic findTopicById(long id) {
        Topic.findById(id);
    }

    public Topic findTopicWithEmailMessageId(String id) {
        List<Message> emails = Message.findAllByMessageId(id);
        if(emails.size() > 0) {
            return emails.get(0).getTopic();
        }
        return null;
    }

    public int countTopics() {
        Topic.count()
    }

    public List<Topic> getAllTopics() {
        Topic.list(sort: "lastActivity", order: "desc")
    }

    public Topic newTopic(Message email) {
        Topic topic = new Topic();
        topic.setTitle(email.getSubject());
        topic.addToEmails(email);
    }

    public Topic findTopicWithSubject(String subject) {
        Topic.findByTitle(subject);
    }

    public Topic findTopicWhichCollectsFromSender(String sender) {
        Topic.findByCollectFromSender(sender);
    }
}
