package jp.com.android.bkmker.dialog;

import jp.com.android.bkmker.R;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public class BookMarkEditorDialogFragment extends DialogFragment {

	// TODO: 今のところダイアログ表示で落ちるので修正する
	
	private IDialogButtonCallback mCallback;
	private EditText mTitleEditor;
	private EditText mUrlEditor;

	public void setCallback(IDialogButtonCallback callback) {
		this.mCallback = callback;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		Dialog dialog = new Dialog(getActivity());

		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		dialog.setContentView(R.layout.custom_dialog);

		// 背景を透明にする
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));

		// OK ボタンのリスナ
		mTitleEditor = (EditText) dialog.findViewById(R.id.positive_button);
		mTitleEditor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCallback != null) {
					String title = mTitleEditor.getText().toString();
					String url = mUrlEditor.getText().toString();
					mCallback.onClickPositiveButton(v, title, url);
				}
				dismiss();
			}
		});
		// Close ボタンのリスナ
		mUrlEditor = (EditText) dialog.findViewById(R.id.negative_button);
		mUrlEditor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCallback != null) {
					mCallback.onClickNegativeButton(v);
				}
				dismiss();
			}
		});

		return dialog;
	}

	public void setTitleEditorText(String text) {
		if (mTitleEditor != null) {
			mTitleEditor.setText(text);
		}
	}

	public void setUrlEditorText(String text) {
		if (mUrlEditor != null) {
			mUrlEditor.setText(text);
		}
	}

	public interface IDialogButtonCallback {
		public void onClickPositiveButton(View v, String title, String url);

		public void onClickNegativeButton(View v);
	}

}
