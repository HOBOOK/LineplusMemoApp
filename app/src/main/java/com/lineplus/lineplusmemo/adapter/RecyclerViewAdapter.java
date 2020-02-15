package com.lineplus.lineplusmemo.adapter;

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
import com.lineplus.lineplusmemo.R;
import com.lineplus.lineplusmemo.manager.NoteDataManager;
import com.lineplus.lineplusmemo.model.NoteData;

import java.util.ArrayList;

// MainActivity 메모 리스트 리사이클러뷰 어댑터 클래스
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>
{
	private ArrayList<NoteData> mDataset;
	private View.OnClickListener onClickListener;
	public class MyViewHolder extends RecyclerView.ViewHolder {
		public TextView TextView_Title;
		public TextView TextView_Content;
		public TextView TextView_Date;
		public SimpleDraweeView ImageView_Thumbnail; // 외부 라이브러리 사용 Fresco의 SimpleDraweeView
		public View view;
		public MyViewHolder(View v) {
			super(v);
			TextView_Title = v.findViewById(R.id.TextView_Title);
			TextView_Content = v.findViewById(R.id.TextView_Content);
			TextView_Date = v.findViewById(R.id.TextView_Date);
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
		String title = getLimitLengthText(mDataset.get(position).getTitle(),12);
		String content = getLimitLengthText(mDataset.get(position).getContent(),20);
		holder.TextView_Title.setText(title);
		holder.TextView_Content.setText(content);
		String date = NoteDataManager.getInstance().getNoteEditDate(mDataset.get(position));
		holder.TextView_Date.setText(date);
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

	// 제한된 글자 수만큼 텍스트 자르기
	private String getLimitLengthText(String text, int size)
	{
		byte[] titleByte = text.getBytes();
		if(titleByte.length>size){
			StringBuilder sb = new StringBuilder();
			int i = 0;
			int byteSize = 0;
			while(byteSize<size){
				sb.append(text.charAt(i));
				byteSize += Math.min(String.valueOf(text.charAt(i)).getBytes().length,2);
				i++;
			}
			return sb.toString() + "..";
		}
		return text;
	}
}
