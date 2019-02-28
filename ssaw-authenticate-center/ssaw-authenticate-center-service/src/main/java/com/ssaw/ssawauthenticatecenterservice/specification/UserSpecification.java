package com.ssaw.ssawauthenticatecenterservice.specification;

import com.ssaw.ssawauthenticatecenterfeign.vo.UserDto;
import com.ssaw.ssawauthenticatecenterservice.dao.entity.user.UserEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HuSen.
 * @date 2019/1/4 15:45.
 */
public class UserSpecification implements Specification<UserEntity> {

    private UserDto userDto;

    public UserSpecification(UserDto userDto) {
        this.userDto = userDto;
    }

    @Override
    public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>(2);
        if(null != userDto) {
            if(null != userDto.getId()) {
                predicates.add(criteriaBuilder.equal(root.get("id").as(Long.class), userDto.getId()));
            }
            if(StringUtils.isNotBlank(userDto.getUsername())) {
                predicates.add(criteriaBuilder.like(root.get("username").as(String.class), "%".concat(userDto.getUsername()).concat("%")));
            }
        }
        return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
    }
}
