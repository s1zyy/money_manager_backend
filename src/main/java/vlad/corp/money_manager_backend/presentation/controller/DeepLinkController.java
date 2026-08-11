package vlad.corp.money_manager_backend.presentation.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static vlad.corp.money_manager_backend.presentation.controller.DeepLinkFallbackPage.render;

@RestController
public class DeepLinkController {
    @GetMapping(value = "/join", produces = MediaType.TEXT_HTML_VALUE)
    public String joinFallback(@RequestParam(required = false) String code) {
        String appLink = "trippace://join" + (code != null ? "?code=" + code : "");
        return render(
                appLink,
                "Join trip in TripPace",
                "You were invited to join a trip. Open TripPace to continue.",
                "Open in TripPace"
        );
    }

    @GetMapping(value = "/invite", produces = MediaType.TEXT_HTML_VALUE)
    public String inviteFallback(@RequestParam(required = false) String token) {
        String appLink = "trippace://invite" + (token != null ? "?token=" + token : "");
        return render(
                appLink,
                "You have been invited to TripPace",
                "You have been invited to join a trip. Open TripPace to continue.",
                "Open in TripPace"
        );
    }

    @GetMapping(value = "/reset", produces = MediaType.TEXT_HTML_VALUE)
    public String resetFallback(@RequestParam(required = false) String token) {
        String appLink = "trippace://reset" + (token != null ? "?token=" + token : "");
        return render(
                appLink,
                "Reset your TripPace password",
                "Open TripPace to set a new password.",
                "Open in TripPace"
        );
    }
}
