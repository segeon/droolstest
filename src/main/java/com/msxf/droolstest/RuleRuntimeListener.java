package com.msxf.droolstest;

import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleRuntimeListener implements RuleRuntimeEventListener {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public void objectInserted(ObjectInsertedEvent objectInsertedEvent) {
        logger.info("objectInserted: object={}, rule={}", objectInsertedEvent.getObject(),objectInsertedEvent.getRule());
    }

    public void objectUpdated(ObjectUpdatedEvent objectUpdatedEvent) {
        logger.info("objectUpdated: object={}, rule={}", objectUpdatedEvent.getObject(),objectUpdatedEvent.getRule());
    }

    public void objectDeleted(ObjectDeletedEvent objectDeletedEvent) {
        logger.info("objectDeleted: object={}, rule={}", objectDeletedEvent.getOldObject(),objectDeletedEvent.getRule());
    }
}
