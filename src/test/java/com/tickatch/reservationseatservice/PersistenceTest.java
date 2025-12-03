package com.tickatch.reservationseatservice;

import io.github.tickatch.common.autoconfig.JpaAuditingAutoConfiguration;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Transactional
@SpringBootTest
@Import(JpaAuditingAutoConfiguration.class)
public @interface PersistenceTest {}
