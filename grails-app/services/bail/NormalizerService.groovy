package bail

import org.apache.commons.lang.StringUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.safety.Whitelist

import java.util.regex.Pattern

class NormalizerService {

    private static final def NOISE_PATTERN = ~/(?i)^\s*(Re:|Aw:|Fwd:|Fw:)+/

    public String normalizeEmailAddress(String emailAddress) {
        emailAddress = StringUtils.defaultString(emailAddress);
        emailAddress = emailAddress.trim();
        emailAddress = emailAddress.toLowerCase();
        return emailAddress;
    }

    public String normalizeSubject(String subject) {
        if(StringUtils.isBlank(subject)) return "";

        def matcher = subject =~ NOISE_PATTERN
        String lessNoisy = matcher.replaceFirst("").trim()
        if(lessNoisy.length() < subject.length())
            return normalizeSubject(lessNoisy);

        return lessNoisy
    }

    public String sanitizeHtml(String html) {
        String clean = Jsoup.clean(html, Whitelist.basic())
        Document doc = Jsoup.parse(clean)
        setTargetBlank(doc)
        suppressExternalImages(doc)
        return doc.toString()
    }

    public String safeHtml(String html) {
        String clean = Jsoup.clean(html, Whitelist.relaxed())
        Document doc = Jsoup.parse(clean);

        setTargetBlank(doc)

        suppressExternalImages(doc)

        return doc.toString()
    }

    private void suppressExternalImages(Document doc) {
        doc.select("img[src]").each {
            String src = it.attr("src")
            if (!StringUtils.startsWithIgnoreCase(src.trim(), "cid")) {
                log.info("Suppressing img src [${src}]")
                it.attr("data-original-src", src)
                it.attr("src", "")
            }
        }
    }

    private void setTargetBlank(Document doc) {
        doc.select("a[href]").each {
            log.info("Setting target=_blank attribute on link [${it.attr('href')}]")
            it.attr("target", "_blank")
        }
    }
}
