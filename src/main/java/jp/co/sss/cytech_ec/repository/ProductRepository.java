package jp.co.sss.cytech_ec.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.co.sss.cytech_ec.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	@Query("SELECT p FROM Product p " +
		       "WHERE (:keyword IS NULL OR p.productName LIKE %:keyword%) " +
		       "AND (:maker IS NULL OR :maker = '' OR p.makerName LIKE %:maker%)")
		List<Product> findByKeywordAndMaker(
		        @Param("keyword") String keyword,
		        @Param("maker") String maker);


}
