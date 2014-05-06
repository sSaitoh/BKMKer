package jp.com.android.bkmker;

import android.os.Bundle;

public class MainActivity extends BKMKerBaseActivity {
	
	// Chrome, firefox, OperaのBookMarkも取得できるようにする
	// ChromeのContentProvider
	// http://stackoverflow.com/questions/14433480/add-bookmark-to-chrome-browser-on-android
	// TODO: Chromeのインストール済み判定方法の調査
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

}
