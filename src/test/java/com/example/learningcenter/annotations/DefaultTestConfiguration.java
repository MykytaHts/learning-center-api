package com.example.learningcenter.annotations;

import com.example.learningcenterapi.LearningCenterApiApplication;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = LearningCenterApiApplication.class)
public @interface DefaultTestConfiguration {
}
