package com.creativeworkes.configserver.utils;

/**
 * 转换器基类
 */
public interface Convertor<S, D> {
  D convert(S source);
}
