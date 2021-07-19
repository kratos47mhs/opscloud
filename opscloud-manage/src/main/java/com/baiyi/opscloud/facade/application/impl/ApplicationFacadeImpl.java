package com.baiyi.opscloud.facade.application.impl;

import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.ApplicationResource;
import com.baiyi.opscloud.domain.param.application.ApplicationParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;
import com.baiyi.opscloud.facade.application.ApplicationFacade;
import com.baiyi.opscloud.packer.application.ApplicationPacker;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.application.ApplicationService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/7/12 12:58 下午
 * @Version 1.0
 */
@Service
public class ApplicationFacadeImpl implements ApplicationFacade {

    @Resource
    private ApplicationService applicationService;

    @Resource
    private ApplicationResourceService applicationResourceService;

    @Resource
    private ApplicationPacker applicationPacker;

    @Override
    public DataTable<ApplicationVO.Application> queryApplicationPage(ApplicationParam.ApplicationPageQuery pageQuery) {
        DataTable<Application> table = applicationService.queryPageByParam(pageQuery);
        return new DataTable<>(applicationPacker.wrapVOList(table.getData(), pageQuery), table.getTotalNum());
    }

    @Override
    public DataTable<ApplicationVO.Application> queryApplicationPageByWebTerminal(ApplicationParam.ApplicationPageQuery pageQuery) {
        DataTable<Application> table = applicationService.queryPageByParam(pageQuery);
        return new DataTable<>(applicationPacker.wrapVOListByKubernetes(table.getData()), table.getTotalNum());
    }

    @Override
    public ApplicationVO.Application queryApplicationById(ApplicationParam.Query query) {
        Application application = applicationService.getById(query.getApplicationId());
        if (application == null)
            throw new CommonRuntimeException(ErrorEnum.APPLICATION_NOT_EXIST);
        return applicationPacker.wrapVO(application);
    }

    @Override
    public void addApplication(ApplicationVO.Application application) {
        if (applicationService.getByKey(application.getApplicationKey()) != null)
            throw new CommonRuntimeException(ErrorEnum.APPLICATION_ALREADY_EXIST);
        Application app = BeanCopierUtil.copyProperties(application, Application.class);
        applicationService.add(app);
    }

    @Override
    public void updateApplication(ApplicationVO.Application application) {
        Application app = applicationService.getByKey(application.getApplicationKey());
        if (app == null)
            throw new CommonRuntimeException(ErrorEnum.APPLICATION_ALREADY_EXIST);
        app.setComment(application.getComment());
        app.setName(application.getName());
        applicationService.update(app);
    }

    @Override
    public void deleteApplication(Integer id) {
        if (!CollectionUtils.isEmpty(applicationResourceService.queryByApplication(id)))
            throw new CommonRuntimeException(ErrorEnum.APPLICATION_RES_IS_NOT_EMPTY);
        applicationService.deleteById(id);
    }

    @Override
    public void bindApplicationResource(ApplicationResourceVO.Resource resource) {
        if (applicationResourceService.getByTypeAndId(resource.getApplicationId(), resource.getBusinessType(), resource.getBusinessId()) != null)
            throw new CommonRuntimeException(ErrorEnum.APPLICATION_RES_ALREADY_EXIST);
        ApplicationResource res = BeanCopierUtil.copyProperties(resource, ApplicationResource.class);
        applicationResourceService.add(res);
    }

    @Override
    public void unbindApplicationResource(Integer id) {
        applicationResourceService.delete(id);
    }
}