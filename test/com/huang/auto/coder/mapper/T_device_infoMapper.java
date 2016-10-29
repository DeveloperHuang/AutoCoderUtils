package com.huang.auto.coder.mapper;
import java.util.List;
import com.huang.auto.coder.bean.pojo.T_device_info;
public interface T_device_infoMapper {
    public List<T_device_info> selectMethodById(T_device_info t_device_info);
    public void deleteMethodById(T_device_info t_device_info);
    public void insertMethod(T_device_info t_device_info);
    public void updateMethodById(T_device_info t_device_info);
}