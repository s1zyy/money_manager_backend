package vlad.corp.money_manager_backend.presentation.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicPageController {

    @GetMapping(value = "/privacy", produces = MediaType.TEXT_HTML_VALUE)
    public String privacy() {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                  <meta charset="UTF-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                  <title>TripPace – Privacy Policy</title>
                  <style>
                    body { font-family: -apple-system, sans-serif; max-width: 700px; margin: 40px auto; padding: 0 20px; color: #333; line-height: 1.6; }
                    h1 { font-size: 28px; } h2 { font-size: 20px; margin-top: 32px; }
                  </style>
                </head>
                <body>
                  <h1>Privacy Policy</h1>
                  <p>Last updated: July 26, 2026</p>
                  <p>TripPace ("we", "our", or "us") is a trip expense-sharing app. This Privacy Policy explains how we collect, use, and protect your information.</p>
                  <h2>Information We Collect</h2>
                  <p>We collect your name, email address, and password (stored encrypted) when you register. We also store trip and expense data you enter in the app. We may collect anonymous usage statistics to improve the app experience.</p>
                  <h2>How We Use Your Information</h2>
                  <p>We use your data to provide the TripPace service: managing trips, splitting expenses, and sending email invitations to trip participants. Anonymous usage data may be used to improve app performance and user experience.</p>
                  <h2>Data Sharing</h2>
                  <p>We do not sell your personal data. We may share data with trusted third-party services necessary to operate the app, including email delivery (Brevo) and analytics tools (such as Google Firebase). These services process data only as needed to provide their functionality.</p>
                  <h2>Data Storage</h2>
                  <p>Your data is stored on secure servers. We retain your data as long as your account is active. You can delete your account at any time from the app settings.</p>
                  <h2>Analytics</h2>
                  <p>We may collect anonymous usage statistics using third-party analytics tools (such as Google Firebase). This data does not identify you personally.</p>
                  <h2>Contact</h2>
                  <p>If you have any questions, contact us at: <a href="mailto:trippace.dev@gmail.com">trippace.dev@gmail.com</a></p>
                </body>
                </html>
                """;
    }

    @GetMapping(value = "/delete-account", produces = MediaType.TEXT_HTML_VALUE)
    public String deleteAccount() {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                  <meta charset="UTF-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                  <title>TripPace – Delete Account</title>
                  <style>
                    body { font-family: -apple-system, sans-serif; max-width: 700px; margin: 40px auto; padding: 0 20px; color: #333; line-height: 1.6; }
                    h1 { font-size: 28px; } h2 { font-size: 20px; margin-top: 32px; } h3 { font-size: 17px; margin-top: 24px; }
                  </style>
                </head>
                <body>
                  <h1>Delete Your TripPace Account</h1>
                  <h2>How to delete your account</h2>
                  <ol>
                    <li>Open TripPace and log in</li>
                    <li>Go to <strong>Profile → Settings</strong></li>
                    <li>Scroll down to <strong>Delete Account</strong></li>
                    <li>Choose one of the two options below and confirm</li>
                  </ol>
                  <h2>Option 1: Deactivate Account</h2>
                  <p>Your name, email address, and password are permanently deleted. However, your trip history and expenses are preserved so other participants can still see their shared records. Your contributions appear as an anonymous deleted user.</p>
                  <h2>Option 2: Delete Everything</h2>
                  <p>Your account and all trips you own are permanently and irreversibly deleted, including all expenses within those trips. Other participants will lose access to those trips.</p>
                  <h2>Timeline</h2>
                  <p>Both options take effect immediately upon confirmation.</p>
                  <h2>Need help?</h2>
                  <p>Contact us at <a href="mailto:trippace.dev@gmail.com">trippace.dev@gmail.com</a></p>
                </body>
                </html>
                """;
    }
}
