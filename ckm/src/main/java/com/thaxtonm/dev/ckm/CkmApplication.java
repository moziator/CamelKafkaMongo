package com.thaxtonm.dev.ckm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@SpringBootApplication
public class CkmApplication {

	@Value("tmd.mongo.url")
	private String mongoURL;

	public static void main(String[] args) {
		SpringApplication.run(CkmApplication.class, args);
	}

	@Bean("tmdMongo")
	public MongoClient mongoClient() {
		ConnectionString connectionString = new ConnectionString(mongoURL);
		MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
			.applyConnectionString(connectionString)
			.build();
		
			return MongoClients.create(mongoClientSettings);
	}

}
