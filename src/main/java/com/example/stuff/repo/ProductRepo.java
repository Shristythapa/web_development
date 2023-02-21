package com.example.stuff.repo;

import com.example.stuff.Entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Integer> {
//    @Query(value = "insert into users_orders(user_id,product_id) values(?1,?2)",nativeQuery = true)
//    void insertIntoOrder(Integer u_id,Integer product_Id);
//}
@Query(value = "select * from products s where s.name like %:keyword% ", nativeQuery = true)
List<Product> findByKeyword(@Param("keyword") String keyword);

}