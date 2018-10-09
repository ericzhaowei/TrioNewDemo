package trio.com.trionewdemo;

import android.content.Context;
import android.content.SharedPreferences;
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

import static trio.com.trionewdemo.Constant.SP_NAME;
import static trio.com.trionewdemo.Constant.PROGRESS_STYLE_KEY;

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
                final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.OnGestureListener() {
                    @Override
                    public boolean onDown(MotionEvent motionEvent) {
                        return false;
                    }

                    @Override
                    public void onShowPress(MotionEvent motionEvent) {

                    }

                    @Override
                    public boolean onSingleTapUp(MotionEvent motionEvent) {
                        return false;
                    }

                    @Override
                    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                        return false;
                    }

                    @Override
                    public void onLongPress(MotionEvent motionEvent) {
//                        if (motionEvent.getSize() > 0.2) {
                            Point p = new Point((int)motionEvent.getRawX(), (int)motionEvent.getRawY());

                            Trio.with(mainActivity).showCard(Constant.cardStyle).withProgress(Constant.progressBarMode, p)
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
//                        }
                    }

                    @Override
                    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                        return false;
                    }
                });

                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return gestureDetector.onTouchEvent(motionEvent);
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
