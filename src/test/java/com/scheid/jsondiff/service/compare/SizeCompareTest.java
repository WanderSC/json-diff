package com.scheid.jsondiff.service.compare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import com.scheid.jsondiff.JsonDiffApplicationTests;
import com.scheid.jsondiff.entity.Element;
import com.scheid.jsondiff.entity.Position;
import com.scheid.jsondiff.service.ComparisonService;
import com.scheid.jsondiff.service.compare.component.JSONSizeCompare;
import com.scheid.jsondiff.service.compare.dto.ComparisonResult;
import com.scheid.jsondiff.service.compare.dto.Diff;
import com.scheid.jsondiff.service.compare.dto.ElementPair;
import com.scheid.jsondiff.service.messages.JsonDiffMessages;

@ActiveProfiles("size")
public class SizeCompareTest extends JsonDiffApplicationTests {
	
	@Autowired
	JSONSizeCompare sizeCompare;
	
	@Autowired
	JsonDiffMessages messages;
	
	@Autowired
	ComparisonService jsonDiffService;
	

	@Test
	public void should_not_return_difference_on_same_element() {
		Element firstElement = new Element(firstBase64EncodedJSON, Position.LEFT);
		Element secondElement = new Element(firstBase64EncodedJSON, Position.RIGHT);
		ElementPair elementPair = new ElementPair(firstElement, secondElement);
		ComparisonResult comparisonResult = sizeCompare.compare(new HashSet<ElementPair>(Arrays.asList(elementPair)));
		assertThat(comparisonResult).isNotNull();
		assertThat(comparisonResult.getComparisonError()).isNullOrEmpty();
		assertTrue(comparisonResult.isAllEqual());
	}
	
	@Test
	public void should_not_return_difference_on_same_sized_elements() {
		Element firstElement = new Element(firstBase64EncodedJSON, Position.LEFT);
		Element secondElement = new Element(secondBase64EncodedJSON, Position.RIGHT);
		ElementPair elementPair = new ElementPair(firstElement, secondElement);
		ComparisonResult comparisonResult = sizeCompare.compare(new HashSet<ElementPair>(Arrays.asList(elementPair)));
		assertThat(comparisonResult).isNotNull();
		assertThat(comparisonResult.getComparisonError()).isNullOrEmpty();
		assertTrue(comparisonResult.isAllEqual());
	}
	
	@Test
	public void should_return_different_size_on_different_elements() {
		Element firstElement = new Element(firstBase64EncodedJSON, Position.LEFT);
		Element secondElement = new Element(thirdBase64EncodedJSON, Position.RIGHT);
		ElementPair elementPair = new ElementPair(firstElement, secondElement);
		ComparisonResult comparisonResult = sizeCompare.compare(new HashSet<ElementPair>(Arrays.asList(elementPair)));
		assertThat(comparisonResult).isNotNull();
		assertThat(comparisonResult.getComparisonError()).isNullOrEmpty();
		Diff diff = comparisonResult.getDiffedElements().get(0);
		assertThat(diff.getDifference()).contains(messages.getMessage("diff.size.difference"));
	}
	
	@Test
	public void should_compare_multiple_pairs() {
		Element firstElement = new Element(firstBase64EncodedJSON, Position.LEFT);
		Element secondElement = new Element(secondBase64EncodedJSON, Position.RIGHT);
		Element thirdElement = new Element(thirdBase64EncodedJSON, Position.UNDEFINED);
		
		ElementPair firstElementPair = new ElementPair(firstElement, secondElement);
		ElementPair secondElementPair = new ElementPair(firstElement, thirdElement);
		ElementPair thirdElementPair = new ElementPair(secondElement, thirdElement);
		List<ElementPair> elementPairs = Arrays.asList(firstElementPair, secondElementPair, thirdElementPair);
		
		ComparisonResult comparisonResult = sizeCompare.compare(new HashSet<ElementPair>(elementPairs));
		assertThat(comparisonResult).isNotNull();
		assertThat(comparisonResult.getComparisonError()).isNullOrEmpty();
		assertThat(comparisonResult.getDiffedElements().size()).isEqualTo(3);
	}
	
}
