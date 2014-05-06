package jp.com.android.bkmker.fragments;

import jp.com.android.bkmker.MainActivity;
import jp.com.android.bkmker.R;
import jp.com.android.bkmker.adapters.BookMarkListAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class BookMarkListFragment extends ListFragment implements
		LoaderCallbacks<Cursor>, OnItemLongClickListener {

	private MainActivity mActivity;
	private BookMarkListAdapter mBookMarkAdapter;

	private static final String[] sProjection = new String[] {
			Browser.BookmarkColumns.BOOKMARK, Browser.BookmarkColumns.CREATED,
			Browser.BookmarkColumns.DATE, Browser.BookmarkColumns.FAVICON,
			Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL,
			Browser.BookmarkColumns.VISITS };

	public static BookMarkListFragment newInstance(String title) {
		BookMarkListFragment fragment = new BookMarkListFragment();
		
		Bundle args = new Bundle();
		args.putString("title", title);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.bookmark_list_fragment,
				container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		mBookMarkAdapter = new BookMarkListAdapter(
				mActivity.getApplicationContext(), null, true);
		setListAdapter(mBookMarkAdapter);
		getListView().setOnItemLongClickListener(this);
		mBookMarkAdapter.notifyDataSetChanged();
		getLoaderManager().initLoader(0, null, this);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (MainActivity) activity;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		getLoaderManager().destroyLoader(0);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		// make selection
		StringBuilder sb = new StringBuilder();
		sb.append(Browser.BookmarkColumns.BOOKMARK).append(" =?");
		String selection = sb.toString();

		// make selectionArgs
		// 0 = history, 1 = bookmark
		String[] selectionArgs = new String[] { "1" };
		
		return new CursorLoader(this.getActivity(), Browser.BOOKMARKS_URI,
				sProjection, selection, selectionArgs, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mBookMarkAdapter.swapCursor(cursor);
		mBookMarkAdapter.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mBookMarkAdapter.swapCursor(null);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Cursor c = (Cursor) getListView().getItemAtPosition(position);
		String url = c.getString(c
				.getColumnIndexOrThrow(Browser.BookmarkColumns.URL));
		if (url != null && !url.equals("")) {
			Uri uri = Uri.parse(url);
			Intent i = new Intent(Intent.ACTION_VIEW, uri);
			i.setClassName("com.android.browser", 
			                "com.android.browser.BrowserActivity");
			startActivity(i);
		}
		super.onListItemClick(l, v, position, id);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View v, int position,
			long id) {
		final int pos = position;
		final CharSequence[] items = {"編集", "削除"};
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("ブックマークの操作");
		builder.setItems(items, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				String choiceItem = (String) items[item];
				if (choiceItem != null && !choiceItem.equals("")) {
					if (choiceItem.equals(items[0])) {
						editBookMark(pos);
					} else if (choiceItem.equals(items[1])) {
						deleteBookMark(pos);
					}
				}
			}
		}).show();
		return false;
	}
	
	private void editBookMark(int position) {
		Cursor c = (Cursor) getListView().getItemAtPosition(position);
		String title = c.getString(c
				.getColumnIndexOrThrow(Browser.BookmarkColumns.TITLE));
		String url = c.getString(c
				.getColumnIndexOrThrow(Browser.BookmarkColumns.URL));
		Browser.saveBookmark(getActivity(), title, url);
	}
	
	private void deleteBookMark(int position) {
		Cursor c = (Cursor) getListView().getItemAtPosition(position);
		int id = c.getInt(c.getColumnIndexOrThrow(Browser.BookmarkColumns._ID));
		
		final ContentResolver cr = getActivity().getContentResolver();
		String where = Browser.BookmarkColumns._ID + " =?";
		String[] selectionArgs = new String[] { String.valueOf(id) };
		cr.delete(Browser.BOOKMARKS_URI, where, selectionArgs);
		Toast.makeText(getActivity(), "ブックマークを削除しました", Toast.LENGTH_SHORT).show();
	}

}
