package com.scheid.jsondiff.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.scheid.jsondiff.entity.Comparison;
import com.scheid.jsondiff.entity.Element;
import com.scheid.jsondiff.entity.Position;
import com.scheid.jsondiff.service.compare.dto.ComparisonResult;
import com.scheid.jsondiff.service.util.JsonParserUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JsonDiffIntegrationTest  {

	@Autowired	
	TestRestTemplate template;
	
	final String leftJson = "{\"_id\": \"5bc733466b9465e3c07fe61e\",\"index\": 0}";
	final String rightJson = "{\"_id\": \"5bc733466b9465e3c07fe61e\",\"index\": 1}";
	final String unfixedJson = "{\"_id\": \"5bc733466b9465e3c07fe61e\",\"index\": 2}";
	final static String DIFF_PATH = "/v1/diff"; 
	
	String leftBase64EncodedJSON;
	String rightBase64EncodedJSON;
	String unfixedBase64EncodedJSON;
	
	@Before
	public void setup() {
		leftBase64EncodedJSON = JsonParserUtil.encodeBinaryBase64(leftJson);
		rightBase64EncodedJSON = JsonParserUtil.encodeBinaryBase64(rightJson);
		unfixedBase64EncodedJSON = JsonParserUtil.encodeBinaryBase64(unfixedJson);
	}
	
	protected String generateIdentifier() {
		return String.valueOf(new Date().getTime());
	}
	
	@Test
	public void should_set_left_diff() {
		String id = generateIdentifier();
		Comparison comparison = template.postForObject(DIFF_PATH + "/" + id + "/left", leftBase64EncodedJSON, Comparison.class);
		assertThat(comparison).isNotNull();
		assertThat(comparison.getElements()).isNotEmpty();
		Element element = comparison.getElements().get(0);
		assertThat(element.getValue()).isEqualTo(leftBase64EncodedJSON);
		assertThat(element.getPosition()).isEqualTo(Position.LEFT);
	}
	
	@Test
	public void should_set_right_diff() {
		String id = generateIdentifier();
		Comparison comparison = template.postForObject(DIFF_PATH + "/" + id + "/right", rightBase64EncodedJSON, Comparison.class);
		assertThat(comparison).isNotNull();
		assertThat(comparison.getElements()).isNotEmpty();
		Element element = comparison.getElements().get(0);
		assertThat(element.getValue()).isEqualTo(rightBase64EncodedJSON);
		assertThat(element.getPosition()).isEqualTo(Position.RIGHT);
	}
	
	@Test
	public void should_add_element_at_unfixed_position() {
		String id = generateIdentifier();
		Comparison comparison = template.postForObject(DIFF_PATH + "/" + id + "/add", unfixedBase64EncodedJSON, Comparison.class);
		assertThat(comparison).isNotNull();
		assertThat(comparison.getElements()).isNotEmpty();
		Element element = comparison.getElements().get(0);
		assertThat(element.getValue()).isEqualTo(unfixedBase64EncodedJSON);
		assertThat(element.getPosition()).isEqualTo(Position.UNDEFINED);
	}
	
	@Test
	public void should_compare_two_elements() {
		String id = generateIdentifier();
		template.postForObject(DIFF_PATH + "/" + id + "/left", leftBase64EncodedJSON, Comparison.class);
		template.postForObject(DIFF_PATH + "/" + id + "/right", rightBase64EncodedJSON, Comparison.class);
		ComparisonResult comparisonResult = template.getForObject(DIFF_PATH + "/" + id, ComparisonResult.class);
		assertThat(comparisonResult).isNotNull();
		assertThat(comparisonResult.getDiffedElements()).isNotEmpty();
	}
	
	@Test
	public void should_compare_several_elements() {
		String id = generateIdentifier();
		template.postForObject(DIFF_PATH + "/" + id + "/left", leftBase64EncodedJSON, Comparison.class);
		template.postForObject(DIFF_PATH + "/" + id + "/right", rightBase64EncodedJSON, Comparison.class);
		template.postForObject(DIFF_PATH + "/" + id + "/add", unfixedBase64EncodedJSON, Comparison.class);
		template.postForObject(DIFF_PATH + "/" + id + "/add", unfixedBase64EncodedJSON, Comparison.class);
		template.postForObject(DIFF_PATH + "/" + id + "/add", unfixedBase64EncodedJSON, Comparison.class);
		ComparisonResult comparisonResult = template.getForObject(DIFF_PATH + "/" + id, ComparisonResult.class);
		assertThat(comparisonResult).isNotNull();
		assertThat(comparisonResult.getDiffedElements().size()).isGreaterThan(5);
	}
	
	@Test
	public void should_not_return_difference_for_same_element() {
		String id = generateIdentifier();
		template.postForObject(DIFF_PATH + "/" + id + "/left", leftBase64EncodedJSON, Comparison.class);
		template.postForObject(DIFF_PATH + "/" + id + "/right", leftBase64EncodedJSON, Comparison.class);
		ComparisonResult comparisonResult = template.getForObject(DIFF_PATH + "/" + id, ComparisonResult.class);
		assertThat(comparisonResult).isNotNull();
		assertTrue(comparisonResult.isAllEqual());
	}

	@Test
	public void should_list_comparison_elements() {
		String id = generateIdentifier();
		template.postForObject(DIFF_PATH + "/" + id + "/left", leftBase64EncodedJSON, Comparison.class);
		template.postForObject(DIFF_PATH + "/" + id + "/right", rightBase64EncodedJSON, Comparison.class);
		Comparison comparison = template.getForObject(DIFF_PATH + "/" + id + "/view", Comparison.class);
		assertThat(comparison).isNotNull();
		assertThat(comparison.getElementAtPostion(Position.LEFT).getValue()).isEqualTo(leftBase64EncodedJSON);
		assertThat(comparison.getElementAtPostion(Position.RIGHT).getValue()).isEqualTo(rightBase64EncodedJSON);
	}
	
	@Test
	public void should_remove_comparison() {
		String id = generateIdentifier();
		template.postForObject(DIFF_PATH + "/" + id + "/left", leftBase64EncodedJSON, Comparison.class);
		template.postForObject(DIFF_PATH + "/" + id + "/right", rightBase64EncodedJSON, Comparison.class);
		template.delete(DIFF_PATH + "/" + id);
		
		Comparison comparison = template.getForObject(DIFF_PATH + "/" + id + "/view", Comparison.class);
		assertThat(comparison).isNull();
	}
	
}
