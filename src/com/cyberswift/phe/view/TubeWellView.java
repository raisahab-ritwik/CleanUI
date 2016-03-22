package com.cyberswift.phe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyberswift.phe.R;
import com.cyberswift.phe.utility.SegmentedGroup;

public class TubeWellView extends RelativeLayout implements OnClickListener, OnCheckedChangeListener {

	private SegmentedGroup sg;
	private TextView tvHeader;
	private EditText etDescription;
	private LinearLayout llDesc;
	private String status = "";
	private String description = "";

	public TubeWellView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TubeWellView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {

		inflate(getContext(), R.layout.tube_well_view_without_photo, this);
		sg = (SegmentedGroup) findViewById(R.id.sg);
		tvHeader = (TextView) findViewById(R.id.tvHeader);

		etDescription = (EditText) findViewById(R.id.etDescription);
		llDesc = (LinearLayout) findViewById(R.id.llDesc);

		sg.setOnCheckedChangeListener(this);

	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch (checkedId) {

		case R.id.rb_Yes:

			if (llDesc.getVisibility() == View.VISIBLE)
				llDesc.setVisibility(View.GONE);
			status="true";
			break;
		case R.id.rb_No:

			if (llDesc.getVisibility() == GONE)
				llDesc.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}

	}

	public void setHeader(String header) {

		tvHeader.setText(header);
	}

}
