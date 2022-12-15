package com.sms.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.common.QueryPageParam;
import com.sms.common.Result;
import com.sms.common.shiro.JwtToken;
import com.sms.common.utils.SaltUtil;
import com.sms.common.utils.JwtUtil;
import com.sms.common.utils.RedisUtil;
import com.sms.entity.MailRequest;
import com.sms.entity.Menu;
import com.sms.entity.User;
import com.sms.service.MenuService;
import com.sms.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author sms
 * @since 2022-11-10
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private MenuService menuService;

    //查询所有
    @GetMapping("/all")
    public Result getAll() {
        List<User> list = userService.list();
        return Result.suc(list);
    }

    //唯一账号
    @GetMapping("findByNo")
    public Result findByNo(@RequestParam String no) {
        List<User> res = userService.lambdaQuery().eq(User::getNo, no).list();
        return res.size() > 0 ? Result.suc(res) : Result.fail();
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody User user) {
        //邮件，用户名唯一
        List<User> list = userService.lambdaQuery()
                .eq(User::getMail, user.getMail())
                .eq(User::getName, user.getName()).list();
        if (list.size() > 0) {
            return Result.fail(Result.MAIL_REPEAT);
        }
        //加密密码
        String source = user.getPassword();
        String salt = SaltUtil.getSalt(10);
        Md5Hash pwd = new Md5Hash(source, salt, SaltUtil.ITERATIONS);
        user.setSalt(salt);
        user.setPassword(pwd.toHex());
        return userService.save(user) ? Result.suc(null) : Result.fail();
    }

    //修改
    @PostMapping("/update")
    public Result update(@RequestBody User user) {
        return userService.updateById(user) ? Result.suc(null) : Result.fail();
    }

    //新增或修改
    @PostMapping("/saveOrMod")
    public Result saveOrMod(@RequestBody User user) {
        return userService.saveOrUpdate(user) ? Result.suc(null) : Result.fail();
    }

    //删除
    @GetMapping("/del")
    public Result delete(@RequestParam("id") String id) {
        return userService.removeById(id) ? Result.suc(null) : Result.fail();
    }

    //分页查询（模糊匹配）
    @PostMapping("/page")
    public Result listPage(@RequestBody QueryPageParam query) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        Page<User> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        //查询条件
        HashMap param = query.getParam();
        String name = (String) param.get("name");
        String sex = (String) param.get("sex");
        String roleId = (String) param.get("roleId");

        if (name != null && !"".equals(name)) {
            wrapper.like(User::getName, name);
        }
        if (sex != null && !"".equals(sex)) {
            wrapper.eq(User::getSex, sex);
        }
        if (roleId != null) {
            wrapper.eq(User::getRoleId, roleId);
        }

        Page<User> result = userService.page(page, wrapper);

        return Result.suc(result.getTotal(), result.getRecords());
    }

    @GetMapping("/logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.suc("成功退出");
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        Subject subject = SecurityUtils.getSubject();
        //数据库查找
        User userInfo = userService.getByNo(user.getNo());
        if (userInfo == null) {
            return Result.fail("用户不存在");
        }
        //拼装token
        String token = JwtUtil.createJWT(user.getNo(),user.getMail());
        JwtToken jwtToken = new JwtToken(token, user.getPassword());
        try {
            subject.login(jwtToken);
        } catch (UnknownAccountException e) {
            Result fail = Result.fail();
            fail.setMsg("账号不存在");
            return fail;
        } catch (IncorrectCredentialsException e) {
            Result fail = Result.fail();
            fail.setMsg("密码错误");
            return fail;
        }
        List<Menu> menuList = menuService.lambdaQuery().like(Menu::getMenuright, userInfo.getRoleId()).list();
        Map<String, Object> map = new HashMap<>();
        map.put("user", userInfo);
        map.put("token", token);
        map.put("menu", menuList);
        return Result.suc(map);
    }

    @PostMapping("/code/login")
    public Result codeLogin(@RequestBody MailRequest mailRequest) {
        String mail = mailRequest.getSendTo();
        String inCode = mailRequest.getCode();
        User user = userService.getByMail(mail);
        if (user == null) {
            return Result.fail("用户不存在");
        }
        String token = JwtUtil.createJWT(user.getNo(), mail);
        JwtToken jwtToken = new JwtToken(token, inCode);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(jwtToken);
        } catch (AuthenticationException e) {
            return Result.fail();
        }
        List<Menu> menuList = menuService.lambdaQuery().like(Menu::getMenuright, user.getRoleId()).list();
        HashMap res = new HashMap();
        res.put("user", user);
        res.put("menu", menuList);
        res.put("token", token);
        return Result.suc(res);
    }
}
