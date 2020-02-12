package com.lineplus.lineplusmemo;

import android.content.Intent;
import android.os.Bundle;
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
	private ArrayList<NoteData> myDataset;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		recyclerView = (RecyclerView)findViewById(R.id.note_list_recycler_view);
		recyclerView.setHasFixedSize(true);
		myDataset = NoteDataManager.getInstance().getNoteList();
		layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
		mAdapter = new RecyclerViewAdapter(myDataset);
		recyclerView.setAdapter(mAdapter);
		recyclerView.setClickable(true);
		recyclerView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(MainActivity.this, DetailActivity.class);
				Toast.makeText(getApplicationContext(),"클릭",Toast.LENGTH_SHORT).show();
				startActivity(intent);
			}
		});
	}
}
