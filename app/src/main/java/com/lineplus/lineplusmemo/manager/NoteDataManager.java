package com.lineplus.lineplusmemo.manager;


import android.util.Log;

import com.lineplus.lineplusmemo.model.NoteData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

// 메모 데이터 관리 클래스 싱글톤 패턴
public class NoteDataManager
{
	private static NoteDataManager instance = null;
	public synchronized static NoteDataManager getInstance()
	{
		if(instance == null){
			instance = new NoteDataManager();
		}
		return instance;
	}
	private ArrayList<NoteData> data = new ArrayList<NoteData>();
	private int sequence; // 새로운 노트 추가시 id값을 주기 위한 변수
	public int getSequence(){
		return sequence;
	}

	// 데이터 정렬 함수 0:날짜순
	public void sortNoteData(int sortType){
		switch (sortType){
			case 0:
				Collections.sort(data, new Comparator<NoteData>()
				{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					@Override
					public int compare(NoteData o1, NoteData o2)
					{
						long comp = 0;
						try{
							comp = sdf.parse(o1.getDate()).getTime()-sdf.parse(o2.getDate()).getTime();
						}catch (ParseException e){
							e.printStackTrace();
						}
						return -(int)comp;
					}
				});
				break;
			default:
				break;
		}
	}
	// 노트데이터 전체 반환
	public ArrayList<NoteData> getNoteList()
	{
		return data;
	}
	// id와 일치하는 노트를 반환
	public NoteData getNote(String id)
	{
		for(NoteData n : data){
			if(n.getId().equals(id)){
				return n;
			}
		}
		return null;
	}
	// id와 일치하는 노트에 새로운 노트데이터로 수정
	public void setNote(String id, NoteData d)
	{
		for(int i = 0; i < data.size(); i++){
			if(data.get(i).getId().equals(id)){
				data.set(i,d);
			}
		}
	}
	// 새로운 노트데이터 추가
	public void addNote(NoteData d){
		data.add(d);
	}
	// 노트데이터 삭제
	public void removeNote(String id){
		for(int i = 0; i < data.size(); i++){
			if(data.get(i).getId().equals(id)){
				data.remove(i);
			}
		}
	}
	// 현재 시간으로 부터 작성일자를 구하는 함수
	public String getNoteEditDate(NoteData data)
	{
		String date ="";
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date nowDate = new Date();
			Date editDate = sdf.parse(data.getDate());
			long diffDate = nowDate.getTime() - editDate.getTime();

			Log.d("date", data.getDate()+" >>> " + diffDate);

			long calDateDays = diffDate / (24*60*60*1000);
			calDateDays = Math.abs(calDateDays);
			if(calDateDays==0){
				long calDateHours = diffDate / (60*60*1000);
				calDateHours = Math.abs(calDateHours);
				if(calDateHours==0){
					long calDateMins = diffDate / (60*1000);
					calDateMins = Math.abs(calDateMins);
					date = String.valueOf(calDateMins) + " 분전";
				}else{
					date = String.valueOf(calDateHours) + " 시간전";
				}
			}else{
				date = String.valueOf(calDateDays) + " 일전";
			}
		}catch (ParseException e){
			e.printStackTrace();
		}
		return date;
	}
	// 데이터 배열을 JSON 형식의 String으로 반환하는 함수
	public String getNoteDataString(){
		try
		{
			sequence = 0;
			JSONObject obj = new JSONObject();
			JSONArray jArray = new JSONArray();
			for (int i = 0; i < data.size(); i++)//배열
			{
				JSONObject sObject = new JSONObject();
				sequence = Math.max(sequence, Integer.parseInt(data.get(i).getId()));
				sObject.put("id", data.get(i).getId());
				sObject.put("date", data.get(i).getDate());
				sObject.put("title", data.get(i).getTitle());
				sObject.put("content", data.get(i).getContent());

				//이미지데이터
				JSONArray imgArray = new JSONArray();
				ArrayList<String> imageDatas = data.get(i).getImageURL();
				for (int j = 0; j < imageDatas.size(); j++)
				{
					JSONObject imgObject = new JSONObject();
					imgObject.put(String.valueOf(j), imageDatas.get(j));
					imgArray.put(imgObject);
				}
				sObject.put("image", imgArray);

				jArray.put(sObject);
			}
			obj.put("sequence", String.valueOf(sequence+1));
			obj.put("notes", jArray);//배열을 넣음
			return obj.toString();
		}catch (JSONException e){
			e.printStackTrace();
		}
		return "";
	}
	// JSON 형식의 String을 받아와서 데이터 배열에 담는 함수
	public void setNoteDataString(String dataString)
	{
		try{
			data.clear();
			JSONObject json = new JSONObject(dataString);
			sequence = Integer.parseInt(json.getString("sequence"));
			JSONArray jArray = json.getJSONArray("notes");
			for(int i = 0; i < jArray.length(); i++){
				JSONObject jNoteObject = new JSONObject(jArray.getString(i));
				NoteData note = new NoteData();
				note.setId(jNoteObject.getString("id"));
				note.setDate(jNoteObject.getString("date"));
				note.setTitle(jNoteObject.getString("title"));
				note.setContent(jNoteObject.getString("content"));
				ArrayList<String> imageURLs = new ArrayList<String>();
				JSONArray jNoteImageArray = jNoteObject.getJSONArray("image");
				for(int j = 0; j < jNoteImageArray.length(); j++){
					JSONObject jImageObject = new JSONObject(jNoteImageArray.getString(j));
					imageURLs.add(jImageObject.getString(String.valueOf(j)));
				}
				note.setImageURL(imageURLs);
				data.add(note);
			}
		}catch (JSONException e){
			e.printStackTrace();
		}
	}
}
