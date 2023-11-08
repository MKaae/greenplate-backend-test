package dk.kea.project.api;

import dk.kea.project.dto.MyRecipe;
import dk.kea.project.service.OpenAIService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/api/recipes/limited")
@CrossOrigin(origins = "*")
public class RecipeControllerLimited {

    // Rate limiting parameters
    private final int BUCKET_CAPACITY = 1;
    private final int REFILL_AMOUNT = 1;
    private final int REFILL_TIME = 9999;

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    // Create a new rate limiting bucket with specified parameters
    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.classic(BUCKET_CAPACITY, Refill.greedy(REFILL_AMOUNT, Duration.ofMinutes(REFILL_TIME)));
        return Bucket.builder().addLimit(limit).build();
    }

    // Get or create a rate limiting bucket for a client based on their IP address
    private Bucket getBucket(String key) {
        return buckets.computeIfAbsent(key, k -> createNewBucket());
    }

    private OpenAIService openAIService;
    final static String SYSTEM_MESSAGE = "Lav kun 1 opskrift på få udvalgte ingredienser. Dit svar skal være komplet og under 110 tokens.";

    public RecipeControllerLimited(OpenAIService openAIService)
    {
        this.openAIService = openAIService;

    }
    @GetMapping
    public MyRecipe makeRequest(String storeId, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        Bucket bucket = getBucket(ip);

        if (!bucket.tryConsume(1)) {
            System.out.println("Too many requests, try again later");
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests, try again later");
        }

        storeId="efba0457-090e-4132-81ba-c72b4c8e7fee";
        return openAIService.makeRequest(storeId, SYSTEM_MESSAGE);
    }
}
