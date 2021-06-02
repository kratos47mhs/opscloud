package com.baiyi.caesar.packer.server;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.ServerGroupType;
import com.baiyi.caesar.domain.param.IExtend;
import com.baiyi.caesar.service.server.ServerGroupService;
import com.baiyi.caesar.service.server.ServerGroupTypeService;
import com.baiyi.caesar.util.ExtendUtil;
import com.baiyi.caesar.domain.vo.server.ServerGroupTypeVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/24 11:10 上午
 * @Version 1.0
 */
@Component
public class ServerGroupTypePacker {

    @Resource
    private ServerGroupService serverGroupService;

    @Resource
    private ServerGroupTypeService serverGroupTypeService;

    public void wrap(ServerGroupTypeVO.IServerGroupType iServerGroupType) {
        ServerGroupType serverGroupType = serverGroupTypeService.getById(iServerGroupType.getServerGroupTypeId());
        iServerGroupType.setServerGroupType(BeanCopierUtil.copyProperties(serverGroupType, ServerGroupTypeVO.ServerGroupType.class));
    }

    public List<ServerGroupTypeVO.ServerGroupType> wrapVOList(List<ServerGroupType> data) {
        return BeanCopierUtil.copyListProperties(data, ServerGroupTypeVO.ServerGroupType.class);
    }

    public List<ServerGroupTypeVO.ServerGroupType> wrapVOList(List<ServerGroupType> data, IExtend iExtend) {
        List<ServerGroupTypeVO.ServerGroupType> voList = wrapVOList(data);

        if (!ExtendUtil.isExtend(iExtend))
            return voList;

        return voList.stream().peek(this::wrap).collect(Collectors.toList());
    }

    public void wrap(ServerGroupTypeVO.ServerGroupType vo) {
        vo.setServerGroupSize(serverGroupService.countByServerGroupTypeId(vo.getId()));
    }
}
