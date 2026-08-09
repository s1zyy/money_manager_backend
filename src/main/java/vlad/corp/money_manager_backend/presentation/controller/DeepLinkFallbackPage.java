package vlad.corp.money_manager_backend.presentation.controller;

public class DeepLinkFallbackPage {

    public static String render(String appLink, String title, String message, String buttonText) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                  <meta charset="UTF-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                  <title>%s</title>
                  <style>
                    body { font-family: -apple-system, sans-serif; display: flex; flex-direction: column;
                           align-items: center; justify-content: center; min-height: 100vh; margin: 0;
                           background: #f5f5f5; text-align: center; padding: 20px; box-sizing: border-box; }
                    h1 { color: #6C63FF; font-size: 24px; margin-bottom: 12px; }
                    p { color: #666; font-size: 16px; margin-bottom: 32px; }
                    a.btn { background: #6C63FF; color: white; padding: 16px 32px; border-radius: 12px;
                            text-decoration: none; font-weight: bold; font-size: 16px; }
                    .hint { margin-top: 24px; font-size: 13px; color: #999; }
                  </style>
                  <script>
                    window.onload = function() {
                      window.location = '%s';
                    };
                  </script>
                </head>
                <body>
                  <h1>TripPace</h1>
                  <p>%s</p>
                  <a class="btn" href="%s">%s</a>
                  <p class="hint">If the app doesn't open, make sure TripPace is installed.</p>
                </body>
                </html>
                """.formatted(title, appLink, message, appLink, buttonText);
    }
}
