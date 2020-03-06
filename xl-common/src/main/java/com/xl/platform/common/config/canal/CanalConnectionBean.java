package com.xl.platform.common.config.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.xl.platform.common.exception.XlApiException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @version 1.0
 * @Author caimingshi
 * @ClassName CanalConnectionBean
 * @Date 2020/3/6 17:21
 * @Since JDK 1.8
 * @Description TODO
 **/
@Component
@ConfigurationProperties(prefix = "canal.config")
@ConditionalOnBean(CanalDealService.class)
@Slf4j
@Data
public class CanalConnectionBean implements InitializingBean {
    @Autowired
    private CanalDealService canalDealService;

    private Boolean open = false;//是否开启
    private Integer batchSize = 1000;//每次取的条数
    private Integer count = 3;//重连次数
    private Integer seconds = 3000;//每次重连的时间间隔
    private String host;//canal服务器地址
    private Integer port;//canal服务器端口
    private String destination = "example";//目标
    private Integer TIMES = 1;//计数器


    @Override
    public void afterPropertiesSet(){
        if(open){
            //新开线程
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 创建链接
                    if(StringUtils.isEmpty(host)){
                        throw new XlApiException("host不能为空");
                    }
                    if(port == null){
                        throw new XlApiException("port不能为空");
                    }
                    CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(host,
                            port), destination, "", "");
                    log.info("canal服务器连接成功---");
                    try {
                        connector.connect();
                        connector.subscribe(".*\\..*");
                        connector.rollback();
                        while (true) {
                            try {
                                Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                                long batchId = message.getId();
                                int size = message.getEntries().size();
                                if (batchId != -1 && size != 0) {
                                    //处理
                                    try {
                                        printEntry(message.getEntries(), canalDealService);
                                    }catch (Exception e){
                                        //有业务异常，抛出RuntimeException
                                        throw new RuntimeException(e);
                                    }
                                }
                                connector.ack(batchId); // 提交确认
                            }catch (NullPointerException e){
                                try {
                                    log.info("canal服务器断开，正在进行第{}次重连", TIMES);
                                    connector.connect();
                                    log.info("canal服务器第{}次连接成功", TIMES);
                                } catch (Exception e1){
                                    log.info("第{}次连接失败", TIMES);
                                    if(TIMES > count){
                                        break;
                                    }
                                    TIMES++;
                                    try {
                                        Thread.sleep(seconds);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }catch (RuntimeException e){
                                log.error("业务异常{}", e);
                                throw e;
                            }
                        }

                    }finally {
                        connector.disconnect();
                    }
                }
            }).start();
        }
    }

    private static void printEntry(List<CanalEntry.Entry> entrys, CanalDealService canalDealService) {
        for (CanalEntry.Entry entry : entrys) {
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }

            CanalEntry.RowChange rowChage = null;
            try {
                rowChage = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser has an error , data:" + entry.toString(),
                        e);
            }
            canalDealService.handler(entry, rowChage);
        }
    }
}
