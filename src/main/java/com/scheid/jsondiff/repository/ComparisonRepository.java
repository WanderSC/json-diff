package com.scheid.jsondiff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scheid.jsondiff.entity.Comparison;

/**
 * Repository for {@link Comparison}
 */
@Repository
public interface ComparisonRepository extends JpaRepository<Comparison, Long>{

	Comparison findByIdentifier(String name);
	
}
