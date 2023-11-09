package dk.kea.project.api;

import dk.kea.project.dto.MyRecipe;
import dk.kea.project.service.OpenAIService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/recipes")
@CrossOrigin(origins = "*")
public class RecipeController {
	private OpenAIService openAIService;
	final static String SYSTEM_MESSAGE = "Lav kun 1 opskrift på få udvalgte ingredienser. Dit svar skal være komplet og under 110 tokens.";

	public RecipeController(OpenAIService openAIService)
		{
			this.openAIService = openAIService;

		}
		@GetMapping
		public MyRecipe makeRequest(String storeId) {
			System.out.println("makeRequest()");
			storeId="efba0457-090e-4132-81ba-c72b4c8e7fee";
			return openAIService.makeRequest(storeId, SYSTEM_MESSAGE);
		}
}
