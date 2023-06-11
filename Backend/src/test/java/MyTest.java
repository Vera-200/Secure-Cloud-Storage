import com.bupt.service.UserService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyTest {

    @Test

    public void test1(HttpServletRequest request, HttpServletResponse response){

        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        UserService userServiceImpl = context.getBean("UserServiceImpl", UserService.class);

        System.out.println(userServiceImpl.queryUserById(1));

    }
}
