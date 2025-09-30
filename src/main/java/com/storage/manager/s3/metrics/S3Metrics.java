package com.storage.manager.s3.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

@Component
public class S3Metrics {

    private final Counter uploadCounter;
    private final Counter downloadCounter;
    private final Counter listCounter;
    private final Counter deleteCounter;
    private final Timer uploadTimer;
    private final Timer downloadTimer;

    public S3Metrics(MeterRegistry meterRegistry) {
        this.uploadCounter = Counter.builder("s3.upload.requests.total")
                .description("Total number of S3 upload requests")
                .register(meterRegistry);
        
        this.downloadCounter = Counter.builder("s3.download.requests.total")
                .description("Total number of S3 download requests")
                .register(meterRegistry);
        
        this.listCounter = Counter.builder("s3.list.requests.total")
                .description("Total number of S3 list requests")
                .register(meterRegistry);
        
        this.deleteCounter = Counter.builder("s3.delete.requests.total")
                .description("Total number of S3 delete requests")
                .register(meterRegistry);
        
        this.uploadTimer = Timer.builder("s3.upload.duration")
                .description("S3 upload operation duration")
                .register(meterRegistry);
        
        this.downloadTimer = Timer.builder("s3.download.duration")
                .description("S3 download operation duration")
                .register(meterRegistry);
    }

    public void incrementUploadCounter() {
        uploadCounter.increment();
    }

    public void incrementDownloadCounter() {
        downloadCounter.increment();
    }

    public void incrementListCounter() {
        listCounter.increment();
    }

    public void incrementDeleteCounter() {
        deleteCounter.increment();
    }

    public Timer.Sample startUploadTimer() {
        return Timer.start();
    }

    public void stopUploadTimer(Timer.Sample sample) {
        sample.stop(uploadTimer);
    }

    public Timer.Sample startDownloadTimer() {
        return Timer.start();
    }

    public void stopDownloadTimer(Timer.Sample sample) {
        sample.stop(downloadTimer);
    }
}
