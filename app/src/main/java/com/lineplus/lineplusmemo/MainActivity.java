package com.lineplus.lineplusmemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.lineplus.lineplusmemo.manager.NoteDataManager;
import com.lineplus.lineplusmemo.model.NoteData;

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

		NoteDataManager.getInstance().loadNoteData();

		recyclerView = (RecyclerView)findViewById(R.id.note_list_recycler_view);
		recyclerView.setHasFixedSize(true);
		myDataset = NoteDataManager.getInstance().getNoteList();
		layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
		mAdapter = new RecyclerViewAdapter(myDataset);
		recyclerView.setAdapter(mAdapter);

		button_add_note = (FloatingActionButton)findViewById(R.id.button_add_note);
		button_add_note.setClickable(true);
		button_add_note.setEnabled(true);
		button_add_note.setOnClickListener(new ButtonNoteAddClickListener());
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
