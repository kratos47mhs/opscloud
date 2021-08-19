package com.baiyi.opscloud.datasource.server.impl;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/22 3:32 下午
 * @Since 1.0
 */


public class ZabbixHostServerProvider {

//    private interface Action {
//        Boolean CRETE = true;
//        Boolean UPDATE = false;
//    }
//
//    @Resource
//    private ZabbixHostHandler zabbixHostHandler;
//
//    @Resource
//    private ZabbixHostGroupHandler zabbixHostGroupHandler;
//
//    @Resource
//    private ZabbixTemplateHandler zabbixTemplateHandler;
//
//    @Resource
//    private ZabbixHostServerProvider zabbixHostServerProvider;
//
//    @Resource
//    private ServerGroupService serverGroupService;
//
//    @Resource
//    private EnvService envService;
//
//    @Override
//    public String getAssetType() {
//        return DsAssetTypeEnum.ZABBIX_HOST.getType();
//    }
//
//    @Override
//    public String getInstanceType() {
//        return DsTypeEnum.ZABBIX.getName();
//    }
//
//    private DsZabbixConfig.Zabbix buildConfig(DsInstanceContext dsInstanceContext) {
//        return dsFactory.build(dsInstanceContext.getDsConfig(), ZabbixDsInstanceConfig.class).getZabbix();
//    }
//
//    @Override
//    public DatasourceInstanceAsset create(DsInstanceContext dsInstanceContext, Server server, Map<String, String> serverProperties) {
//        Map<String, Object> createParamMap = buildParamMap(dsInstanceContext, server, serverProperties, Action.CRETE);
//        String hostId = zabbixHostHandler.createHost(buildConfig(dsInstanceContext), server, createParamMap);
//        return pullAsset(dsInstanceContext, hostId);
//    }
//
//    @Override
//    public DatasourceInstanceAsset update(DsInstanceContext dsInstanceContext, Server server, Map<String, String> serverProperties) {
////        Map<String, Object> createParamMap = buildParamMap(dsInstanceContext, server, serverProperties, Action.UPDATE);
////        DatasourceInstanceAsset asset = getBindAsset(server.getId());
////        String hostId;
////        if (asset != null) {
////            hostId = zabbixHostHandler.updateHost(buildConfig(dsInstanceContext), server, asset.getAssetId(), createParamMap);
////        } else {
////            hostId = zabbixHostHandler.createHost(buildConfig(dsInstanceContext), server, createParamMap);
////        }
////        return pullAsset(dsInstanceContext, hostId);
//        return null;
//    }
//
//    @Override
//    protected void destroy(DsInstanceContext dsInstanceContext, DatasourceInstanceAsset asset) {
//        zabbixHostHandler.deleteHostById(buildConfig(dsInstanceContext), asset.getAssetId());
//    }
//
//    private DatasourceInstanceAsset pullAsset(DsInstanceContext dsInstanceContext, String hostId) {
//        List<SimpleAssetProvider> providers = getSimpleAssetProviderList();
//        Iterator<SimpleAssetProvider> iterator = providers.iterator();
//        UniqueAssetParam param = UniqueAssetParam.builder()
//                .assetId(hostId)
//                .build();
//        DatasourceInstanceAsset asset = iterator.next().pullAsset(dsInstanceContext.getDsInstance().getId(), param);
//        if (iterator.hasNext()) {
//            iterator.next().pullAsset(dsInstanceContext.getDsInstance().getId(), param);
//        }
//        return asset;
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        ServerFactory.register(zabbixHostServerProvider);
//    }
//
//    private Map<String, Object> buildParamMap(DsInstanceContext dsInstanceContext, Server server, Map<String, String> serverProperties, Boolean action) {
//        Map<String, Object> paramMap = new HashMap<>();
//        if (action) {
//            paramMap.put("interfaces", buildCreateInterfaceParam(server));
//        }
//        paramMap.put("groups", buildCreateGroupParam(dsInstanceContext, server));
//        paramMap.put("tags", buildCreateTagParam(server));
//        paramMap.put("templates", buildCreateTemplateParam(dsInstanceContext, serverProperties.get(BusinessPropertyConstants.Zabbix.TEMPLATES)));
//        paramMap.put("macros", JSONArray.parseArray(serverProperties.get(BusinessPropertyConstants.Zabbix.HOST_MACROS)));
//        return paramMap;
//    }
//
//    private ZabbixHostCreateParam.Interface buildCreateInterfaceParam(Server server) {
//        return ZabbixHostCreateParam.Interface.builder()
//                .ip(server.getPrivateIp())
//                .build();
//    }
//
//    private ZabbixHostCreateParam.Groups buildCreateGroupParam(DsInstanceContext dsInstanceContext, Server server) {
//        ServerGroup serverGroup = serverGroupService.getById(server.getServerGroupId());
//        String groupId = zabbixHostGroupHandler.createGroup(buildConfig(dsInstanceContext), serverGroup.getName());
//        return ZabbixHostCreateParam.Groups.builder()
//                .groupid(groupId)
//                .build();
//    }
//
//    private ZabbixHostCreateParam.Tags buildCreateTagParam(Server server) {
//        return ZabbixHostCreateParam.Tags.builder()
//                .tag("env")
//                .value(envService.getByEnvType(server.getEnvType()).getEnvName())
//                .build();
//    }
//
//    private List<ZabbixHostCreateParam.Templates> buildCreateTemplateParam(DsInstanceContext dsInstanceContext, String templateNames) {
//        List<ZabbixHostCreateParam.Templates> list = Lists.newArrayList();
//        String[] names = templateNames.split(",");
//        List<ZabbixTemplate> zabbixTemplates = zabbixTemplateHandler.listTemplateByNames(buildConfig(dsInstanceContext), Lists.newArrayList(names));
//        zabbixTemplates.forEach(zabbixTemplate -> {
//            list.add(ZabbixHostCreateParam.Templates.builder()
//                    .templateid(zabbixTemplate.getTemplateId())
//                    .build());
//        });
//        return list;
//    }
}
