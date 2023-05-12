package SpeechToText;

import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InterruptedIOException;


public class Driver {

	public static void main(String[] args) {
		//Base URL for the API Calls ==  https://api.assemblyai.com/
		
		Transcript transcript = new Transcript(); //custom class to be used with Gson for all future transcriptions
		transcript.setAudio_url("http://study.aitech.ac.jp/tat/281047.mp3");
		
		// Gson object to send the audio file to be transcribed as json
		Gson gson = new Gson();
		String jsonRequest = gson.toJson(transcript);
		
		
		
		try {
	    HttpRequest postRequest = HttpRequest.newBuilder()
	    		.uri(new URI("https://api.assemblyai.com/v2/transcript"))
	    		.header("Authorization","4acdb206824d44e18d66bdecbeca7750")
	    		.POST(BodyPublishers.ofString(jsonRequest))
	    		.build();
	    
		HttpClient httpClient = HttpClient.newHttpClient();
		
		HttpResponse<String> postResponse = httpClient.send(postRequest, BodyHandlers.ofString());
		
		System.out.println(postResponse.body());
		
		transcript = gson.fromJson(postResponse.body(), Transcript.class);
		
		System.out.println(transcript.getId());
		
		HttpRequest getRequest = HttpRequest.newBuilder()
	    		.uri(new URI("https://api.assemblyai.com/v2/transcript/" + transcript.getId()))
	    		.header("Authorization","4acdb206824d44e18d66bdecbeca7750")
	    		.build();
		
		while(true) {
			HttpResponse<String> getResponse = httpClient.send(getRequest, BodyHandlers.ofString());
		
			transcript = gson.fromJson(getResponse.body(), Transcript.class);
			
			System.out.println(transcript.getStatus());
			
			if("completed".equals(transcript.getStatus()) || "error".equals(transcript.getStatus())) {
				
				break;
				
			}
			
			Thread.sleep(1000);
			
		}
		System.out.println("Transcription Completed");
		System.out.println(transcript.getText());
		
		
		}
		catch(URISyntaxException e) {
			System.out.println(e.getMessage());
		}
		catch(InterruptedException e) {
			System.out.println(e.getMessage());
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
		}

		
	}

}
