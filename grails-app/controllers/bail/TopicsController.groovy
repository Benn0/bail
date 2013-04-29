package bail

import grails.converters.JSON

class TopicsController {

    DaoService daoService

    GetEmailService getEmailService

    def index() {
        withFormat {
            html {
                render(view:"index", model: [ list: daoService.getAllTopics() ])
            }
            json {
                render daoService.allTopics.collect {
                    [
                            id : it.id,
                            title : it.title,
                            lastActivity : it.lastActivity,
                            participants : it.participants.join(", ")
                    ]
                } as JSON
            }
        }
    }

    def downloadEmails() {
        getEmailService.readTestfiles()

        redirect(action: "index")
    }

    def showTopic() {
        Topic topic = daoService.findTopicById(params["id"].toLong())
        withFormat {
            html {
                [topic : topic]
            }
            json {
                def json = [
                        id : topic.id,
                        title : topic.title,
                        emails : topic.emails.sort { it.sent }.collect {
                            [
                                    id : it.id,
                                    text : it.text,
                                    convertedFromHtml : it.convertedFromHtml,
                                    sender: (it.sender.contact.isMe ? "Ich" : it.sender.contact.name),
                                    date : it.sent
                            ]
                        }
                ]
                render json as JSON
            }
        }

    }
}
