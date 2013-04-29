import bail.Contact
import bail.Topic

class BootStrap {

    def init = { servletContext ->

        // drop database on startup
        Topic.collection.getDB().dropDatabase()

        Contact c = new Contact()
        c.name = "David Benninger"
        c.isMe = true
        c.save()
    }
    def destroy = {
    }
}
