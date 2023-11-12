package dk.kea.project.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.kea.project.dto.ChatRecipeRequest;
import dk.kea.project.dto.ChatRecipeResponse;
import dk.kea.project.dto.MyRecipe;
import dk.kea.project.entity.ApiUsage;
import dk.kea.project.repository.ApiUsageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@Service
public class OpenAIService {
	public static final Logger logger = LoggerFactory.getLogger(OpenAIService.class);
	@Value("${OPENAI_API_KEY}")
	String OPENAI_API_KEY;
	public final static String URL = "https://api.openai.com/v1/chat/completions";
	public final static String MODEL = "gpt-3.5-turbo";
	public final static double TEMPERATURE = 0.8;
	public final static int MAX_TOKENS = 300;
	public final static double FREQUENCY_PENALTY = 0.0;
	public final static double PRESENCE_PENALTY = 0.0;
	public final static double TOP_P = 1.0;
	private WebClient client;
	SallingService sallingService;
	ApiUsageRepository apiUsageRepository;


	public OpenAIService(SallingService sallingService, ApiUsageRepository apiUsageRepository) {
		this.client = WebClient.create();
		this.sallingService = sallingService;
		this.apiUsageRepository = apiUsageRepository;
	}
	//Use this constructor for testing, to inject a mock client
//	public OpenAIService(WebClient client, SallingService sallingService) {
//		this.client = client;
//		this.sallingService = new SallingService();
//	}


	public MyRecipe makeRequest(String storeID, String _systemMessage) {
		String userPrompt = sallingService.ingredients(storeID);
		ChatRecipeRequest requestDto = new ChatRecipeRequest();
		requestDto.setModel(MODEL);
		requestDto.setTemperature(TEMPERATURE);
		requestDto.setMax_tokens(MAX_TOKENS);
		requestDto.setTop_p(TOP_P);
		requestDto.setFrequency_penalty(FREQUENCY_PENALTY);
		requestDto.setPresence_penalty(PRESENCE_PENALTY);
		requestDto.getMessages().add(new ChatRecipeRequest.Message("system", _systemMessage));
		requestDto.getMessages().add(new ChatRecipeRequest.Message("user", userPrompt));

		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		String err =  null;
		try {
			json = mapper.writeValueAsString(requestDto);
			System.out.println(json);
			ChatRecipeResponse response = client.post()
					.uri(new URI(URL))
					.header("Authorization", "Bearer " + OPENAI_API_KEY)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.body(BodyInserters.fromValue(json))
					.retrieve()
					.bodyToMono(ChatRecipeResponse.class)
					.block();
			String responseMsg = response.getChoices().get(0).getMessage().getContent();
			int tokensUsed = response.getUsage().getTotal_tokens();

			// Save apiusage statistics to database
			ApiUsage apiUsage = new ApiUsage(tokensUsed);
			apiUsageRepository.save(apiUsage);
			
			System.out.print("Tokens used: " + tokensUsed);
			System.out.print(". Cost ($0.0015 / 1K tokens) : $" + String.format("%6f",(tokensUsed * 0.0015 / 1000)));
			System.out.println(". For 1$, this is the amount of similar requests you can make: " + Math.round(1/(tokensUsed * 0.0015 / 1000)));
			System.out.println("response "+responseMsg);
			return new MyRecipe(responseMsg);
		}
		catch (WebClientResponseException e){
			//This is how you can get the status code and message reported back by the remote API
			logger.error("Error response status code: " + e.getRawStatusCode());
			logger.error("Error response body: " + e.getResponseBodyAsString());
			logger.error("WebClientResponseException", e);
			err = "Internal Server Error, due to a failed request to external service. You could try again" +
					"( While you develop, make sure to consult the detailed error message on your backend)";
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, err);
		}
		catch (Exception e) {
			logger.error("Exception", e);
			err = "Internal Server Error - You could try again" +
					"( While you develop, make sure to consult the detailed error message on your backend)";
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, err);
		}
	}
}
