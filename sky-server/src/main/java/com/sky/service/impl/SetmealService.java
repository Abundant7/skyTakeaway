package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 功能：
 * 日期：2023/9/20 14:34
 */
@Service
@Slf4j
public class SetmealService implements com.sky.service.SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void save(SetmealDTO setmealDTO) {

        //新增套餐
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.save(setmeal);
        //新增套餐菜品关系

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        Long setmealId = setmeal.getId();
        for(SetmealDish setmealDish : setmealDishes){
            setmealDish.setSetmealId(setmealId);
            //setmealDishMapper.save(setmealDish);
        }
        setmealDishMapper.insertBatch(setmealDishes);



    }

    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        PageResult pageResult = new PageResult(page.getTotal(),page.getResult());
        return pageResult;

    }

    public void updateStatus(Integer status, Long id) {
        //更新套餐状态
        Setmeal setmeal = new Setmeal();
        setmeal.setId(id);
        setmeal.setStatus(status);
        if(status == StatusConstant.ENABLE){
            Integer reverseStatus = StatusConstant.DISABLE;
            List<Long> dishIdList = setmealDishMapper.getDishIdsBySetmealIdAndStatus(id,reverseStatus);

            if(dishIdList != null && dishIdList.size() > 0){
                throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
            }

        }

        setmealMapper.update(setmeal);



    }

    public void update(SetmealDTO setmealDTO) {

        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        Long setmealId = setmeal.getId();
        for(SetmealDish setmealDish : setmealDishes){
            setmealDish.setSetmealId(setmealId);

        }

        setmealMapper.update(setmeal);
        setmealDishMapper.deleteBySetmealId(setmealDTO.getId());
        setmealDishMapper.insertBatch(setmealDishes);


    }

    public SetmealVO getSetmealByIdWithDishes(Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);

        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    @Transactional
    public void deleteBatch(List<Long> ids) {


        for(Long id : ids){

            Setmeal setmeal = setmealMapper.getById(id);
            if(setmeal.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }

        }

        //删除套餐本身
        setmealMapper.deleteBatch(ids);

        //删除套餐菜品关系
        setmealDishMapper.deleteBatchBySetmealIds(ids);

    }
}