package com.lineplus.lineplusmemo.manager;


import android.util.Log;

import com.lineplus.lineplusmemo.model.NoteData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
			if(data.get(i).getId().equals((id))){
				data.set(i,d);
			}
		}
	}
	// 새로운 노트데이터 추가
	public void addNote(NoteData d){
		data.add(d);
	}
	public String getNoteDataString(){
		try
		{
			JSONObject obj = new JSONObject();
			JSONArray jArray = new JSONArray();
			for (int i = 0; i < data.size(); i++)//배열
			{
				JSONObject sObject = new JSONObject();
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
			obj.put("id", "userID");
			obj.put("notes", jArray);//배열을 넣음
			return obj.toString();
		}catch (JSONException e){
			e.printStackTrace();
		}
		return "";
	}
	public void setNoteDataString(String dataString)
	{
// String to JSON으로 만들어 노트배열에 담는다
		try{

			data.clear();
			JSONObject json = new JSONObject(dataString);
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
