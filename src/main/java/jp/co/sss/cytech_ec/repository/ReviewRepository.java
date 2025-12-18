package jp.co.sss.cytech_ec.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.cytech_ec.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByProduct_ProductId(Integer productId);
}
