package com.scheid.jsondiff.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scheid.jsondiff.entity.Comparison;
import com.scheid.jsondiff.entity.Element;
import com.scheid.jsondiff.entity.Position;

/**
 * Repository for {@link Element}
 */
@Repository
public interface ElementRepository extends JpaRepository<Element, Long>{

	List<Element> findByPositionAndComparison(Position position, Comparison comparison);
	List<Element> findByComparison(Comparison comparison);
}
