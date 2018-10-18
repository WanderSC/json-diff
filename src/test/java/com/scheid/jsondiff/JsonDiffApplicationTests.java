package com.scheid.jsondiff;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import com.scheid.jsondiff.service.util.JsonParserUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JsonDiffApplicationTests {

	/*
	 * 4 JSON files were generated for testing purposes:
	 * 
	 * base_data_1: base randomly generated JSON.
	 * base_data_2: same sized JSON, with a difference on the "balance" node value.
	 * base_data_3: different sized JSON, with a new node "other".
	 * base_data_4: same sized JSON, with different nodes under "friends"
	 * 
	 */
	
	
	@Value("classpath:base_data_1.json")
	Resource firstJsonFile;
	@Value("classpath:base_data_2.json")
	Resource secondJsonFile;
	@Value("classpath:base_data_3.json")
	Resource thirdJsonFile;
	@Value("classpath:base_data_4.json")
	Resource fourthJsonFile;
	
	protected String firstBase64EncodedJSON;
	protected String secondBase64EncodedJSON;
	protected String thirdBase64EncodedJSON;
	protected String fourthBase64EncodedJSON;
	
	@Before
	public void setup() {
		setupFirstJsonFile();
		setupSecondJsonFile();
		setupThirdJsonFile();
		setupFourthJsonFile();
	}
	
	@Test
	public void contextLoads() {}
	
	private void setupFirstJsonFile() {
		String json = readJsonFile(firstJsonFile);
		firstBase64EncodedJSON = JsonParserUtil.encodeBinaryBase64(json);
	}
	
	private void setupSecondJsonFile() {
		String json = readJsonFile(secondJsonFile);
		secondBase64EncodedJSON = JsonParserUtil.encodeBinaryBase64(json);
	}
	
	private void setupThirdJsonFile() {
		String json = readJsonFile(thirdJsonFile);
		thirdBase64EncodedJSON = JsonParserUtil.encodeBinaryBase64(json);
	}
	
	private void setupFourthJsonFile() {
		String json = readJsonFile(fourthJsonFile);
		fourthBase64EncodedJSON = JsonParserUtil.encodeBinaryBase64(json);
	}
	
	protected String readJsonFile(Resource jsonFile) {
		String json = "";
		try {
			json = IOUtils.toString(jsonFile.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	protected String generateIdentifier() {
		return String.valueOf(new Date().getTime());
	}

	protected String generateInvalidIdentifier() {
		StringBuilder invalid500CharactersIdentifier = new StringBuilder();
		for(int i=0; i<500; i++) {
			invalid500CharactersIdentifier.append('a');
		}
		return invalid500CharactersIdentifier.toString();
	}
	
}
