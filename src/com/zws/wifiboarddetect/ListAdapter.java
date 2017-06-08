package com.zws.wifiboarddetect;

import java.util.List;
import com.zws.view.TitileLayout;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {
	private List<MyListItem> list;
	private Context context;
	public ListAdapter(Context context, List<MyListItem> list) {
		this.list = list;
		this.context = context;
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		TitileLayout viewHolder = null;
		if (convertView == null) {
			viewHolder = new TitileLayout(context, "item");
		} else {
			viewHolder = (TitileLayout) convertView;
		}
		MyListItem item = (MyListItem) list.get(position);
		viewHolder.setTitleName(item.ssid);
		viewHolder.setResultValue(item.level + "%");
		return viewHolder;
	}

	class ViewHolder {
		public TextView ssid;
		public TextView level;
	}
}
