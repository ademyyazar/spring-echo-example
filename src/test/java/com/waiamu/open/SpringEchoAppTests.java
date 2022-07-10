package com.waiamu.open;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waiamu.open.controller.EchoController;
import com.waiamu.open.dto.JsonPayload;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SpringEchoAppTests {

	@Autowired
	private EchoController controller;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@LocalServerPort
	private int port;

	Instant now = null;
	String randomId = null;
	HttpHeaders headers = null;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

	@BeforeEach
	void beforeEach() {
		now = Instant.now();
		randomId = UUID.randomUUID().toString();

		headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		headers.set(HttpHeaders.ACCEPT_LANGUAGE, "en-US");
		headers.set(HttpHeaders.COOKIE, "roses=red; violets=blue");
	}

	@Test
	void given_post_request_when_submit_should_echo_back_values() throws JsonProcessingException {

		// Given
		final String url = String.format("http://localhost:%d/submit?now={now}", port);
		final HttpEntity<byte[]> postRequest = preparePayload();

		// When
		final ResponseEntity<JsonPayload> response = restTemplate.exchange(url,
				HttpMethod.POST, postRequest, JsonPayload.class, now.toEpochMilli());

		// Then
		assertResponseStatusIsNotFound(response);
		assertResponseBodyHasKeys(response.getBody());
		assertResponseBodyHasEcho(response.getBody().getAttributes(), postRequest.getBody());
	}

	private void assertResponseBodyHasEcho(Map<String, Object> attributes, byte[] expectedBody) {
		// All parameters got echoed back
		assertThat(attributes.get(JsonPayload.PARAMETERS))
		.extracting("now").asList().first().isEqualTo(Long.toString(now.toEpochMilli()));
	}

	private HttpEntity<byte[]> preparePayload() throws JsonProcessingException {
		final JsonPayload payload = new JsonPayload();
		payload.set("randomId", randomId);

		return new HttpEntity<byte[]>(objectMapper.writeValueAsBytes(payload), headers);
	}

	private void assertResponseBodyHasKeys(JsonPayload payload) {
		assertThat(payload).isNotNull();
		assertThat(payload.getAttributes()).isNotNull();
		assertThat(payload.getAttributes()).containsKey(JsonPayload.PARAMETERS);
	}

	private void assertResponseStatusIsNotFound(ResponseEntity<JsonPayload> response) {
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}



}
