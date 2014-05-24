package jp.com.android.bkmker.adapters;

import jp.com.android.bkmker.R;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Browser;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class BookMarkListAdapter extends CursorAdapter {

	private LayoutInflater mInflater;

	public class ViewHolder {
		ImageView mBookMarkIcon;
		TextView mBookMarkTitle;
	}

	public BookMarkListAdapter(Context context, Cursor c,
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
		// https://groups.google.com/forum/#!msg/android-group-japan/r-P-tTUZ784/3F3jC2DakDEJ
		byte[] favicon = cursor.getBlob(cursor
				.getColumnIndexOrThrow(Browser.BookmarkColumns.FAVICON));
		
		holder.mBookMarkTitle.setText(title);
		// http://stackoverflow.com/questions/7620401/how-to-convert-byte-array-to-bitmap
		if (favicon != null) {
			Bitmap bitmap = BitmapFactory.decodeByteArray(favicon , 0, favicon.length);
			holder.mBookMarkIcon.setImageBitmap(bitmap);
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		final View view = mInflater.inflate(R.layout.bookmark_list_item, null);

		ViewHolder holder = new ViewHolder();
		holder.mBookMarkTitle = (TextView) view
				.findViewById(R.id.bookmark_title);
		holder.mBookMarkIcon = (ImageView) view
				.findViewById(R.id.bookmark_icon);

		view.setTag(holder);

		return view;
	}

}
