package com.boot.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.boot.entity.Catalog;

@Transactional
@Repository
public interface CatalogRepository extends CrudRepository<Catalog, Long> {
	public Catalog findByName(String Catalog);
}
