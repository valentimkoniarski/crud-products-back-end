package hr.hrproduct.listener;


import hr.hrproduct.enums.ProductStatusEnum;
import hr.hrproduct.services.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;



@Component
public class HistoryListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private HistoryService historyService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        ApplicationContext applicationContext = applicationReadyEvent.getApplicationContext();
        historyService = applicationContext.getBean(HistoryService.class);
    }

    @PostPersist
    public void postPersist(Object object) {
        historyService.postPersist(object, ProductStatusEnum.NEW);
    }

    @PostUpdate
    public void postUpdate(Object object) {
        historyService.postPersist(object, ProductStatusEnum.MODYFIED);
    }

    @PostRemove
    public void postRemove(Object object) {
        historyService.postPersist(object, ProductStatusEnum.INACTIVE);
    }
}
