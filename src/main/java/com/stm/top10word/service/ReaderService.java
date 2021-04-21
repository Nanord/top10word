package com.stm.top10word.service;

import com.stm.top10word.pojo.MetaInfo;
import com.stm.top10word.pojo.Path;

public interface ReaderService {
    MetaInfo receiveMetaInfo(Path path);
    Object receiveData(Path path);
}
