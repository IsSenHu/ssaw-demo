package com.ssaw.ssawauthenticatecenterfeign.annotations;

import com.ssaw.ssawauthenticatecenterfeign.config.MenuAutoUploadConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author HuSen
 * @date 2019/2/25 17:40
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(MenuAutoUploadConfiguration.class)
public @interface EnableMenu {}
