package com.cmt.persistence.data.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cmt.persistence.data.entity.Product;

/**
 * Repository for Product
 * @author Manoo.Srivastav
 *
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
	
	/**
	 * Method to fetch the count of product created based on creation date
	 * @param createDate
	 * @return Long
	 * @author Manoo.Srivastav
	 */
	@Query(name="findCountByCreateDate", value="SELECT count(p) FROM Product p WHERE p.createDate=:cDate")
	public Long findCountByCreateDate(@Param("cDate") Date createDate);
	
	/**
	 * Method to fetch the count of products update at least once on given date
	 * @param updateDate
	 * @return Long
	 * @author Manoo.Srivastav
	 */
	@Query(name="findCountByUpdateDate", value="SELECT count(p) FROM Product p WHERE p.lastUpdateDate=:uDate")
	public Long findCountByUpdateDate(@Param("uDate") Date lastUpdateDate);
}
