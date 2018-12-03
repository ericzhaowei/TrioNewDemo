package trio.com.trionewdemo;

import android.graphics.Color;
import android.graphics.Point;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.trio.nnpredict.Requests.RequestCallback;
import com.trio.nnpredict.TrioWrap.Model.NerResult;
import com.trio.nnpredict.TrioWrap.Trio;

import java.util.ArrayList;

import trio.com.trionewdemo.model.ListItemData;

public class MainListViewAdapter extends BaseAdapter {
    private ArrayList<ListItemData> mData;
    private MainActivity mainActivity;

    public static class ItemType {
        public static final int HEAD_TYPE = 0;
        public static final int CONTENT_TYPE = 1;
    }

    public MainListViewAdapter(MainActivity activity, ArrayList<ListItemData> data) {
        this.mData = data;
        this.mainActivity = activity;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).isHead ? ItemType.HEAD_TYPE : ItemType.CONTENT_TYPE;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ListItemData item = mData.get(position);
        ViewHolder viewHolder = null;

//        if (convertView == null) { // 出现左滑删除的条目在复用的时候有问题，暂停复用
            switch (getItemViewType(position)) {
                case ItemType.HEAD_TYPE:
                    view = LayoutInflater.from(mainActivity).inflate(R.layout.group_header_item, null);
                    viewHolder = new ViewHolder();
                    viewHolder.textView = view.findViewById(R.id.text_view);
                    view.setTag(viewHolder);

                    break;
                case ItemType.CONTENT_TYPE:
                    view = LayoutInflater.from(mainActivity).inflate(R.layout.list_view_item, null);
                    viewHolder = new ViewHolder();
                    viewHolder.textView = view.findViewById(R.id.text_view);
                    viewHolder.deleteTV = view.findViewById(R.id.tv_delete);
                    view.setTag(viewHolder);
                    break;
                default:
                    break;
            }
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//            view = convertView;
//        }

        viewHolder.textView.setText(item.content);

        if (item.isHead) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //
                }
            });
        } else {
            viewHolder.deleteTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainActivity.deleteItem(position);
                }
            });

            if (item.content.startsWith("http")) {
                SpannableString spannableString = new SpannableString(item.content);
                //设置下划线文字
                spannableString.setSpan(new UnderlineSpan(), 0, item.content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //设置文字的前景色
                spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 0, item.content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                viewHolder.textView.setText(spannableString);

                viewHolder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mainActivity.openBrowserActivity(item.content);
                    }
                });
            } else {
                // 这里不能使用view.setOnClickListener，无响应，需要使用viewHolder.textView.setOnClickListener
                // 估计是左滑删除功能导致的
                viewHolder.textView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Trio.with(mainActivity).showCard(true).UIType(Constant.uiType)
                                .requestNERInfo(item.content, new RequestCallback<NerResult>() {
                                    @Override
                                    public void onSuccess(NerResult nerResult) {
                                        Log.i("", "onSuccess: ");
                                    }

                                    @Override
                                    public void onError(String s) {
                                        Log.i("", "onError: ");
                                    }
                                });
                        return false;
                    }
                });

                viewHolder.textView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            Trio.with(mainActivity).cancelRequest();
                        }
                        return false;
                    }
                });
            }
        }

        return view;

    }

    public class ViewHolder {
        public TextView textView;
        public TextView deleteTV = null;
    }

}
