package com.fcl.ccmall.listener;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.fcl.ccmall.dao.PersonaDao;
import com.fcl.ccmall.entity.excelmodel.Persona;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ExcelUploadListener implements ReadListener<Persona> {

    /**
     * 每隔1000条存储数据库，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 2000;
    private List<Persona> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    private PersonaDao personaDao;

    public ExcelUploadListener(PersonaDao personaDao) {
        this.personaDao = personaDao;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param persona    one row value. It is same as {@link AnalysisContext#readRowHolder()}
     * @param analysisContext
     */
    @Override
    public void invoke(Persona persona, AnalysisContext analysisContext) {
//        log.info("解析到一条数据:{}", JSON.toJSONString(persona));
        cachedDataList.add(persona);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        // 这里我们判断数据库的表是否生成完毕
        // 获取当前月份
        String yyyy_mm = DateUtil.format(DateUtil.date(), "yyyy_MM");
        String tableName = "persona_" + yyyy_mm;
        personaDao.creatTable(tableName);
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        personaDao.batchInsert(cachedDataList, tableName);
        log.info("存储数据库成功！");
    }
}
