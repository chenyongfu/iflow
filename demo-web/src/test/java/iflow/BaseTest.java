package iflow;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BaseTest {

    public static ApplicationContext context;

    private static Logger logger = LoggerFactory.getLogger(BaseTest.class);



    @Before
    public void init(){

        String[] cfgs = new String[] { "classpath:spring.xml", "classpath:spring-mvc.xml" };
        context = new ClassPathXmlApplicationContext(cfgs);

    }





}
