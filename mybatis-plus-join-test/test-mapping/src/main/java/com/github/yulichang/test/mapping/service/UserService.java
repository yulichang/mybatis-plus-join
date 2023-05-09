package com.github.yulichang.test.mapping.service;

import com.github.yulichang.extension.mapping.base.MPJDeepService;
import com.github.yulichang.extension.mapping.base.MPJRelationService;
import com.github.yulichang.test.mapping.entity.UserDO;

public interface UserService extends MPJDeepService<UserDO>, MPJRelationService<UserDO> {
}
