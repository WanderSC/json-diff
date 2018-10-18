package com.scheid.jsondiff.service.compare;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.scheid.jsondiff.entity.Element;
import com.scheid.jsondiff.service.compare.dto.ComparisonResult;
import com.scheid.jsondiff.service.compare.dto.Diff;
import com.scheid.jsondiff.service.compare.dto.ElementPair;

/**
 * Default Methods for the {@link JsonDiffCompare} interface
 *
 */
public abstract class DefaultJSONCompare implements JsonDiffCompare {
	
	@Override
	public ComparisonResult compare(Set<ElementPair> elementPairs) {
		List<Diff> diffedPairs = new ArrayList<Diff>();
		for(ElementPair comparablePair : elementPairs) {
			Element firstElement = comparablePair.getFirstElement();
			Element secondElement = comparablePair.getSecondElement();
			String difference = getDifference(firstElement, secondElement);
			Diff diffedPair = new Diff(comparablePair, difference);
			diffedPairs.add(diffedPair);
		}
		return new ComparisonResult(diffedPairs);
	}
}
