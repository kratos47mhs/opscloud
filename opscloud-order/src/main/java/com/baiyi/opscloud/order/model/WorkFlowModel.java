package com.baiyi.opscloud.order.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/10/21 11:13 上午
 * @Version 1.0
 */
public class WorkFlowModel {

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class WorkFlow {

        private List<Node> workFlowNodes;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class Node {

        @ApiModelProperty(value = "节点名称")
        private String nodeName;
        @ApiModelProperty(value = "节点类型，参考NodeTypeEnum")
        private Integer nodeType;
        @ApiModelProperty(value = "筛选用户的标签")
        private String tag;
        private String comment;

    }

}
