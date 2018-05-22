package love.wintrue.com.lovestaff.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiUtil {

    static Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    public static boolean hasEmoji(CharSequence source) {

        Matcher emojiMatcher = emoji.matcher(source);

        if (emojiMatcher.find()) {
            return true;
        }
        return false;
    }

}
