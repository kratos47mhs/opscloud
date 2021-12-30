package com.baiyi.opscloud.datasource.business.server.impl.base;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.datasource.business.server.util.ZabbixTemplateUtil;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.model.property.ServerProperty;
import com.baiyi.opscloud.domain.util.ObjectUtil;
import com.baiyi.opscloud.facade.server.SimpleServerNameFacade;
import com.baiyi.opscloud.zabbix.helper.ZabbixGroupHelper;
import com.baiyi.opscloud.zabbix.v5.drive.*;
import com.baiyi.opscloud.zabbix.v5.drive.base.SimpleZabbixV5HostDrive;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixProxy;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixTemplate;
import com.baiyi.opscloud.zabbix.v5.param.ZabbixHostParam;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import com.baiyi.opscloud.zabbix.v5.request.builder.ZabbixRequestBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/8/23 9:48 上午
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractZabbixHostServerProvider extends AbstractServerProvider<ZabbixConfig.Zabbix> {

    @Resource
    private ZabbixGroupHelper zabbixGroupHelper;

    @Resource
    protected ZabbixV5HostDrive zabbixV5HostDrive;

    @Resource
    protected ZabbixV5HostMacroDrive zabbixV5HostMacroDrive;

    @Resource
    protected ZabbixV5HostTagDrive zabbixV5HostTagDrive;

    @Resource
    protected ZabbixV5ProxyDrive zabbixV5ProxyDrive;

    @Resource
    private ZabbixV5TemplateDrive zabbixV5TemplateDrive;

    @Resource
    private SimpleZabbixV5HostDrive simpleZabbixV5HostDrive;

    protected static ThreadLocal<ZabbixConfig.Zabbix> configContext = new ThreadLocal<>();

    @Override
    protected void initialConfig(DatasourceConfig dsConfig) {
        configContext.set(dsConfigHelper.build(dsConfig, ZabbixConfig.class).getZabbix());
    }

    @Override
    protected void doGrant(User user, BaseBusiness.IBusiness businessResource) {
    }

    @Override
    protected void doRevoke(User user, BaseBusiness.IBusiness businessResource) {
    }

    protected void doCreate(Server server, ServerProperty.Server property) {
        if (!isEnable(property)) return;
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .method(SimpleZabbixV5HostDrive.HostAPIMethod.CREATE)
                .putParam("host", SimpleServerNameFacade.toServerName(server))
                .putParam("interfaces", buildHostInterfaceParams(server, property))
                .putParam("groups", buildHostGroupParams(configContext.get(), server))
                .putParam("templates", buildTemplatesParams(configContext.get(), property))
                .putParam("tags", buildTagsParams(server))
                .putParamSkipEmpty("proxy_hostid", getProxyHostid(property))
                .putParamSkipEmpty("macros", property.getZabbix().toMacros())
                .build();
        ZabbixHost.CreateHostResponse response = simpleZabbixV5HostDrive.createHandle(configContext.get(), request);
        if (CollectionUtils.isEmpty(response.getResult().getHostids())) {
            log.error("ZabbixHost创建失败!");
        }
    }

    protected void updateHost(Server server, ServerProperty.Server property, ZabbixHost.Host host, String manageIp) {
        String hostName = SimpleServerNameFacade.toServerName(server);
        ZabbixRequestBuilder requestBuilder = ZabbixRequestBuilder.builder();
        // 更新主机名
        if (!hostName.equals(host.getName())) {
            requestBuilder.putParam("host", hostName);
            zabbixV5HostDrive.evictHostByIp(configContext.get(), getManageIp(server, property));
        }
        putProxyUpdateParam(property, host, requestBuilder);
        putTemplateUpdateParam(property, host, requestBuilder);
        putTagUpdateParam(server, host, requestBuilder);
        putMacroUpdateParam(host, property, requestBuilder);
        ZabbixRequest.DefaultRequest request = requestBuilder.build();
        Map<String, Object> params = request.getParams();
        if (params.keySet().stream().anyMatch(k -> ObjectUtil.isNotEmpty(params.get(k)))) {
            zabbixV5HostDrive.updateHost(configContext.get(), host, request);
            zabbixV5HostDrive.evictHostByIp(configContext.get(), manageIp);
        }
    }

    public void putMacroUpdateParam(ZabbixHost.Host host, ServerProperty.Server property, ZabbixRequestBuilder requestBuilder) {
        List<ServerProperty.Macro> macros = property.getZabbix().toMacros();
        if (CollectionUtils.isEmpty(macros)) return;
        requestBuilder.putParam("macros", macros);
    }

    private void putTagUpdateParam(Server server, ZabbixHost.Host host, ZabbixRequestBuilder requestBuilder) {
        ZabbixHost.Host hostTag = zabbixV5HostTagDrive.getHostTag(configContext.get(), host);
        Env env = getEnv(server);
        if (!CollectionUtils.isEmpty(hostTag.getTags())) {
            Optional<ZabbixHost.HostTag> optionalHostTag = hostTag.getTags().stream().filter(e -> e.getTag().equals("env")).findFirst();
            if (optionalHostTag.isPresent() && env.getEnvName().equals(optionalHostTag.get().getValue())) {
                return;
            }
        }
        ZabbixHostParam.Tag tag = ZabbixHostParam.Tag.builder()
                .tag("env")
                .value(env.getEnvName())
                .build();
        requestBuilder.putParam("tags", Lists.newArrayList(tag));
        zabbixV5HostTagDrive.evictHostTag(configContext.get(), host);  //清理缓存
    }

    /**
     * 更新模板参数（追加）
     *
     * @param property
     * @param host
     */
    private void putTemplateUpdateParam(ServerProperty.Server property, ZabbixHost.Host host, ZabbixRequestBuilder requestBuilder) {
        List<ZabbixTemplate.Template> zabbixTemplates = zabbixV5TemplateDrive.getByHost(configContext.get(), host);
        if (ZabbixTemplateUtil.hostTemplateEquals(zabbixTemplates, property)) return; // 判断主机模板与配置是否相同，相同则跳过更新
        Set<String> templateNamSet = Sets.newHashSet();
        zabbixTemplates.forEach(t -> templateNamSet.add(t.getName()));
        property.getZabbix().getTemplates().forEach(n -> {
            if (!templateNamSet.contains(n)) {
                ZabbixTemplate.Template zabbixTemplate = zabbixV5TemplateDrive.getByName(configContext.get(), n);
                if (zabbixTemplate != null) {
                    zabbixTemplates.add(zabbixTemplate);
                    templateNamSet.add(n);
                }
            }
        });
        // 更新模板参数
        requestBuilder.putParamSkipEmpty("templates", toTemplateParams(zabbixTemplates));
        // 主机模板与配置保持一致，清理多余模版
        if (property.getZabbix().getTemplateUniformity() != null && property.getZabbix().getTemplateUniformity()) {
            clearTemplates(zabbixTemplates, property); // 清理模版
            requestBuilder.putParamSkipEmpty("templates_clear", toTemplateParams(zabbixTemplates));
        }
        zabbixV5TemplateDrive.evictHostTemplate(configContext.get(), host); //清理缓存
    }

    private List<ZabbixHostParam.Template> toTemplateParams(List<com.baiyi.opscloud.zabbix.v5.entity.ZabbixTemplate.Template> templates) {
        return templates.stream().map(e -> ZabbixHostParam.Template.builder().templateid(e.getTemplateid()).build())
                .collect(Collectors.toList());
    }

    private void clearTemplates(List<ZabbixTemplate.Template> templates, ServerProperty.Server property) {
        property.getZabbix().getTemplates().forEach(n -> {
            for (ZabbixTemplate.Template template : templates) {
                if (template.getName().equals(n)) {
                    templates.remove(template);
                    return;
                }
            }
        });
    }

    protected boolean isEnable(ServerProperty.Server property) {
        return Optional.ofNullable(property)
                .map(ServerProperty.Server::getZabbix)
                .map(ServerProperty.Zabbix::getEnabled)
                .orElse(false);
    }

    private void putProxyUpdateParam(ServerProperty.Server property, ZabbixHost.Host host, ZabbixRequestBuilder requestBuilder) {
        String proxyHostid = getProxyHostid(property);
        if (StringUtils.isEmpty(host.getProxyHostid())) {
            if (StringUtils.isEmpty(proxyHostid)) {
                return;
            }
        } else {
            if (host.getProxyHostid().equals(proxyHostid)) {
                return;
            } else {
                proxyHostid = "0";
            }
        }
        requestBuilder.putParam("proxy_hostid", proxyHostid);
    }

    protected String getProxyHostid(ServerProperty.Server property) {
        ZabbixProxy.Proxy proxy = getProxy(property);
        return proxy == null ? null : proxy.getProxyid();
    }

    private ZabbixProxy.Proxy getProxy(ServerProperty.Server property) {
        String proxyName = Optional.ofNullable(property)
                .map(ServerProperty.Server::getZabbix)
                .map(ServerProperty.Zabbix::getProxyName).orElse(null);
        if (StringUtils.isEmpty(proxyName))
            return null;
        return zabbixV5ProxyDrive.getProxy(configContext.get(), proxyName);
    }

    protected ZabbixHostParam.Tag buildTagsParams(Server server) {
        return ZabbixHostParam.Tag.builder()
                .tag("env")
                .value(getEnv(server).getEnvName())
                .build();
    }

    protected List<ZabbixHostParam.Template> buildTemplatesParams(ZabbixConfig.Zabbix zabbix, ServerProperty.Server property) {
        return zabbixV5TemplateDrive.listByNames(zabbix, property.getZabbix().getTemplates()).stream().map(e ->
                ZabbixHostParam.Template.builder()
                        .templateid(e.getTemplateid())
                        .build()
        ).collect(Collectors.toList());
    }

    protected ZabbixHostParam.Group buildHostGroupParams(ZabbixConfig.Zabbix zabbix, Server server) {
        ZabbixHostGroup.HostGroup hostGroup = zabbixGroupHelper.getOrCreateHostGroup(zabbix, getServerGroup(server).getName());
        return ZabbixHostParam.Group.builder()
                .groupid(hostGroup.getGroupid())
                .build();
    }

    protected ZabbixHostParam.Interface buildHostInterfaceParams(Server server, ServerProperty.Server property) {
        return ZabbixHostParam.Interface.builder()
                .ip(getManageIp(server, property))
                .build();
    }

    protected String getManageIp(Server server, ServerProperty.Server property) {
        String manageIp = Optional.ofNullable(property)
                .map(ServerProperty.Server::getMetadata)
                .map(ServerProperty.Metadata::getManageIp)
                .orElse(server.getPrivateIp());
        return StringUtils.isEmpty(manageIp) ? server.getPrivateIp() : manageIp;
    }

}
