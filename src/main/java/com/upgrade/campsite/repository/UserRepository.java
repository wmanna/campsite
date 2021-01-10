package com.upgrade.campsite.repository;

import com.upgrade.campsite.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    User findByEmail(String email);
}