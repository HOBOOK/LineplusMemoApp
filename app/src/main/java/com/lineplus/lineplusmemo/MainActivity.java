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

import com.lineplus.lineplusmemo.adapter.RecyclerViewAdapter;
import com.lineplus.lineplusmemo.implement.IInternalDataServiceImpl;
import com.lineplus.lineplusmemo.manager.NoteDataManager;
import com.lineplus.lineplusmemo.model.NoteData;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IInternalDataServiceImpl
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

		recyclerView = (RecyclerView)findViewById(R.id.recycler_view_list_note);
		recyclerView.setHasFixedSize(true);
		layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
		button_add_note = (FloatingActionButton)findViewById(R.id.button_add_note);
		button_add_note.setClickable(true);
		button_add_note.setEnabled(true);
		button_add_note.setOnClickListener(new ButtonNoteAddClickListener());
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		loadNoteData();
		NoteDataManager.getInstance().sortNoteData(0); // 날짜순 정렬
		myDataset = NoteDataManager.getInstance().getNoteList();
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
	}

	@Override
	protected void onDestroy()
	{
		saveNoteData();
		super.onDestroy();
	}

	long first_time;
	long second_time;
	@Override
	public void onBackPressed()
	{
		second_time = System.currentTimeMillis();
		Toast.makeText(MainActivity.this, "한번 더 뒤로가기 버튼을 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
		if(second_time - first_time < 2000){
			saveNoteData();
			finishAffinity();
			System.runFinalization();
			System.exit(0);
		}
		first_time = System.currentTimeMillis();
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
		// 내부저장소에서 데이터 불러오기
		StringBuffer buffer = new StringBuffer();
		String dataString = "";
		FileInputStream fis;
		try {
			fis = openFileInput("data.txt");
			BufferedReader iReader = new BufferedReader(new InputStreamReader((fis)));
			dataString = iReader.readLine();
			Log.d("Data","읽는중.. > " + dataString);
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
			startActivity(intent);
		}
	}
}
