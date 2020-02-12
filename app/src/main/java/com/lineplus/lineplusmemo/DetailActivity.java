package com.lineplus.lineplusmemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import com.lineplus.lineplusmemo.model.NoteData;

public class DetailActivity extends AppCompatActivity
{
	Intent intent;
	private TextView TextView_TItle;
	private TextView TextView_Content;
	private ImageButton button_toolbar;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		intent = getIntent();
		NoteData data = (NoteData)intent.getSerializableExtra("note");

		TextView_TItle = (TextView)findViewById(R.id.TextView_Title);
		TextView_Content = (TextView)findViewById(R.id.TextView_Content);
		TextView_TItle.setText(data.getTitle());
		TextView_Content.setText(data.getContent());
		button_toolbar = (ImageButton)findViewById(R.id.button_toolbar);
		button_toolbar.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(DetailActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}
