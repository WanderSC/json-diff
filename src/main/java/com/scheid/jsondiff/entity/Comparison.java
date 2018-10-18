package com.scheid.jsondiff.entity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.scheid.jsondiff.service.compare.dto.ElementPair;

/**
 * Entity Used to hold a set of {@link Element}s under a provided identifier 
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "identifier")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class Comparison {

	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String identifier;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "comparison", cascade = CascadeType.ALL)
	private List<Element> elements;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Element> getElements() {
		return elements;
	}

	public void setElements(List<Element> elements) {
		this.elements = elements;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public void addComparable(Element element) {
		if(elements == null) {
			elements = new LinkedList<Element>();
		}
		if(!element.getPosition().isFixedPosition()) {
			elements.add(element);
			return;
		}
		Element previousComparableAtPosition = getElementAtPostion(element.getPosition());
		if(previousComparableAtPosition != null) {
			previousComparableAtPosition.setValue(element.getValue());
			return;
		}
		elements.add(element);
	}
	
	public Element getElementAtPostion(Position position) {
		for(Element element : elements) {
			if(element.getPosition().equals(position)) {
				return element;
			}
		}
		return null;
	}
	
	public Set<ElementPair> getAllElementPairs(){
		if(elements == null || elements.size() < 2) {
			return null;
		}
		
		Set<ElementPair> elementPairs = new HashSet<ElementPair>();
		
		for(Element firstComparable : elements) {
			for(Element secondComparable : elements) {
				if(!firstComparable.equals(secondComparable)) {
					elementPairs.add(new ElementPair(firstComparable, secondComparable));
				}
			}
		}
		return elementPairs;
	}
	
}
