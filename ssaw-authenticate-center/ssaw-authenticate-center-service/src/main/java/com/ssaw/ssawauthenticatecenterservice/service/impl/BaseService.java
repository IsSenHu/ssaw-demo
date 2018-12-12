package com.ssaw.ssawauthenticatecenterservice.service.impl;

import com.ssaw.commons.vo.PageReqDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * @author HuSen.
 * @date 2018/12/12 17:32.
 */
public class BaseService {

    PageRequest getPageRequest(PageReqDto pageReqDto) {
        Sort.Order order;
        String sortValue = pageReqDto.getSortValue();
        String sortType = pageReqDto.getSortType();
        if(StringUtils.isNotBlank(sortValue) && StringUtils.isNotBlank(sortType)
                && (StringUtils.equalsIgnoreCase("asc", sortType) || StringUtils.equalsIgnoreCase("desc", sortType))) {
            order = StringUtils.equalsIgnoreCase("asc", sortType) ? Sort.Order.asc(sortValue) : Sort.Order.desc(sortValue);
        } else if (StringUtils.isNotBlank(sortValue)){
            order = Sort.Order.desc(sortValue);
        } else {
            order = Sort.Order.desc("createTime");
        }
        return PageRequest.of(pageReqDto.getPage() - 1, pageReqDto.getSize(), Sort.by(order));
    }
}
