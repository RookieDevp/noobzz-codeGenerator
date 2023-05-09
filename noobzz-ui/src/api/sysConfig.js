import request from '@/utils/request'

export function getConfig(params) {
  return request({
    url: '/sysConfig/get',
    method: 'get',
    params
  })
}

export function editConfig(data) {
  return request({
    url: '/sysConfig/edit',
    method: 'put',
    data
  })
}

export function getInitConfig(data) {
  return request({
    url: '/sysConfig/getInit',
    method: 'get',
    data
  })
}
