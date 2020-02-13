package com.lineplus.lineplusmemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;


public class RecyclerViewImageAdapter extends RecyclerView.Adapter<RecyclerViewImageAdapter.MyViewHolder>
{
	private ArrayList<String> mDataset;
	private View.OnClickListener onClickListener;
	private Context context;
	public class MyViewHolder extends RecyclerView.ViewHolder {
		public ImageView image_add;
		public TextView text_path;
		public View view;
		public MyViewHolder(View v) {
			super(v);
			image_add = (ImageView)v.findViewById(R.id.image_added);
			text_path = v.findViewById(R.id.text_path);
			view = v;
			v.setClickable(true);
			v.setEnabled(true);
			v.setOnClickListener(onClickListener);
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
			holder.text_path.setText(path);
			BitmapFactory.Options options = new BitmapFactory.Options();
			Bitmap originalBm = BitmapFactory.decodeFile(path, options);
			holder.image_add.setImageBitmap(originalBm);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		holder.view.setTag(position);
	}
	@Override
	public int getItemCount() {
		return mDataset.size();
	}

}
