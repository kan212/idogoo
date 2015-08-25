package com.idogoo.fragment;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.idogoo.R;

public abstract class BaseFragmentHasFooter extends Fragment {

	protected ListView mListView;
	protected ProgressBar mFooterProgressBar;
	protected TextView mFooterMessage;
	
	private View FooterLoadView;
	
	public View onCreateFooterView(LayoutInflater inflater) {
		 FooterLoadView = inflater.inflate(R.layout.load_footer, mListView, false);
		mFooterProgressBar = (ProgressBar) FooterLoadView.findViewById(R.id.foot_progressbar);
		mFooterMessage = (TextView) FooterLoadView.findViewById(R.id.foot_message);
		return FooterLoadView;
	}
	
	
	
}
