package com.lineplus.lineplusmemo.model;

import java.io.Serializable;
import java.util.*;

// 메모 객체 데이터 클래스
public class NoteData implements Serializable
{
	private String id;
	private String date;
	private String title;
	private String content;
	private ArrayList<String> imageURL;
	public NoteData(){}
	public NoteData(String id, String date, String title, String content, ArrayList<String> imageURL){
		this.id = id;
		this.date = date;
		this.title = title;
		this.content = content;
		this.imageURL = imageURL;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public ArrayList<String>  getImageURL()
	{
		return this.imageURL;
	}

	public void setImageURL(ArrayList<String> imageURL)
	{
		this.imageURL = imageURL;
	}

	public void addImage(String url)
	{
		this.imageURL.add(url);
	}
	public void removeImage(int index)
	{
		this.imageURL.remove(index);
	}
}
