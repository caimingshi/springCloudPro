package com.xl.platform.a.canal;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.xl.platform.common.config.canal.CanalDealService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @version 1.0
 * @Author caimingshi
 * @ClassName DoMyCanal
 * @Date 2020/3/6 17:26
 * @Since JDK 1.8
 * @Description 实现自己的逻辑
 **/
@Component
public class DoMyCanal implements CanalDealService {
    @Override
    public void handler(CanalEntry.Entry entry, CanalEntry.RowChange rowChage) {
        CanalEntry.EventType eventType = rowChage.getEventType();
        System.out.println(String.format("================&gt; binlog[%s:%s] , name[%s,%s] , eventType : %s",
                entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                eventType));

        for (CanalEntry.RowData rowData : rowChage.getRowDatasList()) {
            if (eventType == CanalEntry.EventType.DELETE) {
                printColumn(rowData.getBeforeColumnsList());
            } else if (eventType == CanalEntry.EventType.INSERT) {
                printColumn(rowData.getAfterColumnsList());
            } else {//update
                System.out.println("-------&gt; before");
                printColumn(rowData.getBeforeColumnsList());
                System.out.println("-------&gt; after");
                printColumn(rowData.getAfterColumnsList());
            }
        }
    }
    private static void printColumn(List<CanalEntry.Column> columns) {
        for (CanalEntry.Column column : columns) {
            System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        }
    }
}
