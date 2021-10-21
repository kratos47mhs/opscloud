package com.baiyi.opscloud.jenkins.server;

import com.baiyi.opscloud.common.datasource.JenkinsDsInstanceConfig;
import com.offbytwo.jenkins.JenkinsServer;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Author baiyi
 * @Date 2021/7/1 2:10 下午
 * @Version 1.0
 */
public final class JenkinsServerBuilder {

    private JenkinsServerBuilder() {
    }

    public static JenkinsServer build(JenkinsDsInstanceConfig.Jenkins jenkins) throws URISyntaxException {
        return new JenkinsServer(new URI(jenkins.getUrl()),
                jenkins.getUsername(), jenkins.getToken());
    }

}
