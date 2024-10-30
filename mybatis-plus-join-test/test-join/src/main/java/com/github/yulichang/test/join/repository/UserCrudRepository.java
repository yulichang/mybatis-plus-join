package com.github.yulichang.test.join.repository;

import com.github.yulichang.repository.JoinCrudRepository;
import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.join.mapper.UserMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserCrudRepository extends JoinCrudRepository<UserMapper, UserDO> {

}
