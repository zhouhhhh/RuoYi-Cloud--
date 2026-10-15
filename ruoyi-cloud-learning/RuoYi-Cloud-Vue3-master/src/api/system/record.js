import request from '@/utils/request'

// 查询CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。列表
export function listRecord(query) {
  return request({
    url: '/system/followrecord/list',
    method: 'get',
    params: query
  })
}

// 查询CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。详细
export function getRecord(followId) {
  return request({
    url: '/system/record/' + followId,
    method: 'get'
  })
}

// 新增CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
export function addRecord(data) {
  return request({
    url: '/system/followrecord',
    method: 'post',
    data: data
  })
}

// 修改CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
export function updateRecord(data) {
  return request({
    url: '/system/followrecord',
    method: 'put',
    data: data
  })
}

// 删除CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
export function delRecord(followId) {
  return request({
    url: '/system/followrecord/' + followId,
    method: 'delete'
  })
}
