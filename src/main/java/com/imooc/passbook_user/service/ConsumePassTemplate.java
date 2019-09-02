package com.imooc.passbook_user.service;

import com.alibaba.fastjson.JSON;
import com.imooc.passbook_user.constant.Constants;
import com.imooc.passbook_user.vo.PassTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * 消费kafka中的PassTemplate
 */
@Slf4j
@Component
public class ConsumePassTemplate {

    @Autowired
    private IHBasePassTemplateService iHBasePassTemplateService;

    /**
     *
     * @param passTemplate
     * @param key
     * @param partition
     * @param topic
     */
    @KafkaListener(topics = {Constants.TEMPLATE_TOPIC})
    public void receive(@Payload String passTemplate,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Consumer Receive PassTemplate: {}", passTemplate);

        PassTemplate pt;

        try {
            pt = JSON.parseObject(passTemplate, PassTemplate.class);
        } catch (Exception e) {
            log.error("Parse PassTemplate Error: {}", e.getMessage());
            return;
        }

        log.info("DropPassTemplateToHBase: {}", iHBasePassTemplateService.dropPassTemplateToHBase(pt));
    }

}
