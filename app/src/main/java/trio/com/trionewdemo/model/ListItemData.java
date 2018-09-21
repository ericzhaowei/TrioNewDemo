package trio.com.trionewdemo.model;

import android.widget.ListView;

public class ListItemData extends BaseModel {
    public ListItemData(boolean isHead, String content) {
        this.isHead = isHead;
        this.content = content;
    }

    public boolean isHead;
    public String content;
}
