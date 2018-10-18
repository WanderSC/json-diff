package com.scheid.jsondiff.service.messages;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.MapDifference;

/**
 * Component used to build the messages displayed when a difference is found.
 */
@Component
public class DifferenceStringBuilder {

	@Autowired
	JsonDiffMessages messages;
	
	public String buildSizeDifferenceString(int firstElementSize, int secondElementSize) {
		return buildDifferenceString(messages.getMessage("diff.size.difference"), 
				String.valueOf(firstElementSize), 
				String.valueOf(secondElementSize));
	}
	
	public String buildContentDifferenceString(MapDifference<String, Object> mapDifference) {
		return buildDifferenceString(messages.getMessage("diff.content.difference"), 
				buildFirstElementDifferenceString(mapDifference), 
				buildSecondElementDifferenceString(mapDifference));
	}
	
	public String buildDifferenceString(String differenceType, String firstElementDiff, String secondElementDiff) {
		StringBuilder sb = new StringBuilder();
		sb.append(differenceType);
		sb.append(": ");
		sb.append(messages.getMessage("diff.first.element"));
		sb.append(": ");
		sb.append(firstElementDiff);
		sb.append("; ");
		sb.append(messages.getMessage("diff.second.element"));
		sb.append(": ");
		sb.append(secondElementDiff);
		return sb.toString();
	}

	public String buildFirstElementDifferenceString(MapDifference<String, Object> mapDifference) {
		Set<String> entriesOnlyOnFirstElement = mapDifference.entriesOnlyOnLeft().keySet();
		if(entriesOnlyOnFirstElement.isEmpty()) {
			return messages.getMessage("diff.no.difference");
		}
		StringBuilder sb = new StringBuilder();
		sb.append(messages.getMessage("diff.first.element.nodes"));
		sb.append(entriesOnlyOnFirstElement.toString());
		return sb.toString();
	}
	
	public String buildSecondElementDifferenceString(MapDifference<String, Object> mapDifference) {
		Set<String> entriesOnlyOnSecondElement = mapDifference.entriesOnlyOnRight().keySet();
		StringBuilder sb = new StringBuilder();
		if(entriesOnlyOnSecondElement.isEmpty()) {
			sb.append(messages.getMessage("diff.no.difference"));
		}else {
			sb.append(messages.getMessage("diff.second.element.nodes"));
			sb.append(entriesOnlyOnSecondElement.toString());
		}
		Set<String> entriesDiffering = mapDifference.entriesDiffering().keySet();
		if(entriesDiffering.isEmpty()) {
			return sb.toString();
		}
		sb.append("; ");
		sb.append(messages.getMessage("diff.node.content"));
		sb.append(entriesDiffering.toString());
		return sb.toString();
	}
	
}
