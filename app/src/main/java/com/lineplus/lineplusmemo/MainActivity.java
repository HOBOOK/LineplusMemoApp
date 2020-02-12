package com.lineplus.lineplusmemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lineplus.lineplusmemo.manager.NoteDataManager;
import com.lineplus.lineplusmemo.model.NoteData;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
	private RecyclerView recyclerView;
	private RecyclerView.Adapter mAdapter;
	private RecyclerView.LayoutManager layoutManager;
	private FloatingActionButton button_add_note;
	private ArrayList<NoteData> myDataset;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		loadNoteData();
		recyclerView = (RecyclerView)findViewById(R.id.note_list_recycler_view);
		recyclerView.setHasFixedSize(true);
		myDataset = NoteDataManager.getInstance().getNoteList();
		layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
		mAdapter = new RecyclerViewAdapter(myDataset, MainActivity.this, new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				int position = v.getTag() != null ? (int)v.getTag() : 0;
				NoteData data = ((RecyclerViewAdapter)mAdapter).getNoteData(position);
				Intent intent = new Intent(MainActivity.this, DetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("note",data);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		recyclerView.setAdapter(mAdapter);

		button_add_note = (FloatingActionButton)findViewById(R.id.button_add_note);
		button_add_note.setClickable(true);
		button_add_note.setEnabled(true);
		button_add_note.setOnClickListener(new ButtonNoteAddClickListener());
	}

	@Override
	protected void onDestroy()
	{
		saveNoteData();
		super.onDestroy();
	}

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
	public void loadNoteData()
	{
		// 내부저장소에서 데이터 불러오기
		StringBuffer buffer = new StringBuffer();
		String dataString = "";
		FileInputStream fis;
		try {
			fis = openFileInput("data.txt");
			BufferedReader iReader = new BufferedReader(new InputStreamReader((fis)));
			dataString = iReader.readLine();
			Log.d("Data","읽는중.. > " + dataString);
//			while(dataString != null)
//			{
//				buffer.append(dataString);
//				dataString = iReader.readLine();
//			}
//			buffer.append("\n");
			iReader.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		NoteDataManager.getInstance().setNoteDataString(dataString);
	}

	// 새로운 메모 추가 버튼 이벤트 리스너
	class ButtonNoteAddClickListener implements View.OnClickListener{
		@Override
		public void onClick(View v){
			Intent intent = new Intent(MainActivity.this, AddActivity.class);
			Toast.makeText(getApplicationContext(),"클릭",Toast.LENGTH_SHORT).show();
			startActivity(intent);
		}
	}
}
