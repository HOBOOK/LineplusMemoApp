package com.lineplus.lineplusmemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

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

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		intent = getIntent();
		data = (NoteData)intent.getSerializableExtra("note");

		ImageButton button_toolbar = (ImageButton)findViewById(R.id.button_toolbar);
		button_toolbar.setOnClickListener(this);
		text_edit_title = (TextInputEditText)findViewById(R.id.text_edit_title);
		text_edit_content = (TextInputEditText)findViewById(R.id.text_edit_content);
		button_save = (Button)findViewById(R.id.button_save);
		button_save.setOnClickListener(this);

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
			default:
				break;
		}
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
