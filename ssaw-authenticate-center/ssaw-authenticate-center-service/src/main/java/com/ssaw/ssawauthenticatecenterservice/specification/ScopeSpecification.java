package com.ssaw.ssawauthenticatecenterservice.specification;

import com.ssaw.ssawauthenticatecenterfeign.dto.ScopeDto;
import com.ssaw.ssawauthenticatecenterservice.entity.ScopeEntity;
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
 * @date 2018/12/13 14:55.
 */
public class ScopeSpecification implements Specification<ScopeEntity> {
    private ScopeDto scopeDto;

    public ScopeSpecification(ScopeDto scopeDto) {
        this.scopeDto = scopeDto;
    }

    @Override
    public Predicate toPredicate(Root<ScopeEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if(StringUtils.isNotBlank(scopeDto.getScope())) {
            predicates.add(criteriaBuilder.like(root.get("scope").as(String.class), "%" + scopeDto.getScope() + "%"));
        }
        if(null != scopeDto.getResourceId()) {
            predicates.add(criteriaBuilder.equal(root.get("resourceId").as(Integer.class), scopeDto.getResourceId()));
        }
        return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
    }
}
