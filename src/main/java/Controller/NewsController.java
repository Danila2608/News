package Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@RestController
class NewsController {

    @Value("${newsapi.key}")
    private String API_KEY;

    @GetMapping("/news")
    public ResponseEntity<Object> getNews(@RequestParam(value = "category", defaultValue = "general") String category) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://newsapi.org/v2/top-headlines?country=us&category=" + category + "&apiKey=" + API_KEY;
        try {
            return ResponseEntity.ok(restTemplate.getForObject(url, Object.class));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid category. Please use one of the following categories: business, entertainment, general, health, science, sports, technology.");
        }
    }

}
