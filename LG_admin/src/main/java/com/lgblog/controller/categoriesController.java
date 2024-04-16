package com.lgblog.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.lgblog.entity.Category;
import com.lgblog.entity.dto.CategoryStatusDto;
import com.lgblog.entity.vo.adminVo.CategoryListVo;
import com.lgblog.entity.vo.adminVo.ExcelCategoryVo;
import com.lgblog.result.AppHttpCodeEnum;
import com.lgblog.result.ResponseResult;
import com.lgblog.service.CategoryService;
import com.lgblog.util.copyBeanUtil.BeanCopy;
import com.lgblog.util.webUtil.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.lgblog.annotation.mySystemlog;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping("/content/category")
public class categoriesController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/listAllCategory")
    public ResponseResult AllCategory()
    {
        return categoryService.AllCategoryForAdmin();
    }

    @PreAuthorize("@ps.hasPermission('content:category:export')")//判断用户的集合里面是否含有该字段
    @GetMapping("/export")
    //注意返回值类型是void
    public void export(HttpServletResponse response){
        try {
            //设置下载文件的请求头，下载下来的Excel文件叫'分类.xlsx'
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导出的数据
            List<Category> category = categoryService.list();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopy.copyListByBean(category, ExcelCategoryVo.class);
            //把数据写入到Excel中，也就是把ExcelCategoryVo实体类的字段作为Excel表格的列头
            //sheet方法里面的字符串是Excel表格左下角工作簿的名字
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("文章分类")
                    .doWrite(excelCategoryVos);

        } catch (Exception e) {
            //如果出现异常,就返回失败的json数据给前端
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            //renderString方法是将json字符串写入到请求体，然后返回给前端
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
    @GetMapping("/list")
    @mySystemlog(businessName = "分类分页查询")
    public ResponseResult getList(Integer pageNum,Integer pageSize,String name,String status)
    {
        return categoryService.getList(pageNum,pageSize,name,status);
    }
    @PostMapping
    @mySystemlog(businessName = "新增文章分类")
    public ResponseResult addCategory(@RequestBody CategoryListVo category)
    {
        return categoryService.addCategory(category);
    }

    /**
     * 自己添加的功能，已经实现了
     * @param dto
     * @return
     */
    @PutMapping("/changeStatus")
    @mySystemlog(businessName = "文章分类状态修改")
    public ResponseResult changeStatus(@RequestBody CategoryStatusDto dto)
    {
        return categoryService.changeStatus(dto);
    }
    @GetMapping("/{id}")
    @mySystemlog(businessName = "前端数据回显")
    public ResponseResult categoryDataBack(@PathVariable Long id)
    {
        return categoryService.categoryDataBack(id);
    }

    @PutMapping
    @mySystemlog(businessName = "修改数据上传")
    public ResponseResult subCategoryData(@RequestBody CategoryListVo vo)
    {
        return categoryService.subCategoryData(vo);
    }
}
