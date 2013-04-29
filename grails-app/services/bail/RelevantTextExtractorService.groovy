package bail

import org.apache.commons.lang.StringUtils

class RelevantTextExtractorService {

    public void extract(Message message) {

        removeTextNoise(message)

        removeExcessiveLineBreaks(message)

    }

    private void removeExcessiveLineBreaks(Message email) {
        final int MAX = 3;
        String text = email.text
        String[] lines = StringUtils.splitByWholeSeparatorPreserveAllTokens(text.trim(), "\n");
        List<String> result = new ArrayList<>(lines.length);
        int blankLineCounter = 0;
        for(String line : lines) {
            if(StringUtils.isBlank(line)) {
                blankLineCounter++;
            } else {
                blankLineCounter = 0;
            }
            if(blankLineCounter < MAX) {
                result.add(line);
            }
        }
        email.text = result.join("\n")
    }

    private void removeTextNoise(Message email) {
        String[] lines = StringUtils.splitByWholeSeparatorPreserveAllTokens(email.text, "\n");
        List<String> result = []
        for(String line : lines) {
            if(isBeginningOfNoise(line.trim())) {
                // forget the rest of the message
                break;
            }
            result.add(line);
        }
        email.text = result.join("\n")
    }

    protected boolean isBeginningOfNoise(String line) {
        StringUtils.equalsIgnoreCase(line, "-----Original Message-----") ||
        StringUtils.equalsIgnoreCase(line, "-------- Original-Nachricht --------") ||
        line == "--" ||
        line =~ /^Am(\s)(.+)(\s)schrieb(\s)(.+):$/ ||
        line =~ /^On(\s)(.+),(\s)at(\s)(.+),(\s)(.+)(\s)wrote:$/
    }
}
