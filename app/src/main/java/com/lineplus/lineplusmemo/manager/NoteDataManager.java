package com.lineplus.lineplusmemo.manager;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.lineplus.lineplusmemo.model.NoteData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.io.*;

public class NoteDataManager extends AppCompatActivity
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
	public void saveNoteData()
	{
		try {
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
				for(int j = 0; j < imageDatas.size(); j++){
					JSONObject imgObject = new JSONObject();
					imgObject.put(String.valueOf(j),imageDatas.get(j));
					imgArray.put(imgObject);
				}
				sObject.put("image",imgArray);

				jArray.put(sObject);
			}
			obj.put("id", "userID");
			obj.put("notes", jArray);//배열을 넣음

			//내부저장소에 저장
			FileOutputStream fos = null;
			try {
				fos = openFileOutput("data.txt", Context.MODE_PRIVATE);
				fos.write(obj.toString().getBytes());
				fos.close();

			}catch (FileNotFoundException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void loadNoteData()
	{
		File file = new File("data.txt");
		if(file.exists())
		{
			// 내부저장소에서 데이터 불러오기
			StringBuffer buffer = new StringBuffer();
			String dataString = null;
			FileInputStream fis = null;
			try {
				fis = openFileInput("data.txt");
				BufferedReader iReader = new BufferedReader(new InputStreamReader((fis)));
				dataString = iReader.readLine();
				while(dataString != null)
				{
					buffer.append(dataString);
					dataString = iReader.readLine();
				}
				buffer.append("\n");
				iReader.close();
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}

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

					ArrayList<String> imageURLs = new ArrayList<String>();
					JSONArray jNoteImageArray = jNoteObject.getJSONArray("image");
					for(int j = 0; j < jNoteImageArray.length(); j++){
						JSONObject jImageObject = new JSONObject(jNoteImageArray.getString(i));
						imageURLs.add(jImageObject.getString(String.valueOf(j)));
					}
					note.setImageURL(imageURLs);
					data.add(note);
				}
			}catch (JSONException e){
				e.printStackTrace();
			}
		}

		//TEST
		ArrayList<String> imageURL = new ArrayList<>();
		data.clear();
		data.add(new NoteData("0","2020-02-12","테스트1","테스트내용",imageURL));
		data.add(new NoteData("1","2020-02-13","테스트2","테스트내용2",imageURL));
		data.add(new NoteData("2","2020-02-14","테스트3","테스트내용3",imageURL));
	}
}
