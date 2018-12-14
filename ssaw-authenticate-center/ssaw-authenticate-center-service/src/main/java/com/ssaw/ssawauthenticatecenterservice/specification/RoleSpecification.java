package com.ssaw.ssawauthenticatecenterservice.specification;

import com.ssaw.ssawauthenticatecenterfeign.dto.RoleDto;
import com.ssaw.ssawauthenticatecenterservice.entity.RoleEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author HuSen.
 * @date 2018/12/14 19:43.
 */
public class RoleSpecification implements Specification<RoleEntity> {

    private RoleDto roleDto;

    public RoleSpecification(RoleDto roleDto) {
        this.roleDto = roleDto;
    }

    @Override
    public Predicate toPredicate(Root<RoleEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if(!Objects.isNull(roleDto) && StringUtils.isNotBlank(roleDto.getName())) {
            predicates.add(criteriaBuilder.like(root.get("name").as(String.class), "%".concat(roleDto.getName()).concat("%")));
        }
        return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
    }
}
