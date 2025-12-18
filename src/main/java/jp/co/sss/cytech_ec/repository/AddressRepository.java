package jp.co.sss.cytech_ec.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.sss.cytech_ec.model.Address;
import jp.co.sss.cytech_ec.model.User;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    List<Address> findByUser(User user);
    List<Address> findByUser_UserId(Integer userId);
    
    


}
