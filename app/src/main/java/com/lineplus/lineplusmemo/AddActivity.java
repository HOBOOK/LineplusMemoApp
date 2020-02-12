package com.lineplus.lineplusmemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class AddActivity extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);

		ImageButton button_toolbar = (ImageButton)findViewById(R.id.button_toolbar);
		button_toolbar.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(AddActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}
