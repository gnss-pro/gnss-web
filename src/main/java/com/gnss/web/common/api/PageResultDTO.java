package com.gnss.web.common.api;

import com.gnss.web.common.constant.ResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 分页响应结果</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-9-14
 */
@ApiModel("分页响应结果")
@Data
public class PageResultDTO<T> implements Serializable {

    @ApiModelProperty(value = "响应代码", position = 1)
    private int code;

    @ApiModelProperty(value = "响应消息", position = 2)
    private String msg = ResultCodeEnum.SUCCESS.getDesc();

    @ApiModelProperty(value = "分页结果", position = 3)
    private PageResult<T> data;

    public PageResultDTO(List<T> content, Page<?> page) {
        this.data = new PageResult<>(content, page);
    }

    @ApiModel("分页结果")
    @Getter
    private class PageResult<T> {
        @ApiModelProperty(value = "结果", position = 1)
        private List<T> content = new ArrayList();
        @ApiModelProperty(value = "总条数", position = 2)
        private long totalElements;
        @ApiModelProperty(value = "总页数", position = 3)
        private int totalPages;
        @ApiModelProperty(value = "第几页", position = 4)
        private int pageNumber;
        @ApiModelProperty(value = "每页显示条数", position = 5)
        private int pageSize;

        private PageResult(List<T> content, Page<?> page) {
            this.content.addAll(content);
            this.totalElements = page.getTotalElements();
            this.totalPages = page.getTotalPages();
            this.pageNumber = page.getNumber();
            this.pageSize = page.getSize();
        }
    }
}
