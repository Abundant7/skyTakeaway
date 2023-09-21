package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface SetmealDishMapper {
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    void insertBatch(List<SetmealDish> setmealDishes);

    List<Long> getDishIdsBySetmealIdAndStatus(Long id, Integer status);

    List<SetmealDish> getBySetmealId(Long id);

    void deleteBySetmealId(Long id);

    void deleteBatchBySetmealIds(List<Long> ids);
}
