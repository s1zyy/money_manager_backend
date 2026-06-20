package vlad.corp.money_manager_backend.infrastructure.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vlad.corp.money_manager_backend.application.trip.UpdateTripStatusesUseCase;

@Component
public class TripStatusScheduler {

    private final UpdateTripStatusesUseCase updateTripStatusesUseCase;

    public TripStatusScheduler(UpdateTripStatusesUseCase updateTripStatusesUseCase) {
        this.updateTripStatusesUseCase = updateTripStatusesUseCase;
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void updateTripStatuses() {
        System.out.println("=== [SCHEDULER] Starting automatic trip status update task ===");

        updateTripStatusesUseCase.execute();

        System.out.println("=== [SCHEDULER] Trip status update task finished successfully ===");

    }


}
