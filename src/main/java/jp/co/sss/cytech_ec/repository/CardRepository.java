package jp.co.sss.cytech_ec.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.sss.cytech_ec.model.Card;
import jp.co.sss.cytech_ec.model.User;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {

    // Userエンティティを直接検索
    List<Card> findByUser(User user);

    // userId で検索
    List<Card> findByUser_UserId(Integer userId);

}
