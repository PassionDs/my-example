package me.mason.demo.dynamicdatasource.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.mason.demo.dynamicdatasource.constants.DataSourceConstants;
import me.mason.demo.dynamicdatasource.context.DynamicDataSourceContextHolder;
import me.mason.demo.dynamicdatasource.entity.TestUser;
import me.mason.demo.dynamicdatasource.mapper.TestUserMapper;
import me.mason.demo.dynamicdatasource.service.TestUserService;
import me.mason.demo.dynamicdatasource.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户 Controller
 *
 * @author mason
 * @date 2020-01-08
 */
@Api(tags = "用户操作", protocols = "application/json")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestUserController {

    private final TestUserMapper testUserMapper;

    private final TestUserService testUserService;

    /**
     * 查询
     */
    @ApiOperation(value = "根据id查询用户 # Passion_Ds/2020-06-10#", notes = "根据id查询用户", nickname = "TestUserController" +
            "-find")
    @ApiImplicitParam(name = "id", value = "用户id", type = "query", required = true, dataType =
            "int")
    @GetMapping("/find")
    public Object find(@RequestParam("id") int id) {
        TestUser testUser = testUserMapper.selectOne(new QueryWrapper<TestUser>().eq("id" , id));
        if (testUser != null) {
            return ResponseResult.success(testUser);
        } else {
            return ResponseResult.error("没有找到该对象");
        }
    }

    /**
     * 查询全部
     */
    @ApiOperation(value = "查询所有用户 # Passion_Ds/2020-06-10#", notes = "查询所有用户", nickname = "TestUserController" +
            "-listAll")
    @GetMapping("/listall")
    public Object listAll() {
        int initSize = 2;
        Map<String, Object> result = new HashMap<>(initSize);

//        //默认master数据源查询
//        QueryWrapper<TestUser> queryWrapper = new QueryWrapper<>();
//        List<TestUser> resultData = testUserMapper.selectAll(queryWrapper.isNotNull("name"));
//        result.put(DataSourceConstants.DS_KEY_MASTER, resultData);
//
//        //切换数据源
//        DynamicDataSourceContextHolder.setContextKey(DataSourceConstants.DS_KEY_SLAVE);
//        //mp内置接口
//        List<TestUser> resultDataSlave = testUserMapper.selectList(null);
//        result.put(DataSourceConstants.DS_KEY_SLAVE, resultDataSlave);
//        //恢复数据源
//        DynamicDataSourceContextHolder.removeContextKey();

        //默认master数据源查询
        List<TestUser> masterUser = testUserService.getMasterUser();
        result.put(DataSourceConstants.DS_KEY_MASTER, masterUser);
        //从slave数据源查询
        List<TestUser> slaveUser = testUserService.getSlaveUser();
        result.put(DataSourceConstants.DS_KEY_SLAVE, slaveUser);
        //返回数据
        return ResponseResult.success(result);
    }

}
