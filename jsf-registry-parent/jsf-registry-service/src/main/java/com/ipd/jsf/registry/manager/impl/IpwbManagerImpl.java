/**
 * Copyright 2004-2048 .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ipd.jsf.registry.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipd.jsf.registry.dao.IpwblistDao;
import com.ipd.jsf.registry.domain.InterfaceInfo;
import com.ipd.jsf.registry.domain.Ipwblist;
import com.ipd.jsf.registry.manager.IpwbManager;
import com.ipd.jsf.gd.util.Constants;

@Service
public class IpwbManagerImpl implements IpwbManager {
    @Autowired
    private IpwblistDao ipwblistDao;

    /**
     * @param list
     * @return
     */
    private Map<String, Map<String, List<Ipwblist>>> convertListToMap(List<Ipwblist> list) {
        Map<String, Map<String, List<Ipwblist>>> map = new HashMap<String, Map<String, List<Ipwblist>>>();
        if (list != null && list.size() > 0) {
            for (Ipwblist ipwb : list) {
                Map<String, List<Ipwblist>> ipmap = map.get(ipwb.getInterfaceName());
                if (ipmap == null) {
                    ipmap = new HashMap<String, List<Ipwblist>>();
                    map.put(ipwb.getInterfaceName(), ipmap);
                }
                if (ipwb.getWbType() == Ipwblist.WHITETYPE) {
                    List<Ipwblist> wlist = ipmap.get(Constants.SETTING_INVOKE_WHITELIST);
                    if (wlist == null) {
                        wlist = new ArrayList<Ipwblist>();
                        ipmap.put(Constants.SETTING_INVOKE_WHITELIST, wlist);
                    }
                    wlist.add(ipwb);
                    continue;
                }
                if (ipwb.getWbType() == Ipwblist.BLACKTYPE) {
                    List<Ipwblist> blist = ipmap.get(Constants.SETTING_INVOKE_BLACKLIST);
                    if (blist == null) {
                        blist = new ArrayList<Ipwblist>();
                        ipmap.put(Constants.SETTING_INVOKE_BLACKLIST, blist);
                    }
                    blist.add(ipwb);
                    continue;
                }
            }
        }
        return map;
    }

    @Override
    public Map<String, Map<String, List<Ipwblist>>> getListByInterfaceIdList(List<InterfaceInfo> list) throws Exception {
        if (list != null && list.size() > 0) {
            List<Integer> ifaceIdList = new ArrayList<Integer>();
            for (InterfaceInfo iface : list) {
                if (iface != null && iface.getInterfaceId() != 0) {
                    ifaceIdList.add(iface.getInterfaceId());
                }
            }
            List<Ipwblist> result = ipwblistDao.getListByInterfaceIdList(ifaceIdList);
            return convertListToMap(result);
        }
        return new HashMap<String, Map<String, List<Ipwblist>>>();
    }
}
