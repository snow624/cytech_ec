package jp.co.sss.cytech_ec.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.cytech_ec.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
