package com.ssaw.ssawauthenticatecenterservice.specification;

import com.ssaw.ssawauthenticatecenterfeign.vo.ResourceDto;
import com.ssaw.ssawauthenticatecenterservice.dao.entity.resource.ResourceEntity;
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
public class ResourceSpecification implements Specification<ResourceEntity> {

    private ResourceDto resourceDto;

    public ResourceSpecification(ResourceDto resourceDto) {
        this.resourceDto = resourceDto;
    }

    @Override
    public Predicate toPredicate(Root<ResourceEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if(!Objects.isNull(resourceDto) && StringUtils.isNotBlank(resourceDto.getResourceId())) {
            // 资源ID
            predicates.add(criteriaBuilder.like(root.get("resourceId").as(String.class), "%" + resourceDto.getResourceId() + "%"));
        }
        return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
    }
}
