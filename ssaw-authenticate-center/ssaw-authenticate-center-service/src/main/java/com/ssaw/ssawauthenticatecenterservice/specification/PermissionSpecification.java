package com.ssaw.ssawauthenticatecenterservice.specification;

import com.ssaw.ssawauthenticatecenterfeign.vo.PermissionDto;
import com.ssaw.ssawauthenticatecenterservice.dao.entity.permission.PermissionEntity;
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
 * @date 2018/12/12 17:00.
 */
public class PermissionSpecification implements Specification<PermissionEntity> {

    private PermissionDto permissionDto;

    public PermissionSpecification(PermissionDto permissionDto) {
        this.permissionDto = permissionDto;
    }

    @Override
    public Predicate toPredicate(Root<PermissionEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if(StringUtils.isNotBlank(permissionDto.getName())) {
            predicates.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + permissionDto.getName() + "%"));
        }
        if(!Objects.isNull(permissionDto.getResourceId())) {
            predicates.add(criteriaBuilder.equal(root.get("resourceId").as(Long.class), permissionDto.getResourceId()));
        }
        return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
    }
}
