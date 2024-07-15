package com.rocketseat.planner.activity;

import com.rocketseat.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository repository;

    public ActivityResponse registerActivity (ActivityRequestPayload payload, Trip trip){
        Activity newActivity = new Activity(payload.title(), payload.occurs_at(), trip);

        this.repository.save(newActivity);

        return new ActivityResponse(newActivity.getId());
    }

    public boolean checkActivityDate(ActivityRequestPayload payload, Trip trip){
        LocalDateTime dateTime = LocalDateTime.parse(payload.occurs_at(), DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime tripStart = trip.getStartsAt();
        LocalDateTime tripEnd = trip.getEndsAt();

        return dateTime.isAfter(tripStart) && dateTime.isBefore(tripEnd);
    }

    public List<ActivityData> getAllActivitiesFromEvent(UUID tripId){
        return this.repository.findByTripId(tripId).stream().map(activity -> new ActivityData(activity.getId(), activity.getTitle(), activity.getOccursAt())).toList();
    }

}
