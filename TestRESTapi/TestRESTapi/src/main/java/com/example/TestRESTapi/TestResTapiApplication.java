package com.example.TestRESTapi;

import com.example.TestRESTapi.DTO.ResponseUserDTO;
import com.example.TestRESTapi.mappery.UserMappingStrategy;
import com.example.TestRESTapi.model.Address;
import com.example.TestRESTapi.model.UserModel;
import com.github.javafaker.Faker;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableMongoRepositories
@EnableMongoAuditing
public class TestResTapiApplication implements CommandLineRunner {

	private final Faker faker = new Faker();
	private final UserMappingStrategy mappingStrategy;
	private final MeterRegistry meterRegistry;

	public TestResTapiApplication(UserMappingStrategy mappingStrategy,
								  MeterRegistry meterRegistry) {
		this.mappingStrategy = mappingStrategy;
		this.meterRegistry = meterRegistry;
	}

	public static void main(String[] args) {
		SpringApplication.run(TestResTapiApplication.class, args);
	}

	@Override
	public void run(String... args) {
		runMappingTests();
	}

	private void runMappingTests() {
		int[] sizes = {100, 1_000, 10_000};

		for (int count : sizes) {

			//Przygotowanie danych testowych
			List<UserModel> users = generateUsers(count);

			//Przygotowanie metryki heap (Gauge)
			Gauge heapUsedGauge = meterRegistry
					.find("jvm.memory.used")
					.tag("area", "heap")
					.gauge();

			double peakHeapBytes = 0;

			//Snapshot GC przed testem
			GcSnapshot gcBefore = GcSnapshot.capture(meterRegistry);

			//Pomiar czasu
			long start = System.nanoTime();

			//Właściwe mapowanie + pomiar peak heap
			for (UserModel u : users) {
				ResponseUserDTO dto = mappingStrategy.toDto(u);

				if (heapUsedGauge != null) {
					double currentHeap = heapUsedGauge.value();
					peakHeapBytes = Math.max(peakHeapBytes, currentHeap);
				}
			}

			long end = System.nanoTime();

			//Snapshot GC po teście
			GcSnapshot gcAfter = GcSnapshot.capture(meterRegistry);
			GcDiff gcDiff = gcAfter.diff(gcBefore);

			//Obliczenia końcowe
			double timeMs = (end - start) / 1_000_000.0;
			double peakHeapMb = peakHeapBytes / (1024 * 1024);

			//Wyniki
			System.out.printf(
					"""
                    Users: %d
                    Time: %.2f ms
                    Peak heap: %.2f MB
                    GC count: %d
                    GC time: %.2f ms
                    Avg GC pause: %.2f ms
                    Full GC count: %d
                    --------------------------------
                    """,
					count,
					timeMs,
					peakHeapMb,
					gcDiff.gcCount(),
					gcDiff.gcTimeMs(),
					gcDiff.avgPauseMs(),
					gcDiff.fullGcCount()
			);
		}
	}

	private List<UserModel> generateUsers(int count) {
		List<UserModel> users = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			UserModel user = new UserModel();
			user.setFirstName(faker.name().firstName());
			user.setLastName(faker.name().lastName());
			user.setEmail(faker.internet().emailAddress());
			user.setBirthDate(faker.date().birthday());
			Address address = new Address();
			address.setCity(faker.address().city());
			address.setStreet(faker.address().streetName());
			address.setZip(faker.address().zipCode());
			user.setAddress(address);
			user.setActive(faker.bool().bool());
			List<String> roles = List.of(faker.hobbit().character(), faker.hobbit().character());
			user.setRoles(roles);
			user.setCreatedAt(faker.date().past(10, TimeUnit.HOURS));
			users.add(user);
		}
		return users;
	}
	record GcSnapshot(
			long gcCount,
			double gcTimeMs,
			long fullGcCount
	) {

		static GcSnapshot capture(MeterRegistry registry) {
			Timer allGc = registry.find("jvm.gc.pause").timer();

			long count = allGc == null ? 0 : allGc.count();
			double time = allGc == null ? 0 : allGc.totalTime(TimeUnit.MILLISECONDS);

			Timer fullGc = registry.find("jvm.gc.pause")
					.tag("action", "end of major GC")
					.timer();

			long fullCount = fullGc == null ? 0 : fullGc.count();

			return new GcSnapshot(count, time, fullCount);
		}

		GcDiff diff(GcSnapshot before) {
			long deltaCount = this.gcCount - before.gcCount;
			double deltaTime = this.gcTimeMs - before.gcTimeMs;
			long deltaFull = this.fullGcCount - before.fullGcCount;

			double avgPause = deltaCount == 0 ? 0 : deltaTime / deltaCount;

			return new GcDiff(deltaCount, deltaTime, avgPause, deltaFull);
		}
	}
	record GcDiff(
			long gcCount,
			double gcTimeMs,
			double avgPauseMs,
			long fullGcCount
	) {}
}
