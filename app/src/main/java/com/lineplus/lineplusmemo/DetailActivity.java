package com.lineplus.lineplusmemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lineplus.lineplusmemo.adapter.RecyclerViewImageAdapter;
import com.lineplus.lineplusmemo.implement.IInternalDataServiceImpl;
import com.lineplus.lineplusmemo.manager.NoteDataManager;
import com.lineplus.lineplusmemo.model.NoteData;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener, IInternalDataServiceImpl
{
	Intent intent;
	private TextView TextView_Date;
	private TextView TextView_TItle;
	private TextView TextView_Content;
	private ImageButton button_toolbar;
	private ImageButton button_remove;
	private ImageButton button_edit;
	private ScrollView layout_scroll_container;

	// 이미지 리스트 뷰
	private RecyclerView.Adapter mAdapter;
	private RecyclerView.LayoutManager layoutManager;
	private RecyclerView recycler_view_list_image;

	private NoteData data;

	//region 앱 생명 주기 함수
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		intent = getIntent();
		data = (NoteData)intent.getSerializableExtra("note");

		layout_scroll_container = (ScrollView)findViewById(R.id.layout_scroll_container);
		TextView_Date = (TextView)findViewById(R.id.TextView_Date);
		TextView_TItle = (TextView)findViewById(R.id.TextView_Title);
		TextView_Content = (TextView)findViewById(R.id.TextView_Content);
		TextView_Date.setText(data.getDate());
		TextView_TItle.setText(data.getTitle());
		TextView_Content.setText(data.getContent());
		button_toolbar = (ImageButton)findViewById(R.id.button_toolbar);
		button_toolbar.setOnClickListener(this);
		button_remove = (ImageButton)findViewById(R.id.button_remove);
		button_remove.setOnClickListener(this);
		button_edit = (ImageButton)findViewById(R.id.button_edit);
		button_edit.setOnClickListener(this);

		layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
		recycler_view_list_image = (RecyclerView)findViewById(R.id.recycler_view_list_image);
		recycler_view_list_image.setHasFixedSize(true);
		recycler_view_list_image.setLayoutManager(layoutManager);
		if(data.getImageURL().size()>0){
			getRecyclerViewImageData();
		} else {
			recycler_view_list_image.setVisibility(View.GONE);
			ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) layout_scroll_container.getLayoutParams();
			layoutParams.setMargins(layoutParams.leftMargin,layoutParams.topMargin,layoutParams.rightMargin,(int)(this.getResources().getDisplayMetrics().density * 50));
			layout_scroll_container.setLayoutParams(layoutParams);
		}

	}
	//endregion

	//region 이벤트 리스너
	@Override
	public void onClick(View v)
	{
		switch (v.getId()){
			// 툴바 뒤로가기 버튼
			case R.id.button_toolbar:
				Intent intent = new Intent(DetailActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
				break;
			// 삭제 버튼
			case R.id.button_remove:
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
				alertDialogBuilder.setTitle(getString(R.string.dialog_title_delete_note));
				alertDialogBuilder.setMessage(getString(R.string.dialog_alert_delete_note));
				alertDialogBuilder.setCancelable(true);
				alertDialogBuilder.setPositiveButton(getString(R.string.result_yes), new DialogInterface.OnClickListener()
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
				alertDialogBuilder.setNegativeButton(getString(R.string.result_no), new DialogInterface.OnClickListener()
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
			// 수정 버튼
			case R.id.button_edit:
				Intent intentToAdd = new Intent(DetailActivity.this, AddActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("note",data);
				intentToAdd.putExtras(bundle);
				startActivity(intentToAdd);
				finish();
				break;
			default:
				break;
		}
	}

	// 이미지 데이터를 리사이클러뷰에 연결
	void getRecyclerViewImageData(){
		mAdapter = new RecyclerViewImageAdapter(data.getImageURL(), DetailActivity.this, new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				int position = v.getTag() != null ? (int)v.getTag() : 0;
				Intent imageIntent = new Intent(DetailActivity.this, ImageActivity.class);
				imageIntent.putExtra("imageURL", data.getImageURL().get(position));
				startActivity(imageIntent);
			}
		});
		recycler_view_list_image.setAdapter(mAdapter);
	}

	//endregion

	//region 데이터 입출력 관리 함수
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
	//endregion
}
