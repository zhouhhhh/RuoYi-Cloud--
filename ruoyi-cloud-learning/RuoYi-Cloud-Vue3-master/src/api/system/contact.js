import request from '@/utils/request'

// 查询联系人管理列表
export function listContact(query) {
  return request({
    url: '/system/contact/list',
    method: 'get',
    params: query
  })
}

// 查询联系人管理详细
export function getContact(contactId) {
  return request({
    url: '/system/contact/' + contactId,
    method: 'get'
  })
}

// 新增联系人管理
export function addContact(data) {
  return request({
    url: '/system/contact',
    method: 'post',
    data: data
  })
}

// 修改联系人管理
export function updateContact(data) {
  return request({
    url: '/system/contact',
    method: 'put',
    data: data
  })
}

// 删除联系人管理
export function delContact(contactId) {
  return request({
    url: '/system/contact/' + contactId,
    method: 'delete'
  })
}
