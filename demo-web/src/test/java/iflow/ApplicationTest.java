package iflow;

import com.alibaba.fastjson.JSON;
import com.demo.web.controller.IflowController;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public class ApplicationTest extends BaseTest {

    private static Logger logger = LoggerFactory.getLogger(ApplicationTest.class);



    @Test
    public void start() {

        logger.info("logger is ok");

        DataSource ds = (DataSource) context.getBean("dataSource");
        logger.info(JSON.toJSONString("log4j-slf4j-ok: "+ ds.toString()));

        IflowController controller = (IflowController) context.getBean("iflowController");
        logger.info(JSON.toJSONString("controller: "+ controller.toString()));

    }





}
