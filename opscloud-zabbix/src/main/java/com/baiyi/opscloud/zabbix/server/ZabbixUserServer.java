package com.baiyi.opscloud.zabbix.server;

import com.baiyi.opscloud.zabbix.entry.ZabbixHostgroup;
import com.baiyi.opscloud.zabbix.entry.ZabbixUser;
import com.baiyi.opscloud.zabbix.entry.ZabbixUserMedia;
import com.baiyi.opscloud.zabbix.entry.ZabbixUsergroup;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/1/8 1:01 下午
 * @Version 1.0
 */
public interface ZabbixUserServer {


    ZabbixUser getUser(String username);

    List<ZabbixUser> getAllZabbixUser();

    Boolean createUser(ZabbixUser user, List<ZabbixUserMedia> mediaList, List<Map<String, String>> usrgrps);

    Boolean updateUser(ZabbixUser user, List<ZabbixUserMedia> mediaList, List<Map<String, String>> usrgrps);

    Boolean deleteUser(String username);

    ZabbixUsergroup getUsergroup(String usergroup);

    /**
     * 创建用户组
     *
     * @param usergroupName   eg: users_xxxx
     * @param zabbixHostgroup 绑定的hostgroup
     * @return
     */
    ZabbixUsergroup createUsergroup(String usergroupName, ZabbixHostgroup zabbixHostgroup);
}
