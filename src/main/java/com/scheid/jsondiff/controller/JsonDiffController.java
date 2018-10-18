package com.scheid.jsondiff.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.scheid.jsondiff.entity.Comparison;
import com.scheid.jsondiff.entity.Element;
import com.scheid.jsondiff.entity.Position;
import com.scheid.jsondiff.service.ComparisonService;
import com.scheid.jsondiff.service.compare.dto.ComparisonResult;

/**
 * REST Controller with the main JsonDiff Endpoints. 
 */
@RestController
@RequestMapping(value="/v1/diff")
public class JsonDiffController {
	
	@Autowired
	ComparisonService comparisonService;
	
	/**
	 * Set the left element of a comparison 
	 * 
	 * @param base64String Element value assumed to be a JSON base64 encoded binary data.
	 * @param id Unique identifier for the comparison.
	 * @return {@link Comparison} The updated or created comparison, with its {@link Element}s
	 */
	@RequestMapping(value="/{id}/left", method=RequestMethod.POST)
	public Comparison setLeftElement(@RequestBody String base64String, @PathVariable String id){
		return comparisonService.addElement(base64String, id, Position.LEFT);
	}
	
	/**
	 * Set the right element of a comparison 
	 * 
	 * @param base64String Element value assumed to be a JSON base64 encoded binary data.
	 * @param id Unique identifier for the comparison.
	 * @return {@link Comparison} The updated or created comparison, with its {@link Element}s
	 */
	@RequestMapping(value="/{id}/right", method=RequestMethod.POST)
	public Comparison setRightElement(@RequestBody String base64String, @PathVariable String id){
		return comparisonService.addElement(base64String, id, Position.RIGHT);
	}
	
	/**
	 * Add a new element to a comparison, at a unfixed position 
	 * 
	 * @param base64String Element value assumed to be a JSON base64 encoded binary data.
	 * @param id Unique identifier for the comparison.
	 * @return {@link Comparison} The updated or created comparison, with its {@link Element}s
	 */
	@RequestMapping(value="/{id}/add", method=RequestMethod.POST)
	public Comparison addElement(@RequestBody String base64String, @PathVariable String id){
		return comparisonService.addElement(base64String, id);
	}
	
	/**
	 * Verify the difference between the elements in a certain comparison 
	 * 
	 * @param id Unique identifier for the comparison.
	 * @return {@link ComparisonResult} The comparison's result
	 */
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ComparisonResult diff(@PathVariable String id){
		return comparisonService.compare(id);
	}
	
	/**
	 * List the current elements of a comparison 
	 * 
	 * @param id Unique identifier for the comparison.
	 * @return {@link Comparison} The comparison, with its {@link Element}s
	 */
	@RequestMapping(value="/{id}/view", method=RequestMethod.GET)
	public Comparison viewComparison(@PathVariable String id){
		return comparisonService.findComparisonByIdentifier(id);
	}
	
	/**
	 * Delete a comparison and its elements 
	 * 
	 * @param id Unique identifier for the comparison.
	 * @return {@link Comparison} Ok Status if the comparison was found and successfully deleted.
	 */
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<String> deleteComparison(@PathVariable String id){
		if(comparisonService.deleteComparison(id)) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

}
