package com.scheid.jsondiff.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.scheid.jsondiff.JsonDiffApplicationTests;
import com.scheid.jsondiff.entity.Comparison;
import com.scheid.jsondiff.entity.Element;
import com.scheid.jsondiff.entity.Position;
import com.scheid.jsondiff.exception.JsonDiffException;
import com.scheid.jsondiff.repository.ComparisonRepository;
import com.scheid.jsondiff.repository.ElementRepository;
import com.scheid.jsondiff.service.compare.dto.ComparisonResult;
import com.scheid.jsondiff.service.messages.JsonDiffMessages;

public class ComparisonServiceTest extends JsonDiffApplicationTests {

	@Autowired
	ComparisonService jsonDiffService;
	
	@Autowired
	JsonDiffMessages messages;
	
	@Autowired
	ComparisonRepository comparisonRepository;
	
	@Autowired
	ElementRepository elementRepository;
	
	@Test
	public void should_add_element_at_fixed_position() {
		final String identifier = generateIdentifier();
		jsonDiffService.addElement(firstBase64EncodedJSON, identifier, Position.LEFT);
		
		Comparison comparison = comparisonRepository.findByIdentifier(identifier);
		assertThat(comparison).isNotNull();
		List<Element> elements = elementRepository.findByPositionAndComparison(Position.LEFT, comparison);
		assertThat(elements).isNotNull();
		assertTrue(elements.size() == 1);
		assertThat(elements.get(0).getPosition()).isEqualTo(Position.LEFT);
	}
	
	@Test
	public void should_add_elements_at_undefined_position() {
		final String identifier = generateIdentifier();
		jsonDiffService.addElement(firstBase64EncodedJSON, identifier);
		jsonDiffService.addElement(firstBase64EncodedJSON, identifier);
		jsonDiffService.addElement(firstBase64EncodedJSON, identifier);
		
		Comparison comparison = comparisonRepository.findByIdentifier(identifier);
		assertThat(comparison).isNotNull();
		List<Element> elements = elementRepository.findByComparison(comparison);
		assertThat(elements).isNotNull();
		assertThat(elements.size()).isEqualTo(3);
		for(Element element : elements) {
			assertThat(element.getValue()).isEqualTo(firstBase64EncodedJSON);
			assertThat(element.getPosition()).isEqualTo(Position.UNDEFINED);
		}
	}
	
	@Test
	public void should_replace_element_at_same_fixed_position() {
		final String identifier = generateIdentifier();
		jsonDiffService.addElement(firstBase64EncodedJSON, identifier, Position.LEFT);
		jsonDiffService.addElement(secondBase64EncodedJSON, identifier, Position.LEFT);
		
		Comparison comparison = comparisonRepository.findByIdentifier(identifier);
		assertThat(comparison).isNotNull();
		List<Element> elements = elementRepository.findByPositionAndComparison(Position.LEFT, comparison);
		assertThat(elements).isNotNull();
		assertTrue(elements.size() == 1);
		assertThat(elements.get(0).getPosition()).isEqualTo(Position.LEFT);
		assertThat(elements.get(0).getValue()).isEqualTo(secondBase64EncodedJSON);
	}
	
	@Test
	public void should_not_compare_only_one_element() {
		final String identifier = generateIdentifier();
		jsonDiffService.addElement(firstBase64EncodedJSON, identifier, Position.LEFT);
		
		ComparisonResult comparisonResult = jsonDiffService.compare(identifier);
		assertThat(comparisonResult).isNotNull();
		assertThat(comparisonResult.getDiffedElements()).isNullOrEmpty();
		assertThat(comparisonResult.getComparisonError()).isEqualTo(messages.getMessage("error.not.enough.elements"));
	}
	
	@Test
	public void should_not_compare_empty_comparisons() {
		final String identifier = generateIdentifier();
		
		ComparisonResult comparisonResult = jsonDiffService.compare(identifier);
		assertThat(comparisonResult).isNotNull();
		assertThat(comparisonResult.getDiffedElements()).isNullOrEmpty();
		assertThat(comparisonResult.getComparisonError()).isEqualTo(messages.getMessage("error.empty.comparison"));
	}
	
	@Test
	public void should_not_compare_invalid_data() {
		final String identifier = generateIdentifier();
		jsonDiffService.addElement("´´invalid\\", identifier, Position.LEFT);
		jsonDiffService.addElement(secondBase64EncodedJSON, identifier, Position.RIGHT);
		
		ComparisonResult comparisonResult = jsonDiffService.compare(identifier);
		assertThat(comparisonResult).isNotNull();
		assertThat(comparisonResult.getDiffedElements()).isNullOrEmpty();
		assertThat(comparisonResult.getComparisonError()).isEqualTo(messages.getMessage("error.invalid.json"));
	}
	
	@Test(expected = JsonDiffException.class)
	public void should_handle_invalid_identifier() {
		String invalidIdentifier = generateInvalidIdentifier();
		jsonDiffService.addElement(firstBase64EncodedJSON, invalidIdentifier, Position.LEFT);
	}
	
	@Test
	public void should_fully_delete_comparisons() {
		final String identifier = generateIdentifier();
		jsonDiffService.addElement(firstBase64EncodedJSON, identifier, Position.LEFT);
		jsonDiffService.addElement(secondBase64EncodedJSON, identifier, Position.LEFT);
		
		jsonDiffService.deleteComparison(identifier);
		
		Comparison comparison = comparisonRepository.findByIdentifier(identifier);
		assertThat(comparison).isNull();
	}
}
