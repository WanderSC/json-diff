package com.scheid.jsondiff.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scheid.jsondiff.entity.Comparison;
import com.scheid.jsondiff.entity.Element;
import com.scheid.jsondiff.entity.Position;
import com.scheid.jsondiff.exception.JsonDiffException;
import com.scheid.jsondiff.repository.ComparisonRepository;
import com.scheid.jsondiff.service.compare.JsonDiffCompare;
import com.scheid.jsondiff.service.compare.dto.ComparisonResult;
import com.scheid.jsondiff.service.compare.dto.ElementPair;
import com.scheid.jsondiff.service.messages.JsonDiffMessages;

/**
 * Service to interface components that execute the comparison. 
 * Persists the {@link Comparison} and its {@link Element}s. 
 * 
 */
@Service
public class ComparisonService {

	private static final int MAXIMUM_COMPARISON_IDENTIFIER_LENGHT = 255;

	@Autowired 
	private JsonDiffCompare comparator;
	
	@Autowired 
	private ComparisonRepository comparisonRepository;
	
	@Autowired 
	private JsonDiffMessages messages;
	
	/**
	 * Compares all {@link Element}s found on the {@link Comparison} with the provided identifier.
	 * 
	 * At least two valid Elements are required for a comparison.
	 * A comparison error, if present, will be returned.
	 *  
	 * @param comparisonIdentifier Identifier for the {@link Comparison} to be processed.
	 * @return {@link ComparisonResult} The comparison result for each {@link Element} found. 
	 */
	public ComparisonResult compare(String comparisonIdentifier) {
		validateIdentifer(comparisonIdentifier);
		Comparison comparison = comparisonRepository.findByIdentifier(comparisonIdentifier);
		if(comparison == null) {
			return new ComparisonResult(messages.getMessage("error.empty.comparison"));
		}
		Set<ElementPair> comparablePairs = comparison.getAllElementPairs();
		if(comparablePairs == null || comparablePairs.isEmpty()) {
			return new ComparisonResult(messages.getMessage("error.not.enough.elements"));
		}
		try {
			return comparator.compare(comparablePairs);
		}catch(JsonDiffException e) {
			return new ComparisonResult(messages.getMessage(e.getMessage()));
		}
	}
	
	/**
	 * Find a {@link Comparison} by its identifier.
	 * 
	 * @param comparisonIdentifier Unique identifier for the {@link Comparison}.
	 * @return {@link Comparison} The complete comparison, with its {@link Element}s
	 */
	public Comparison findComparisonByIdentifier(String comparisonIdentifier) {
		validateIdentifer(comparisonIdentifier);
		return comparisonRepository.findByIdentifier(comparisonIdentifier);
	}
	
	/**
	 * Add a new {@link Element} to a {@link Comparison}.
	 * 
	 * The provided value will replace a previous element if the provided position was already occupied
	 * on the comparison.
	 * 
	 * @param base64String Element value assumed to be a JSON base64 encoded binary data.
	 * @param comparisonIdentifier Unique identifier for the {@link Comparison}.
	 * @param position {@link Element}'s {@link Position} in the {@link Comparison}.
	 * @return {@link Comparison} The updated or created comparison, with its {@link Element}s
	 */
	public Comparison addElement(String base64String, String comparisonIdentifier, Position position) {
		validateIdentifer(comparisonIdentifier);
		
		Comparison comparison = comparisonRepository.findByIdentifier(comparisonIdentifier);
		if(comparison == null) {
			comparison = new Comparison();
			comparison.setIdentifier(comparisonIdentifier);
		}
		Element comparable = new Element(base64String, position, comparison);
		comparison.addComparable(comparable);
		comparisonRepository.save(comparison);
		return comparison;
	}
	
	/**
	 * Add a new {@link Element} to a {@link Comparison} with the provided element's value and 
	 * comparison's identifier.
	 *  
	 * The provided value will not assume a fixed {@link Position} and will not replace any previous elements
	 * in the comparison.  
	 * 
	 * @param base64String Element value assumed to be a JSON base64 encoded binary data.
	 * @param comparisonIdentifier Unique identifier for the {@link Comparison}.
	 * @return {@link Comparison} The updated or created comparison, with its {@link Element}s
	 */
	public Comparison addElement(String base64String, String comparisonIdentifier) {
		return addElement(base64String, comparisonIdentifier, Position.UNDEFINED);
	}
	
	/**
	 * Delete a {@link Comparison} 
	 *   
	 * @param comparisonIdentifier Unique identifier for the {@link Comparison}.
	 * @return true if the Comparison was successfully deleted.
	 */
	public boolean deleteComparison(String comparisonIdentifier) {
		validateIdentifer(comparisonIdentifier);
		Comparison comparison = comparisonRepository.findByIdentifier(comparisonIdentifier);
		if(comparison == null) {
			return false;
		}
		comparisonRepository.delete(comparison);
		return true;
	}
	
	private void validateIdentifer(String comparisonIdentifier) {
		if(comparisonIdentifier.length() > MAXIMUM_COMPARISON_IDENTIFIER_LENGHT) {
			throw new JsonDiffException("error.invalid.identifier");
		}
	}
	
}
