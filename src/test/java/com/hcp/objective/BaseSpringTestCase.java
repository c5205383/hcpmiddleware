package com.hcp.objective;


import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
//使用这个Annotate会在跑单元测试的时候真实的启一个web服务，然后开始调用Controller的Rest API，待单元测试跑完之后再将web服务停掉;
@WebAppConfiguration
//指定Bean/service/component的配置文件信息，可以有多种方式
@ContextConfiguration(classes = SpringAppConfig4Test.class)
public class BaseSpringTestCase {
	
	
	
}
