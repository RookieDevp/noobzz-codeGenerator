import request from '@/utils/request'

export function getTemplateList(data) {
  return request({
    url: '/template/getTemplateDynamic',
    method: 'post',
    data: data
  })
}

export function addTemplateList(data) {
  return request({
    url: '/template',
    method: 'post',
    data: data
  })
}

export function importTemplate(data) {
  return request({
    url: '/template/importTemplate/' + data,
    method: 'post'
  })
}

export function getTemplate(templateId) {
  return request({
    url: '/template/' + templateId,
    method: 'get'
  })
}

export function deleteTemplate(data) {
  return request({
    url: '/template/' + data,
    method: 'delete'
  })
}

export function editTemplate(data) {
  return request({
    url: '/template',
    method: 'put',
    data: data
  })
}

export function javaConfig() {
  return request({
    baseURL: 'http://localhost:9528/',
    url: `/java.json?q=${new Date().getTime()}`,
    method: 'get'
  })
}
