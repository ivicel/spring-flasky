package info.ivicel.springflasky.core.common;

public class Const {

    public static final String API_URI_PREFIX = "/api/v1";

    /**
     * default access denied page: 403, thymeleaf path base
     */
    public static final String DEFAULT_403_PAGE = "error/403";

    public static final String DEFAULT_404_PAGE = "error/404";

    public static String[] ALLOWED_HTML_TAG = {
            "b", "blockquote", "code", "del", "dd", "dl", "dt", "em",
            "h1", "h2", "h3", "i", "kbd", "li", "ol", "p",
            "pre", "s", "sup", "sub", "strong", "strike", "ul", "br", "hr"
    };
}
