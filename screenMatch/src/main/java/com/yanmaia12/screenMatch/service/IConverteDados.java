package com.yanmaia12.screenMatch.service;

public interface IConverteDados {
    <T> T  obterDados(String json, Class<T> classe);
}
