package com.cmt.persistence.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cmt.persistence.data.entity.DeleteProduct;

/**
 * Repository to handle deleted products
 * @author Manoo.Srivastav
 *
 */
@Repository
public interface ProductDeleteRepository extends JpaRepository<DeleteProduct, Long> {

}
