package com.lineplus.lineplusmemo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.lineplus.lineplusmemo.manager.NoteDataManager;
import com.lineplus.lineplusmemo.model.NoteData;
import com.lineplus.lineplusmemo.module.IInternalDataServiceImpl;

import java.io.File;
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
	private ArrayList<String> imageData;
	private TextInputEditText text_edit_title;
	private TextInputEditText text_edit_content;
	private Button button_save;
	private ImageButton button_add_image;
	private ImageButton button_toolbar;
	private ScrollView container;
	private LinearLayout layout_add_image;

	// 이미지 선택 뷰
	private RecyclerView.Adapter mAdapter;
	private RecyclerView.LayoutManager layoutManager;
	private RecyclerView recycler_view_list_image;

	private boolean isImageLayoutOn=false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);

		intent = getIntent();
		data = (NoteData)intent.getSerializableExtra("note");

		button_toolbar = (ImageButton)findViewById(R.id.button_toolbar);
		button_toolbar.setOnClickListener(this);
		container = (ScrollView)findViewById(R.id.scroll_content);

		layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

		recycler_view_list_image = (RecyclerView)findViewById(R.id.recycler_view_list_image);
		recycler_view_list_image.setHasFixedSize(true);
		recycler_view_list_image.setLayoutManager(layoutManager);
		recycler_view_list_image.setVisibility(View.GONE);

		layout_add_image = (LinearLayout)findViewById(R.id.layout_add_image);
		text_edit_title = (TextInputEditText)findViewById(R.id.text_edit_title);
		text_edit_content = (TextInputEditText)findViewById(R.id.text_edit_content);
		button_save = (Button)findViewById(R.id.button_save);
		button_save.setOnClickListener(this);
		button_add_image = (ImageButton) findViewById(R.id.button_add_image);
		button_add_image.setOnClickListener(this);
		if(data!=null){
			text_edit_title.setText(data.getTitle());
			text_edit_content.setText(data.getContent());
			imageData = data.getImageURL();
		} else {
			imageData = new ArrayList<String>();
		}
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		getRecyclerViewImageData();
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
				hideKeyboard();
				//이미지 추가창이 숨겨져 있을 때
				isImageLayoutOn = !isImageLayoutOn;
				// 콘텐트 레이아웃 마진바텀
				ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) container.getLayoutParams();
				layoutParams.setMargins(0,0,0,sizeOfImageLayout(isImageLayoutOn));
				container.setLayoutParams(layoutParams);
				// 바텀 레이아웃 사이즈
				ChangeBounds changeBounds = new ChangeBounds();
				changeBounds.setStartDelay(300);
				changeBounds.setInterpolator(new AnticipateOvershootInterpolator());
				changeBounds.setDuration(1000);
				TransitionManager.beginDelayedTransition(layout_add_image,changeBounds);
				layout_add_image.getLayoutParams().height = sizeOfImageLayout(isImageLayoutOn);

				ChangeTransform changeTransform = new ChangeTransform();
				changeTransform.setStartDelay(1000);
				TransitionManager.beginDelayedTransition(recycler_view_list_image, changeTransform);
				recycler_view_list_image.postDelayed(new Runnable() {
					@Override
					public void run() {
						TransitionManager.beginDelayedTransition(recycler_view_list_image, new AutoTransition().setStartDelay(1000));
						if(isImageLayoutOn){
							recycler_view_list_image.setVisibility(View.VISIBLE);
						}else
						{
							recycler_view_list_image.setVisibility(View.GONE);
						}
					}
				}, 1000);
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

	void hideKeyboard()
	{
		text_edit_title.clearFocus();
		text_edit_content.clearFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(text_edit_title.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(text_edit_content.getWindowToken(), 0);
}

	// 이미지 데이터를 리사이클러뷰에 연결
	void getRecyclerViewImageData(){
		mAdapter = new RecyclerViewImageAdapter(imageData, AddActivity.this, new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				int position = v.getTag() != null ? (int)v.getTag() : 0;
				// 이미지 이벤트 추가
			}
		});
		recycler_view_list_image.setAdapter(mAdapter);

		ImageButton button_test = (ImageButton) findViewById(R.id.button_test);
		button_test.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showTakeImageDialog();
			}
		});
	}

	// 사진 가져오기 위한 퍼미션 체크
	private void checkPermission()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
				Log.d("Permission", "권한 설정 완료");
			} else {
				Log.d("Permission", "권한 설정 요청");
				ActivityCompat.requestPermissions(AddActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
			}
		}
	}

	// 사진 가져오는 방법 선택
	private static final int TAKE_FROM_GALLERY = 1;
	private static final int TAKE_FROM_CAMERA = 2;
	private static final int TAKE_FROM_URI = 3;
	private void showTakeImageDialog(){
		checkPermission();
		String[] takeImageTypes = {"앨범에서 가져오기", "카메라에서 가져오기", "외부 URL로 가져오기"};
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(AddActivity.this);
		alt_bld.setTitle("이미지 메모").setIcon(R.drawable.button_add_image_256x256);
		alt_bld.setSingleChoiceItems(takeImageTypes, -1, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				switch (which)
				{
					case 0:
						getImageFromAlbum();
						break;
					case 1:
						getImageFromCamera();
						break;
					case 2:
						getImageFromURL();
						break;
				}
			}
		});
		AlertDialog alert = alt_bld.create();
		alert.show();
	}

	// 앨범에서 이미지 가져오기
	void getImageFromAlbum()
	{
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
		startActivityForResult(intent, TAKE_FROM_GALLERY);
	}

	// 촬영한 사진에서 가져오기
	private File tempFile;
	void getImageFromCamera()
	{
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
			String imageFileName = "Linememo_" + timeStamp + "_";
			File storageDir = new File(Environment.getExternalStorageDirectory() + "/LineMemo/");
			if (!storageDir.exists()) storageDir.mkdirs();
			tempFile = File.createTempFile(imageFileName, ".jpg", storageDir);
			if (tempFile != null) {
				Uri photoUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", tempFile);
				intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
				startActivityForResult(intent, TAKE_FROM_CAMERA);
			}
		} catch (IOException e) {
			Toast.makeText(this, "이미지 처리 중 오류가 발생하였습니다. 다시 시도 해주세요.", Toast.LENGTH_SHORT).show();
			finish();
			e.printStackTrace();
		}
	}

	// 외부 이미지 URL에서 이미지 가져오기
	void getImageFromURL()
	{

	}

	// 사진 가져오기 콜백
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Cursor cursor = null;
		String path = "";
		if (requestCode == TAKE_FROM_GALLERY) {
			if(resultCode == RESULT_OK){
				Uri photoUri = data.getData();
				path = photoUri.toString();
				addImage(path);
			}
		}else if(requestCode == TAKE_FROM_CAMERA){
			if(resultCode == RESULT_OK){
				Uri photoUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", tempFile);
				path = photoUri.toString();
				addImage(path);
			}else{
				if(tempFile != null) {
					if (tempFile.exists()) {
						if (tempFile.delete()) {
							tempFile = null;
						}
					}
				}
			}
		}
	}
	void addImage(String path)
	{
		imageData.add(path);
		getImageFromAlbum();
	}

	@Override
	public void saveNoteData()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
		if(data!=null){
			data.setDate(sdf.format(new Date()));
			data.setTitle(text_edit_title.getText().toString());
			data.setContent(text_edit_content.getText().toString());
			data.setImageURL(imageData);
			NoteDataManager.getInstance().setNote(data.getId(),data);
		}else{
			String id = String.valueOf(NoteDataManager.getInstance().getSequence());
			String date = sdf.format(new Date());
			String title = text_edit_title.getText().toString();
			String content = text_edit_content.getText().toString();
			data = new NoteData(id,date,title,content,imageData);
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
