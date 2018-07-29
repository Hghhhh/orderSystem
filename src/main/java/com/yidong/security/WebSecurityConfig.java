package com.yidong.security;
/**
 * springsecurity配置类
 */

import com.yidong.util.MyPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启注解声明权限
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // Spring会自动寻找同样类型的具体类注入，这里就是JwtUserDetailsServiceImpl了
    @Autowired
    @Qualifier("JwtUserDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    /**
     * 生成一个用于jwt验证的filter bean
     * @return JwtAuthenticationTokenFilter
     * @throws Exception
     */
    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

    /**
     * AuthenticationManager用于鉴定认证
     * @return AuthenticationManager
     * @throws Exception
     */
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public MyPasswordEncoder myPasswordEncoderBean()throws Exception {
        return new MyPasswordEncoder();
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService)
        //这里略坑，SpringSecurity默认必须得要有一个passwordEncoder，不然报错，
        // 同时，如果你有且仅有一个passwordEncoder的bean，即使你不设置它，springsecurity也会帮你自动设置它为passwordEncoder
        //测试的时候用一个什么都不做的NoOpPasswordEncoder
               .passwordEncoder(myPasswordEncoderBean());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()

                .antMatchers("/login").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/selectOneGoods").permitAll()
                .antMatchers("/selectGoods").permitAll()
                .antMatchers("/selectStateGoods").permitAll()
                .antMatchers("/selectTypeGoods").permitAll()
                .antMatchers("/selectArea").permitAll()
                .antMatchers("/getCity").permitAll()
                .antMatchers("/getBannerPicture").permitAll()
                .antMatchers("/bigType").permitAll()
                .antMatchers("/smallType").permitAll()
                .antMatchers("/getSms").permitAll()
                .antMatchers("/updatePassword").permitAll()
                .antMatchers("/selectCommentByGoodsId").permitAll()
                .antMatchers("/wxLogin").permitAll()
                .antMatchers("/wxNotify").permitAll()
                //除了登录和注册页面不用权限外，其他都需要权限认证
                .anyRequest().authenticated();
        //配置jwt的filter
        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        // 禁用缓存
        httpSecurity.headers().cacheControl();
    }
}