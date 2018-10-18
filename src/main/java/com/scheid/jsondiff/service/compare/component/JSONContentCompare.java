package com.scheid.jsondiff.service.compare.component;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.scheid.jsondiff.entity.Element;
import com.scheid.jsondiff.service.messages.DifferenceStringBuilder;
import com.scheid.jsondiff.service.util.JsonParserUtil;

/**
 * Content difference implementation for sets of base64 encoded jsons.  
 * Will only execute its logic if {@link JSONSizeCompare} can't identify a difference.
 * Will only be used if the 'content' profile is active.
 * Uses the {@link Maps} implementation from Guava for json map comparison, parsing the {@link MapDifference} result
 */
@Component
@Profile("content")
public class JSONContentCompare extends JSONSizeCompare {
	
	@Autowired
	DifferenceStringBuilder differenceStringBuilder;

	@Override
	public String getDifference(Element firstElement, Element secondElement) {
		String difference = super.getDifference(firstElement, secondElement);
		if(difference != null) {
			return difference;
		}
		String firstElementValue = JsonParserUtil.decodeBinaryBase64(firstElement.getValue());
		String secondElementValue = JsonParserUtil.decodeBinaryBase64(secondElement.getValue());
		
		Map<String, Object> firstElementMap = JsonParserUtil.getJsonMap(firstElementValue);
		Map<String, Object> secondElementMap = JsonParserUtil.getJsonMap(secondElementValue);
		
		MapDifference<String, Object> mapDifference = Maps.difference(firstElementMap, secondElementMap);
		
		if(mapDifference.areEqual()) {
			return null;
		}
		
		return differenceStringBuilder.buildContentDifferenceString(mapDifference);
		
	}
	
	

	
	

}
