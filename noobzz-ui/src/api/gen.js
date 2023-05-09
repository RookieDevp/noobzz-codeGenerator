import request from '@/utils/request'
import { data } from 'autoprefixer'

export function getTemplateList(query) {
  return request({
    url: '/gen/getTemplateList',
    method: 'get',
    params: query
  })
}

export function getTemplate(query) {
  return request({
    url: '/gen/getTemplate',
    method: 'get',
    params: query
  })
}

export function deleteTemplate(query) {
  return request({
    url: '/gen/delete',
    method: 'delete',
    params: { templatePath: query }
  })
}

export function createTemplate(data) {
  return request({
    url: '/gen/createTemplate',
    method: 'post',
    data: data
  })
}

// 查询生成表数据
export function listTable(query) {
  return request({
    url: '/gen/list',
    method: 'get',
    params: query
  })
}
// 查询db数据库列表
export function listDbTable(query) {
  return request({
    url: '/gen/db/list',
    method: 'get',
    params: query
  })
}

// 查询表详细信息
export function getGenTable(tableId) {
  return request({
    url: '/gen/' + tableId,
    method: 'get'
  })
}

// 修改代码生成信息
export function updateGenTable(data) {
  return request({
    url: '/gen',
    method: 'put',
    data: data
  })
}

// 导入表
export function importTable(data) {
  return request({
    url: '/gen/importTable',
    method: 'post',
    params: data
  })
}

// 预览生成代码
export function previewTable(tableId) {
  return request({
    url: '/gen/preview/' + tableId,
    method: 'get'
  })
}

// 删除表数据
export function delTable(tableId) {
  return request({
    url: '/gen/' + tableId,
    method: 'delete'
  })
}

// 生成代码（自定义路径）
export function genCode(tableName) {
  return request({
    url: '/gen/genCode/' + tableName,
    method: 'get'
  })
}

// 同步数据库
export function synchDb(tableName) {
  return request({
    url: '/gen/synchDb/' + tableName,
    method: 'get'
  })
}

export function customPreview(tableId) {
  return request({
    url: '/gen/customPreview/' + tableId,
    method: 'get'
  })
}

export function movePathWithCode(tableId,template,movePath) {
  return request({
    url: '/gen/movePathWithCode',
    method: 'post',
    data:{
      "tableId": tableId+'',
      "template": template,
      "movePath": movePath
    }
  })
}
