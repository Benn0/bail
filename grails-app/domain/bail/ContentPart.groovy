package bail

import org.apache.commons.lang.StringUtils

class ContentPart {

    String contentType

    String content

    byte[] binary

    List<String> headers

    static constraints = {
    }

    static embedded = ['headers']

    public boolean isText() {
        StringUtils.startsWithIgnoreCase(contentType, "text/plain")
    }

    public boolean isHtml() {
        StringUtils.startsWithIgnoreCase(contentType, "text/html")
    }
}
