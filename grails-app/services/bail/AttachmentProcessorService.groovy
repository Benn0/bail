package bail

import org.apache.commons.lang.StringUtils
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

class AttachmentProcessorService {

    public void processAttachments(Message message) {

        createAttachments(message)

        createPreviews(message)

    }

    private void createPreviews(Message message) {
        message.attachments.each {
            createPreview(it)
        }
    }

    private void createPreview(Attachment a) {
        if(a.mimeType.equalsIgnoreCase("application/pdf")) {
            PDDocument document = null
            try {
                document = PDDocument.load(new ByteArrayInputStream(a.content));
                List pages = document.getDocumentCatalog().getAllPages();

                PDPage page = (PDPage)pages.get(0);
                BufferedImage image = page.convertToImage(BufferedImage.TYPE_INT_RGB, 90);

                ByteArrayOutputStream out = new ByteArrayOutputStream()
                ImageIO.write(image, "png", out)

                a.preview = out.toByteArray()

                log.info("Successfully created preview image of pdf attachment [${a.name}]")
            } catch (Exception e) {
                log.error("Failed to create image from pdf!", e)
            } finally {
                if(document) document.close()
            }
        } else {
            log.info("Ignored create preview request for mimeType [${a.mimeType}]")
        }
    }

    private void createAttachments(Message message) {
        message.rawEmail.parts.findAll {
            it.binary != null
        }.each {
            Attachment a = new Attachment()
            a.name = getHeaderAttribute(it.contentType, "name")
            a.mimeType = StringUtils.substringBefore(it.contentType, ";")
            a.content = it.binary
            message.addToAttachments(a)
        }
    }

    private String getHeaderAttribute(String headerValue, String name) {
        for(String it : headerValue.split(";")) {
            String[] parts = it.trim().split("=")
            if(parts.length > 1 && parts[0].trim() == name) {
                String trimmed = parts[1].trim()
                trimmed = StringUtils.removeEnd(trimmed, "\"")
                trimmed = StringUtils.removeStart(trimmed, "\"")
                return trimmed
            }
        }
        return null
    }
}
