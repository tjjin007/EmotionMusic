package kr.co.utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.dao.MediaDAO;
import kr.co.dto.DictionaryDTO;
import kr.co.dto.HistoryDTO;
import kr.co.dto.MediaDTO;
import kr.co.test.HomeController;

public class Utility {
	private static final String root = "/test";
	private static final Logger logger = LoggerFactory.getLogger(Utility.class);

	public static synchronized String getRoot() {
		return root;
	}
	
	public static String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		return sdf.format(new Date());
	} // getCurrentDate() end
	
	
	// 유투브 검색결과 title 비교하기 : 3가지 경우
	public void compareTitle(String subject, String[] youtubeTitle){
		String searchSubject = subject.replace(" ", "+");				// 검색어에서 공백을 +로 
		String compare="";		// 유투브와 비교할 검색어
		String compareSource[];		// 제목을 한 단어씩 넣어둠
		
		// 1. 제목앞에 숫자가 붙는 경우
		int index = subject.indexOf(".");	
		if(index==1) {	
			searchSubject = "0"+ searchSubject.replace(".", "+");
			compare = searchSubject.substring(index+1, searchSubject.length()).replace("+", " ").toLowerCase().trim();
		} else if(index==2) {
			compare = searchSubject.substring(index+1, searchSubject.length()).replace("+", " ").toLowerCase().trim();
		} else {
			compare = subject;
		}
		compareSource = compare.split(" ");		
		// 2. 제목 자체가 틀린 경우 단어별로 정확도 측정하기
		// = titleAccuracy(urlTitle);
		// 3. 앨범명이 노래 제목과 같은 경우
	}// compareTitle() end
	
	/* Headr 관련 메소드*/
	// 전체 노래 리스트로 부터 감정별 갯수 파악
	public static JSONObject getJsonAllEmotionMusic(List<MediaDTO> musicList  ){				 
		Iterator<MediaDTO> iterator = musicList.iterator();
		String emotionType[] ={"happy","disgust","fear","interest","pain","rage","sad"};
		int count[] = new int[7];
		while(iterator.hasNext()){
			MediaDTO dto = iterator.next();
			if(dto.getHappy()>15)		{count[0]++;}		// 0 기쁨
			if(dto.getDisgust()>15)		{count[1]++;}		// 1 혐오
			if(dto.getFear()>15)			{count[2]++;}		// 2 공포
			if(dto.getInterest()>15)		{count[3]++;}		// 3 흥미
			if(dto.getPain()>15)			{count[4]++;}		// 4 아픔
			if(dto.getRage()>15)		{count[5]++;}		// 5 분노
			if(dto.getSad()>15)			{count[6]++;}		// 6 슬픔
		}
		// 최종 완성될 JSONObject 선언(전체)
		JSONObject jsonObject = new JSONObject();
		// person의 JSON정보를 담을 Array 선언
		JSONArray jsonArray = new JSONArray();
		// person의 한명 정보가 들어갈 JSONObject 선언
		JSONObject jsonEmotion = new JSONObject();
		for(int i=0; i<emotionType.length; i++){
			jsonEmotion.put(emotionType[i], count[i]);
			
		}
		logger.debug(jsonEmotion.toString());
		return jsonEmotion;
	}

	public static JSONObject getHistoryMusic(List<HistoryDTO> musicList  ){				 
		Iterator<HistoryDTO> iterator = musicList.iterator();
		String emotionType[] ={"happy","disgust","fear","interest","pain","rage","sad"};
		int count[] = new int[7];//7가지 감정
		 
		while(iterator.hasNext()){
			HistoryDTO dto = iterator.next();
			if(dto.getEmotion()=="happy") {count[0]++;}
			if(dto.getEmotion()=="disgust") {count[1]++;}
			if(dto.getEmotion()=="fear") {count[2]++;}
			if(dto.getEmotion()=="interest") {count[3]++;}
			if(dto.getEmotion()=="pain") {count[4]++;}
			if(dto.getEmotion()=="rage") {count[5]++;}
			if(dto.getEmotion()=="sad") {count[6]++;}
		}
		
		// 최종 완성될 JSONObject 선언(전체)
		JSONObject jsonObject = new JSONObject();
		// person의 JSON정보를 담을 Array 선언
		JSONArray jsonArray = new JSONArray();
		// person의 한명 정보가 들어갈 JSONObject 선언
		JSONObject jsonHistory = new JSONObject();
		for(int i=0; i<emotionType.length; i++){
			jsonHistory.put(emotionType[i], count[i]);
			
		}
		logger.debug(jsonHistory.toString());
		return jsonHistory;
	}
	
	
	/*Bubble Menu 관련 메소드*/
	// 랜덤으로 감정 별 키워드를 세개씩 반환 하는 함수
	@SuppressWarnings("unchecked")
	public static JSONObject getJsonBubbleMenu(List<DictionaryDTO> dictionaryList  ){				 
		Iterator<DictionaryDTO> iterator = dictionaryList.iterator();
		Random randomEmotion = new Random();
		String emotionType[] ={"happy","disgust","fear","interest","pain","rage","sad"};
		ArrayList<String> listOfEmotionWord[] = new ArrayList[7];			// 감정별 단어를 담을 배열
		for(int index=0; index<listOfEmotionWord.length; index++){
			listOfEmotionWord[index] = new ArrayList<String>();
		}
		//  지루함, 놀람 빠져 있다. - 버블 차트에 추가 해야하는지 의논
		while(iterator.hasNext()){								
			DictionaryDTO dto = iterator.next();
			if(dto.getEmotion().equals("기쁨")){
				listOfEmotionWord[0].add(dto.getWord());
			}
			if(dto.getEmotion().equals("혐오")){
				listOfEmotionWord[1].add(dto.getWord());
			}
			if(dto.getEmotion().equals("공포")){
				listOfEmotionWord[2].add(dto.getWord());
			}
			if(dto.getEmotion().equals("흥미")){
				listOfEmotionWord[3].add(dto.getWord());
			}
			if(dto.getEmotion().equals("통증")){
				listOfEmotionWord[4].add(dto.getWord());
			}
			if(dto.getEmotion().equals("분노")){
				listOfEmotionWord[5].add(dto.getWord());
			}
			if(dto.getEmotion().equals("슬픔")){
				listOfEmotionWord[6].add(dto.getWord());
			}
		}
		
		
		// bubble circle 한개 정보가 들어갈 JSONObject 선언
		JSONObject jsonEmotion = new JSONObject();
		for(int i=0; i<emotionType.length; i++){
			jsonEmotion.put("test", "test");
			
		}
		//logger.debug(jsonEmotion.toString());
		return jsonEmotion;
	}
	
	
	/*simple dashBoard 관련 메소드*/
	// 감정별 점수 json 객체로 반환
	@SuppressWarnings("unchecked")
	public static JSONArray getJsonTopMusic(List<MediaDTO> mediaDTOList){
		//최종 완성될 JSONObject 선언(전체)
		JSONArray jsonTopMusic = new JSONArray();

		Iterator<MediaDTO> iteratorMedia = mediaDTOList.iterator();
		//String emotionType[] ={"Happy","Disgust","Fear","Interest","Pain","Rage","Sad"};
		ArrayList<Integer> listOfTopTenEmotion[] = new ArrayList[7];			// 한곡당 감정 점수를 담을 배열
		
		for(int index=0; index<listOfTopTenEmotion.length; index++){
			listOfTopTenEmotion[index] = new ArrayList<Integer>();
		}
		
		// 한곡 당 감정별 점수를 ArrayList에 저장
		while(iteratorMedia.hasNext()){
			MediaDTO dto = iteratorMedia.next();
			listOfTopTenEmotion[0].add(dto.getHappy());
			listOfTopTenEmotion[1].add(dto.getSad());
			listOfTopTenEmotion[2].add(dto.getRage());
			listOfTopTenEmotion[3].add(dto.getDisgust());
			listOfTopTenEmotion[4].add(dto.getInterest());
			listOfTopTenEmotion[5].add(dto.getPain());
			listOfTopTenEmotion[6].add(dto.getFear());
		}// end while
	
		for(ArrayList<Integer> point : listOfTopTenEmotion){
			logger.debug(String.valueOf(point));
		}
	
		// 노래 한곡을 JSONObject 형태로 저장
		for(int i=0; i<10;i++){																// 전체 10곡 
			JSONObject innerJson = new JSONObject();								// #1 innerJson <감정 7가지 점수 >
			JSONObject outterJson = new JSONObject();	
			innerJson.put("happy", listOfTopTenEmotion[0].get(i));
			innerJson.put("sad", listOfTopTenEmotion[1].get(i));
			innerJson.put("rage", listOfTopTenEmotion[2].get(i));
			innerJson.put("disgust", listOfTopTenEmotion[3].get(i));
			innerJson.put("interest", listOfTopTenEmotion[4].get(i));
			innerJson.put("pain", listOfTopTenEmotion[5].get(i));
			innerJson.put("fear", listOfTopTenEmotion[6].get(i));
			outterJson.put("freq", innerJson);											// #2 outterJson index2
			outterJson.put("State", "Top"+(i+1));										// #2 outterJson index1
			jsonTopMusic.add(outterJson);
		}
		
		logger.debug(jsonTopMusic.get(0).toString());
		return jsonTopMusic;
		
	}
} // class end
