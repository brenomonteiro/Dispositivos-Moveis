package gabrielgrs.com.br.twittersearch.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import gabrielgrs.com.br.twittersearch.R;

/**
 * Created by gabrielgrs
 * Date: 17/09/2018
 * Time: 7:43 PM
 * Project: TwitterSearch
 */
public class MainAdapter extends BaseAdapter {

    Activity activity;
    private List<String> listTags;

    public MainAdapter(List<String> listTags, Activity activity) {
        this.listTags = listTags;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return listTags.size();
    }

    @Override
    public Object getItem(int i) {
        return listTags.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View inflate = activity.getLayoutInflater().inflate(R.layout.list_item, viewGroup, false);

        TextView tagName = inflate.findViewById(R.id.list_item_tag_textview);
        String tagNameItem = listTags.get(i);

        tagName.setText(tagNameItem);

        return inflate;
    }
}
