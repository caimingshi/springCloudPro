package com.xl.platform.common.config.canal;

import com.alibaba.otter.canal.protocol.CanalEntry;

/**
 * @version 1.0
 * @Author caimingshi
 * @InterfaceName CanalDealService
 * @Date 2020/3/6 17:23
 * @Since JDK 1.8
 * @Description TODO
 **/
public interface CanalDealService {
    void handler(CanalEntry.Entry entry, CanalEntry.RowChange rowChage);
}
