package com.hjh.baselib.listener;

/**
 * 滚动数据填充/获取接口
 * @author hjh
 * @2015-7-12上午9:48:29
 */
public interface WheelAdapter {
    public int getItemsCount();

    /**
     * Gets a wheel item by index.
     *
     * @param index the item index
     * @return the wheel item text or null
     */
    public String getItem(int index);

    /**
     * Gets maximum item length. It is used to determine the wheel width.
     * If -1 is returned there will be used the default wheel width.
     *
     * @return the maximum item length or -1
     */
    public int getMaximumLength();
}
