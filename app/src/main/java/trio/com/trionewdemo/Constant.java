package trio.com.trionewdemo;

import com.trio.nnpredict.TrioWrap.Trio;

public class Constant {
    public static final String SP_NAME = "custom_sp";
    public static final String LIST_DATA_KEY = "list_data";
    public static final String CARD_SHOW_TYPE_KEY = "show_type";
    public static final String PROGRESS_STYLE_KEY = "style";
    public static final String USERID_KEY = "user_id";
    public static final String APP_KEY = "app_key";
    public static final String SERVER_TYPE_KEY = "server_type";

    public static Trio.ProgressBarMode progressBarMode = Trio.ProgressBarMode.RADIATION;
    public static Trio.CardStyle cardStyle = Trio.CardStyle.DIALOG;

    public static final String Settings_TRIO_USERID = "trio_userId";
    public static final String Settings_TRIO_APPKEY = "trio_appKey";

    public static class CARD_SHOW_TYPE {
        public static final String FULL_SCREEN = "full_screen";
        public static final String FROM_BOTTOM = "from_bottom";
    }

    public static class PROGRESS_STYLE_TYPE {
        public static final String LINE = "line";
        public static final String CIRCLE = "circle";
        public static final String WAVE = "wave";
    }

    public static class SERVER_TYPE {
        public static final String TEST_SERVER = "test_server";
        public static final String ONLINE_SERVER = "online_server";
    }
}
