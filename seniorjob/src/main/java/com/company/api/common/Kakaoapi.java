package com.company.api.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.company.users.service.UsersVO;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class Kakaoapi {

	//kakao login(엑세스 토큰 받기)
	
	public String getAccessToken(String authorize_code) {
		String access_Token = "";
		String refresh_Token ="";
		String reqURL ="https://kauth.kakao.com/oauth/token";
	
	try {
		URL url = new URL(reqURL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		//post 요청을 위해 기본값이 false인 setDoOut을 true로
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		
		//post 요청에 필요로 요구하는 parameter stream을 통해 전송
		 BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
         StringBuilder sb = new StringBuilder();
         sb.append("grant_type=authorization_code");
         sb.append("&client_id=3cab068ec0c75ace569fa7942cb9c9f5"); //본인이 발급받은 카카오 rest api key 
         sb.append("&redirect_uri=http://localhost/kakaologin");  //본인이 설정 해놓은 경로
         sb.append("&code=" + authorize_code);
         bw.write(sb.toString());
         bw.flush();
		
         //결과 code가 200이라면 성공
         int responseCode = conn.getResponseCode();
         System.out.println("responseCode:" + responseCode);
         
         //요청을 통해 얻은 JSON type의 Response message 읽어오기
         BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
         String line = "";
         String result = "";
         
         while ((line = br.readLine()) != null) {
             result += line;
         }
         System.out.println("response body : " + result);
         
         //Gson 라이브러리에 포함된 class로 JSON parsing 객체 생성
         JsonParser parser = new JsonParser();
         JsonElement element = parser.parse(result);
         
         access_Token = element.getAsJsonObject().get("access_token").getAsString();
         refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();
         
         System.out.println("access_token : " + access_Token);
         System.out.println("refresh_token : " + refresh_Token);
         
         br.close();
         bw.close();
         
	} catch (IOException e) {
		
		e.printStackTrace();
	}
	return access_Token;
	
	}
	
	//kakao 사용자 정보 (Header에 발급 받은 토큰)가져오기
	public HashMap<String, Object> getUserInfo (String access_Token) {
	 
	    // 요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
	    HashMap<String, Object> userInfo = new HashMap<>();
	    String reqURL = "https://kapi.kakao.com/v2/user/me";
	    
	    try {
	        URL url = new URL(reqURL);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("POST");
	        
	        // 요청에 필요한 Header에 포함될 내용
	        conn.setRequestProperty("Authorization", "Bearer " + access_Token);
	        // 출력되는 값이 200이면 정상작동
	        int responseCode = conn.getResponseCode();
	        System.out.println("responseCode : " + responseCode);
	        
	        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        
	        String line = "";
	        String result = "";
	        
	        while ((line = br.readLine()) != null) {
	            result += line;
	        }
	        System.out.println("response body : " + result);
	        
	        JsonParser parser = new JsonParser();
	        JsonElement element = parser.parse(result);
	        
	        String id = element.getAsJsonObject().get("id").toString();
	        JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
	        JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
			
	        String nickname = properties.getAsJsonObject().get("nickname").getAsString();
	        
	        System.out.println("kakao id : " + id);
	        if(kakao_account.get("has_email").getAsBoolean() && kakao_account.get("email") != null) {
	        	String email = kakao_account.get("email").getAsString();
	        	userInfo.put("email", email);
	        }
	        userInfo.put("nickname", nickname);
	        userInfo.put("kakaoId", id);
	        //userInfo.put("email", email);
	        
	        
	        
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    
	    return userInfo;
	}
	
	
	//kakao logout
	public void kakakoLogout(String access_Token) {
		String reqURL ="https://kapi.kakao.com/v1/user/logout";
		try {
			URL url = new URL(reqURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");  //요청방식 
			conn.setRequestProperty("Authorization", "Bearer" + access_Token);  //Request Header값 셋팅
		
			int responseCode = conn.getResponseCode();
			System.out.println("responseCode:" + responseCode);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String result = "";
			String line = "";
			
			while((line = br.readLine()) != null) {
				result += line;
			}
			System.out.println(result);
		
			} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
}