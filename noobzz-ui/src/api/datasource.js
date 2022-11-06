import request from '@/utils/request'

// 查询数据源列表
export function listDatasource(query) {
  return request({
    url: '/datasource/list',
    method: 'get',
    params: query
  })
}

// 查询数据源详细
export function getDatasource(datasourceId) {
  return request({
    url: '/datasource/' + datasourceId,
    method: 'get'
  })
}

export function testDatasource(data) {
  return request({
    url: '/datasource/test',
    method: 'post',
    data: data
  })
}

// 新增数据源
export function addDatasource(data) {
  return request({
    url: '/datasource',
    method: 'post',
    data: data
  })
}

// 修改数据源
export function updateDatasource(data) {
  return request({
    url: '/datasource',
    method: 'put',
    data: data
  })
}

// 删除数据源
export function delDatasource(datasourceId) {
  return request({
    url: '/datasource/' + datasourceId,
    method: 'delete'
  })
}
