package com.scheid.jsondiff.service.compare.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Json returned by the rest services with the comparison results 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComparisonResult implements Serializable {

	private static final long serialVersionUID = -4546956203508668485L;
	
	private String comparisonError;
	private List<Diff> diffedElements;
	
	public ComparisonResult() {}
	
	public ComparisonResult(List<Diff> diffedComparables) {
		this.setDiffedElements(diffedComparables);
	}
	
	public ComparisonResult(String comparisonError) {
		this.comparisonError = comparisonError;
	}
	
	public boolean isAllEqual() {
		if(getDiffedElements() == null) {
			return false;
		}
		for(Diff diff : getDiffedElements()) {
			if(!diff.isEqual()) {
				return false;
			}
		}
		return true;
	}

	public String getComparisonError() {
		return comparisonError;
	}

	public void setComparisonError(String comparisonError) {
		this.comparisonError = comparisonError;
	}

	public List<Diff> getDiffedElements() {
		return diffedElements;
	}

	public void setDiffedElements(List<Diff> diffedComparables) {
		this.diffedElements = diffedComparables;
	}
	
}
