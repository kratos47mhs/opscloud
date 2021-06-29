package com.baiyi.caesar.datasource.kubernetes.provider;

import com.baiyi.caesar.common.datasource.KubernetesDsInstanceConfig;
import com.baiyi.caesar.common.datasource.config.DsKubernetesConfig;
import com.baiyi.caesar.common.type.DsTypeEnum;
import com.baiyi.caesar.common.util.IOUtil;
import com.baiyi.caesar.datasource.model.DsInstanceContext;
import com.baiyi.caesar.datasource.provider.base.common.AbstractSetDsInstanceConfigProvider;
import com.baiyi.caesar.datasource.util.SystemEnvUtil;
import com.baiyi.caesar.domain.generator.caesar.Credential;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/6/24 7:14 下午
 * @Version 1.0
 */
@Component
public class KubernetesSetConfigProvider extends AbstractSetDsInstanceConfigProvider {

    @Override
    public String getInstanceType() {
        return DsTypeEnum.KUBERNETES.name();
    }

    private DsKubernetesConfig.Kubernetes buildConfig(DatasourceConfig dsConfig) {
        return dsFactory.build(dsConfig, KubernetesDsInstanceConfig.class).getKubernetes();
    }

    protected void doSet(DsInstanceContext dsInstanceContext) {
        DsKubernetesConfig.Kubernetes kubernetes = buildConfig(dsInstanceContext.getDsConfig());
        // 取配置文件路径
        String kubeconfigPath = SystemEnvUtil.renderEnvHome(kubernetes.getKubeconfig().getPath());
        Credential credential = getCredential(dsInstanceContext.getDsConfig().getCredentialId());
        String kubeconfig = stringEncryptor.decrypt(credential.getCredential());
        IOUtil.writeFile(kubeconfig, Joiner.on("/").join(kubeconfigPath, io.fabric8.kubernetes.client.Config.KUBERNETES_KUBECONFIG_FILE));
    }
}
