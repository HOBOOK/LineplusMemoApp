package com.lineplus.lineplusmemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.lineplus.lineplusmemo.manager.NoteDataManager;
import com.lineplus.lineplusmemo.model.NoteData;
import com.lineplus.lineplusmemo.module.IInternalDataServiceImpl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AddActivity extends AppCompatActivity implements View.OnClickListener, IInternalDataServiceImpl
{
	Intent intent;
	private NoteData data;
	private TextInputEditText text_edit_title;
	private TextInputEditText text_edit_content;
	private Button button_save;
	private ImageButton button_add_image;
	private ScrollView container;
	private LinearLayout layout_add_image;
	private RecyclerView recycler_view_list_image;
	private boolean isImageLayoutOn=false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		intent = getIntent();
		data = (NoteData)intent.getSerializableExtra("note");

		ImageButton button_toolbar = (ImageButton)findViewById(R.id.button_toolbar);
		button_toolbar.setOnClickListener(this);
		container = (ScrollView)findViewById(R.id.scroll_content);
		layout_add_image = (LinearLayout)findViewById(R.id.layout_add_image);
		recycler_view_list_image = (RecyclerView)findViewById(R.id.recycler_view_list_image);
		recycler_view_list_image.setVisibility(View.GONE);
		text_edit_title = (TextInputEditText)findViewById(R.id.text_edit_title);
		text_edit_content = (TextInputEditText)findViewById(R.id.text_edit_content);
		button_save = (Button)findViewById(R.id.button_save);
		button_save.setOnClickListener(this);
		button_add_image = (ImageButton) findViewById(R.id.button_add_image);
		button_add_image.setOnClickListener(this);

		if(data!=null){
			text_edit_title.setText(data.getTitle());
			text_edit_content.setText(data.getContent());
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId()){
			case R.id.button_toolbar:
				Intent intent = new Intent(AddActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
				break;
			case R.id.button_save:
				saveNoteData();
				Intent intentToDetail = new Intent(AddActivity.this, DetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("note",data);
				intentToDetail.putExtras(bundle);
				startActivity(intentToDetail);
				break;
			case R.id.button_add_image:
				// 키보드창 숨김
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(text_edit_title.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(text_edit_content.getWindowToken(), 0);
				text_edit_title.clearFocus();
				text_edit_content.clearFocus();
				//이미지 추가창이 숨겨져 있을 때
				isImageLayoutOn = !isImageLayoutOn;
				// 콘텐트 레이아웃 마진바텀
				ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) container.getLayoutParams();
				layoutParams.setMargins(0,0,0,sizeOfImageLayout(isImageLayoutOn));
				container.setLayoutParams(layoutParams);
				// 바텀 레이아웃 사이즈
				layout_add_image.getLayoutParams().height = sizeOfImageLayout(isImageLayoutOn);
				if(isImageLayoutOn){
					recycler_view_list_image.setVisibility(View.VISIBLE);
				}else{
					recycler_view_list_image.setVisibility(View.GONE);
				}

				// 버튼 딜레이
				((ImageButton) findViewById(R.id.button_add_image)).setEnabled(false);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						((ImageButton) findViewById(R.id.button_add_image))
								.setEnabled(true);
					}
				}, 1000);
				break;
			default:
				break;
		}
	}

	int sizeOfImageLayout(boolean on){

		float dp = this.getResources().getDisplayMetrics().density;
		return on ? (int)(150 * dp) : (int)(50 * dp);
	}
	@Override
	public void saveNoteData()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
		if(data!=null){
			data.setDate(sdf.format(new Date()));
			data.setTitle(text_edit_title.getText().toString());
			data.setContent(text_edit_content.getText().toString());
			NoteDataManager.getInstance().setNote(data.getId(),data);
		}else{
			String id = String.valueOf(NoteDataManager.getInstance().getSequence());
			String date = sdf.format(new Date());
			String title = text_edit_title.getText().toString();
			String content = text_edit_content.getText().toString();
			ArrayList<String> imageURL = new ArrayList<String>();
			data = new NoteData(id,date,title,content,imageURL);
			NoteDataManager.getInstance().addNote(data);
		}
		String obj = NoteDataManager.getInstance().getNoteDataString();
		//내부저장소에 저장
		FileOutputStream fos;
		try
		{
			fos = openFileOutput("data.txt", Context.MODE_PRIVATE);
			fos.write(obj.getBytes());
			fos.close();
			Log.d("Data","데이터 저장 성공");

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void loadNoteData()
	{

	}
}
