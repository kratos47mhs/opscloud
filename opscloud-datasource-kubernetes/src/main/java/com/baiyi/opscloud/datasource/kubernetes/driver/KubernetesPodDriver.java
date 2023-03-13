package com.baiyi.opscloud.datasource.kubernetes.driver;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ExecListener;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import io.fabric8.kubernetes.client.dsl.Listable;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2021/6/24 11:16 下午
 * @Version 1.0
 */
@Slf4j
public class KubernetesPodDriver {

    public static List<Pod> listPod(KubernetesConfig.Kubernetes kubernetes) {
        try (KubernetesClient kc = KubernetesClientBuilder.build(kubernetes)) {
            PodList podList = kc.pods()
                    .list();
            return podList.getItems();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static List<Pod> listPod(KubernetesConfig.Kubernetes kubernetes, String namespace) {
        try (KubernetesClient kc = KubernetesClientBuilder.build(kubernetes)) {
            PodList podList = kc.pods()
                    .inNamespace(namespace)
                    .list();
            if (CollectionUtils.isEmpty(podList.getItems())) {
                return Collections.emptyList();
            }
            return podList.getItems();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static List<Pod> listPod(KubernetesConfig.Kubernetes kubernetes, String namespace, String deploymentName) {
        try (KubernetesClient kc = KubernetesClientBuilder.build(kubernetes)) {
            Map<String, String> matchLabels = kc.apps()
                    .deployments()
                    .inNamespace(namespace)
                    .withName(deploymentName)
                    .get()
                    .getSpec()
                    .getTemplate()
                    .getMetadata()
                    .getLabels();
            if (matchLabels.isEmpty()) {
                return Collections.emptyList();
            }
            return Optional.of(kc.pods().inNamespace(namespace).withLabels(matchLabels))
                    .map(Listable::list)
                    .map(PodList::getItems)
                    .orElse(Collections.emptyList());
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static List<Pod> listPod(KubernetesConfig.Kubernetes kubernetes, String namespace, Map<String, String> labels) {
        try (KubernetesClient kc = KubernetesClientBuilder.build(kubernetes)) {
            PodList podList = kc.pods()
                    .inNamespace(namespace)
                    .withLabels(labels)
                    .list();
            if (CollectionUtils.isEmpty(podList.getItems())) {
                return Collections.emptyList();
            }
            return podList.getItems();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    /**
     * @param kubernetes
     * @param namespace
     * @param name       podName
     * @return
     */
    public static Pod getPod(KubernetesConfig.Kubernetes kubernetes, String namespace, String name) {
        try (KubernetesClient kc = KubernetesClientBuilder.build(kubernetes)) {
            return kc.pods()
                    .inNamespace(namespace)
                    .withName(name)
                    .get();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static String getPodLog(KubernetesConfig.Kubernetes kubernetes, String namespace, String name, String container) {
        try (KubernetesClient kc = KubernetesClientBuilder.build(kubernetes)) {
            return kc.pods()
                    .inNamespace(namespace)
                    .withName(name)
                    .inContainer(container)
                    .getLog();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static LogWatch getPodLogWatch(KubernetesConfig.Kubernetes kubernetes, String namespace, String podName) {
        return KubernetesClientBuilder.build(kubernetes).pods()
                .inNamespace(namespace)
                .withName(podName)
                .watchLog();
    }

    @Deprecated
    public static LogWatch getPodLogWatch(KubernetesConfig.Kubernetes kubernetes, String namespace, String podName, String containerName, Integer lines, OutputStream outputStream) {
        return KubernetesClientBuilder.build(kubernetes)
                .pods()
                .inNamespace(namespace)
                .withName(podName)
                .inContainer(containerName)
                .tailingLines(lines)
                .watchLog(outputStream);
    }

    @Deprecated
    public static LogWatch getPodLogWatch2(KubernetesConfig.Kubernetes kubernetes, String namespace, String podName, String containerName, Integer lines, OutputStream outputStream) {
        return KubernetesClientBuilder.build(kubernetes)
                .pods()
                .inNamespace(namespace)
                .withName(podName)
                .inContainer(containerName)
                .withLogWaitTimeout(0)
                .watchLog(outputStream);
    }


    /**
     * @param kubernetes
     * @param namespace
     * @return
     */
    @Deprecated
    public static ExecWatch loginPod(KubernetesConfig.Kubernetes kubernetes,
                                     String namespace,
                                     String podName,
                                     String containerName,
                                     SimpleListener listener,
                                     OutputStream out) {
        return KubernetesClientBuilder.build(kubernetes).pods()
                .inNamespace(namespace)
                .withName(podName)
                // 如果Pod中只有一个容器，不需要指定
                .inContainer(containerName)
                .redirectingInput()
                //.redirectingOutput()
                //.redirectingError()
                //.redirectingErrorChannel()
                .writingOutput(out)
                .writingError(out)
                .withTTY()
                .usingListener(listener)
                .exec("env", "TERM=xterm", "sh");
    }

    @Data
    public static class SimpleListener implements ExecListener {

        private boolean isClosed = false;

        @Override
        public void onOpen() {
        }

        @Override
        public void onFailure(Throwable t, Response response) {
            this.isClosed = true;
            // throw new SshRuntimeException("Kubernetes Container Failure!");
        }

        @Override
        public void onClose(int code, String reason) {
            this.isClosed = true;
            // throw new SshRuntimeException("Kubernetes Container Close!");
        }
    }

}
