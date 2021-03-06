package com.lineplus.lineplusmemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.facebook.drawee.view.SimpleDraweeView;

public class ImageActivity extends AppCompatActivity implements View.OnClickListener
{
	SimpleDraweeView image_select;
	ImageButton button_close;
	boolean isFullScreen;

	//region 앱 생명 주기 함수
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);

		Intent intent = getIntent();
		String url = intent.getStringExtra("imageURL");

		isFullScreen=true;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		image_select = (SimpleDraweeView)findViewById(R.id.image_select);
		image_select.setImageURI(Uri.parse(url));
		image_select.setOnClickListener(this);
		button_close = (ImageButton)findViewById(R.id.button_close);
		button_close.setOnClickListener(this);
		button_close.setVisibility(View.GONE);
	}
	//endregion

	//region 이벤트 리스너
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			// 액티비티 종료 버튼
			case R.id.button_close:
				finish();
				break;
			// 이미지 선택 버튼
			case R.id.image_select:
				isFullScreen = !isFullScreen;
				if(isFullScreen){
					getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
					getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
					button_close.setVisibility(View.GONE);
				} else {
					getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
					getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
					button_close.setVisibility(View.VISIBLE);
				}
				break;
			default:
				break;
		}
	}
	//endregion
}
