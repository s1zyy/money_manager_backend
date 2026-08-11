package vlad.corp.money_manager_backend.presentation.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WellKnownController {

    @GetMapping(value = "/.well-known/apple-app-site-association", produces = MediaType.APPLICATION_JSON_VALUE)
    public String appleAppSiteAssociation() {
        return """
                {
                  "applinks": {
                    "details": [
                      {
                        "appIDs": ["TZK3J42645.com.trippace.app"],
                        "components": [
                          { "/": "/join*" },
                          { "/": "/invite*" },
                          { "/": "/reset*" }
                        ]
                      }
                    ]
                  }
                }
                """;
    }
}
