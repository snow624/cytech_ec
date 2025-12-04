package jp.co.sss.cytech_ec.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.cytech_ec.model.User;

//userテーブルとJavaを接続
public interface UserRepository extends JpaRepository<User, Integer> {
	
//	login
	User findByEmailAndPasswords(String email, String passwords);

	
}
