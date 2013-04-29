package bail

class AttachmentController {

    def download() {
        Attachment a = Attachment.get(params.id)
        if(a) {
            response.addHeader("Content-Disposition", "attachment; filename=\"${a.name}\"")
            response.setContentType(a.mimeType)
            response.outputStream << a.content
        } else {
            render "Attachment not found"
        }
    }

    def preview() {
        Attachment a = Attachment.get(params.id)
        if(a) {
            response.setContentType("image/png")
            response.outputStream << a.preview
        } else {
            render "Attachment not found"
        }
    }
}
