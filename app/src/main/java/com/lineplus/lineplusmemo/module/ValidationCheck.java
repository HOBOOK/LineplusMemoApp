package com.lineplus.lineplusmemo.module;

import android.text.Editable;
import android.util.Patterns;
import android.webkit.URLUtil;

/// 유효서 검사 클래스
public class ValidationCheck
{
	private static ValidationCheck instnace = null;
	public static ValidationCheck getInstance()
	{
		if(instnace==null)
			instnace = new ValidationCheck();
		return instnace;
	}

	// 이미지 URL 유효성 검사
	public boolean checkImageURL(String url){
		if(URLUtil.isValidUrl(url) || Patterns.WEB_URL.matcher(url).matches()){
			return true;
		}
		return false;
	}

	// 메모 저장 유효성 검사
	public boolean checkEditableMemo(Editable edit){
		return edit!=null && !edit.toString().equals("");
	}
}
