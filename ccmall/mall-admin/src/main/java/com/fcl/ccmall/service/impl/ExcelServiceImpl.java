package com.fcl.ccmall.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.api.ResultCode;
import com.fcl.ccmall.dao.PersonaDao;
import com.fcl.ccmall.entity.excelmodel.Persona;
import com.fcl.ccmall.enums.JobStatusEnum;
import com.fcl.ccmall.listener.ExcelUploadListener;
import com.fcl.ccmall.model.ExcelOutputJob;
import com.fcl.ccmall.service.DataAnalysis;
import com.fcl.ccmall.service.ExcelOutputJobService;
import com.fcl.ccmall.service.ExcelService;
import com.fcl.ccmall.service.OssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

@Service
@Slf4j
@EnableAsync
public class ExcelServiceImpl implements ExcelService {

    private final int limit = 100000;
    private String SUB_FIX = ".xlsx";
    private String DEFAULT_URL = "https://fclmall-oss.oss-cn-hangzhou.aliyuncs.com/excel/";
    @Value("${TEMP_PATH}")
    private String TEMP_PATH;

    @Resource
    private PersonaDao personaDao;

    @Resource
    private DataAnalysis dataAnalysis;

    @Resource
    private ExcelOutputJobService excelOutputJobService;

    @Resource
    private OssService ossService;

    @Override
    public CommonResult upload(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),
                    Persona.class,
                    new ExcelUploadListener(personaDao)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
            return CommonResult.failed("文件解析错误");
        }
        dataAnalysis.personaAnalysis();
        log.info("上传完成");
        return CommonResult.success();
    }

    @Override
    public void downLoad(HttpServletResponse response) throws IOException{
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String format = DateUtil.format(DateUtil.date(), "yyyy_MM");
            String fileName = URLEncoder.encode("数据信息" + format, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            String tableName = "persona_" + format;
            // 获取数据总数
            Integer dataCount = personaDao.getCount(tableName);
            if (dataCount < limit) {
                // 这里需要设置不关闭流
                EasyExcel.write(response.getOutputStream(), Persona.class)
                        .autoCloseStream(Boolean.FALSE).sheet("sheet0")
                        .doWrite(personaDao.getList(0, limit, tableName));
            }else {
                int count = 0;
                if (dataCount % limit == 0) {
                    count = dataCount / limit;
                }else {
                    count = dataCount / limit + 1;
                }
                for (int i = 0; i < count; i++) {
                    EasyExcel.write(response.getOutputStream(), Persona.class)
                            .autoCloseStream(Boolean.FALSE).sheet("sheet" + count)
                            .doWrite(personaDao.getList((i + 1) * limit, limit, tableName));
                }
            }
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, Object> map = MapUtils.newHashMap();
            map.put("code", ResultCode.FAILED);
//            map.put("message", "下载文件失败" + e.getMessage());
            map.put("message", "下载文件失败");
            response.getWriter().println(JSON.toJSONString(map));
        }
    }

    @Override
    @Async
    public void generateExcel(Integer jobId) {
        // 更新jog状态
        excelOutputJobService.update(null, new LambdaUpdateWrapper<ExcelOutputJob>()
        .eq(ExcelOutputJob::getId, jobId)
        .set(ExcelOutputJob::getJobStatus, JobStatusEnum.EXECUTE.getCode()));

        String format = DateUtil.format(DateUtil.date(), "yyyy_MM");
        String tableName = "persona_" + format;
        // 获取数据总数
        Integer dataCount = personaDao.getCount(tableName);
        StringBuilder sb = new StringBuilder();
        sb.append(TEMP_PATH);
        String randomString = RandomUtil.randomString(20);
        sb.append(randomString);
        sb.append(SUB_FIX);
        File file = new File(sb.toString());
        //判断文件是否存在
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                excelOutputJobService.update(null, new LambdaUpdateWrapper<ExcelOutputJob>()
                .eq(ExcelOutputJob::getId, jobId)
                .set(ExcelOutputJob::getJobStatus, JobStatusEnum.FAILED.getCode())
                .set(ExcelOutputJob::getFailedReason, e.getMessage()));
            }
        }
        if (dataCount < limit) {
            // 这里需要设置不关闭流
            EasyExcel.write(file, Persona.class)
                    .autoCloseStream(Boolean.FALSE).sheet("sheet0")
                    .doWrite(personaDao.getList(0, limit, tableName));
        }else {
            int count = 0;
            if (dataCount % limit == 0) {
                count = dataCount / limit;
            }else {
                count = dataCount / limit + 1;
            }
            for (int i = 0; i < count; i++) {
                EasyExcel.write(file, Persona.class)
                        .autoCloseStream(Boolean.FALSE).sheet("sheet" + count)
                        .doWrite(personaDao.getList((i + 1) * limit, limit, tableName));
            }
        }
        // excel写入完毕 开始上传阿里云
        Boolean aBoolean = ossService.excelUpload(file, randomString + SUB_FIX);
        // 更新job状态
        if (aBoolean) {
            excelOutputJobService.update(null, new LambdaUpdateWrapper<ExcelOutputJob>()
                    .eq(ExcelOutputJob::getId, jobId)
                    .set(ExcelOutputJob::getJobStatus, JobStatusEnum.SUCCESS.getCode())
                    .set(ExcelOutputJob::getFinishTime, LocalDateTime.now())
                    .set(ExcelOutputJob::getDownloadUrl, DEFAULT_URL + randomString + SUB_FIX));
        }else {
            excelOutputJobService.update(null, new LambdaUpdateWrapper<ExcelOutputJob>()
                    .eq(ExcelOutputJob::getId, jobId)
                    .set(ExcelOutputJob::getJobStatus, JobStatusEnum.FAILED.getCode())
                    .set(ExcelOutputJob::getFinishTime, LocalDateTime.now())
                    .set(ExcelOutputJob::getFailedReason, "上传文件服务器失败"));
        }
    }

    @Override
    public void templateDownLoad(HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String format = DateUtil.format(DateUtil.date(), "yyyy_MM");
            String fileName = URLEncoder.encode("模板" + format, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), Persona.class)
                    .autoCloseStream(Boolean.FALSE).sheet("Sheet1")
                    .doWrite(new ArrayList<>());
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, Object> map = MapUtils.newHashMap();
            map.put("code", ResultCode.FAILED);
//            map.put("message", "下载文件失败" + e.getMessage());
            map.put("message", "下载文件失败");
            response.getWriter().println(JSON.toJSONString(map));
        }
    }
}
