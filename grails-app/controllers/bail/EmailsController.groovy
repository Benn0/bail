package bail

import grails.converters.JSON
import org.apache.commons.io.IOUtils

class EmailsController {

    NormalizerService normalizerService

    def show() {
        Message email = Message.findById(params.id.toLong())

        withFormat {
            html {
                [headers : email.rawEmail.headers, parts : email.rawEmail.parts]
            }
            json {
                ContentPart html = email.rawEmail.parts.find { it.isHtml() }
                if(html) {
                    def json = [
                            id : email.id,
                            subject : email.subject,
                            from : email.sender.name + " <" + email.sender.address + ">",
                            sent : email.sent,
                            htmltext : normalizerService.safeHtml(html.content)
                    ]
                    render json as JSON
                }
            }
        }

    }
}
