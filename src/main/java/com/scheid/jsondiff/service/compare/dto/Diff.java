package com.scheid.jsondiff.service.compare.dto;

import java.io.Serializable;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Holds a single comparison between two elements and its result.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Diff implements Serializable {

	private static final long serialVersionUID = -709886981243039776L;

	private ElementPair comparison;
	private String difference;
	
	public Diff(ElementPair elementPair, String difference) {
		this.comparison = elementPair;
		this.difference = difference;
	}
	
	public boolean isEqual() {
		return StringUtils.isEmpty(difference);
	}
	
	public String getDifference() {
		return difference;
	}
	
	public void setDifference(String difference) {
		this.difference = difference;
	}

	public ElementPair getComparison() {
		return comparison;
	}

	public void setComparison(ElementPair comparison) {
		this.comparison = comparison;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comparison == null) ? 0 : comparison.hashCode());
		result = prime * result + ((difference == null) ? 0 : difference.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Diff other = (Diff) obj;
		if (comparison == null) {
			if (other.comparison != null)
				return false;
		} else if (!comparison.equals(other.comparison))
			return false;
		if (difference == null) {
			if (other.difference != null)
				return false;
		} else if (!difference.equals(other.difference))
			return false;
		return true;
	}
	
	
}
