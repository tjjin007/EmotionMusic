package kr.co.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test2 {

	public static String getCurrentData() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		return sdf.format(new Date());
	}

	public static void main(String[] args) throws ClientProtocolException, IOException {
		// 1. 가져오기전 시간 찍기
		System.out.println(" Start Date : " + getCurrentData());
		int no = 1;

		for (no = 30; no <= 30; no++) {
			// 2. 가져올 HTTP 주소 세팅
			HttpPost http = new HttpPost("http://gasazip.com/" + no + "");

			// 3. 가져오기를 실행할 클라이언트 객체 생성
			HttpClient httpClient = HttpClientBuilder.create().build();

			// 4. 실행 및 실행 데이터를 Response 객체에 담음
			HttpResponse response = httpClient.execute(http);

			// 5. Response 받은 데이터 중, DOM 데이터를 가져와 Entity에 담음
			HttpEntity entity = response.getEntity();

			// 6. Charset을 알아내기 위해 DOM의 컨텐트 타입을 가져와 담고 Charset을 가져옴
			ContentType contentType = ContentType.getOrDefault(entity);
			Charset charset = contentType.getCharset();

			// 7. DOM 데이터를 한 줄씩 읽기 위해 Reader에 담음 (InputStream / Buffered 중 선택은 개인취향)
			BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), charset));

			// 8. 가져온 DOM 데이터를 담기위한 그릇
			StringBuffer sb = new StringBuffer();

			// 9. DOM 데이터 가져오기
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}

			// 11. Jsoup으로 파싱해보자.
			Document doc = Jsoup.parse(sb.toString());
			
			try {
				String subject = doc.select("div.col-md-8").get(0).text(); 	// 제목
				String lyrics = doc.select("div.col-md-8").get(1).text(); 	// 가사
				String metadata = doc.select("div.col-md-4").get(0).text();	// 가수
				String singer = metadata.split(" ")[0].toString();
				String album ="";
				
				if(metadata.contains("집")){								// 앨범 
					album = metadata.substring(metadata.indexOf(":")+2, metadata.indexOf("집")+1);	// 공백제외
				}
				
				if (metadata.contains("Unknown")) {							// 가수가 없는 경우 예외처리
					System.out.println(no + " is pass");
					continue;
				}
				// 가사집 크롤링 종료 
				
				// youtube url 가져오기 (*함수형으로)
				String searchSubject = subject.replace(" ", "+");
				String url = "https://www.youtube.com/results?search_query=" + searchSubject + "+" + singer + "+" + album; // youtube 검색 결과 url
				http = new HttpPost(url);
				httpClient = HttpClientBuilder.create().build();
				response = httpClient.execute(http);
				entity = response.getEntity();
				contentType = ContentType.getOrDefault(entity);
				charset = contentType.getCharset();
				br = new BufferedReader(new InputStreamReader(entity.getContent(), charset));
				sb = new StringBuffer();
				line = ""; 
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				doc = Jsoup.parse(sb.toString());
				// 결과 중 목록 형태가 아닌 하나의 영상이 연결된 링크 부분 가져오기
				
				// 결과페이지의 title과 비교하기 위해 가수에 포함된 번호제거 
				int index = subject.indexOf(".");
				
				
				System.out.println(url);
				Elements atag = doc.select("h3.yt-lockup-title a");
				System.out.println(atag);
				for(Element element: atag) {
					if(element.attr("title").contains(subject)){
						System.out.println("요청 타이틀 : " + subject );
						System.out.println("응답 타이틀 : " +  element.attr("title"));
						break;
					}
				}

				/*String resultValue = "";
				for (int idx = 0; idx < varray.length; idx++) {
					if (varray[idx].length() > 6) {
						if (varray[idx].substring(0, 4).equals("href")) {
							resultValue = varray[idx].substring(6, 26); // 링크의
																		// 주소부분
																		// 추출
						}
					}
					if (!resultValue.isEmpty()) // 링크주소를 얻고나면 빠져나오기
						break;
				}*/
				String sourceUrl = "https://www.youtube.com"; 

				BufferedWriter out = new BufferedWriter(new FileWriter("./lyrics/" + no + ".txt")); // 출력파일 만들기 : 번호.txt
				out.write("제목 : " + subject);
				out.newLine();
				out.write("가수 : " + singer);
				out.newLine();
				out.write(lyrics);
				out.newLine();
				out.write(sourceUrl);
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 12. 얼마나 걸렸나 찍어보자
		System.out.println(" End Date : " + getCurrentData());
	}
}
