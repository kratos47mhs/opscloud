package com.baiyi.opscloud.domain.vo.leo;

import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2023/2/22 21:00
 * @Version 1.0
 */
public class LeoJobVersionVO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class JobVersion implements EnvVO.IEnv, Serializable {

        private static final long serialVersionUID = -2925847833549310319L;

        private String jobName;
        private EnvVO.Env env;
        private Integer envType;
        private List<DeploymentVersion> deploymentVersions;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class DeploymentVersion implements Serializable {

        private static final long serialVersionUID = 1901939181360045590L;

        public static final DeploymentVersion EMPTY = DeploymentVersion.builder().build();

        private DoDeployVersion doDeployVersion;

        private Integer jobId;

        /**
         * env:deploymentName
         */
        @Builder.Default
        private String name = "-";

        private String deploymentName;

        private Integer replicas = 0;

        /**
         * 用于计算版本顺序
         */
        private Integer buildId = -1;

        private String versionTypeDesc;

        /**
         * 资产ID
         */
        private Integer assetId;

        @Builder.Default
        private String image = "-";
        @Builder.Default
        private String versionName = "-";
        @Builder.Default
        private String versionDesc = "";

        private String versionType;

        private Map<String, String> properties = Maps.newHashMap();

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class DoDeployVersion implements Serializable {

        private static final long serialVersionUID = -5057578961795862955L;

        public static final DoDeployVersion INVALID = DoDeployVersion.builder()
                .isActive(false)
                .build();

        @ApiModelProperty(value = "有效")
        @Builder.Default
        private boolean isActive = true;

//        @Min(value = 0, message = "关联任务ID不能为空")
//        @ApiModelProperty(value = "关联任务ID")
//        private Integer jobId;

        @ApiModelProperty(value = "构建ID")
        private Integer buildId;

//        @Min(value = 0, message = "Deployment资产ID不能为空")
//        @ApiModelProperty(value = "Deployment资产ID")
//        private Integer assetId;
//
//        @ApiModelProperty(value = "部署类型")
//        private String deployType;

    }

}
