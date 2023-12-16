package com.handwoong.everyonewaiter.util;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseCleaner implements InitializingBean {
    @Autowired
    private EntityManager entityManager;
    private List<String> tables;

    @Override
    public void afterPropertiesSet() {
        tables = entityManager.getMetamodel().getEntities().stream()
                .map(entityType -> entityType.getName().toLowerCase(Locale.ROOT))
                .toList();
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        for (final String table : tables) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + table).executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE " + table + " AUTO_INCREMENT = 1").executeUpdate();
        }
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }
}
