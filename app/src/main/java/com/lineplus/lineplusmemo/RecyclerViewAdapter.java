package com.lineplus.lineplusmemo;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lineplus.lineplusmemo.model.NoteData;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>
{
	private ArrayList<NoteData> mDataset;
	private View.OnClickListener onClickListener;
	public class MyViewHolder extends RecyclerView.ViewHolder {
		public TextView TextView_Title;
		public TextView TextView_Content;
		public SimpleDraweeView ImageView_Thumbnail;
		public View view;
		public MyViewHolder(View v) {
			super(v);
			TextView_Title = v.findViewById(R.id.TextView_Title);
			TextView_Content = v.findViewById(R.id.TextView_Content);
			ImageView_Thumbnail = v.findViewById(R.id.ImageView_Thumbnail);
			view = v;
			v.setClickable(true);
			v.setEnabled(true);
			v.setOnClickListener(onClickListener);
		}
	}
	public NoteData getNoteData(int position){
		return mDataset != null ? mDataset.get(position) : null;
	}

	public RecyclerViewAdapter(ArrayList<NoteData> myDataset, Context context, View.OnClickListener onClick) {
		mDataset = myDataset;
		onClickListener = onClick;
		Fresco.initialize(context);
	}

	@Override
	public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
		LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
				.inflate(R.layout.slot_note, parent, false);
		MyViewHolder vh = new MyViewHolder(v);
		return vh;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		holder.TextView_Title.setText(mDataset.get(position).getTitle());
		holder.TextView_Content.setText(mDataset.get(position).getContent());
		try{
			String path = mDataset.get(position).getImageURL().get(0);
			Uri uri = Uri.parse(path);
			holder.ImageView_Thumbnail.setImageURI(uri);
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
