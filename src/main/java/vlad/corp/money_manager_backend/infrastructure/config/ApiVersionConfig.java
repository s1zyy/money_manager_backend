package vlad.corp.money_manager_backend.infrastructure.config;

import java.util.List;

public final class ApiVersionConfig {

    private ApiVersionConfig() {}

    public static final String CURRENT_VERSION = "1.0.0";


    public static final List<String> SUPPORTED_VERSIONS = List.of(
            "1.0.0"
    );

    public static final String HEADER_APP_VERSION = "X-App-Version";
    public static final String REQUEST_ATTR_APP_VERSION = "appVersion";
}
