package jp.com.android.bkmker.adapters;

import jp.com.android.bkmker.R;
import android.content.Context;
import android.database.Cursor;
import android.provider.Browser;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HistoryListAdapter extends CursorAdapter {
	
	private static final String TAG = "HistoryListAdapter";
	private LayoutInflater mInflater;

	public class ViewHolder {
		TextView mHistoryTitle;
		TextView mHistoryUrl;
		TextView mHistoryDate;
	}

	public HistoryListAdapter(Context context, Cursor c,
			int flagRegisterContentObserver) {
		super(context, c, flagRegisterContentObserver);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder = (ViewHolder) view.getTag();
		String title = cursor.getString(cursor
				.getColumnIndexOrThrow(Browser.BookmarkColumns.TITLE));
		String url = cursor.getString(cursor
				.getColumnIndexOrThrow(Browser.BookmarkColumns.URL));
		long date = cursor.getLong(cursor
				.getColumnIndexOrThrow(Browser.BookmarkColumns.DATE));

		holder.mHistoryTitle.setText(title);
		holder.mHistoryUrl.setText(url);
		holder.mHistoryDate.setText("2014/14/14");

		Log.i(TAG, "date : " + date );
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		final View view = mInflater.inflate(R.layout.history_list_item, null);
		ViewHolder holder = new ViewHolder();
		holder.mHistoryTitle = (TextView) view.findViewById(R.id.history_title);
		holder.mHistoryUrl = (TextView) view.findViewById(R.id.history_url);
		holder.mHistoryDate = (TextView) view.findViewById(R.id.history_date);
		view.setTag(holder);
		return view;
	}

}
