package com.lineplus.lineplusmemo.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lineplus.lineplusmemo.AddActivity;
import com.lineplus.lineplusmemo.R;

import java.util.ArrayList;

// AddActivity 이미지리스트 리사이클러뷰 어댑터 클래스
public class RecyclerViewImageAdapter extends RecyclerView.Adapter<RecyclerViewImageAdapter.MyViewHolder>
{
	private ArrayList<String> mDataset;
	private View.OnClickListener onClickListener;
	private Context context;
	public class MyViewHolder extends RecyclerView.ViewHolder {
		public SimpleDraweeView image_add; // 외부 라이브러리 사용 Fresco의 SimpleDraweeView
		public View view;
		public ImageButton button_remove_image;
		public MyViewHolder(View v) {
			super(v);
			view = v;
			image_add = (SimpleDraweeView)v.findViewById(R.id.image_added);
			button_remove_image = (ImageButton)v.findViewById(R.id.button_remove_image);

			if(context instanceof AddActivity){
				button_remove_image.setClickable(true);
				button_remove_image.setEnabled(true);
				button_remove_image.setVisibility(View.VISIBLE);
				button_remove_image.setOnClickListener(onClickListener);
			}
		}
	}

	public RecyclerViewImageAdapter(ArrayList<String> myDataset, Context context, View.OnClickListener onClick) {
		mDataset = myDataset;
		onClickListener = onClick;
		this.context = context;
		Fresco.initialize(context);
	}

	@Override
	public RecyclerViewImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
				.inflate(R.layout.slot_image, parent, false);
		MyViewHolder vh = new MyViewHolder(v);
		return vh;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		try{
			String path = mDataset.get(position);
			Uri uri = Uri.parse(path);
			holder.image_add.setImageURI(uri);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		holder.button_remove_image.setTag(position);
	}
	@Override
	public int getItemCount() {
		return mDataset.size();
	}


}
