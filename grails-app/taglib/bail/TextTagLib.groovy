package bail

import org.h2.util.StringUtils

class TextTagLib {

    def text = { attrs ->
        out << StringUtils.replaceAll(attrs.value, "\r\n", "<br/>")
    }
}
