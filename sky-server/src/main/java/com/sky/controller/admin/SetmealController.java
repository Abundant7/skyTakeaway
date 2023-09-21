package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.impl.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 功能：
 * 日期：2023/9/20 14:29
 */
@RestController
@RequestMapping("/admin/setmeal")

@Slf4j
@Api(tags = "套餐相关接口")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    public Result save(@RequestBody SetmealDTO setmealDTO){

        setmealService.save(setmealDTO);


        return Result.success();
    }


    @GetMapping("/page")
    @ApiOperation("分页查询套餐")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * 修改套餐状态，起售时判断是否包含未起售菜品
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("修改套餐状态")
    public Result updateStatus(@PathVariable Integer status, Long id){

        setmealService.updateStatus(status,id);


        return Result.success();
    }
    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id){

        SetmealVO setmealVO = setmealService.getSetmealByIdWithDishes(id);
        return Result.success(setmealVO);
    }

    /**
     * 修改套餐信息
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改套餐信息")
    public Result update(@RequestBody SetmealDTO setmealDTO){
        setmealService.update(setmealDTO);
        return Result.success();
    }

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids){
        setmealService.deleteBatch(ids);
        return Result.success();
    }

}