package com.hcp.objective;


import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
//ʹ�����Annotate�����ܵ�Ԫ���Ե�ʱ����ʵ����һ��web����Ȼ��ʼ����Controller��Rest API������Ԫ��������֮���ٽ�web����ͣ��;
@WebAppConfiguration
//ָ��Bean/service/component�������ļ���Ϣ�������ж��ַ�ʽ
@ContextConfiguration(classes = SpringAppConfig4Test.class)
public class BaseSpringTestCase {
	
	
	
}
