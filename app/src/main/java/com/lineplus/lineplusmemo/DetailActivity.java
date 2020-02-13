package com.lineplus.lineplusmemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lineplus.lineplusmemo.manager.NoteDataManager;
import com.lineplus.lineplusmemo.model.NoteData;
import com.lineplus.lineplusmemo.module.IInternalDataServiceImpl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener, IInternalDataServiceImpl
{
	Intent intent;
	private TextView TextView_TItle;
	private TextView TextView_Content;
	private ImageButton button_toolbar;
	private ImageButton button_remove;
	private ImageButton button_edit;
	private NoteData data;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		intent = getIntent();
		data = (NoteData)intent.getSerializableExtra("note");

		TextView_TItle = (TextView)findViewById(R.id.TextView_Title);
		TextView_Content = (TextView)findViewById(R.id.TextView_Content);
		TextView_TItle.setText(data.getTitle());
		TextView_Content.setText(data.getContent());
		button_toolbar = (ImageButton)findViewById(R.id.button_toolbar);
		button_toolbar.setOnClickListener(this);
		button_remove = (ImageButton)findViewById(R.id.button_remove);
		button_remove.setOnClickListener(this);
		button_edit = (ImageButton)findViewById(R.id.button_edit);
		button_edit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId()){
			case R.id.button_toolbar:
				Intent intent = new Intent(DetailActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
				break;
			case R.id.button_remove:
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
				alertDialogBuilder.setTitle("메모 삭제");
				alertDialogBuilder.setMessage("메모를 삭제하시겠습니까?");
				alertDialogBuilder.setCancelable(true);
				alertDialogBuilder.setPositiveButton("예", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								NoteDataManager.getInstance().removeNote(data.getId());
								saveNoteData();
								Intent intent = new Intent(DetailActivity.this, MainActivity.class);
								startActivity(intent);
								finish();
							}
						});
				alertDialogBuilder.setNegativeButton("아니오", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.cancel();
							}
						});
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
				break;
			case R.id.button_edit:
				Toast.makeText(this,"수정",Toast.LENGTH_SHORT);
				Intent intentToAdd = new Intent(DetailActivity.this, AddActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("note",data);
				intentToAdd.putExtras(bundle);
				startActivity(intentToAdd);
				break;
			default:
				break;
		}
	}

	@Override
	public void saveNoteData()
	{
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
