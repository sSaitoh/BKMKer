package jp.com.android.bkmker.fragments;

import jp.com.android.bkmker.MainActivity;
import jp.com.android.bkmker.R;
import jp.com.android.bkmker.adapters.HistoryListAdapter;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Browser;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class HistoryListFragment extends ListFragment implements
		LoaderCallbacks<Cursor> {

	private MainActivity mActivity;
	private HistoryListAdapter mHistoryAdapter;

	private static final String[] sProjection = new String[] {
			Browser.BookmarkColumns.BOOKMARK, Browser.BookmarkColumns.CREATED,
			Browser.BookmarkColumns.DATE, Browser.BookmarkColumns.FAVICON,
			Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL,
			Browser.BookmarkColumns.VISITS };

	public static HistoryListFragment newInstance(String title) {
		HistoryListFragment fragment = new HistoryListFragment();

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
		mHistoryAdapter = new HistoryListAdapter(
				mActivity.getApplicationContext(), null, true);
		setListAdapter(mHistoryAdapter);
		mHistoryAdapter.notifyDataSetChanged();
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
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// make selection
		StringBuilder sb = new StringBuilder();
		sb.append(Browser.BookmarkColumns.BOOKMARK).append(" =?");
		String selection = sb.toString();

		// make selectionArgs
		// 0 = history, 1 = bookmark
		String[] selectionArgs = new String[] { "0" };

		return new CursorLoader(this.getActivity(), Browser.BOOKMARKS_URI,
				sProjection, selection, selectionArgs, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mHistoryAdapter.swapCursor(cursor);
		mHistoryAdapter.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mHistoryAdapter.swapCursor(null);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		Cursor c = (Cursor) getListView().getItemAtPosition(position);
		String title = c.getString(c
				.getColumnIndexOrThrow(Browser.BookmarkColumns.TITLE));
		String url = c.getString(c
				.getColumnIndexOrThrow(Browser.BookmarkColumns.URL));

		Browser.saveBookmark(getActivity(), title, url);
		super.onListItemClick(l, v, position, id);
	}

}
