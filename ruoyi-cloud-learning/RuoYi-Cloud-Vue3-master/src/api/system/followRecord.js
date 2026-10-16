import request from '@/utils/request'

// 查询CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。列表
export function listFollowRecord(query) {
  return request({
    url: '/system/followRecord/list',
    method: 'get',
    params: query
  })
}

// 查询CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。详细
export function getFollowRecord(followId) {
  return request({
    url: '/system/followRecord/' + followId,
    method: 'get'
  })
}

// 新增CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
export function addFollowRecord(data) {
  return request({
    url: '/system/followRecord',
    method: 'post',
    data: data
  })
}

// 修改CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
export function updateFollowRecord(data) {
  return request({
    url: '/system/followRecord',
    method: 'put',
    data: data
  })
}

// 删除CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
export function delFollowRecord(followId) {
  return request({
    url: '/system/followRecord/' + followId,
    method: 'delete'
  })
}
