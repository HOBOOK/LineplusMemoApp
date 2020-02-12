package com.lineplus.lineplusmemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lineplus.lineplusmemo.model.NoteData;
import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements ItemTouchHelperAdapter
{
	private ArrayList<NoteData> mDataset;
	public class MyViewHolder extends RecyclerView.ViewHolder implements
			ItemTouchHelperViewHolder {
		public TextView TextView_Title;
		public TextView TextView_Content;
		public ImageView ImageView_Thumbnail;
		public MyViewHolder(View v) {
			super(v);
			TextView_Title = v.findViewById(R.id.TextView_Title);
			TextView_Content = v.findViewById(R.id.TextView_Content);
			ImageView_Thumbnail = v.findViewWithTag(R.id.ImageView_Thumbnail);
		}
	}

	public RecyclerViewAdapter(ArrayList<NoteData> myDataset) {
		mDataset = myDataset;
	}

	@Override
	public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
	                                                 int viewType) {
		LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
				.inflate(R.layout.slot_note, parent, false);
		MyViewHolder vh = new MyViewHolder(v);
		return vh;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		holder.TextView_Title.setText(mDataset.get(position).getTitle());
		holder.TextView_Content.setText(mDataset.get(position).getContent());
	}

	@Override
	public int getItemCount() {
		return mDataset.size();
	}

}
