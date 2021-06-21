package com.baiyi.caesar.controller;

import com.baiyi.caesar.common.HttpResult;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.auth.AuthGroupParam;
import com.baiyi.caesar.domain.param.auth.AuthResourceParam;
import com.baiyi.caesar.domain.param.auth.AuthRoleParam;
import com.baiyi.caesar.domain.param.auth.AuthUserRoleParam;
import com.baiyi.caesar.facade.auth.AuthFacade;
import com.baiyi.caesar.domain.vo.auth.AuthGroupVO;
import com.baiyi.caesar.domain.vo.auth.AuthResourceVO;
import com.baiyi.caesar.domain.vo.auth.AuthRoleResourceVO;
import com.baiyi.caesar.domain.vo.auth.AuthRoleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/2/13 8:02 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/auth")
@Api(tags = "权限配置")
public class AuthController {

    @Resource
    private AuthFacade authFacade;

    @ApiOperation(value = "分页查询role列表")
    @PostMapping(value = "/role/page/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<AuthRoleVO.Role>> queryRolePage(@RequestBody @Valid AuthRoleParam.AuthRolePageQuery pageQuery) {
        return new HttpResult<>(authFacade.queryRolePage(pageQuery));
    }

    @ApiOperation(value = "新增role")
    @PostMapping(value = "/role/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addRole(@RequestBody @Valid AuthRoleVO.Role role) {
        authFacade.addRole(role);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新role")
    @PutMapping(value = "/role/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateRole(@RequestBody @Valid AuthRoleVO.Role role) {
        authFacade.updateRole(role);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除指定的role")
    @DeleteMapping(value = "/role/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteRoleById(@RequestParam @Valid int id) {
        authFacade.deleteRoleById(id);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "分页查询资源组列表")
    @PostMapping(value = "/group/page/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<AuthGroupVO.Group>> queryGroupPage(@RequestBody @Valid AuthGroupParam.AuthGroupPageQuery pageQuery) {
        return new HttpResult<>(authFacade.queryGroupPage(pageQuery));
    }

    @ApiOperation(value = "新增资源组")
    @PostMapping(value = "/group/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addGroup(@RequestBody @Valid AuthGroupVO.Group group) {
        authFacade.addGroup(group);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新资源组")
    @PutMapping(value = "/group/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateGroup(@RequestBody @Valid AuthGroupVO.Group group) {
        authFacade.updateGroup(group);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除指定的资源组")
    @DeleteMapping(value = "/group/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteGroupById(@RequestParam @Valid int id) {
        authFacade.deleteGroupById(id);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "分页查询角色绑定的资源列表")
    @PostMapping(value = "/role/resource/bind/page/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<AuthResourceVO.Resource>> queryRoleBindResourcePage(@RequestBody @Valid AuthResourceParam.RoleBindResourcePageQuery pageQuery) {
        return new HttpResult<>(authFacade.queryRoleBindResourcePage(pageQuery));
    }

    @ApiOperation(value = "角色绑定资源")
    @PostMapping(value = "/role/resource/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addRoleResource(@RequestBody @Valid AuthRoleResourceVO.RoleResource roleResource) {
        authFacade.addRoleResource(roleResource);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "角色解除绑定资源")
    @DeleteMapping(value = "/role/resource/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteRoleResourceById(@RequestParam @Valid int id) {
        authFacade.deleteRoleResourceById(id);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "分页查询资源列表")
    @PostMapping(value = "/resource/page/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<AuthResourceVO.Resource>> queryResourcePage(@RequestBody @Valid AuthResourceParam.AuthResourcePageQuery pageQuery) {
        return new HttpResult<>(authFacade.queryResourcePage(pageQuery));
    }

    @ApiOperation(value = "新增资源")
    @PostMapping(value = "/resource/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addResource(@RequestBody @Valid AuthResourceVO.Resource resource) {
        authFacade.addResource(resource);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新资源")
    @PutMapping(value = "/resource/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateResource(@RequestBody @Valid AuthResourceVO.Resource resource) {
        authFacade.updateResource(resource);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除资源")
    @DeleteMapping(value = "/resource/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteResourceById(@RequestParam @Valid int id) {
        authFacade.deleteResourceById(id);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新用户角色")
    @PutMapping(value = "/user/role/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateUserRole(@RequestBody @Valid AuthUserRoleParam.UpdateUserRole updateUserRole) {
        authFacade.updateUserRole(updateUserRole);
        return HttpResult.SUCCESS;
    }

}