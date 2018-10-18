package com.scheid.jsondiff.service.compare.dto;

import java.io.Serializable;

import com.scheid.jsondiff.entity.Element;

/**
 * Holds a pair of elements for comparison.
 * An ElementPair is equal to another if its {@link Element}s are the same, regardless of {@link Element} order.
 */
public class ElementPair implements Serializable {

	private static final long serialVersionUID = 3924293325054755811L;
	
	private Element firstElement;
	private Element secondElement;
	
	public ElementPair(Element firstElement, Element secondElement) {
		this.firstElement = firstElement;
		this.secondElement = secondElement;
	}
	
	public Element getFirstElement() {
		return firstElement;
	}
	
	public void setFirstElement(Element firstElement) {
		this.firstElement = firstElement;
	}
	
	public Element getSecondElement() {
		return secondElement;
	}
	
	public void setSecondElement(Element secondElement) {
		this.secondElement = secondElement;
	}

	@Override
	public int hashCode() {
		int result = ((firstElement == null) ? 0 : firstElement.hashCode());
		result += ((secondElement == null) ? 0 : secondElement.hashCode());
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
		ElementPair other = (ElementPair) obj;
		if (firstElement == null) {
			if (other.firstElement != null)
				return false;
		} else if (!firstElement.equals(other.firstElement) 
				&& !(firstElement.equals(other.secondElement) && other.firstElement.equals(secondElement)))
			return false;
		if (secondElement == null) {
			if (other.secondElement != null)
				return false;
		} else if (!secondElement.equals(other.secondElement)
				&& !(firstElement.equals(other.secondElement) && other.firstElement.equals(secondElement))) 
			return false;
		
		return true;
	}
	
	
}
