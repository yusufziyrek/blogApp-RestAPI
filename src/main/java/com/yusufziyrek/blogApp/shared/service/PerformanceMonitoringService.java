package com.yusufziyrek.blogApp.shared.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class PerformanceMonitoringService {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter postCreationCounter;
    private final Counter userRegistrationCounter;
    private final Counter loginAttemptCounter;
    private final Counter searchRequestCounter;

    // Timers
    private final Timer postCreationTimer;
    private final Timer userRegistrationTimer;
    private final Timer searchTimer;
    private final Timer authenticationTimer;

    public PerformanceMonitoringService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        
        // Initialize counters
        this.postCreationCounter = Counter.builder("blogapp.posts.created")
            .description("Number of posts created")
            .register(meterRegistry);
            
        this.userRegistrationCounter = Counter.builder("blogapp.users.registered")
            .description("Number of users registered")
            .register(meterRegistry);
            
        this.loginAttemptCounter = Counter.builder("blogapp.auth.login.attempts")
            .description("Number of login attempts")
            .register(meterRegistry);
            
        this.searchRequestCounter = Counter.builder("blogapp.search.requests")
            .description("Number of search requests")
            .register(meterRegistry);

        // Initialize timers
        this.postCreationTimer = Timer.builder("blogapp.posts.creation.time")
            .description("Time taken to create posts")
            .register(meterRegistry);
            
        this.userRegistrationTimer = Timer.builder("blogapp.users.registration.time")
            .description("Time taken to register users")
            .register(meterRegistry);
            
        this.searchTimer = Timer.builder("blogapp.search.time")
            .description("Time taken for search operations")
            .register(meterRegistry);
            
        this.authenticationTimer = Timer.builder("blogapp.auth.time")
            .description("Time taken for authentication")
            .register(meterRegistry);
    }

    public void incrementPostCreation() {
        postCreationCounter.increment();
        log.debug("Post creation counter incremented");
    }

    public void incrementUserRegistration() {
        userRegistrationCounter.increment();
        log.debug("User registration counter incremented");
    }

    public void incrementLoginAttempt() {
        loginAttemptCounter.increment();
        log.debug("Login attempt counter incremented");
    }

    public void incrementSearchRequest() {
        searchRequestCounter.increment();
        log.debug("Search request counter incremented");
    }

    public Timer.Sample startPostCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopPostCreationTimer(Timer.Sample sample) {
        sample.stop(postCreationTimer);
        log.debug("Post creation timer stopped");
    }

    public Timer.Sample startUserRegistrationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopUserRegistrationTimer(Timer.Sample sample) {
        sample.stop(userRegistrationTimer);
        log.debug("User registration timer stopped");
    }

    public Timer.Sample startSearchTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopSearchTimer(Timer.Sample sample) {
        sample.stop(searchTimer);
        log.debug("Search timer stopped");
    }

    public Timer.Sample startAuthenticationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopAuthenticationTimer(Timer.Sample sample) {
        sample.stop(authenticationTimer);
        log.debug("Authentication timer stopped");
    }

    public void recordDatabaseQueryTime(String queryName, long timeInMs) {
        Timer.builder("blogapp.database.query.time")
            .tag("query", queryName)
            .register(meterRegistry)
            .record(timeInMs, TimeUnit.MILLISECONDS);
        log.debug("Database query time recorded: {} - {}ms", queryName, timeInMs);
    }

    public void recordCacheHit(String cacheName) {
        Counter.builder("blogapp.cache.hits")
            .tag("cache", cacheName)
            .register(meterRegistry)
            .increment();
        log.debug("Cache hit recorded for: {}", cacheName);
    }

    public void recordCacheMiss(String cacheName) {
        Counter.builder("blogapp.cache.misses")
            .tag("cache", cacheName)
            .register(meterRegistry)
            .increment();
        log.debug("Cache miss recorded for: {}", cacheName);
    }
} 