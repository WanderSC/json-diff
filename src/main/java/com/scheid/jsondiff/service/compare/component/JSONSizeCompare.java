package com.scheid.jsondiff.service.compare.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.scheid.jsondiff.entity.Element;
import com.scheid.jsondiff.service.compare.DefaultJSONCompare;
import com.scheid.jsondiff.service.messages.DifferenceStringBuilder;
import com.scheid.jsondiff.service.messages.JsonDiffMessages;
import com.scheid.jsondiff.service.util.JsonParserUtil;

/**
 * Size difference implementation for sets of Base64 encoded jsons.  
 * Will be the only implementation used if the 'size' profile is active.
 * Compares the binary data size of the provided {@link Element} pair's base64 value. 
 */
@Component
@Profile("size")
public class JSONSizeCompare extends DefaultJSONCompare {
	
	@Autowired
	DifferenceStringBuilder differenceStringBuilder;
	
	@Autowired
	JsonDiffMessages messages;
	
	@Override
	public String getDifference(Element firstElement, Element secondElement) {
		byte[] firstElementValue = JsonParserUtil.getBinaryFromBase64(firstElement.getValue());
		byte[] secondElementValue = JsonParserUtil.getBinaryFromBase64(secondElement.getValue());
		if(firstElementValue.length == secondElementValue.length) {
			return null;
		}
		return differenceStringBuilder.buildSizeDifferenceString(firstElementValue.length, 
																secondElementValue.length);
	}
	
}
