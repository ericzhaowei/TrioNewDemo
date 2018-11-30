package trio.com.trionewdemo;

import com.trio.nnpredict.TrioWrap.Trio;

public class Constant {
    public static final String SP_NAME = "custom_sp";
    public static final String LIST_DATA_KEY = "list_data";
    public static final String UI_TYPE_KEY = "ui_type";
    public static final String USERID_KEY = "user_id";
    public static final String APP_KEY = "app_key";
    public static final String SERVER_TYPE_KEY = "server_type";

    public static Trio.UIType uiType = Trio.UIType.UI_ONE;

    public static final String Settings_TRIO_USERID = "trio_userId";
    public static final String Settings_TRIO_APPKEY = "trio_appKey";

    public static class SERVER_TYPE {
        public static final String TEST_SERVER = "test_server";
        public static final String ONLINE_SERVER = "online_server";
    }
}
