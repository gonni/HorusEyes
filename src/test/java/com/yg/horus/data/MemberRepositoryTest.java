package com.yg.horus.data;

import org.apache.catalina.security.SecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by a1000074 on 22/01/2021.
 */
//@SpringBootTest
@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaRepositories(basePackages = "com.yg.horus.data")
@WebAppConfiguration
@ActiveProfiles("local")
//@PropertySource("classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository = null ;

    @Test
    public void getListTest() {
        List<MemberVo> usrList = this.memberRepository.findAll();
        usrList.stream().forEach(System.out::println);
        assertTrue(usrList.size() > 0);
    }
}
