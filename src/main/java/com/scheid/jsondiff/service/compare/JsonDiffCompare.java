package com.scheid.jsondiff.service.compare;

import java.util.Set;

import com.scheid.jsondiff.entity.Element;
import com.scheid.jsondiff.service.compare.dto.ComparisonResult;
import com.scheid.jsondiff.service.compare.dto.ElementPair;

/**
 * Interface to allow multiple implementations for JSON base64 encoded binary data comparison.
 * 
 */
public interface JsonDiffCompare {
	
	/**
	 * Compare a Set of {@link ElementPair} to determine the possible differences.
	 * 
	 * @param elementPairs Sets of {@link ElementPair} to be compared
	 * @return {@link ComparisonResult} The comparison result for each {@link ElementPair} provided. A comparison error, if present, will be displayed on comparisonError.   
	 */
	ComparisonResult compare(Set<ElementPair> elementPairs);
	
	/**
	 * Compare two {@link Element}s to determine the possible difference  
	 * 
	 * @param firstComparable First {@link Element} to compare
	 * @param secondComparable Second {@link Element} to compare 
	 * @return {@link String} The difference, if found. Equal {@link Element}s will result in a 'null' difference.      
	 */
	String getDifference(Element firstComparable, Element secondComparable);
}
